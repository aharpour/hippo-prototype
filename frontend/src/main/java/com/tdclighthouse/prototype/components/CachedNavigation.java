package com.tdclighthouse.prototype.components;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hippoecm.hst.cache.CacheElement;
import org.hippoecm.hst.cache.HstCache;
import org.hippoecm.hst.cache.ehcache.CacheElementEhCache;
import org.hippoecm.hst.configuration.sitemap.HstSiteMapItem;
import org.hippoecm.hst.configuration.sitemenu.HstSiteMenuConfigurationService;
import org.hippoecm.hst.configuration.sitemenu.HstSiteMenusConfiguration;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.site.HstServices;

import com.tdclighthouse.prototype.beans.compounds.CacheableSiteMenu;
import com.tdclighthouse.prototype.componentsinfo.NavigationInfo;
import com.tdclighthouse.prototype.provider.RepoBasedMenuProvider;
import com.tdclighthouse.prototype.utils.BeanUtils;
import com.tdclighthouse.prototype.utils.Constants.AttributesConstants;
import com.tdclighthouse.prototype.utils.TdcUtils;
import com.tdclighthouse.prototype.utils.TdcUtils.Call;

@ParametersInfo(type = NavigationInfo.class)
public class CachedNavigation extends WebDocumentDetail {
    private static final String CACHED_MENU = "cachedMenu-";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        try {
            super.doBeforeRender(request, response);
            Key key = getCacheKey(request);
            // key being null means there is no menu with the given name
            if (key != null) {

                CacheableSiteMenu menu = TdcUtils.getCachedCall(new RequestCachedCallBack(), request, CACHED_MENU
                        + key.name);

                request.setAttribute(AttributesConstants.MENU, menu);
            }
            request.setAttribute(AttributesConstants.PARAM_INFO, getComponentParametersInfo(request));
            request.setAttribute(AttributesConstants.LABELS, BeanUtils.getLabels(getComponentParametersInfo(request)));
        } catch (Exception e) {
            throw new HstComponentException(e.getMessage(), e);
        }

    }

    private Key getCacheKey(HstRequest request) {
        Key result = null;
        HstRequestContext requestContext = request.getRequestContext();
        HstSiteMapItem selectedSiteMapItem = requestContext.getResolvedSiteMapItem().getHstSiteMapItem();
        HstSiteMenusConfiguration siteMenusConfiguration = selectedSiteMapItem.getHstSiteMap().getSite()
                .getSiteMenusConfiguration();
        String menuName = getComponentParameters(NavigationInfo.MENU_NAME, NavigationInfo.MENU_NAME_DEFAULT,
                String.class);
        HstSiteMenuConfigurationService service = (HstSiteMenuConfigurationService) siteMenusConfiguration
                .getSiteMenuConfiguration(menuName);
        if (service != null) {
            result = new Key(service.getCanonicalPath());
        }
        return result;
    }

    public class RequestCachedCallBack implements Call<CacheableSiteMenu> {

        @Override
        public CacheableSiteMenu makeCall(HstRequest request) {

            return getCachedMenu(request);

        }

        private CacheableSiteMenu getCachedMenu(HstRequest request) {
            try {
                Key key = getCacheKey(request);
                HstCache cache = HstServices.getComponentManager().getComponent("pageCache");
                CacheElement cacheElement;
                cacheElement = cache.get(key, new HippoCachedCallback(request));
                CacheableSiteMenu menu = (CacheableSiteMenu) cacheElement.getContent();
                if (menu != null) {
                    menu.setState(request);
                }
                return menu;
            } catch (Exception e) {
                throw new HstComponentException(e.getMessage(), e);
            }
        }

        @Override
        public Class<CacheableSiteMenu> getType() {
            return CacheableSiteMenu.class;
        }

    }

    private class HippoCachedCallback implements Callable<CacheElementEhCache> {

        private final HstRequest request;

        public HippoCachedCallback(HstRequest request) {
            this.request = request;
        }

        @Override
        public CacheElementEhCache call() throws Exception {
            EditableMenu editableMenu = null;
            String menuName = getComponentParameters(NavigationInfo.MENU_NAME, NavigationInfo.MENU_NAME_DEFAULT,
                    String.class);
            HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(menuName);
            if (menu != null) {
                editableMenu = menu.getEditableMenu();
                boolean showFacet = getComponentParameters(NavigationInfo.SHOW_FACETED_NAVIGATION,
                        NavigationInfo.SHOW_FACETED_NAVIGATION_DEFAULT, Boolean.class);
                boolean userIndexDocument = getComponentParameters(NavigationInfo.USE_INDEX_DOCUMENT,
                        NavigationInfo.USE_INDEX_DOCUMENT_DEFAULT, Boolean.class);
                new RepoBasedMenuProvider(request.getRequestContext().getSiteContentBaseBean(), showFacet,
                        userIndexDocument, request).addRepoBasedMenuItems(editableMenu);
            }
            return editableMenu != null ? new CacheElementEhCache(getCacheKey(request), new CacheableSiteMenu(
                    editableMenu)) : null;
        }

    }

    private static class Key {
        private String name;

        public Key(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Key && this.name.equals(((Key) obj).name);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(31, 41).append(name).hashCode();
        }

    }

}
