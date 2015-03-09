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
package com.tdclighthouse.prototype.utils;

import java.util.ArrayList;
import java.util.List;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetNavigation;
import org.hippoecm.hst.core.sitemenu.CommonMenu;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.EditableMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;

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
            result = folder.getBean(Constants.NodeNameConstants.INDEX);
            // when there is no index document in a folder then the first
            // document is selected instead
            if (result == null) {
                List<HippoDocumentBean> documents = folder.getDocuments();
                result = (documents != null && !documents.isEmpty() ? documents.get(0) : null);
            }
        } else {
            throw new IllegalArgumentException(
                    "Expect childbearingBean to be either a HippoFolderBean or a HippoFacetNavigation");
        }
        return result;
    }

    public static EditableMenuItem getSiteMapItemByPath(EditableMenu editableMenu, String relativePath) {
        EditableMenuItem result = null;
        String normalizedPath = normalizePath(relativePath);
        String[] split = normalizedPath.split("/");
        List<EditableMenuItem> menuItems = editableMenu.getMenuItems();
        for (String segment : split) {
            EditableMenuItem temp = getSiteMenuItemByPathSegement(menuItems, segment);
            if (temp == null) {
                break;
            } else {
                result = temp;
                menuItems = temp.getChildMenuItems();
            }
        }
        return result;
    }

    public static List<? extends CommonMenuItem> getMenuItems(CommonMenu menu) {
        List<? extends CommonMenuItem> result;
        if (menu instanceof EditableMenu) {
            result = ((EditableMenu) menu).getMenuItems();
        } else if (menu instanceof HstSiteMenu) {
            result = ((HstSiteMenu) menu).getSiteMenuItems();
        } else {
            result = new ArrayList<CommonMenuItem>();
        }
        return result;
    }

    public static List<? extends CommonMenuItem> getSubmenuItems(CommonMenuItem menuItem) {
        List<? extends CommonMenuItem> result;
        if (menuItem instanceof EditableMenuItem) {
            result = ((EditableMenuItem) menuItem).getChildMenuItems();
        } else if (menuItem instanceof HstSiteMenuItem) {
            result = ((HstSiteMenuItem) menuItem).getChildMenuItems();
        } else {
            result = new ArrayList<CommonMenuItem>();
        }
        return result;
    }

    private static EditableMenuItem getSiteMenuItemByPathSegement(List<EditableMenuItem> menuItems, String pathSegment) {
        EditableMenuItem result = null;
        List<EditableMenuItem> currentMenuItems = menuItems;
        for (EditableMenuItem editable : currentMenuItems) {
            if (editable.getName().equals(pathSegment)) {
                result = editable;
                break;
            }
        }
        return result;
    }

    private static String normalizePath(String relativePath) {
        String result = relativePath;
        if (relativePath.startsWith("/")) {
            result = relativePath.substring(1);
        }
        return result;
    }

}
