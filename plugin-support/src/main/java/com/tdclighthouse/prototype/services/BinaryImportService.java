/*
 *  Copyright 2012 Smile B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.services;

import java.io.File;

import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tdclighthouse.prototype.services.FolderCreationService.NewFolderCallBackFactory;
import com.tdclighthouse.prototype.services.callbacks.BinaryCreationCallBack;
import com.tdclighthouse.prototype.services.callbacks.ImageCreationCallBack;
import com.tdclighthouse.prototype.services.callbacks.NewAssetFolderCallBack;
import com.tdclighthouse.prototype.services.callbacks.NewImageFolderCallBack;
import com.tdclighthouse.prototype.support.AbstractSessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.support.DocumentManager;
import com.tdclighthouse.prototype.utils.ImageUtils;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.RuntimeRepositoryException;

/**
 * @author Ebrahim Aharpour
 *
 */
public class BinaryImportService extends AbstractImportService {

    private static final String FILE_COPY_LOG_MESSAGE = "file: {} is going to be put in {}";

    private static final Logger LOG = LoggerFactory.getLogger(BinaryImportService.class);

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private FolderCreationService folderCreationService;

    private String imageType;

    @Required
    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.tdclighthouse.prototype.services.ImportService#importItem(java.
     * io.File, java.lang.String)
     */
    @Override
    public void importItem(File file, String path) {
        try {
            if (ImageUtils.isHippoFriendlyImage(file)) {
                migrateImages(file, path);
            } else {
                importBinary(file, path);
            }
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    public void importBinary(File file, String relPath) throws RepositoryException {
        String absPath = folderCreationService.generateFolders(PluginConstants.Paths.ASSETS, relPath,
                new NewFolderCallBackFactory() {
                    @Override
                    public SessionCallBack<String> getNewFolderCallBack(String parentPath, String folderName) {
                        return new NewAssetFolderCallBack(parentPath, folderName);
                    }
                });
        LOG.debug(FILE_COPY_LOG_MESSAGE, file.getAbsolutePath(), absPath);
        documentManager.runInSession(new BinaryCreationCallBack(absPath, file));
    }

    private void migrateImages(File file, String relPath) throws RepositoryException {
        try {
            String absPath = folderCreationService.generateFolders(PluginConstants.Paths.GALLERY, relPath,
                    new NewFolderCallBackFactory() {
                        @Override
                        public SessionCallBack<String> getNewFolderCallBack(String parentPath, String folderName) {
                            return new NewImageFolderCallBack(parentPath, folderName);
                        }
                    });
            LOG.debug(FILE_COPY_LOG_MESSAGE, file.getAbsolutePath(), absPath);
            documentManager.runInSession(new ImageCreationCallBack(absPath, file, imageType));
        } catch (Exception e) {
            LOG.error("Migration of image \"" + relPath + "\" fail for the following reason.", e);
        }
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public void setFolderCreationService(FolderCreationService folderCreationService) {
        this.folderCreationService = folderCreationService;
    }

}
