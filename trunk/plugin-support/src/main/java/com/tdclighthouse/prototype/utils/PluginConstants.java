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
public class PluginConstants {

	protected PluginConstants() {
	}

	public static class NodeName {
		public static final String HIPPOTRANSLATION_TRANSLATIONS = "hippotranslation:translations";
		public static final String TDC_FLEXIBLE_BLOCK = "tdc:flexibleBlock";
		public static final String TDC_LINK = "tdc:link";
		public static final String HIPPOGALLERY_ASSET = "hippogallery:asset";
	}

	public static class NodeType {
		public static final String HIPPO_HANDLE = "hippo:handle";
		public static final String HIPPOGALLERYPICKER_IMAGELINK = "hippogallerypicker:imagelink";
		public static final String TDC_PARAGRAPH_IMAGE = "tdc:ParagraphImage";
		public static final String HIPPOGALLERY_STD_IMAGE_GALLERY = "image gallery";
		public static final String FRONTEND_PLUGINCONFIG = "frontend:pluginconfig";
		public static final String HIPPOGALLERY_IMAGE = "hippogallery:image";
		public static final String HIPPO_FACETSELECT = "hippo:facetselect";
		public static final String HIPPO_RESOURCE = "hippo:resource";
		public static final String HIPPOSTD_HTML = "hippostd:html";
		public static final String HIPPOSTD_FOLDER = "hippostd:folder";
		public static final String HIPPOGALLERY_EXAMPLE_ASSET_SET = "hippogallery:exampleAssetSet";
		public static final String HIPPOGALLERY_STD_ASSET_GALLERY = "asset gallery";
	}

	public static class PropertyName {
		public static final String HIPPOTRANSLATION_ID = "hippotranslation:id";
		public static final String HIPPOSTD_STATE = "hippostd:state";
		public static final String HIPPOTRANSLATION_LOCALE = "hippotranslation:locale";
		public static final String TDC_TITLE = "tdc:title";
		public static final String HIPPO_DOCBASE = "hippo:docbase";
		public static final String HIPPOSTD_CONTENT = "hippostd:content";
		public static final String HIPPO_MODES = "hippo:modes";
		public static final String HIPPO_VALUES = "hippo:values";
		public static final String HIPPO_FACETS = "hippo:facets";
		public static final String TDC_CAPTION = "tdc:caption";
		public static final String TDC_ALT = "tdc:alt";
		public static final String TDC_HORIZONTAL_POSITION = "tdc:horizontalPosition";
		public static final String HIPPOGALLERY_WIDTH = "hippogallery:width";
		public static final String HIPPOGALLERY_HEIGHT = "hippogallery:height";
		public static final String JCR_LAST_MODIFIED = "jcr:lastModified";
		public static final String JCR_MIME_TYPE = "jcr:mimeType";
		public static final String JCR_DATA = "jcr:data";
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";
		public static final String HIPPOSTDPUBWF_LOCALE = "hippostdpubwf:locale";
	}

	public static class WorkflowName {
		public static final String INTERNAL = "internal";
		public static final String DEFAULT = "default";
		public static final String SHORTCUTS = "shortcuts";
	}
	
	public static class Uuids {
		public static final String ROOT_NODE = "";
	}

	public static class MixinTypes {
	}

	public static class Paths {
		public static final String GALLERY_PROCESSOR_SERVICE = "/hippo:configuration/hippo:frontend/cms/cms-services/galleryProcessorService";
		public static final String GALLERY = "/content/gallery";
		public static final String ASSETS = "/content/assets";
		public static final String DOCUMENTS = "/content/documents";
	}
	
	public static class Regex {
		public static final String COMMA_SEPARATOR = "\\s*,\\s*";
	}

}
