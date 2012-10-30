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
package com.tdclighthouse.prototype.services.callbacks;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.hippoecm.repository.api.HippoNode;

import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 *
 */
public class NewAssetFolderCallBack extends CreateNodeCallBack {


	private final String parentPath;
	private final String folderName;

	public NewAssetFolderCallBack(String parentPath, String folderName) {
		this.parentPath = parentPath;
		this.folderName = folderName;
	}

	@Override
	public void edit(HippoNode editableNode) throws RepositoryException {
	}

	@Override
	protected Node getParentNode(Session session) throws RepositoryException {
		return session.getNode(parentPath);
	}

	@Override
	public String getNodeName(Session session) throws RepositoryException {
		return folderName;
	}

	@Override
	public String getNodeType(Session session) throws RepositoryException {
		return Constants.NodeType.HIPPOGALLERY_STD_ASSET_GALLERY;
	}

}
