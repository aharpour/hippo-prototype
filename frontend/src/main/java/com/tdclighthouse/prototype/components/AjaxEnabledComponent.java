/*
 *  Copyright 2013 Smile B.V.
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
package com.tdclighthouse.prototype.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.utils.Constants.AttributesConstants;
import com.tdclighthouse.prototype.utils.Constants.EncodingsConstants;
import com.tdclighthouse.prototype.utils.Constants.MimeTypeConstants;
import com.tdclighthouse.prototype.utils.Constants.SpringComponentsConstants;
import com.tdclighthouse.prototype.utils.MIMEParse;
import com.tdclighthouse.prototype.utils.ObjectSerializer;

/**
 * @author Ebrahim Aharpour
 *
 */
public abstract class AjaxEnabledComponent extends AbstractComponent {

    public static final String BLANK_TEMPLATE = "";

    private static final Logger LOG = LoggerFactory.getLogger(AjaxEnabledComponent.class);

    private final ObjectSerializer jsonSerializer = HstServices.getComponentManager().getComponent(
            SpringComponentsConstants.JSON_SERIALIZER);

    private final ObjectSerializer xmlSerializer = HstServices.getComponentManager().getComponent(
            SpringComponentsConstants.XML_SERIALIZER);

    public Object getJsonAjaxModel(HstRequest request, HstResponse response) {
        return getModel(request, response);
    }

    public Object getXmlAjaxModel(HstRequest request, HstResponse response) {
        return getModel(request, response);
    }

    public Object getHtmlAjaxModel(HstRequest request, HstResponse response) {
        return getModel(request, response);
    }

    /**
     * Override this method If you want to have different HTML template then
     * default for you Ajax call.
     * 
     * @param request
     *            HstRequest.
     * @param response
     *            HstResponse;
     * @return a String containing a template name or null if you want to use
     *         the template configured via HST configuration.
     */
    public String getAjaxTemplate(HstRequest request, HstResponse response) {
        return null;
    }

    @Override
    public final void doBeforeServeResource(HstRequest request, HstResponse response) throws HstComponentException {
        try {
            String acceptHeader = request.getHeader("Accept");
            String bestMatch = MIMEParse.bestMatch(ECPECTED_MIME_TYPES, acceptHeader);
            if (MimeTypeConstants.APPLICATION_JSON.equals(bestMatch)
                    || MimeTypeConstants.TEXT_JAVASCRIPT.equals(bestMatch)) {
                if (jsonSerializer != null) {
                    returnJsonResponse(request, response);
                } else {
                    LOG.error("JSON is not support since a jsonSerializer has not been provied.");
                    returnHtmlResponse(request, response);
                }
            } else if (MimeTypeConstants.APPLICATION_XML.equals(bestMatch)
                    || MimeTypeConstants.TEXT_XML.equals(bestMatch)) {
                if (xmlSerializer != null) {
                    returnXmlResponse(request, response);
                } else {
                    LOG.error("XML is not support since a xmlSerializer has not been provied.");
                    returnHtmlResponse(request, response);
                }
            } else {
                returnHtmlResponse(request, response);
            }
        } catch (Exception e) {
            throw new HstComponentException(e.getLocalizedMessage(), e);
        }

    }

    private final void returnHtmlResponse(HstRequest request, HstResponse response) throws Exception {
        request.setAttribute(AttributesConstants.MODEL, getHtmlAjaxModel(request, response));
        String ajaxTemplate = getAjaxTemplate(request, response);
        if (ajaxTemplate != null) {
            response.setServeResourcePath(ajaxTemplate);
        }
    }

    private void returnXmlResponse(HstRequest request, HstResponse response) throws Exception {
        Object model = getXmlAjaxModel(request, response);
        response.setServeResourcePath(BLANK_TEMPLATE);
        response.setContentType(MimeTypeConstants.APPLICATION_XML);
        response.setCharacterEncoding(getCharacterEndcoding());
        xmlSerializer.serialize(model, response.getOutputStream());
    }

    private void returnJsonResponse(HstRequest request, HstResponse response) throws Exception {
        Object model = getJsonAjaxModel(request, response);
        jsonSerializer.serialize(model, response.getOutputStream());
        response.setServeResourcePath(BLANK_TEMPLATE);
        response.setContentType(MimeTypeConstants.APPLICATION_JSON);
        response.setCharacterEncoding(getCharacterEndcoding());
    }

    protected String getCharacterEndcoding() {
        return EncodingsConstants.UTF8;
    }

    @SuppressWarnings("unchecked")
    private final static List<String> ECPECTED_MIME_TYPES = ListUtils.unmodifiableList(new ArrayList<String>() {
        private static final long serialVersionUID = 1L;

        {
            add(MimeTypeConstants.TEXT_HTML);
            add(MimeTypeConstants.APPLICATION_XHTML_XML);
            add(MimeTypeConstants.APPLICATION_JSON);
            add(MimeTypeConstants.TEXT_JAVASCRIPT);
            add(MimeTypeConstants.APPLICATION_XML);
            add(MimeTypeConstants.TEXT_XML);
        }
    });

}
