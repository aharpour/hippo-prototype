package com.tdclighthouse.prototype.services;

import java.io.File;

public interface ImportService {

    void importFolder(File folder, String relPath) throws ImportException;

    void importItem(File file, String relPath) throws ImportException;

}