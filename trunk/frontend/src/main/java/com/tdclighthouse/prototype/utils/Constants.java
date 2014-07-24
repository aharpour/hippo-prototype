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

/**
 * @author Ebrahim Aharpour
 * 
 */
public class Constants {

    public static class AttributesConstants {

        public static final String DOCUMENT = "document";
        public static final String MODEL = "model";
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
        public static final String WARN_MESSAGE = "warningMessage";
        public static final String WEBMASTER_ERROR_MESSAGE = "webmasterErrorMessage";
        public static final String WEBMASTER_WARN_MESSAGE = "webmasterWarningMessage";
        public static final String MENU = "menu";
        public static final String CHANNEL_INFO = "channelInfo";
        public static final String PARAM_INFO = "paramInfo";

        protected AttributesConstants() {
            super();
        }

    }

    public static class FieldNameConstants {

        public static final String TDC_RELEASE_DATE = "tdc:releaseDate";
        public static final String TDC_TITLE = "tdc:title";

        protected FieldNameConstants() {
            super();
        }
    }

    public static class HippoFacetAttributesConstants {

        public static final String HIPPOFACNAV_FACETNODENAMES = "hippofacnav:facetnodenames";
        public static final String HIPPOFACNAV_FACETS = "hippofacnav:facets";
        public static final String HIPPO_DOCBASE = "hippo:docbase";

        protected HippoFacetAttributesConstants() {
            super();
        }

    }

    public static class HippoNodeTypesConstants {

        public static final String IMAGE_NODE_TYPE = "hippogallery:imageset";
        public static final String ASSET_NODE_TYPE = "hippogallery:exampleAssetSet";

        protected HippoNodeTypesConstants() {
            super();
        }

    }

    public static class HttpHeaderConstants {

        public static final String AGE = "Age";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String MAX_AGE = "max-age";

        protected HttpHeaderConstants() {
            super();
        }

    }

    public static class MimeTypeConstants {
        public static final String APPLICATION_JSON = "application/json";
        public static final String TEXT_XML = "text/xml";
        public static final String APPLICATION_XML = "application/xml";
        public static final String TEXT_JAVASCRIPT = "text/javascript";
        public static final String TEXT_HTML = "text/html";
        public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";

        protected MimeTypeConstants() {
            super();
        }

    }

    public static class ParametersConstants {
        public static final String QUERY = "q";
        public static final String PAGE = "page";
        public static final String PAGE_SIZE = "size";
        public static final String PATH = "path";

        protected ParametersConstants() {
            super();
        }

    }

    public static class HstParametersConstants {
        public static final String CONTENT_BEAN_PATH = "contentBeanPath";
        public static final String EXPANDED = "expanded";
        public static final String EXPAND_ONLY_CURRENT_ITEM = "expandOnlyCurrentItem";
        public static final String ROOT = "root";
        public static final String TEMPLATE = "template";
        public static final String INVISIBLE = "invisible";
        public static final String DISABLED = "disabled";

        protected HstParametersConstants() {
            super();
        }
    }

    public static class ValuesConstants {
        public static final String ASCENDING = "ascending";
        public static final String DESCENDING = "descending";
        public static final String TRUE = "true";

        protected ValuesConstants() {
            super();
        }
    }

    public static class PikcerTypesConstants {
        public static final String ASSET_PICKER = "cms-pickers/assets";
        public static final String DOCUMENT_PICKER = "cms-pickers/documents";
        public static final String FOLDER_PICKER = "cms-pickers/folders";
        public static final String IMAGE_PICKER = "cms-pickers/images";
        public static final String DOCUMENT_PICKER_ONLY = "cms-pickers/documents-only";

        protected PikcerTypesConstants() {
            super();
        }

    }

    public static class NodeNameConstants {
        public static final String INDEX = "index";

        protected NodeNameConstants() {
            super();
        }

    }

    public static class RegexConstants {
        public static final String COMMA_SEPARATOR = "\\s*,\\s*";

        protected RegexConstants() {
            super();
        }

    }

    public static class EncodingsConstants {
        public static final String UTF8 = "UTF-8";

        protected EncodingsConstants() {
            super();
        }

    }

    public static class SchemesConstants {
        public static final String HTTP = "http";
        public static final String HTTPS = "https";

        protected SchemesConstants() {
            super();
        }

    }

    public static class SpringComponentsConstants {
        public static final String JSON_SERIALIZER = "com.tdclighthouse.prototype.utils.JsonSerializer";
        public static final String XML_SERIALIZER = "com.tdclighthouse.prototype.utils.XmlSerializer";

        protected SpringComponentsConstants() {
            super();
        }

    }

    protected Constants() {
        super();
    }

}
