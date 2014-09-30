package com.tdclighthouse.prototype.tag;

import java.util.ArrayList;
import java.util.List;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetSubNavigation;

import com.tdclighthouse.prototype.beans.Page;

public class Functions {

    private Functions() {
    }

    public static boolean isSubclassOfWebdocument(HippoBean hippoBean) {
        boolean result = false;
        if (hippoBean instanceof Page) {
            result = true;
        }
        return result;
    }

    public static List<HippoFacetSubNavigation> removeListByCategory(String category, String childCategory,
            HippoFacetSubNavigation item) {
        List<HippoFacetSubNavigation> removeList = new ArrayList<HippoFacetSubNavigation>();
        for (HippoFacetSubNavigation ancestors : item.getAncestorsAndSelf()) {
            String actuallyCategory = ancestors.getParentBean().getName();
            if (category.equals(actuallyCategory) || childCategory.equals(actuallyCategory)) {
                removeList.add(ancestors);
            }
        }
        return removeList;
    }

}
