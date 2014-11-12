package com.tdclighthouse.prototype.services;

import java.io.File;

import com.tdclighthouse.prototype.utils.ImportException;

public interface ImportService {

    void importFolder(File folder, String relPath) throws ImportException;

    void importItem(File file, String relPath) throws ImportException;

}