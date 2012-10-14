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

import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.FacetedOverviewPageInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.PaginatorWidget;

@ParametersInfo(type = FacetedOverviewPageInfo.class)
public class FacetedOverview extends FacetSupport {


	public static final Logger log = LoggerFactory.getLogger(FacetedOverview.class);

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		setContentBean(request);
		setItems(request);
		setQueryIfExists(request);
	}

	protected HippoBean obtainContentBean(HstRequest request) {
		HippoBean result = null;
		HippoBean contentBean = getContentBean(request);
		if (!(contentBean instanceof HippoFacetNavigationBean)) {
			result = contentBean;
		} else {
			HippoBean bean = getContentBeanViaParameters(request, (ContentBeanPathInfo) getParametersInfo(request));
			if (!(bean instanceof HippoFacetNavigationBean)) {
				result = bean;
			}
		}
		return result;
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
			HippoFacetNavigationBean facetBean = facetedNavBean;
			HippoResultSetBean resultSet = facetBean.getResultSet();
			PaginatorWidget paginatorWidget = setPaginator(request, getPageSize(request), resultSet.getCount()
					.intValue());
			request.setAttribute(Constants.Attributes.FACET_BEAN, facetBean);
			request.setAttribute(Constants.Attributes.ITEMS, getItemsFromResultSet(resultSet, paginatorWidget));
		} else {
			throw new HstComponentException("content Bean is not of the type HippoFactNavigation");
		}
	}

	protected void setQueryIfExists(HstRequest request) {
		String query = getPublicRequestParameter(request, Constants.Parameters.QUERY);
		if(StringUtils.isNotBlank(query)) {
			request.setAttribute(Constants.Attributes.QUERY, query);
		}
	}

}
