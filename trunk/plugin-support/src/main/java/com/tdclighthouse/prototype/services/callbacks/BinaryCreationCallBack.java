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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

import javax.activation.MimetypesFileTypeMap;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.jackrabbit.util.Text;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.MappingException;
import org.hippoecm.repository.gallery.GalleryWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.support.SessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 *
 */
public class BinaryCreationCallBack implements SessionCallBack<String> {

	public static final Logger log = LoggerFactory.getLogger(BinaryCreationCallBack.class);

	private final String parentPath;
	private final File file;

	public BinaryCreationCallBack(String parentPath, File file) {
		this.parentPath = parentPath;
		this.file = file;
	}

	@Override
	public String doInSession(Session session) throws RepositoryException {
		try {
			Node node = createAssetNode(session,
					Constants.NodeType.HIPPOGALLERY_EXAMPLE_ASSET_SET);
			String mimeType = new MimetypesFileTypeMap().getContentType(file);
			Node asset = getNode(node, Constants.NodeName.HIPPOGALLERY_ASSET, Constants.NodeType.HIPPO_RESOURCE);
			asset.setProperty(Constants.PropertyName.JCR_DATA,
					session.getValueFactory().createBinary(new FileInputStream(file)));
			asset.setProperty(Constants.PropertyName.JCR_MIME_TYPE, mimeType);
			asset.setProperty(Constants.PropertyName.JCR_LAST_MODIFIED, new GregorianCalendar());

		} catch (RemoteException e) {
			throw new RepositoryException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private Node createAssetNode(Session session, String type) throws PathNotFoundException, RepositoryException,
			MappingException, RemoteException, ItemNotFoundException {
		HippoWorkspace workspace = (HippoWorkspace) session.getWorkspace();
		Node parent = session.getNode(parentPath);
		GalleryWorkflow workflow = (GalleryWorkflow) workspace.getWorkflowManager().getWorkflow("gallery", parent);
		Document document = workflow.createGalleryItem(Text.escapeIllegalJcrChars(file.getName()), type);
		return session.getNodeByIdentifier(document.getIdentity());
	}

	private Node getNode(Node node, String relPath, String nodeType) throws RepositoryException, ItemExistsException,
			PathNotFoundException, NoSuchNodeTypeException, LockException, VersionException,
			ConstraintViolationException {
		Node subjectNode;
		try {
			subjectNode = node.getNode(relPath);
		} catch (PathNotFoundException e) {
			subjectNode = node.addNode(relPath, nodeType);
		}
		return subjectNode;
	}

}
