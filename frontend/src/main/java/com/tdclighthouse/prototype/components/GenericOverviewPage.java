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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;

import com.tdclighthouse.prototype.componentsinfo.GenericOverviewPageInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.PaginatorWidget;

/**
 * @author Ebrahim Aharpour
 *
 */
@ParametersInfo(type = GenericOverviewPageInfo.class)
public class GenericOverviewPage extends BaseTdcComponent {

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		try {
			setDocumentToRequest(request);

			GenericOverviewPageInfo parametersInfo = getParametersInfo(request);
			HstQuery query = getQuery(request);
			HstQueryResult queryResult = query.execute();
			PaginatorWidget paginator = setPaginator(request, getPageSize(request), queryResult.getTotalSize());
			List<HippoBean> items = getItems(queryResult);
			
			request.setAttribute(Constants.Attributes.ITEMS, items);
			if (parametersInfo.getShowPaginator()) {
				request.setAttribute(Constants.Attributes.PAGINATOR, paginator);
			}

		} catch (QueryException e) {
			throw new HstComponentException(e);
		}
	}

	private List<HippoBean> getItems(HstQueryResult queryResult) {
		List<HippoBean> items = new ArrayList<HippoBean>();
		for (HippoBeanIterator hippoBeans = queryResult.getHippoBeans(); hippoBeans.hasNext();) {
			items.add(hippoBeans.nextHippoBean());
		}
		return items;
	}

	private void setDocumentToRequest(HstRequest request) {
		HippoBean contentBean = getContentBean(request);
		if (contentBean != null) {
			request.setAttribute(Constants.Attributes.DOCUMENT, contentBean);
		}
	}

	protected HstQuery getQuery(HstRequest request) throws QueryException {
		GenericOverviewPageInfo parametersInfo = getParametersInfo(request);
		HippoBean scope = getQueryScope(request, parametersInfo);
		HstQuery query = getQueryManager(request).createQuery(scope, parametersInfo.getShowTypes());

		String sortBy = parametersInfo.getSortBy();
		if (StringUtils.isNotBlank(sortBy)) {
			if (Constants.Values.DESCENDING.equals(parametersInfo.getSortOrder())) {
				query.addOrderByDescending(sortBy);
			} else {
				query.addOrderByAscending(sortBy);
			}
		}

		setLimitAndOffset(request, query);
		addFilter(query);
		return query;
	}

	protected void addFilter(HstQuery query) {
	}

	private void setLimitAndOffset(HstRequest request, HstQuery query) {
		int pageSize = getPageSize(request);
		int pageNumber = getPageNumber(request);
		query.setLimit(pageSize);
		query.setOffset((pageNumber - 1) * pageSize);
	}
	
	protected HippoBean getQueryScope(HstRequest request, GenericOverviewPageInfo parametersInfo) {
		HippoBean scope = getContentBean(request);
		if (!(scope instanceof HippoFolderBean)) {
			scope = getContentBeanViaParameters(request, parametersInfo);
		}
		return scope;
	}

}
