/*
 *  Copyright 2012 Smile Group Benelux B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
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
import java.io.FileFilter;

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
import com.tdclighthouse.prototype.support.DocumentManager;
import com.tdclighthouse.prototype.support.SessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.ImageUtils;

/**
 * @author Ebrahim Aharpour
 *
 */
public class BinaryImportService {

	public static final Logger log = LoggerFactory.getLogger(BinaryImportService.class);

	@Autowired
	private DocumentManager documentManager;

	@Autowired
	private FolderCreationService folderCreationService;

	private String imageType;

	@Required
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	private final FileFilter fileFilter = new FileFilter() {

		@Override
		public boolean accept(File file) {
			return file.isDirectory() && !".svn".equals(file.getName());
		}
	};
	private final FileFilter folderFilter = new FileFilter() {

		@Override
		public boolean accept(File file) {
			return !file.isDirectory();
		}
	};

	public void migrateFolder(File folder, String path) throws RepositoryException {
		// first take cares of the file in the folder
		File[] files = folder.listFiles(folderFilter);
		for (File file : files) {
			if (ImageUtils.isHippoFriendlyImage(file)) {
				migrateImages(file, path);
			} else {
				migrateBinaries(file, path);
			}
		}
		// then recurse trough sub-folders
		File[] folders = folder.listFiles(fileFilter);
		for (File subfolder : folders) {
			migrateFolder(subfolder, path + "/" + subfolder.getName());
		}
	}

	private void migrateBinaries(File file, String relPath) throws RepositoryException {
		String absPath = folderCreationService.generateFolders(Constants.Paths.ASSETS, relPath,
				new NewFolderCallBackFactory() {
					@Override
					public SessionCallBack<String> getNewFolderCallBack(String parentPath, String folderName) {
						return new NewAssetFolderCallBack(parentPath, folderName);
					}
				});
		log.debug("file: " + file.getAbsolutePath() + " is going to be put in " + absPath);
		documentManager.runInSession(new BinaryCreationCallBack(absPath, file));
	}

	private void migrateImages(File file, String relPath) throws RepositoryException {
		try {
			String absPath = folderCreationService.generateFolders(Constants.Paths.GALLERY, relPath,
					new NewFolderCallBackFactory() {
						@Override
						public SessionCallBack<String> getNewFolderCallBack(String parentPath, String folderName) {
							return new NewImageFolderCallBack(parentPath, folderName);
						}
					});
			log.debug("file: " + file.getAbsolutePath() + " is going to be put in " + absPath);
			documentManager.runInSession(new ImageCreationCallBack(absPath, file, imageType));
		} catch (Exception e) {
			log.error("Migration of image \"" + relPath + "\" fail for the following reason.", e);
		}
	}

	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}

	public void setFolderCreationService(FolderCreationService folderCreationService) {
		this.folderCreationService = folderCreationService;
	}

}
