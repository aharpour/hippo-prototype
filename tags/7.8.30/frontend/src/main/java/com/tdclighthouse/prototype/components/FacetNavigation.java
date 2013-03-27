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

/**
 * @author Ebrahim Aharpour
 *
 */
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
