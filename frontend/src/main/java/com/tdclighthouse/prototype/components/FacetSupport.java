package com.tdclighthouse.prototype.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.utils.BeanUtils;

import com.tdclighthouse.prototype.componentsinfo.FacetedNavigationInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.SearchQueryUtils;

public class FacetSupport extends BaseTdcComponent {

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
		Object parametersInfo = getParametersInfo(request);
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
