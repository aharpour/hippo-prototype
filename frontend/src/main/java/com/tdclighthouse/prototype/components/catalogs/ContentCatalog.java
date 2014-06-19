/*
 *  Copyright 2012 Smile B.V.
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
package com.tdclighthouse.prototype.components.catalogs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;

import com.tdclighthouse.prototype.components.AjaxEnabledComponent;
import com.tdclighthouse.prototype.componentsinfo.catalogs.ContentCatalogInfo;
import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 * 
 */
@ParametersInfo(type = ContentCatalogInfo.class)
public class ContentCatalog extends AjaxEnabledComponent<Map<String, Object>> {

    @Override
    public Map<String, Object> getModel(HstRequest request, HstResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();

        ContentCatalogInfo parametersInfo = (ContentCatalogInfo) getComponentParametersInfo(request);
        model.put("parameterInfo", parametersInfo);

        if (parametersInfo != null && StringUtils.isNotBlank(parametersInfo.getTemplate())) {
            response.setRenderPath("jcr:" + parametersInfo.getTemplate());
        }

        HippoBean contentBean = request.getRequestContext().getContentBean();
        if (contentBean != null) {
            model.put(Constants.Attributes.DOCUMENT, contentBean);
        }

        return model;
    }

}
