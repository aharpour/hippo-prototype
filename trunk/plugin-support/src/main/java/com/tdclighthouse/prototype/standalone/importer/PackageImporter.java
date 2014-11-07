package com.tdclighthouse.prototype.standalone.importer;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.zip.ZipFile;

import javax.jcr.RepositoryException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tdclighthouse.prototype.services.BinaryImportService;
import com.tdclighthouse.prototype.utils.FileUtils;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.RuntimeRepositoryException;
import com.tdclighthouse.prototype.utils.exceptions.RuntimeIOException;

public class PackageImporter {

    private static final String EXTENSION_ZIP = ".zip";
    private final String importFolder;
    public static final ClassPathXmlApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
            PackageImporter.class.getPackage().getName().replace('.', '/') + "/importer-context.xml");

    private FilenameFilter folderFilter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String name) {
            return dir.isDirectory();
        }
    };

    public PackageImporter(String importFolder) {
        this.importFolder = importFolder;
    }

    public static void main(String[] args) {
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

    private void importFileOrFolder(File resource) {
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
            throw new RuntimeIOException(e);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    private void bulkImport(File resource) throws RepositoryException {
        File[] folders = resource.listFiles(folderFilter);
        for (File folder : folders) {
            importItem(folder);
        }
    }

    private void importItem(File folder) throws RepositoryException {
        BinaryImportService binaryImportService = APPLICATION_CONTEXT.getBean(BinaryImportService.class);
        File[] binaries = folder.listFiles(new FilterFolderByName("binaries"));
        if (binaries.length > 0) {
            binaryImportService.migrateFolder(binaries[0], importFolder);
        }
        
        File[] images = folder.listFiles(new FilterFolderByName("images"));
        if (images.length > 0) {
            binaryImportService.migrateFolder(images[0], importFolder);
        }
        
        folder.listFiles(new FilterFolderByName("xmls"));
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

    private class FilterFolderByName implements FileFilter {
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
