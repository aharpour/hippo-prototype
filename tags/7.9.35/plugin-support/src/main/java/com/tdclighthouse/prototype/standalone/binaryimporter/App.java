package com.tdclighthouse.prototype.standalone.binaryimporter;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import javax.jcr.RepositoryException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tdclighthouse.prototype.services.BinaryImportService;
import com.tdclighthouse.prototype.utils.FileUtils;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.exceptions.RuntimeIOException;

public class App {

    private static final String EXTENSION_ZIP = ".zip";
    private final String importFolder;
    public static final ClassPathXmlApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(
            App.class.getPackage().getName().replace('.', '/') + "/binary-importer-context.xml");

    public App(String importFolder) {
        this.importFolder = importFolder;
    }

    public static void main(String[] args) throws RepositoryException {
        if (args.length != 1) {
            throw new IllegalArgumentException(
                    "This application requires a single command line argument pointing to resource you want to import.");
        }
        File resource = new File(args[0]);
        if (resource.exists()) {
            App app = APPLICATION_CONTEXT.getBean(App.class);
            app.importBinaries(resource);
        }
    }

    public String getImportFolder() {
        return importFolder;
    }

    private void importBinaries(File resource) throws RepositoryException {
        try {
            BinaryImportService binaryImportService = APPLICATION_CONTEXT.getBean(BinaryImportService.class);
            if (resource.isDirectory()) {
                binaryImportService.migrateFolder(resource, importFolder);
            } else {
                if (resource.getName().endsWith(EXTENSION_ZIP)) {
                    ZipFile zipFile = new ZipFile(resource);
                    File temp = createTempFolder(zipFile);
                    try {
                        FileUtils.unzip(zipFile, temp);
                        binaryImportService.migrateFolder(temp, importFolder);
                    } finally {
                        FileUtils.forcefulDeletion(temp);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
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
}
