package com.tdclighthouse.prototype.components;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;

import com.tdclighthouse.prototype.componentsinfo.GenericOverviewPageInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.PaginatorWidget;

@ParametersInfo(type = GenericOverviewPageInfo.class)
public class GenericOverviewPage extends BaseTdcComponent {

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		try {
			setDocumentToRequest(request);

			GenericOverviewPageInfo parametersInfo = getParametersInfo(request);
			HstQuery query = getQuery(request);
			HstQueryResult queryResult = query.execute();
			PaginatorWidget paginator = new PaginatorWidget(queryResult.getSize(), getPageNumber(request),
					getPageSize(request));
			List<HippoBean> items = getItemsFromHippoBeanIterator(queryResult.getHippoBeans(), paginator);

			request.setAttribute(Constants.Attributes.ITEMS, items);
			if (parametersInfo.getShowPaginator()) {
				request.setAttribute(Constants.Attributes.PAGINATOR, paginator);
			}

		} catch (QueryException e) {
			throw new HstComponentException(e);
		}
	}

	private void setDocumentToRequest(HstRequest request) {
		HippoBean contentBean = getContentBean(request);
		if (contentBean != null) {
			request.setAttribute(Constants.Attributes.DOCUMENT, contentBean);
		}
	}

	protected HstQuery getQuery(HstRequest request) throws QueryException {
		GenericOverviewPageInfo parametersInfo = getParametersInfo(request);
		HippoBean scope = getContentBeanViaParameters(request, parametersInfo);
		HstQuery query = getQueryManager(request).createQuery(scope, parametersInfo.getShowTypes());

		String sortBy = parametersInfo.getSortBy();
		if (StringUtils.isNotBlank(sortBy)) {
			if (Constants.Values.DESCENDING.equals(parametersInfo.getSortOrder())) {
				query.addOrderByDescending(sortBy);
			} else {
				query.addOrderByAscending(sortBy);
			}
		}

		getLimitAndOffset(request, query);
		addFilter(query);
		return query;
	}

	protected void addFilter(HstQuery query) {
	}

	private void getLimitAndOffset(HstRequest request, HstQuery query) {
		int pageSize = getPageSize(request);
		int pageNumber = getPageNumber(request);
		query.setLimit(pageSize);
		query.setOffset((pageNumber - 1) * pageSize);
	}

}
