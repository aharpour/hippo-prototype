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
package com.tdclighthouse.prototype.utils;

import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.nodetype.NodeType;

import org.hippoecm.repository.api.HippoNode;

/**
 * @author Ebrahim Aharpour
 *
 */
public class NodeUtils {

	protected NodeUtils() {
	}

	public static Node getTranslation(Locale locale, Node node) throws ValueFormatException, RepositoryException,
			PathNotFoundException {
		validateTranslatability(node);
		Node result = null;
		if (!locale.getLanguage().equalsIgnoreCase(
				node.getProperty(PluginConstants.PropertyName.HIPPOTRANSLATION_LOCALE).getString())) {
			try {
				Node translations = node.getNode(PluginConstants.NodeName.HIPPOTRANSLATION_TRANSLATIONS);
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

	public static String normalizeRelativePath(String relativePath) {
		return (relativePath.startsWith("/") ? relativePath.substring(1) : relativePath);
	}

	public static String normalizeAbsolutePath(String relativePath) {
		return (relativePath.startsWith("/") ? relativePath : "/" + relativePath);
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
