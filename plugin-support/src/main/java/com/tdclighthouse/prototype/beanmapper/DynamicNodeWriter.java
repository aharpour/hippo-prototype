package com.tdclighthouse.prototype.beanmapper;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.hippoecm.repository.api.StringCodecFactory.UriEncoding;
import org.hippoecm.repository.api.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tdclighthouse.hippo.beanmapper.BadFormatedBeanException;
import com.tdclighthouse.hippo.beanmapper.DynamicNode;
import com.tdclighthouse.hippo.beanmapper.DynamicNodeGenerator;
import com.tdclighthouse.hippo.beanmapper.annotations.NodeType;
import com.tdclighthouse.prototype.support.DocumentManager;
import com.tdclighthouse.prototype.support.DocumentManager.NodeModificationCallBack;

/**
 * @author Ebrahim Aharpour
 */
public class DynamicNodeWriter {

	private final Logger log = LoggerFactory.getLogger(DynamicNodeWriter.class);

	@Autowired
	private DocumentManager documentManager;
	private DynamicNodeUpdater beforeSaveUpdater;
	private boolean publish = false;
	private DateFormat dateFormat;

	private final UriEncoding uriEncoding = new UriEncoding();

	public String createOrUpdateNode(Object bean, String pathToFolder, String name) throws RepositoryException,
			PathNotFoundException, ParseException, BadFormatedBeanException, UnsupportedRepositoryOperationException,
			RemoteException, WorkflowException {
		Node node = perpareJcrNode(bean, pathToFolder, name);
		DynamicNode dynamicNode = perpareDynamicNode(bean, node);
		if (log.isDebugEnabled()) {
			log.debug("Saving the following dynamic node at \"{}\":\n{}", node.getPath(), dynamicNode.toString());
		}
		saveDynamicNode(node, dynamicNode);
		String identifier = node.getParent().getIdentifier();
		if (publish) {
			documentManager.publish(node);
		}
		if (bean instanceof UuidAware) {
			((UuidAware) bean).setUuid(identifier);
		}
		return identifier;
	}

	private DynamicNode perpareDynamicNode(Object bean, Node node) throws ParseException, BadFormatedBeanException,
			UnsupportedRepositoryOperationException, RepositoryException {
		DynamicNode dynamicNode = DynamicNodeGenerator.generate(bean, node.getSession().getValueFactory(), dateFormat);
		if (beforeSaveUpdater != null) {
			dynamicNode = beforeSaveUpdater.update(dynamicNode);
		}
		return dynamicNode;
	}

	private Node perpareJcrNode(Object bean, String pathToFolder, String name) throws BadFormatedBeanException,
			RepositoryException, PathNotFoundException {
		NodeType annotation = bean.getClass().getAnnotation(NodeType.class);
		if (annotation == null) {
			throw new BadFormatedBeanException(MessageFormat.format("{0} annotation is required",
					NodeType.class.getSimpleName()));
		}
		String type = annotation.value();
		Node folder = documentManager.getNode(pathToFolder);
		String nodeName = uriEncoding.encode(name);
		Node node = createOrFetchNode(type, folder, nodeName);
		return node;
	}

	private Node createOrFetchNode(String type, Node folder, String nodeName) throws RepositoryException,
			PathNotFoundException {
		Node node;
		if (folder.hasNode(nodeName) && folder.getNode(nodeName).hasNode(nodeName)
				&& folder.getNode(nodeName).getNode(nodeName).isNodeType(type)) {
			node = folder.getNode(nodeName);
			log.debug("Already existing node has been selected at \"{}\" of type \"{}\" to be updated", node.getPath(),
					type);
		} else {
			log.debug("creating a new node at \"{}\" with the name \"{}\" and of type \"{}\"",
					new Object[] { folder.getPath(), nodeName, type });
			String path = documentManager.createDocument(folder, nodeName, type);
			node = documentManager.getNode(path);
		}
		return node;
	}

	private void saveDynamicNode(final Node node, final DynamicNode dynamicNode) throws RepositoryException {
		documentManager.editDocument(new NodeModificationCallBack() {

			@Override
			public Node getNode() {
				return node;
			}

			@Override
			public void doAction(Node editableNode) throws RepositoryException {
				dynamicNode.save(editableNode);
			}
		});
	}

	public void setDocumentManager(DocumentManager documentManager) {
		this.documentManager = documentManager;
	}

	public void setBeforeSaveUpdater(DynamicNodeUpdater beforeSaveUpdater) {
		this.beforeSaveUpdater = beforeSaveUpdater;
	}

	@Required
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public interface DynamicNodeUpdater {

		public DynamicNode update(DynamicNode dynamicNode) throws RepositoryException;

	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}
}
