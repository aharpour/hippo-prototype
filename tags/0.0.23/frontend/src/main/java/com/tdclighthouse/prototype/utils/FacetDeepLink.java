/*
 *  Copyright 2012 Finalist B.V.
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
package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;

/**
 * @author Ebrahim Aharpour
 *
 */
public class FacetDeepLink {

	public static HippoBean getDeepLinkBean(HippoBean facet, Map<String, String[]> filter)
			throws NodeNotFoundExceptoin, NotFacetedPropertyExceptoin {
		HippoBean targetNode = facet;
		String[] facetedNames = facet.getProperty(Constants.HippoFactAttributes.HIPPOFACNAV_FACETNODENAMES);
		String[] facets = facet.getProperty(Constants.HippoFactAttributes.HIPPOFACNAV_FACETS);
		validateInput(facetedNames, facets);
		Map<String, String> facetMap = getFacetMap(facetedNames, facets);
		Iterator<String> keyIterator = filter.keySet().iterator();
		while (keyIterator.hasNext()) {
			String propertyName = keyIterator.next();
			String[] propertyValues = filter.get(propertyName);
			String facetName = facetMap.get(propertyName);
			if (StringUtils.isNotBlank(facetName)) {
				for (String value : propertyValues) {
					targetNode = getFilterNode(targetNode, facetName, value);
				}
			} else {
				throw new NotFacetedPropertyExceptoin("the given fact does not facet on the property: " + propertyName);
			}
		}
		return targetNode;
	}

	private static HippoBean getFilterNode(HippoBean targetNode, String facetName, String value)
			throws NodeNotFoundExceptoin {
		targetNode = targetNode.getBean(facetName);
		if (targetNode != null) {
			targetNode = targetNode.getBean(value);
		}
		if (targetNode == null) {
			throw new NodeNotFoundExceptoin();
		}
		return targetNode;
	}

	private static Map<String, String> getFacetMap(String[] facetedNames, String[] facets) {
		Map<String, String> facetMap = new HashMap<String, String>();
		for (int i = 0; i < facets.length; i++) {
			facetMap.put(facets[i], cleanFacetName(facetedNames[i]));
		}
		return facetMap;
	}

	private static String cleanFacetName(String facetName) {
		int indexOfExpression = facetName.indexOf("${");
		if (indexOfExpression > 0) {
			facetName = facetName.substring(0, indexOfExpression);
		}
		return facetName;
	}

	private static void validateInput(String[] facetedNames, String[] facets) {
		if (facetedNames.length != facets.length) {
			throw new IllegalArgumentException(
					"\"hippofacnav:facetnodenames\" and \"hippofacnav:facets\" has to have the same lenght.");
		}
	}

	public static class NodeNotFoundExceptoin extends Exception {

		private static final long serialVersionUID = 1L;

		public NodeNotFoundExceptoin() {
		}

		public NodeNotFoundExceptoin(String string) {
			super(string);
		}

	}

	public static class NotFacetedPropertyExceptoin extends Exception {

		private static final long serialVersionUID = 1L;

		public NotFacetedPropertyExceptoin() {
		}

		public NotFacetedPropertyExceptoin(String message) {
			super(message);
		}

	}
}
