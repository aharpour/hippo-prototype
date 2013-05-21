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

/**
 * @author Ebrahim Aharpour
 *
 */
public class Constants {

	public static class Attributes {
		public static final String DOCUMENT = "document";
		public static final String FACET_BEAN = "facetBean";
		public static final String ITEMS = "items";
		public static final String ANCESTORS = "ancestors";
		public static final String PAGINATOR = "paginator";
		public static final String LABELS = "labels";
		public static final String FACETNAV = "facetnav";
		public static final String CHILDNAV = "childNav";
		public static final String QUERY = "query";
		public static final String JSON = "json";
		public static final String ERROR_MESSAGE = "errorMessage";
	}

	public static class FieldName {
		public static final String TDC_RELEASE_DATE = "tdc:releaseDate";
		public static final String TDC_TITLE = "tdc:title";
	}

	public static class HippoFactAttributes {
		public static final String HIPPOFACNAV_FACETNODENAMES = "hippofacnav:facetnodenames";
		public static final String HIPPOFACNAV_FACETS = "hippofacnav:facets";
	}

	public static class HippoNodeTypes {
		public static final String IMAGE_NODE_TYPE = "hippogallery:imageset";
		public static final String ASSET_NODE_TYPE = "hippogallery:exampleAssetSet";
	}

	public static class HttpHeader {
		public static final String AGE = "Age";
		public static final String CACHE_CONTROL = "Cache-Control";
		public static final String MAX_AGE = "max-age";
	}

	public static class MimeType {
		public static final String APPLICATION_JSON = "application/json";
	}

	public static class Parameters {
		public static final String QUERY = "q";
		public static final String PAGE = "page";
		public static final String PAGE_SIZE = "size";
		public static final String PATH = "path";
	}

	public static class HstParameters {
		public static final String CONTENT_BEAN_PATH = "contentBeanPath";
	}

	public static class Values {
		public static final String DESCENDING = "descending";
	}
}