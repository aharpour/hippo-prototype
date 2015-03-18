package com.tdclighthouse.prototype.beans.compounds;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.sitemenu.CommonMenu;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.utils.PathUtils;

public class CacheableSiteMenu implements HstSiteMenu {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableSiteMenu.class);

    private final String name;

    private final Map<String, List<ImmutableSiteMenuItem>> siteMenuItemRegistery = new HashMap<String, List<ImmutableSiteMenuItem>>();
    private final List<HstSiteMenuItem> children;
    private final ThreadLocal<State> state = new ThreadLocal<State>();

    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    public CacheableSiteMenu(CommonMenu menu) {
        LOG.debug("A CacheableSiteMenu is being created for menu: {}.", menu.getName());
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
        if (item.getPath() != null) {
            if (siteMenuItemRegistery.containsKey(item.getPath())) {
                siteMenuItemRegistery.get(item.getPath()).add(item);
            } else {
                List<ImmutableSiteMenuItem> list = new ArrayList<ImmutableSiteMenuItem>();
                list.add(item);
                siteMenuItemRegistery.put(item.getPath(), list);
            }
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
        return getState() != null && !getState().getExpanded().isEmpty();
    }

    @Override
    public HstSiteMenuItem getSelectSiteMenuItem() {
        HstSiteMenuItem result = null;
        State s = getState();
        if (s != null && siteMenuItemRegistery.containsKey(s.getCurrentPath())
                && !siteMenuItemRegistery.get(s.getCurrentPath()).isEmpty()) {
            result = siteMenuItemRegistery.get(s.getCurrentPath()).get(0);
        }
        return result;
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
        return getState().getDeepestExpanded();
    }

    @Override
    public EditableMenu getEditableMenu() {
        throw new UnsupportedOperationException();
    }

    public static class State {

        private final String currentPath;
        private final WeakReference<Set<ImmutableSiteMenuItem>> expanded;
        private final WeakReference<ImmutableSiteMenuItem> deepestExpanded;

        public State(HstRequest request, CacheableSiteMenu siteMenu) {
            Set<ImmutableSiteMenuItem> selectedItems = new HashSet<>();
            currentPath = PathUtils.normalize(request.getPathInfo());
            if (contains(siteMenu, currentPath)) {
                deepestExpanded = new WeakReference<ImmutableSiteMenuItem>(siteMenu.siteMenuItemRegistery.get(
                        currentPath).get(0));
                addSelectedAndAncestorsToExpanded(siteMenu, selectedItems);
            } else {
                Matcher matcher = SLASH_PATTERN.matcher(currentPath);
                ImmutableSiteMenuItem i = null;
                while (matcher.find()) {
                    String path = currentPath.substring(0, matcher.start());
                    if (contains(siteMenu, path)) {
                        List<ImmutableSiteMenuItem> list = siteMenu.siteMenuItemRegistery.get(path);
                        i = list.get(0);
                        selectedItems.addAll(list);
                    }
                }
                deepestExpanded = new WeakReference<ImmutableSiteMenuItem>(i);
            }

            expanded = new WeakReference<Set<ImmutableSiteMenuItem>>(Collections.unmodifiableSet(selectedItems));
        }

        private boolean contains(CacheableSiteMenu siteMenu, String path) {
            return siteMenu.siteMenuItemRegistery.containsKey(path)
                    && !siteMenu.siteMenuItemRegistery.get(path).isEmpty();
        }

        private void addSelectedAndAncestorsToExpanded(CacheableSiteMenu siteMenu,
                Set<ImmutableSiteMenuItem> selectedItems) {
            List<ImmutableSiteMenuItem> list = siteMenu.siteMenuItemRegistery.get(currentPath);
            for (ImmutableSiteMenuItem selectedItem : list) {
                ImmutableSiteMenuItem item = selectedItem;
                while (item != null) {
                    selectedItems.add(item);
                    item = (ImmutableSiteMenuItem) item.getParentItem();
                }
            }
        }

        public String getCurrentPath() {
            return currentPath;
        }

        public Set<ImmutableSiteMenuItem> getExpanded() {
            return expanded.get();
        }

        public ImmutableSiteMenuItem getDeepestExpanded() {
            return deepestExpanded.get();
        }

    }

}
