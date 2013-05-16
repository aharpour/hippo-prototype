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
public class FacetedOverview extends FacetSupport {

	public static final Logger log = LoggerFactory.getLogger(FacetedOverview.class);

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		setContentBean(request);
		setItems(request);
		setQueryIfExists(request);
	}

	protected void setContentBean(HstRequest request) {
		HippoBean contentBean = obtainContentBean(request);
		if (contentBean != null) {
			request.setAttribute(Constants.Attributes.DOCUMENT, contentBean);
		} else {
			log.error("No content bean was found.");
		}
	}

	protected void setItems(HstRequest request) {
		HippoFacetNavigationBean facetedNavBean = getFacetNavigationBean(request);
		if (facetedNavBean != null) {
			facetedNavBean = applyQueryToFacetBean(request, facetedNavBean);
			HippoResultSetBean resultSet = facetedNavBean.getResultSet();
			PaginatorWidget paginatorWidget = setPaginator(request, getPageSize(request), resultSet.getCount()
					.intValue());
			request.setAttribute(Constants.Attributes.FACET_BEAN, facetedNavBean);
			request.setAttribute(Constants.Attributes.ITEMS, getItemsFromResultSet(resultSet, paginatorWidget));
		} else {
			throw new HstComponentException("content Bean is not of the type HippoFactNavigation");
		}
	}

	protected void setQueryIfExists(HstRequest request) {
		String query = getPublicRequestParameter(request, Constants.Parameters.QUERY);
		if (StringUtils.isNotBlank(query)) {
			request.setAttribute(Constants.Attributes.QUERY, query);
		}
	}

}
