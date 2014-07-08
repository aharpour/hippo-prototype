package com.tdclighthouse.prototype.components;

import javax.servlet.http.HttpServletResponse;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;

import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.utils.BeanUtils;
import com.tdclighthouse.prototype.utils.Constants.AttributesConstants;

@ParametersInfo(type = ContentBeanPathInfo.class)
public class CustomNotFound extends BaseHstComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        ContentBeanPathInfo componentParametersInfo = getComponentParametersInfo(request);
        HippoBean bean = BeanUtils.getContentBeanViaParameters(request, componentParametersInfo);
        request.setAttribute(AttributesConstants.DOCUMENT, bean);
    }
}
