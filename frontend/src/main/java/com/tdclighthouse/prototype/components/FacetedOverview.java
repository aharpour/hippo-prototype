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
package com.tdclighthouse.prototype.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoResultSetBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.componentsinfo.FacetedOverviewPageInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.PaginatorWidget;

/**
 * @author Ebrahim Aharpour
 * 
 */
@ParametersInfo(type = FacetedOverviewPageInfo.class)
public class FacetedOverview extends FacetSupport<Map<String, Object>> {

    private static final Logger LOG = LoggerFactory.getLogger(FacetedOverview.class);

    @Override
    public Map<String, Object> getModel(HstRequest request, HstResponse response) {
        Map<String, Object> model = new HashMap<String, Object>();
        setContentBean(request, model);
        setItems(request, model);
        setQueryIfExists(request, model);
        return model;
    }

    @Override
    public Object getJsonAjaxModel(HstRequest request, HstResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();
        setItems(request, map);
        return map.get(Constants.Attributes.ITEMS);
    }

    @Override
    public Object getXmlAjaxModel(HstRequest request, HstResponse response) {
        return getJsonAjaxModel(request, response);
    }

    protected void setContentBean(HstRequest request, Map<String, Object> model) {
        HippoBean contentBean = obtainContentBean(request);
        if (contentBean != null) {
            model.put(Constants.Attributes.DOCUMENT, contentBean);
        } else {
            LOG.error("No content bean was found.");
        }
    }

    protected void setItems(HstRequest request, Map<String, Object> model) {
        HippoFacetNavigationBean facetedNavBean = getFacetNavigationBean(request);
        if (facetedNavBean != null) {
            facetedNavBean = applyQueryToFacetBean(request, facetedNavBean);
            HippoResultSetBean resultSet = facetedNavBean.getResultSet();
            PaginatorWidget paginatorWidget = getPaginator(request, getPageSize(request), resultSet.getCount()
                    .intValue());
            model.put(Constants.Attributes.FACET_BEAN, facetedNavBean);
            model.put(Constants.Attributes.ITEMS, getItemsFromResultSet(resultSet, paginatorWidget));
            FacetedOverviewPageInfo parametersInfo = getComponentParametersInfo(request);
            if (parametersInfo.getShowPaginator()) {
                request.setAttribute(Constants.Attributes.PAGINATOR, paginatorWidget);
            }
        } else {
            throw new HstComponentException("content Bean is not of the type HippoFactNavigation");
        }
    }

    protected void setQueryIfExists(HstRequest request, Map<String, Object> model) {
        String query = getPublicRequestParameter(request, Constants.Parameters.QUERY);
        if (StringUtils.isNotBlank(query)) {
            model.put(Constants.Attributes.QUERY, query);
        }
    }

}
