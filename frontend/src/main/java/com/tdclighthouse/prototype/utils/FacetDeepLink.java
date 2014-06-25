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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;

/**
 * @author Ebrahim Aharpour
 *
 */
public class FacetDeepLink {

    private FacetDeepLink() {
    }

    public static HippoFacetNavigationBean getDeepLinkBean(HippoFacetNavigationBean facet, Map<String, String[]> filter)
            throws FacetDeepLinkExceptoin {
        HippoFacetNavigationBean targetNode = facet;
        String[] facetedNames = facet.getProperty(Constants.HippoFacetAttributesConstants.HIPPOFACNAV_FACETNODENAMES);
        String[] facets = facet.getProperty(Constants.HippoFacetAttributesConstants.HIPPOFACNAV_FACETS);
        validateInput(facetedNames, facets);
        Map<String, String> facetMap = getFacetMap(facetedNames, facets);
        for (Iterator<Entry<String, String[]>> iterator = filter.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, String[]> entry = iterator.next();
            String propertyName = entry.getKey();
            String[] propertyValues = entry.getValue();
            String facetName = facetMap.get(propertyName);
            if (StringUtils.isNotBlank(facetName)) {
                for (String value : propertyValues) {
                    targetNode = getFilterNode(targetNode, facetName, value);
                }
            } else {
                throw new NotFacetedPropertyExceptoin("the given facet does not facet on the property: " + propertyName);
            }
        }
        return targetNode;
    }

    private static HippoFacetNavigationBean getFilterNode(HippoFacetNavigationBean targetNode, String facetName,
            String value) throws NodeNotFoundExceptoin {
        HippoFacetNavigationBean result = targetNode.getBean(facetName);
        if (result != null) {
            result = result.getBean(value);
        }
        if (result == null) {
            throw new NodeNotFoundExceptoin();
        }
        return result;
    }

    public static Map<String, String> getFacetMap(String[] facetedNames, String[] facets) {
        Map<String, String> facetMap = new HashMap<String, String>();
        for (int i = 0; i < facets.length; i++) {
            facetMap.put(facets[i], cleanFacetName(facetedNames[i]));
        }
        return facetMap;
    }

    private static String cleanFacetName(String facetName) {
        String result = facetName;
        int indexOfExpression = facetName.indexOf("$");
        if (indexOfExpression > 0) {
            result = facetName.substring(0, indexOfExpression);
        }
        return result;
    }

    private static void validateInput(String[] facetedNames, String[] facets) {
        if (facetedNames.length != facets.length) {
            throw new IllegalArgumentException(
                    "\"hippofacnav:facetnodenames\" and \"hippofacnav:facets\" has to have the same lenght.");
        }
    }

    public static class FacetDeepLinkExceptoin extends Exception {

        private static final long serialVersionUID = 1L;

        public FacetDeepLinkExceptoin() {
        }

        public FacetDeepLinkExceptoin(String string) {
            super(string);
        }

    }

    public static class NodeNotFoundExceptoin extends FacetDeepLinkExceptoin {

        private static final long serialVersionUID = 1L;

        public NodeNotFoundExceptoin() {
        }

        public NodeNotFoundExceptoin(String string) {
            super(string);
        }

    }

    public static class NotFacetedPropertyExceptoin extends FacetDeepLinkExceptoin {

        private static final long serialVersionUID = 1L;

        public NotFacetedPropertyExceptoin() {
        }

        public NotFacetedPropertyExceptoin(String message) {
            super(message);
        }

    }
}
