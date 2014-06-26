package com.tdclighthouse.prototype.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.PathUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdclighthouse.prototype.beans.FacetLinkConfigBean;
import com.tdclighthouse.prototype.componentsinfo.FacetDeepLinkNavInfo;
import com.tdclighthouse.prototype.utils.Constants.AttributesConstants;
import com.tdclighthouse.prototype.utils.FacetDeepLink;
import com.tdclighthouse.prototype.utils.exceptions.FacetDeepLinkExceptoin;

@ParametersInfo(type = FacetDeepLinkNavInfo.class)
public class FacetDeepLinkNav extends BaseHstComponent {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        request.setAttribute(AttributesConstants.MODEL, getModel(request, response));
    }

    private Map<String, Object> getModel(HstRequest request, HstResponse response) {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            HippoBean hippoBean = FacetDeepLink.getDeepLinkBean(getFacet(request), getFilters(request));
            model.put(AttributesConstants.FACET_BEAN, hippoBean);
            model.put(AttributesConstants.LABELS, new HashMap<String, String>());
            return model;
        } catch (FacetDeepLinkExceptoin e) {
            throw new HstComponentException(e.getMessage(), e);
        }
    }

    private Map<String, Object[]> getFilters(HstRequest request) {
        try {
            Map<String, Object[]> result = new HashMap<String, Object[]>();
            HippoBean contentBean = request.getRequestContext().getContentBean();
            Map<String, Object> properties = contentBean.getProperties();
            FacetDeepLinkNavInfo parametersInfo = getComponentParametersInfo(request);
            List<String> propertyNames = mapper.readValue(parametersInfo.getConfiguration(), FacetLinkConfigBean.class)
                    .getPropertyNames();
            for (Iterator<String> iterator = propertyNames.iterator(); iterator.hasNext();) {
                String propertyName = iterator.next();
                Object[] value = getPropertyAsArray(properties, propertyName);
                if (value != null) {
                    result.put(propertyName, value);
                }
            }
            return result;
        } catch (IOException e) {
            throw new HstComponentException(e.getMessage(), e);
        }
    }

    private Object[] getPropertyAsArray(Map<String, Object> properties, String propertyName) {
        Object[] result = null;
        Object object = properties.get(propertyName);
        if (object.getClass().isArray()) {
            result = (Object[]) object;
        } else {
            result = new Object[] { object };
        }
        return result;
    }

    private HippoFacetNavigationBean getFacet(HstRequest request) {
        HippoFacetNavigationBean result;
        FacetDeepLinkNavInfo parametersInfo = getComponentParametersInfo(request);
        String facetPath = parametersInfo.getFacetPath();
        Object bean = request.getRequestContext().getSiteContentBaseBean().getBean(PathUtils.normalizePath(facetPath));
        if (bean instanceof HippoFacetNavigationBean) {
            result = (HippoFacetNavigationBean) bean;
        } else {
            throw new HstComponentException("component miss configuration: a faceted navigation was not found at "
                    + facetPath);
        }
        return result;
    }

}
