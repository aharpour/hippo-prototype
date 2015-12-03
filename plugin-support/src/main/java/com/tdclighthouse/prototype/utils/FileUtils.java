package com.tdclighthouse.prototype.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

    public static void unzip(ZipFile zipFile, File toFolder) throws IOException {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String path = toFolder.getAbsoluteFile() + "/" + entry.getName();
            if (entry.isDirectory()) {
                boolean mkdir = new File(path).mkdir();
                if (!mkdir) {
                    throw new IOException("failed to create a folder at " + path);
                }
            } else {
                copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(path)));
            }
        }
        zipFile.close();
    }

    public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    /**
     * This method forceful deletes a file or folder. In case of a folder it
     * does not matter whether there are files and folder in it.
     * 
     * @return <code>true</code> if the deletion is completely successful,
     *         otherwise return <code>false</code>
     */
    public static boolean forcefulDeletion(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file argument is required");
        }
        boolean result = true;
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    result = forcefulDeletion(child) && result;
                    if (child.isDirectory()) {
                        result = child.delete() && result;
                    }
                }
            }
            result = file.delete() && result;
        }
        return result;
    }

    public static String getFileName(String path) {
        int lastIndexOf = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return path.substring(lastIndexOf + 1);
    }
    
    public static String removeExtention(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;  
    }

}
