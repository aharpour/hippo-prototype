package com.tdclighthouse.prototype.beans.compounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.sitemenu.CommonMenu;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenus;

import com.tdclighthouse.prototype.utils.PathUtils;

public class CacheableSiteMenu implements HstSiteMenu {

    private final String name;
    private final Map<String, List<ImmutableSiteMenuItem>> siteMenuItemRegistery = new HashMap<>();
    private final List<HstSiteMenuItem> children;
    private final ThreadLocal<State> state = new ThreadLocal<State>();

    public CacheableSiteMenu(CommonMenu menu) {
        this.name = menu.getName();
        List<HstSiteMenuItem> childrenList = new ArrayList<>();
        if (menu instanceof EditableMenu) {
            addChildren(childrenList, ((EditableMenu) menu).getMenuItems());
        } else if (menu instanceof HstSiteMenuItem) {
            addChildren(childrenList, ((HstSiteMenuItem) menu).getChildMenuItems());
        }
        this.children = Collections.unmodifiableList(childrenList);
    }

    void register(ImmutableSiteMenuItem item) {
        if (siteMenuItemRegistery.containsKey(item.getPath())) {
            siteMenuItemRegistery.get(item.getPath()).add(item);
        } else {
            List<ImmutableSiteMenuItem> list = new ArrayList<ImmutableSiteMenuItem>();
            list.add(item);
            siteMenuItemRegistery.put(item.getPath(), list);
        }
    }

    private void addChildren(List<HstSiteMenuItem> childrenList, List<? extends CommonMenuItem> menuItems) {
        for (CommonMenuItem item : menuItems) {
            childrenList.add(new ImmutableSiteMenuItem(this, item, null));
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public State getState() {
        return state.get();
    }

    public void setState(HstRequest request) {
        this.state.set(new State(request, this));
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

    public static class State {

        private final HstRequestContext requestContext;
        private final String currentPath;
        private final Set<String> expandedPaths;

        public State(HstRequest request, CacheableSiteMenu siteMenu) {
            Set<String> paths = new HashSet<>();
            requestContext = request.getRequestContext();
            currentPath = PathUtils.normalize(request.getPathInfo());
            if (siteMenu.siteMenuItemRegistery.containsKey(currentPath)) {
                List<ImmutableSiteMenuItem> list = siteMenu.siteMenuItemRegistery.get(currentPath);
                for (ImmutableSiteMenuItem selectedItem : list) {
                    ImmutableSiteMenuItem item = selectedItem;
                    while (item != null) {
                        if (StringUtils.isNotBlank(item.getPath())) {
                            paths.add(item.getPath());
                        }
                        item = (ImmutableSiteMenuItem) item.getParentItem();
                    }
                }
            }

            expandedPaths = Collections.unmodifiableSet(paths);
            System.out.println(siteMenu.name);
            System.out.println(expandedPaths);
        }

        public HstRequestContext getRequestContext() {
            return requestContext;
        }

        public String getCurrentPath() {
            return currentPath;
        }

        public Set<String> getExpandedPaths() {
            return expandedPaths;
        }

    }

}
