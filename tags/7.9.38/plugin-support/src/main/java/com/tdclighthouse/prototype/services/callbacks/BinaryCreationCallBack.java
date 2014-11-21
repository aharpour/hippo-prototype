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
package com.tdclighthouse.prototype.services.callbacks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

import javax.activation.MimetypesFileTypeMap;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.util.Text;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.gallery.GalleryWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.support.DocumentManager;
import com.tdclighthouse.prototype.support.AbstractSessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.utils.PluginConstants;
import com.tdclighthouse.prototype.utils.exceptions.RuntimeIOException;

/**
 * @author Ebrahim Aharpour
 *
 */
public class BinaryCreationCallBack implements SessionCallBack<String> {

    private static final Logger LOG = LoggerFactory.getLogger(BinaryCreationCallBack.class);

    private final String parentPath;
    private final File file;

    public BinaryCreationCallBack(String parentPath, File file) {
        this.parentPath = parentPath;
        this.file = file;
    }

    @Override
    public String doInSession(Session session) throws RepositoryException {
        String uuid = null;
        try {
            Node node = createAssetNode(session, PluginConstants.NodeType.HIPPOGALLERY_EXAMPLE_ASSET_SET);
            uuid = DocumentManager.getHandle(node).getIdentifier();
            String mimeType = new MimetypesFileTypeMap().getContentType(file);
            Node asset = getNode(node, PluginConstants.NodeName.HIPPOGALLERY_ASSET,
                    PluginConstants.NodeType.HIPPO_RESOURCE);
            asset.setProperty(PluginConstants.PropertyName.JCR_DATA,
                    session.getValueFactory().createBinary(new FileInputStream(file)));
            asset.setProperty(PluginConstants.PropertyName.JCR_MIME_TYPE, mimeType);
            asset.setProperty(PluginConstants.PropertyName.JCR_LAST_MODIFIED, new GregorianCalendar());

        } catch (RemoteException e) {
            throw new RepositoryException(e);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        return uuid;
    }

    private Node createAssetNode(Session session, String type) throws RepositoryException, RemoteException {
        Node result;
        HippoWorkspace workspace = (HippoWorkspace) session.getWorkspace();
        Node parent = session.getNode(parentPath);
        String nodeName = Text.escapeIllegalJcrChars(file.getName());

        if (parent.hasNode(nodeName) && parent.getNode(nodeName).getNode(nodeName).isNodeType(type)) {
            result = parent.getNode(nodeName).getNode(nodeName);
        } else {
            GalleryWorkflow workflow = (GalleryWorkflow) workspace.getWorkflowManager().getWorkflow("gallery", parent);
            Document document = workflow.createGalleryItem(nodeName, type);
            result = session.getNodeByIdentifier(document.getIdentity());
        }
        return result;
    }

    private Node getNode(Node node, String relPath, String nodeType) throws RepositoryException {
        Node subjectNode;
        try {
            subjectNode = node.getNode(relPath);
        } catch (PathNotFoundException e) {
            LOG.debug(e.getMessage(), e);
            subjectNode = node.addNode(relPath, nodeType);
        }
        return subjectNode;
    }

}
