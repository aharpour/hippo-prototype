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
package com.tdclighthouse.prototype.components;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.utils.ParameterUtils;

import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.utils.BeanUtils;
import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 * 
 */
@ParametersInfo(type = ContentBeanPathInfo.class)
public class WebDocumentDetail extends BaseHstComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        HippoBean contentBean = BeanUtils.getWebDocumetBean(request, getComponentParametersInfo(request));
        if (contentBean != null) {
            request.setAttribute(Constants.AttributesConstants.DOCUMENT, contentBean);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T getComponentParameters(String parameterName, String defaultValue, Class<T> type) {
        T result = null;
        String parameterValue = getComponentParameter(parameterName);
        if (StringUtils.isBlank(parameterValue)) {
            parameterValue = defaultValue;
        }
        Object converted = ParameterUtils.DEFAULT_HST_PARAMETER_VALUE_CONVERTER.convert(parameterValue, type);
        if (type.isAssignableFrom(converted.getClass())) {
            result = (T) converted;
        }
        return result;
    }

}
