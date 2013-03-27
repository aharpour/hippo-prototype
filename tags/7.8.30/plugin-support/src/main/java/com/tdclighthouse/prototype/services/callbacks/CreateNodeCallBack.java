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

import java.rmi.RemoteException;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.util.Text;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoNode;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.MappingException;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.api.WorkflowManager;
import org.hippoecm.repository.standardworkflow.EditableWorkflow;
import org.hippoecm.repository.standardworkflow.FolderWorkflow;

import com.tdclighthouse.prototype.support.AbstractSessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.utils.PluginConstants;

/**
 * @author Ebrahim Aharpour
 *
 */
public abstract class CreateNodeCallBack implements SessionCallBack<String> {

	@Override
	public String doInSession(Session session) throws RepositoryException {
		String nodePath = null;
		try {
			Node node = createWorkflow(session);
			nodePath = node.getPath();
			editWorkflow(session, node);
		} catch (RemoteException e) {
			throw new RepositoryException(e);
		} catch (WorkflowException e) {
			throw new RepositoryException(e);
		}
		return nodePath;
	}

	private Node createWorkflow(Session session) throws RepositoryException, RemoteException, WorkflowException {
		String nodePath;
		HippoWorkspace workspace = (HippoWorkspace) session.getWorkspace();
		WorkflowManager workflowManager = workspace.getWorkflowManager();
		FolderWorkflow folderWorkflow = (FolderWorkflow) workflowManager
				.getWorkflow("internal", getParentNode(session));
		String nodeType = getNodeType(session);
		String nodeName = Text.escapeIllegalJcrChars(getNodeName(session));
		if (PluginConstants.NodeType.HIPPOSTD_FOLDER.equals(nodeType)) {
			nodePath = folderWorkflow.add("new-translated-folder", nodeType, nodeName);
		} else if (PluginConstants.NodeType.HIPPOGALLERY_STD_IMAGE_GALLERY.equals(nodeType)) {
			nodePath = folderWorkflow.add("new-image-folder", nodeType, nodeName);
		} else if (PluginConstants.NodeType.HIPPOGALLERY_STD_ASSET_GALLERY.equals(nodeType)) {
			nodePath = folderWorkflow.add("new-file-folder", nodeType, nodeName);
		} else {
			nodePath = folderWorkflow.add("new-document", nodeType, nodeName);
		}
		return session.getNode(nodePath);
	}

	private void editWorkflow(Session session, Node node) throws RepositoryException, MappingException,
			WorkflowException, RemoteException, ItemNotFoundException {
		HippoWorkspace workspace = (HippoWorkspace) session.getWorkspace();
		WorkflowManager workflowManager = workspace.getWorkflowManager();
		EditableWorkflow editableWorkflow = (EditableWorkflow) workflowManager.getWorkflow("default", node);
		Document doc = editableWorkflow.obtainEditableInstance();
		String uuid = doc.getIdentity();
		HippoNode editableNode = (HippoNode) session.getNodeByIdentifier(uuid);
		edit(editableNode);
		editableWorkflow.commitEditableInstance();
	}

	public abstract void edit(HippoNode editableNode) throws RepositoryException;

	protected abstract Node getParentNode(Session session) throws RepositoryException;

	public abstract String getNodeName(Session session) throws RepositoryException;

	public abstract String getNodeType(Session session) throws RepositoryException;
}
