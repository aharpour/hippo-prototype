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

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.util.ContentBeanUtils;

import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.SearchQueryUtils;

/**
 * @author Ebrahim Aharpour
 * 
 */
public abstract class FacetSupport<M> extends AjaxEnabledComponent<M> {

    protected HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
        HippoFacetNavigationBean result = null;
        HippoBean contentBean = request.getRequestContext().getContentBean();
        if (contentBean instanceof HippoFacetNavigationBean) {
            result = (HippoFacetNavigationBean) contentBean;
        } else {
            Object parametersInfo = getComponentParametersInfo(request);
            if (parametersInfo instanceof ContentBeanPathInfo) {
                HippoBean bean = getContentBeanViaParameters(request, (ContentBeanPathInfo) parametersInfo);
                if (bean instanceof HippoFacetNavigationBean) {
                    result = (HippoFacetNavigationBean) bean;
                }
            }
        }
        return result;
    }

    protected HippoBean obtainContentBean(HstRequest request) {
        HippoBean result = null;
        HippoBean contentBean = request.getRequestContext().getContentBean();
        if (!(contentBean instanceof HippoFacetNavigationBean)) {
            result = contentBean;
        } else {
            HippoBean bean = getContentBeanViaParameters(request,
                    (ContentBeanPathInfo) getComponentParametersInfo(request));
            if (!(bean instanceof HippoFacetNavigationBean)) {
                result = bean;
            }
        }
        return result;
    }

    protected HippoFacetNavigationBean applyQueryToFacetBean(HstRequest request, HippoFacetNavigationBean facetedNavBean) {
        try {
            HstQuery hstQuery = getHstQuery(request);
            if (hstQuery != null) {
                facetedNavBean = ContentBeanUtils.getFacetNavigationBean(hstQuery,
                        absolutToRelativePath(facetedNavBean.getPath(), request));
            } else {
                String queryString = getPublicRequestParameter(request, Constants.ParametersConstants.QUERY);
                if (StringUtils.isNotBlank(queryString)) {
                    queryString = SearchQueryUtils.parseAndEscapeBadCharacters(enhanceQuery(queryString));
                    if (StringUtils.isNotBlank(queryString)) {
                        facetedNavBean = ContentBeanUtils.getFacetNavigationBean(
                                absolutToRelativePath(facetedNavBean.getPath(), request), queryString);
                    }
                }
            }
        } catch (QueryException e) {
            throw new HstComponentException(e);
        }
        return facetedNavBean;
    }

    protected String enhanceQuery(String query) {
        return query;
    }

    protected HstQuery getHstQuery(HstRequest request) throws QueryException {
        return null;
    }

}
