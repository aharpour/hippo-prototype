/*
 *  Copyright 2012 Smile B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
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

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;

import com.tdclighthouse.prototype.componentsinfo.NavigationInfo;
import com.tdclighthouse.prototype.provider.RepoBasedMenuProvider;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.TdcUtils;
import com.tdclighthouse.prototype.utils.TdcUtils.Call;

/**
 * @author Ebrahim Aharpour
 * 
 */
@ParametersInfo(type = NavigationInfo.class)
public class Navigation extends WebDocumentDetail {

    private static final String EDITABLE_MENU_ATTRIBUTE = "editableMenu";

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        final NavigationInfo parametersInfo = this.<NavigationInfo> getComponentParametersInfo(request);
        EditableMenu editableMenu = TdcUtils.getCachedCall(new Call<EditableMenu>() {

            @Override
            public EditableMenu makeCall(HstRequest request) {
                EditableMenu result = null;
                String menuName = parametersInfo.getMenuName();
                HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(menuName);
                if (menu != null) {
                    result = menu.getEditableMenu();
                    boolean showFacet = parametersInfo.isShowFacetedNavigation();
                    new RepoBasedMenuProvider(request.getRequestContext().getSiteContentBaseBean(), showFacet, request)
                            .addRepoBasedMenuItems(result);
                }
                return result;
            }

            @Override
            public Class<EditableMenu> getType() {
                return EditableMenu.class;
            }

        }, request, EDITABLE_MENU_ATTRIBUTE);

        request.setAttribute(Constants.Attributes.MENU, editableMenu);

    }

}
