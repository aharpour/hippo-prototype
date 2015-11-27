package com.tdclighthouse.prototype.standalone.importer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.ZipFile;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.VersionManager;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tdclighthouse.prototype.services.BinaryImportService;
import com.tdclighthouse.prototype.services.ReferenceRegistry;
import com.tdclighthouse.prototype.services.XmlDocumentImportService;
import com.tdclighthouse.prototype.support.AbstractSessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.support.SessionTemplate;
import com.tdclighthouse.prototype.utils.FileUtils;
import com.tdclighthouse.prototype.utils.ImportException;
import com.tdclighthouse.prototype.utils.NodeUtils;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.PluginConstants.NodeType;
import com.tdclighthouse.prototype.utils.PluginConstants.Paths;

public class PackageImporter {

    private static final String EXTENSION_ZIP = ".zip";
    private static final Logger LOG = LoggerFactory.getLogger(PackageImporter.class);

    @Value("${import.folder}")
    private String importFolder;

    @Autowired
    private SessionTemplate sessionTemplate;

    @Autowired
    private ReferenceRegistry referenceRegistry;

    @Autowired
    private BinaryImportService binaryImportService;

    @Autowired
    private XmlDocumentImportService xmlDocumentImportService;

    public static final ClassPathXmlApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
            PackageImporter.class.getPackage().getName().replace('.', '/') + "/importer-context.xml");

    private FilenameFilter folderFilter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            return dir.isDirectory();
        }
    };

    public static void main(String[] args) throws ImportException {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "This application requires a single command line argument pointing to resource you want to import.");
        }
        File resource = new File(args[0]);
        if (resource.exists()) {
            PackageImporter app = APPLICATION_CONTEXT.getBean(PackageImporter.class);
            app.importFileOrFolder(resource);
        }
    }

    public String getImportFolder() {
        return importFolder;
    }

    private void importFileOrFolder(File resource) throws ImportException {
        try {
            if (resource.isDirectory()) {
                bulkImport(resource);
            } else {
                if (resource.getName().endsWith(EXTENSION_ZIP)) {
                    ZipFile zipFile = new ZipFile(resource);
                    File temp = createTempFolder(zipFile);
                    try {
                        FileUtils.unzip(zipFile, temp);
                        bulkImport(temp);
                    } finally {
                        FileUtils.forcefulDeletion(temp);
                    }
                }
            }
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }

    private void bulkImport(File resource) throws ImportException {
        File[] folders = resource.listFiles(folderFilter);
        for (File folder : folders) {
            importItem(new Item(getSubfolderByName(folder, "binaries"), getSubfolderByName(folder, "images"),
                    getSubfolderByName(folder, "xmls")));
        }
    }

    private void importItem(Item item) throws ImportException {
        if (item.binaries != null) {
            binaryImportService.importFolder(item.binaries, importFolder);
        }

        if (item.images != null) {
            binaryImportService.importFolder(item.images, importFolder);
        }
        if (item.xmls != null) {
            xmlDocumentImportService.importFolder(item.xmls, importFolder);
        }

        linkUpItems(item);
    }

    private void linkUpItems(final Item item) throws ImportException {
        try {
            LOG.info("starting to link up items in the package {}", item.parentFolder.getAbsolutePath());
            sessionTemplate.execute(new SessionCallBack<Object>() {

                @Override
                public Object doInSession(Session session) throws RepositoryException {
                    QueryManager queryManager = session.getWorkspace().getQueryManager();
                    for (String type : new String[] { NodeType.HIPPO_FACETSELECT, NodeType.HIPPO_MIRROR,
                            NodeType.HIPPO_FACETSEARCH }) {
                        Query query = queryManager.createQuery(createStatement(type), "xpath");
                        QueryResult queryResult = query.execute();
                        for (NodeIterator nodes = queryResult.getNodes(); nodes.hasNext();) {
                            linkReferences(item, nodes);
                        }
                    }
                    return null;
                }

            });
        } catch (RepositoryException e) {
            throw new ImportException(e);
        }

    }

    private void linkReferences(final Item item, NodeIterator nodes) throws RepositoryException {
        Node node = nodes.nextNode();
        String docbase = node.getProperty(PluginConstants.PropertyName.HIPPO_DOCBASE).getString();
        if (StringUtils.isNotBlank(docbase)
                && (docbase.length() != 36 || docbase.contains(PluginConstants.Paths.FILE_SEPARATOR))) {
            String uuid = getUuid(docbase, item);
            if (StringUtils.isNotBlank(uuid)) {
                Node publishableAncestor = getPublishableNode(node);
                if (publishableAncestor != null) {
                    VersionManager versionManager = node.getSession().getWorkspace().getVersionManager();
                    versionManager.checkout(publishableAncestor.getPath());
                    node.setProperty(PluginConstants.PropertyName.HIPPO_DOCBASE, uuid);
                    node.getSession().save();
                    versionManager.checkin(publishableAncestor.getPath());
                } else {
                    node.setProperty(PluginConstants.PropertyName.HIPPO_DOCBASE, uuid);
                }
            }
        }
    }

    private Node getPublishableNode(Node node) throws RepositoryException {
        Node result = null;
        Node n = node;
        while (n != null) {
            if (n.isNodeType("mix:versionable")) {
                result = n;
                break;
            } else {
                n = n.getParent();
            }
        }
        return result;
    }

    private String getUuid(String docbase, Item item) {
        String result = null;
        if (item.parentFolder != null) {
            String reference = item.parentFolder.toURI().toString() + NodeUtils.normalizeRelativePath(docbase);
            LOG.debug("trying to dereference \"{}\"", reference);
            String uuid = referenceRegistry.lookup(reference);
            if (StringUtils.isNotBlank(uuid)) {
                result = uuid;
                LOG.debug("dereference {} to {}", docbase, uuid);
            } else {
                LOG.error("failed to dereference  \"{}\".", reference);
            }
        }
        return result;
    }

    private String createStatement(String type) {
        StringBuilder sb = new StringBuilder(Paths.FILE_SEPARATOR);
        sb.append(PluginConstants.Paths.DOCUMENTS);
        sb.append(NodeUtils.normalizeAbsolutePath(importFolder));
        sb.append("//element(*,").append(type).append(")[@hippo:docbase]");
        return sb.toString();
    }

    private File getSubfolderByName(File folder, String folderName) {
        File result = null;
        File[] xmls = folder.listFiles(new FilterFolderByName(folderName));
        if (xmls.length > 0) {
            result = xmls[0];
        }
        return result;
    }

    private File createTempFolder(ZipFile zipFile) throws IOException {
        String fileName = getSimpleFileName(zipFile);
        File tempFolder = new File(System.getProperty(PluginConstants.SystemPropertyName.JAVA_IO_TMPDIR)
                + PluginConstants.Paths.FILE_SEPARATOR + fileName + Long.toString(System.nanoTime()));
        if (!tempFolder.mkdir()) {
            throw new IOException("Could not create temp directory: " + tempFolder.getAbsolutePath());
        }
        return tempFolder;
    }

    private String getSimpleFileName(ZipFile zipFile) {
        return FilenameUtils.removeExtension(FileUtils.getFileName(zipFile.getName()));
    }

    private static class FilterFolderByName implements FileFilter {
        private final String folderName;

        public FilterFolderByName(String folderName) {
            this.folderName = folderName;
        }

        @Override
        public boolean accept(File file) {
            return file.isDirectory() && folderName.equalsIgnoreCase(file.getName());
        }
    }

    private static class Item {

        private final File binaries;
        private final File images;
        private final File xmls;
        private final File parentFolder;

        public Item(File binaries, File images, File xmls) {
            this.binaries = binaries;
            this.images = images;
            this.xmls = xmls;
            this.parentFolder = (xmls != null ? xmls : binaries != null ? binaries : images).getParentFile();

        }

    }
}
