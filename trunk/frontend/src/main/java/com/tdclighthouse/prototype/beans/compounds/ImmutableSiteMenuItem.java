package com.tdclighthouse.prototype.beans.compounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;

public class ImmutableSiteMenuItem implements HstSiteMenuItem {

    private final String name;
    private final String externalLink;
    private final ImmutableSiteMenuItem parent;
    private final List<HstSiteMenuItem> children;
    // this map only contains String, Boolean, Int, Long, Double or Calendar
    // Objects which are all immutable and can be safely cached.
    private final Map<String, Object> properties;
    private final Map<String, String> parameters;
    private final Map<String, String> localParameters;
    private final CacheableSiteMenu siteMenu;

    public ImmutableSiteMenuItem(CacheableSiteMenu siteMenu, CommonMenuItem menuItem, ImmutableSiteMenuItem parent) {
        this.parent = parent;
        this.siteMenu = siteMenu;
        this.name = menuItem.getName();
        this.externalLink = menuItem.getExternalLink();
        this.properties = menuItem.getProperties();

        Map<String, String> p = null;
        Map<String, String> lp = null;
        List<HstSiteMenuItem> childrenList = new ArrayList<>();
        if (menuItem instanceof HstSiteMenuItem) {
            HstSiteMenuItem hstSiteMenuItem = (HstSiteMenuItem) menuItem;
            addChildren(siteMenu, childrenList, hstSiteMenuItem.getChildMenuItems());
            p = hstSiteMenuItem.getParameters();
            lp = hstSiteMenuItem.getLocalParameters();
        } else if (menuItem instanceof EditableMenu) {
            addChildren(siteMenu, childrenList, ((EditableMenu) menuItem).getMenuItems());
        }
        this.parameters = p;
        this.localParameters = lp;

        this.children = Collections.unmodifiableList(childrenList);

    }

    private void addChildren(CacheableSiteMenu siteMenu, List<HstSiteMenuItem> childrenList,
            List<? extends CommonMenuItem> menuItems) {
        for (CommonMenuItem item : menuItems) {
            childrenList.add(new ImmutableSiteMenuItem(siteMenu, item, this));
        }
    }

    @Override
    public HstLink getHstLink() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isExpanded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSelected() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getExternalLink() {
        return this.externalLink;
    }

    @Override
    public ResolvedSiteMapItem resolveToSiteMapItem(HstRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public boolean isRepositoryBased() {
        return false;
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public List<HstSiteMenuItem> getChildMenuItems() {
        return children;
    }

    @Override
    public HstSiteMenuItem getParentItem() {
        return parent;
    }

    @Override
    public HstSiteMenu getHstSiteMenu() {
        return siteMenu;
    }

    @Override
    public String getParameter(String name) {
        String result = null;
        if (parameters != null) {
            result = parameters.get(name);
        }
        return result;
    }

    @Override
    public String getLocalParameter(String name) {
        String result = null;
        if (localParameters != null) {
            result = localParameters.get(name);
        }
        return result;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public Map<String, String> getLocalParameters() {
        return localParameters;
    }

}
