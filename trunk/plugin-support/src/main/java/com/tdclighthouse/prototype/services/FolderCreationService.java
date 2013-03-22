/*
 *  Copyright 2012 Smile B.V.
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

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.springframework.beans.factory.annotation.Autowired;

import com.tdclighthouse.prototype.support.DocumentManager;
import com.tdclighthouse.prototype.support.AbstractSessionTemplate.SessionCallBack;

/**
 * @author Ebrahim Aharpour
 *
 */
public class FolderCreationService {

	@Autowired
	private DocumentManager documentManager;

	public String generateFolders(String parentPath, String relPath, NewFolderCallBackFactory factory)
			throws RepositoryException, PathNotFoundException {
		Node node = documentManager.getNode(parentPath);
		String[] nodes = relPath.split("/");
		for (String nodePath : nodes) {
			if (StringUtils.isNotBlank(nodePath)) {
				String cleansedPath = Text.escapeIllegalJcrChars(nodePath);
				if (node.hasNode(cleansedPath)) {
					node = node.getNode(cleansedPath);
				} else {
					String path = documentManager.runInSession(factory.getNewFolderCallBack(node.getPath(),
							cleansedPath));
					node = documentManager.getNode(path);
				}
			}
		}
		String absPath = node.getPath();
		return absPath;
	}

	public interface NewFolderCallBackFactory {
		public SessionCallBack<String> getNewFolderCallBack(String parentPath, String folderName);
	}

	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}

}
