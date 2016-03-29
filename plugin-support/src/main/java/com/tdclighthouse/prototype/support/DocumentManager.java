/*
 *  Copyright 2012 Finalist B.V.
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
package com.tdclighthouse.prototype.support;

import javax.jcr.*;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import com.tdclighthouse.prototype.support.AbstractSessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.utils.PluginConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.hippoecm.repository.api.*;
import org.hippoecm.repository.gallery.GalleryWorkflow;
import org.hippoecm.repository.standardworkflow.EditableWorkflow;
import org.hippoecm.repository.standardworkflow.FolderWorkflow;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class DocumentManager {

    private final AbstractSessionTemplate sessionTemplate;

    public DocumentManager(AbstractSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public String createDocument(final Node parentNode, final String nodeName, final String nodeType)
            throws RepositoryException {
        return sessionTemplate.execute(new SessionCallBack<String>() {

            @Override
            public String doInSession(Session session) throws RepositoryException {
                try {
                    String nodePath;
                    Workflow workflow = getWorkflow(parentNode, session, PluginConstants.WorkflowName.INTERNAL);
                    if (workflow instanceof FolderWorkflow) {
                        FolderWorkflow folderWorkflow = (FolderWorkflow) workflow;
                        String name = Text.escapeIllegalJcrChars(nodeName);
                        if (PluginConstants.NodeType.HIPPOSTD_FOLDER.equals(nodeType)) {
                            nodePath = folderWorkflow.add("new-translated-folder", nodeType, name);
                        } else if (PluginConstants.NodeType.HIPPOGALLERY_STD_IMAGE_GALLERY.equals(nodeType)) {
                            nodePath = folderWorkflow.add("new-image-folder", nodeType, name);
                        } else if (PluginConstants.NodeType.HIPPOGALLERY_STD_ASSET_GALLERY.equals(nodeType)) {
                            nodePath = folderWorkflow.add("new-file-folder", nodeType, name);
                        } else {
                            nodePath = folderWorkflow.add("new-document", nodeType, name);
                        }
                    } else {
                        throw new RuntimeException("Could not obtain the necessary workspace.");
                    }
                    return nodePath;
                } catch (RemoteException e) {
                    throw new RepositoryException(e);
                } catch (WorkflowException e) {
                    throw new RepositoryException(e);
                }
            }

        });
    }

    public void deleteAsset(final Node node) throws RepositoryException, RemoteException, WorkflowException {
        sessionTemplate.execute(new SessionCallBack<Object>() {

            @Override
            public Object doInSession(Session session) throws RepositoryException {
                Workflow workflow = getWorkflow(node, session, PluginConstants.WorkflowName.SHORTCUTS);
                if (workflow instanceof GalleryWorkflow) {
                    @SuppressWarnings("unused")
                    GalleryWorkflow galleryWorkflow = (GalleryWorkflow) workflow;
                    // TODO galleryWorkflow.
                } else {
                    throw new RuntimeException("Could not obtain the necessary workspace.");
                }
                throw new UnsupportedOperationException();
            }

        });

    }

    public void deleteDocument(Node node) throws RepositoryException {
        Node handle = getHandle(node);
        node = getNodeOfState(NodeState.UNPUBLISHED, handle);
        if (node != null) {
            final Node unpublishedNode = node;
            sessionTemplate.execute(new SessionCallBack<Object>() {

                @Override
                public Object doInSession(Session session) throws RepositoryException {
                    try {
                        DocumentWorkflow reviewWorkflow = getFullReviewedActionsWorkflow(unpublishedNode,
                                session);
                        reviewWorkflow.delete();
                        return null;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (WorkflowException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            throw new IllegalArgumentException("can not delete a published document");
        }

    }

    public void deleteDocumentForceFully(Node node) throws RepositoryException, RemoteException, WorkflowException {
        depublish(node);
        deleteDocument(node);
    }

    public Node depublish(Node node) throws RepositoryException, RemoteException, WorkflowException {
        final Node handle = getHandle(node);
        final Node publishedNode = getNodeOfState(NodeState.PUBLISHED, handle);
        if (publishedNode != null) {
            sessionTemplate.execute(new SessionCallBack<Object>() {
                @Override
                public Object doInSession(Session session) throws RepositoryException {
                    try {
                        DocumentWorkflow reviewWorkflow = getFullReviewedActionsWorkflow(publishedNode,
                                session);
                        try {
                            reviewWorkflow.depublish();
                        } catch (WorkflowException e) {
                            reviewWorkflow.commitEditableInstance();
                            reviewWorkflow = getFullReviewedActionsWorkflow(handle.getNode(handle.getName()), session);
                            reviewWorkflow.depublish();
                        }
                        return null;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (WorkflowException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return getNodeOfState(NodeState.UNPUBLISHED, handle);
    }

    public Node editDocument(final NodeModificationCallBack callBack) throws RepositoryException {
        Node node = getDocuemntNode(callBack);
        return editAmendedNode(callBack, node);
    }

    public Node editNode(final NodeModificationCallBack callBack) throws RepositoryException {
        final Node node = callBack.getNode();
        return editAmendedNode(callBack, node);
    }

    public static Node getHandle(Node node) throws RepositoryException {
        Node result = null;
        while (node != null) {
            if (node.isNodeType(PluginConstants.NodeType.HIPPO_HANDLE)) {
                result = node;
                break;
            }
            try {
                node = node.getParent();
            } catch (ItemNotFoundException e) {
                throw new IllegalArgumentException("could not obtain a handle node from the given node.");
            }
        }
        return result;
    }

    public Node getNode(final String nodePath) throws RepositoryException {
        return sessionTemplate.execute(new SessionCallBack<Node>() {

            @Override
            public Node doInSession(Session session) throws RepositoryException {
                return session.getNode(nodePath);
            }
        });
    }

    public static Node getNodeOfState(NodeState nodeState, Node handle) throws RepositoryException {
        if (!handle.isNodeType(PluginConstants.NodeType.HIPPO_HANDLE)) {
            throw new IllegalArgumentException("the given node is not of the type "
                    + PluginConstants.NodeType.HIPPO_HANDLE);
        }
        Node result = null;
        for (NodeIterator nodes = handle.getNodes(); nodes.hasNext();) {
            Node child = nodes.nextNode();
            if (child.hasProperty(PluginConstants.PropertyName.HIPPOSTD_STATE)
                    && nodeState.getStateString().equals(
                            child.getProperty(PluginConstants.PropertyName.HIPPOSTD_STATE).getString())) {
                result = child;
                break;
            }
        }
        return result;
    }

    public void importXML(final Node node, final InputStream xmlfile) throws RepositoryException {
        sessionTemplate.execute(new SessionCallBack<Object>() {

            @Override
            public Object doInSession(Session session) throws RepositoryException {
                try {
                    session.importXML(node.getPath(), xmlfile, ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("a IOException was thrown while importing an xml file", e);
                }
                return null;
            }
        });

    }

    public void logout() {
        sessionTemplate.logout();
    }

    public Node publish(Node node) throws RepositoryException, RemoteException, WorkflowException {
        final Node handle = getHandle(node);
        final Node targetNode = getNodeOfState(NodeState.UNPUBLISHED, handle);
        if (targetNode != null) {
            sessionTemplate.execute(new SessionCallBack<Object>() {

                @Override
                public Object doInSession(Session session) throws RepositoryException {
                    try {
                        DocumentWorkflow reviewWorkflow = getFullReviewedActionsWorkflow(targetNode, session);
                        try {
                            reviewWorkflow.publish();
                        } catch (WorkflowException e) {
                            reviewWorkflow.commitEditableInstance();
                            reviewWorkflow = getFullReviewedActionsWorkflow(handle.getNode(handle.getName()), session);
                            reviewWorkflow.publish();
                        }
                        return null;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (WorkflowException e) {
                        throw new RuntimeException(e);
                    }
                }

            });
        }
        return getNodeOfState(NodeState.PUBLISHED, handle);
    }

    public <T> T runInSession(SessionCallBack<T> sessionCallBack) throws RepositoryException {
        return sessionTemplate.execute(sessionCallBack);
    }

    private Node editAmendedNode(final NodeModificationCallBack callBack, final Node node) throws RepositoryException {
        final Node handle = getHandle(node);
        final String nodeName = node.getName();
        return sessionTemplate.execute(new SessionCallBack<Node>() {

            @Override
            public Node doInSession(Session session) throws RepositoryException {
                try {
                    EditableWorkflow editableWorkflow = getEditableWorkflow(node, session);
                    Document doc;
                    try {
                        doc = editableWorkflow.obtainEditableInstance();
                    } catch (WorkflowException e) {
                        editableWorkflow.commitEditableInstance();
                        if ((handle != null) && StringUtils.isNotBlank(nodeName)) {
                            editableWorkflow = getEditableWorkflow(handle.getNode(nodeName), session);
                        }
                        doc = editableWorkflow.obtainEditableInstance();
                    }
                    String uuid = doc.getIdentity();
                    HippoNode editableNode = (HippoNode) session.getNodeByIdentifier(uuid);
                    callBack.doAction(editableNode);
                    session.save();
                    editableWorkflow.commitEditableInstance();
                    return session.getNodeByIdentifier(uuid);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (WorkflowException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Node getDocuemntNode(final NodeModificationCallBack callBack) throws RepositoryException {
        Node handle = getHandle(callBack.getNode());
        Node node = getNodeOfState(NodeState.PUBLISHED, handle);
        if (node == null) {
            node = getNodeOfState(NodeState.UNPUBLISHED, handle);
        }
        if (node == null) {
            node = getNodeOfState(NodeState.DRAFT, handle);
        }
        return node;
    }

    private EditableWorkflow getEditableWorkflow(final Node node, Session session) throws MappingException,
            RepositoryException {
        EditableWorkflow result;
        Workflow workflow = getWorkflow(node, session, PluginConstants.WorkflowName.DEFAULT);
        if (workflow instanceof EditableWorkflow) {
            result = (EditableWorkflow) workflow;
        } else {
            throw new IllegalArgumentException("could not get an instance of Editable Workflow for the node: \""
                    + node.getPath() + "\"");
        }
        return result;
    }

    private DocumentWorkflow getFullReviewedActionsWorkflow(final Node node, Session session)
            throws MappingException, RepositoryException {
        DocumentWorkflow result;
        Workflow workflow = getWorkflow(node, session, PluginConstants.WorkflowName.DEFAULT);
        if (workflow instanceof DocumentWorkflow) {
            result = (DocumentWorkflow) workflow;
        } else {
            throw new IllegalArgumentException("the obtained workflow can not be casted to FullReviewedActionsWorkflow");
        }
        return result;
    }

    private Workflow getWorkflow(final Node targetNode, Session session, String workflowName)
            throws RepositoryException, MappingException {
        HippoWorkspace workspace = (HippoWorkspace) session.getWorkspace();
        WorkflowManager workflowManager = workspace.getWorkflowManager();
        Workflow workflow = workflowManager.getWorkflow(workflowName, targetNode);
        return workflow;
    }

    public interface NodeModificationCallBack {

        public void doAction(Node editableNode) throws RepositoryException;

        public Node getNode();
    }

    public enum NodeState {

        PUBLISHED("published"), UNPUBLISHED("unpublished"), DRAFT("draft"), STALE("stale");

        private String stateString;

        private NodeState(String stateString) {
            this.stateString = stateString;
        }

        public String getStateString() {
            return stateString;
        }

    }

}
