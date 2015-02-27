package com.tdclighthouse.prototype.beans.compounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.sitemenu.CommonMenu;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenus;

public class CacheableSiteMenu implements HstSiteMenu {
    
    private final String name;
    private final List<HstSiteMenuItem> children;
    private final ThreadLocal<HstRequestContext> requestContext = new ThreadLocal<HstRequestContext>();
    
    public CacheableSiteMenu(CommonMenu menu) {
        this.name = menu.getName();
        List<HstSiteMenuItem> childrenList = new ArrayList<>();
        if (menu instanceof EditableMenu) {
            addChildren(childrenList, ((EditableMenu)menu).getMenuItems());
        } else if (menu instanceof HstSiteMenuItem) {
            addChildren(childrenList, ((HstSiteMenuItem)menu).getChildMenuItems());
        }
        this.children = Collections.unmodifiableList(childrenList);
    }
    
    private void addChildren(List<HstSiteMenuItem> childrenList,
            List<? extends CommonMenuItem> menuItems) {
        for (CommonMenuItem item : menuItems) {
            childrenList.add(new ImmutableSiteMenuItem(this, item, null));
        }
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    protected HstRequestContext getRequestContext() {
        return requestContext.get();
    }
    
    public void setRequestContext(HstRequestContext requestContext) {
        this.requestContext.set(requestContext);
    }

    @Override
    public boolean isExpanded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public HstSiteMenuItem getSelectSiteMenuItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<HstSiteMenuItem> getSiteMenuItems() {
        return this.children;
    }

    @Override
    public HstSiteMenus getHstSiteMenus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HstSiteMenuItem getDeepestExpandedItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EditableMenu getEditableMenu() {
        throw new UnsupportedOperationException();
    }


}
