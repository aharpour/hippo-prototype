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
package com.tdclighthouse.prototype.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.componentsinfo.NavigationInfo;
import com.tdclighthouse.prototype.provider.RepoBasedMenuProvider;
import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 * 
 */
@ParametersInfo(type = NavigationInfo.class)
public class Navigation extends BaseHstComponent {

	protected static Logger log = LoggerFactory.getLogger(Navigation.class);

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		NavigationInfo parametersInfo = this.<NavigationInfo> getParametersInfo(request);
		String menuName = parametersInfo.getMenuName();
		HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(menuName);
		if (menu != null) {
			EditableMenu editableMenu = menu.getEditableMenu();
			boolean showFacet = parametersInfo.isShowFacetedNavigation();
			new RepoBasedMenuProvider(getSiteContentBaseBean(request), showFacet, request)
			.addRepoBasedMenuItems(editableMenu);
			request.setAttribute(Constants.Attributes.MENU, editableMenu);
		}
	}

}
