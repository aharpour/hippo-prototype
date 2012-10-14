package com.tdclighthouse.prototype.utils;

import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.NodeType;

import org.hippoecm.repository.api.HippoNode;

public class NodeUtils {

	protected NodeUtils() {
	}

	public static Node getTranslation(Locale locale, Node node) throws ValueFormatException, RepositoryException,
			PathNotFoundException {
		validateTranslatability(node);
		Node result = null;
		if (!locale.getLanguage().equalsIgnoreCase(
				node.getProperty(Constants.PropertyName.HIPPOTRANSLATION_LOCALE).getString())) {
			try {
				Node translations = node.getNode(Constants.NodeName.HIPPOTRANSLATION_TRANSLATIONS);
				result = ((HippoNode) translations.getNode(locale.getLanguage())).getCanonicalNode();
			} catch (PathNotFoundException e) {
				// ignore
			}
		} else {
			result = node;
		}
		return result;
	}

	public static Property getProperty(Node node, String propertyName) throws RepositoryException,
			PathNotFoundException {
		Property property = null;
		if (node.hasProperty(propertyName)) {
			property = node.getProperty(propertyName);
		}
		return property;
	}

	private static void validateTranslatability(Node node) throws RepositoryException {
		NodeType[] mixinNodeTypes = node.getMixinNodeTypes();
		boolean hasTheRightMixin = false;
		for (NodeType nodeType : mixinNodeTypes) {
			if (nodeType.isNodeType("hippotranslation:translated")) {
				hasTheRightMixin = true;
				break;
			}
		}
		if (!hasTheRightMixin) {
			throw new IllegalArgumentException("the given node is not a translated node");
		}
	}

}
