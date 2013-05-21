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
package com.tdclighthouse.prototype.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.utils.BeanUtils;

import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.FacetedNavigationInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.SearchQueryUtils;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class FacetSupport extends BaseTdcComponent {

	protected HippoFacetNavigationBean getFacetNavigationBean(HstRequest request) {
		HippoFacetNavigationBean result = null;
		HippoBean contentBean = getContentBean(request);
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
		HippoBean contentBean = getContentBean(request);
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
				facetedNavBean = BeanUtils.getFacetNavigationBean(request, hstQuery,
						absolutToRelativePath(facetedNavBean.getPath(), request), getObjectConverter());
			} else {
				String queryString = getPublicRequestParameter(request, Constants.Parameters.QUERY);
				if (StringUtils.isNotBlank(queryString)) {
					queryString = SearchQueryUtils.parseAndEscapeBadCharacters(enhanceQuery(queryString));
					if (StringUtils.isNotBlank(queryString)) {
						facetedNavBean = BeanUtils.getFacetNavigationBean(request,
								absolutToRelativePath(facetedNavBean.getPath(), request), queryString,
								getObjectConverter());
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

	protected Map<String, String> getLabels(HstRequest request) {
		Map<String, String> labels = new HashMap<String, String>();
		Object parametersInfo = getComponentParametersInfo(request);
		if (parametersInfo instanceof FacetedNavigationInfo) {
			FacetedNavigationInfo parameters = (FacetedNavigationInfo) parametersInfo;
			String labelPaths = parameters.getLabelPaths();
			if (StringUtils.isNotBlank(labelPaths)) {
				String[] paths = labelPaths.split(",");
				for (String path : paths) {
					labels.putAll(getSelectionOptionsMap(request, path, request.getLocale().getLanguage()));
				}
			}
		}
		return labels;
	}
}
