/*
 *  Copyright 2012 Smile B.V.
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
package com.tdclighthouse.prototype.utils;

import java.util.List;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetNavigation;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class NavigationUtils {

	private NavigationUtils() {
	}

	public static HippoBean getIndexBean(HippoBean childbeering) {
		HippoBean result;
		if (childbeering instanceof HippoFacetNavigation) {
			result = childbeering;
		} else if (childbeering instanceof HippoFolderBean) {
			HippoFolderBean folder = (HippoFolderBean) childbeering;
			result = folder.getBean(Constants.NodeName.INDEX);
			// when there is no index document in a folder then the first
			// document is selected instead
			if (result == null) {
				List<HippoDocumentBean> documents = folder.getDocuments();
				result = (documents.size() > 0 ? documents.get(0) : null);
			}
		} else {
			throw new IllegalArgumentException(
					"Expect childbearingBean to be either a HippoFolderBean or a HippoFacetNavigation");
		}
		return result;
	}
}
