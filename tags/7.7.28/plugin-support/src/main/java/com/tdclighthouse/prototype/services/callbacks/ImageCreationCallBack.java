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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.jackrabbit.util.Text;
import org.hippoecm.frontend.plugins.gallery.imageutil.ImageUtils;
import org.hippoecm.frontend.plugins.gallery.imageutil.ImageUtils.ScalingStrategy;
import org.hippoecm.repository.api.Document;
import org.hippoecm.repository.api.HippoWorkspace;
import org.hippoecm.repository.api.MappingException;
import org.hippoecm.repository.gallery.GalleryWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.support.SessionTemplate.SessionCallBack;
import com.tdclighthouse.prototype.utils.PluginConstants;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class ImageCreationCallBack implements SessionCallBack<String> {

	private static final int MAX_SUPPORTED_PIXELS = 65500;

	public static final Logger log = LoggerFactory.getLogger(ImageCreationCallBack.class);

	private final String parentPath;
	private final File file;
	private final String contentType;

	public ImageCreationCallBack(String parentPath, File file, String contentType) {
		this.parentPath = parentPath;
		this.file = file;
		this.contentType = contentType;
	}

	@Override
	public String doInSession(Session session) throws RepositoryException {
		try {
			// FIXME refactor
			Node node = createImageSetNode(session, contentType);
			String mimeType = new MimetypesFileTypeMap().getContentType(file);
			Node galleryProcessorService = session.getNode(PluginConstants.Paths.GALLERY_PROCESSOR_SERVICE);

			for (NodeIterator sizes = galleryProcessorService.getNodes(); sizes.hasNext();) {
				Node size = sizes.nextNode();
				if (size.isNodeType(PluginConstants.NodeType.FRONTEND_PLUGINCONFIG)) {
					Node subjectNode = getNode(node, size.getName(), PluginConstants.NodeType.HIPPOGALLERY_IMAGE);
					Long height = size.getProperty(PluginConstants.PropertyName.HEIGHT).getLong();
					Long width = size.getProperty(PluginConstants.PropertyName.WIDTH).getLong();
					BufferedImage bufferedImage = ImageIO.read(file);
					InputStream inputStream;
					if (height == 0 && width == 0) {
						inputStream = new FileInputStream(file);
						height = (long) bufferedImage.getHeight();
						width = (long) bufferedImage.getWidth();
					} else {
						ImageSize scaledSize = getScaledSize(height, width, bufferedImage);
						BufferedImage scaledImage = ImageUtils.scaleImage(bufferedImage, scaledSize.width.intValue(),
								scaledSize.height.intValue(), ScalingStrategy.BEST_QUALITY);
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						ImageIO.write(scaledImage, "jpg", byteArrayOutputStream);
						byteArrayOutputStream.flush();
						inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
						height = (long) scaledImage.getHeight();
						width = (long) scaledImage.getWidth();
					}
					subjectNode.setProperty(PluginConstants.PropertyName.JCR_DATA,
							session.getValueFactory().createBinary(inputStream));
					subjectNode.setProperty(PluginConstants.PropertyName.JCR_MIME_TYPE, mimeType);
					subjectNode.setProperty(PluginConstants.PropertyName.JCR_LAST_MODIFIED, new GregorianCalendar());
					subjectNode.setProperty(PluginConstants.PropertyName.HIPPOGALLERY_HEIGHT, height);
					subjectNode.setProperty(PluginConstants.PropertyName.HIPPOGALLERY_WIDTH, width);
				}
			}

		} catch (RemoteException e) {
			throw new RepositoryException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	private Node createImageSetNode(Session session, String type) throws PathNotFoundException, RepositoryException,
			MappingException, RemoteException, ItemNotFoundException {
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

	private ImageSize getScaledSize(Long height, Long width, BufferedImage image) {
		width = (width == 0 ? MAX_SUPPORTED_PIXELS : width);
		height = (height == 0 ? MAX_SUPPORTED_PIXELS : height);
		double widthScalingFactor = ((double) width) / image.getWidth();
		double heightScalingFactor = ((double) height) / image.getHeight();
		double scalingFacotr = Math.min(widthScalingFactor, heightScalingFactor);
		if (scalingFacotr > 0) {
			width = (long) Math.round(Math.max(scalingFacotr * image.getWidth(), 1));
			height = (long) Math.round(Math.max(scalingFacotr * image.getHeight(), 1));
		}
		return new ImageSize(height, width);
	}

	private static class ImageSize {
		public final Long height;
		public final Long width;

		public ImageSize(Long height, Long width) {
			this.height = height;
			this.width = width;
		}

	}

}
