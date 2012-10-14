package com.tdclighthouse.prototype.components;

import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoFacetChildNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.componentsinfo.FacetedOverviewPageInfo;
import com.tdclighthouse.prototype.utils.Constants;

@ParametersInfo(type = FacetedOverviewPageInfo.class)
public class FacetNavigation extends FacetSupport {

	public static final Logger log = LoggerFactory.getLogger(FacetNavigation.class);

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) {
		HippoFacetNavigationBean facetedNavBean = getFacetNavigationBean(request);
		if (facetedNavBean != null) {
			facetedNavBean = applyQueryToFacetBean(request, facetedNavBean);
			request.setAttribute(Constants.Attributes.FACETNAV, facetedNavBean);
			Map<String, String> labels = getLabels(request);
			request.setAttribute(Constants.Attributes.LABELS, labels);

			if (facetedNavBean instanceof HippoFacetChildNavigationBean) {
				request.setAttribute(Constants.Attributes.CHILDNAV, "true");
			}
		}
	}
}
