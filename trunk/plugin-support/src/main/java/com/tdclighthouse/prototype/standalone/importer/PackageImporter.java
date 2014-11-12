package com.tdclighthouse.prototype.standalone.importer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.ZipFile;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tdclighthouse.prototype.services.BinaryImportService;
import com.tdclighthouse.prototype.services.XmlDocumentImportService;
import com.tdclighthouse.prototype.utils.FileUtils;
import com.tdclighthouse.prototype.utils.ImportException;
import com.tdclighthouse.prototype.utils.PluginConstants;

public class PackageImporter {

    private static final String EXTENSION_ZIP = ".zip";

    @Value("${import.folder}")
    private String importFolder;
    
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
            importItem(folder);
        }
    }

    private void importItem(File folder) throws ImportException {
        File[] binaries = folder.listFiles(new FilterFolderByName("binaries"));
        if (binaries.length > 0) {
            binaryImportService.importFolder(binaries[0], importFolder);
        }

        File[] images = folder.listFiles(new FilterFolderByName("images"));
        if (images.length > 0) {
            binaryImportService.importFolder(images[0], importFolder);
        }

        File[] xmls = folder.listFiles(new FilterFolderByName("xmls"));
        if (xmls.length > 0) {
            xmlDocumentImportService.importFolder(xmls[0], importFolder);
        }
    }

    private File createTempFolder(ZipFile zipFile) throws IOException {
        String fileName = getSimpleFileName(zipFile);
        File tempFolder = new File(System.getProperty(PluginConstants.SystemPropertyName.JAVA_IO_TMPDIR) + "/"
                + fileName + Long.toString(System.nanoTime()));
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
}
