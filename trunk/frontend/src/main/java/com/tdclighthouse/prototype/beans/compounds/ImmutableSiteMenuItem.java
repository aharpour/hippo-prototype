package com.tdclighthouse.prototype.beans.compounds;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.configuration.hosting.NotFoundException;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenuItem;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.beans.compounds.CacheableSiteMenu.State;

public class ImmutableSiteMenuItem implements HstSiteMenuItem {

    private static final Logger LOG = LoggerFactory.getLogger(ImmutableSiteMenuItem.class);

    private final String name;
    private final String externalLink;
    private final WeakReference<ImmutableSiteMenuItem> parent;
    private final List<WeakReference<HstSiteMenuItem>> children;
    // this map only contains String, Boolean, Int, Long, Double or Calendar
    // Objects which are all immutable and can be safely cached.
    private final Map<String, Object> properties;
    private final Map<String, String> parameters;
    private final Map<String, String> localParameters;
    private final WeakReference<CacheableSiteMenu> siteMenu;
    private final String path;

    public ImmutableSiteMenuItem(CacheableSiteMenu siteMenu, CommonMenuItem menuItem, ImmutableSiteMenuItem parent) {
        this.parent = new WeakReference<ImmutableSiteMenuItem>(parent);
        this.siteMenu = new WeakReference<CacheableSiteMenu>(siteMenu);
        this.name = menuItem.getName();
        this.externalLink = menuItem.getExternalLink();
        this.properties = menuItem.getProperties();
        HstLink hstLink = menuItem.getHstLink();

        if (hstLink != null) {
            this.path = hstLink.getPath();
        } else {
            this.path = null;
        }

        Map<String, String> p = null;
        Map<String, String> lp = null;
        List<WeakReference<HstSiteMenuItem>> childrenList = new ArrayList<>();
        if (menuItem instanceof HstSiteMenuItem) {
            HstSiteMenuItem hstSiteMenuItem = (HstSiteMenuItem) menuItem;
            addChildren(siteMenu, childrenList, hstSiteMenuItem.getChildMenuItems());
            p = hstSiteMenuItem.getParameters();
            lp = hstSiteMenuItem.getLocalParameters();
        } else if (menuItem instanceof EditableMenuItem) {
            addChildren(siteMenu, childrenList, ((EditableMenuItem) menuItem).getChildMenuItems());
        }
        this.parameters = p;
        this.localParameters = lp;

        this.children = Collections.unmodifiableList(childrenList);
        siteMenu.register(this);
    }

    private void addChildren(CacheableSiteMenu siteMenu, List<WeakReference<HstSiteMenuItem>> childrenList,
            List<? extends CommonMenuItem> menuItems) {
        for (CommonMenuItem item : menuItems) {
            childrenList.add(new WeakReference<HstSiteMenuItem>(new ImmutableSiteMenuItem(siteMenu, item, this)));
        }
    }

    @Override
    public HstLink getHstLink() {
        HstLink result = null;
        if (path != null) {
            HstRequestContext rc = RequestContextProvider.get();
            result = rc.getHstLinkCreator().create(path, rc.getResolvedMount().getMount());
        }
        return result;
    }

    @Override
    public boolean isExpanded() {
        boolean result = false;
        State state = getstate();
        if (state != null && state.getExpanded() != null && state.getExpanded().contains(this)) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean isSelected() {
        boolean result = false;
        State state = getstate();
        if (state != null && state.getCurrentPath().equals(path)) {
            result = true;
        }
        return result;
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
        ResolvedSiteMapItem resolvedSiteMapItem = null;
        try {
            HstRequestContext ctx = request.getRequestContext();
            if (StringUtils.isNotBlank(this.getPath())) {
                resolvedSiteMapItem = ctx.getSiteMapMatcher().match(this.getPath(),
                        ctx.getResolvedSiteMapItem().getResolvedMount());
            }
        } catch (NotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        return resolvedSiteMapItem;
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
        List<HstSiteMenuItem> result = new ArrayList<HstSiteMenuItem>();
        for (WeakReference<HstSiteMenuItem> weakReference : children) {
            HstSiteMenuItem hstSiteMenuItem = weakReference.get();
            if (hstSiteMenuItem != null) {
                result.add(hstSiteMenuItem);
            }
        }
        return result;
    }

    @Override
    public HstSiteMenuItem getParentItem() {
        return parent.get();
    }

    @Override
    public HstSiteMenu getHstSiteMenu() {
        return siteMenu.get();
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

    private State getstate() {
        State result = null;
        if (siteMenu != null) {
            CacheableSiteMenu cacheableSiteMenu = siteMenu.get();
            if (cacheableSiteMenu != null) {
                result = cacheableSiteMenu.getState();
            }
        }
        return result;
    }

    String getPath() {
        return path;
    }

}
