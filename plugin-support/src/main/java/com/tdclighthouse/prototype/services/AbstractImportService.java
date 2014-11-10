package com.tdclighthouse.prototype.services;

import java.io.File;
import java.io.FileFilter;

import com.tdclighthouse.prototype.utils.PluginConstants;

public abstract class AbstractImportService implements ImportService {
    
    protected FileFilter filesFilter = PluginConstants.FileFilters.FILE_FILTER;
    protected FileFilter foldersFilter = PluginConstants.FileFilters.FOLDER_FILTER;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.tdclighthouse.prototype.services.ImportService#importFolder(java.
     * io.File, java.lang.String)
     */
    @Override
    public void importFolder(File folder, String relPath) throws ImportException {
        // first take cares of the file in the folder
        File[] files = folder.listFiles(filesFilter);
        for (File file : files) {
            importItem(file, relPath);
        }
        // then recurse trough sub-folders
        File[] folders = folder.listFiles(foldersFilter);
        for (File subfolder : folders) {
            importFolder(subfolder, relPath + "/" + subfolder.getName());
        }
    }

    @Override
    public abstract void importItem(File file, String relPath) throws ImportException;

}
