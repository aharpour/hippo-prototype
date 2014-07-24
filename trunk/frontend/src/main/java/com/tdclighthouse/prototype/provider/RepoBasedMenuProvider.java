package com.tdclighthouse.prototype.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.configuration.HstNodeTypes;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetNavigation;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetSubNavigation;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetsAvailableNavigation;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.core.sitemenu.CommonMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.EditableMenuItem;
import org.hippoecm.hst.core.sitemenu.EditableMenuItemImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.commons.utils.hippo.Essentials;
import com.tdclighthouse.prototype.beans.Page;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.Constants.HstParametersConstants;
import com.tdclighthouse.prototype.utils.Constants.ValuesConstants;
import com.tdclighthouse.prototype.utils.NavigationUtils;

/**
 * @author Ebrahim Aharpour
 * 
 *         this component only shows sub-items of a faceted navigation item only
 *         if there is just one facetsavailablenavigation under the facet
 * 
 */
public class RepoBasedMenuProvider {

    private static final Logger LOG = LoggerFactory.getLogger(RepoBasedMenuProvider.class);
    private final HstRequest request;
    private final String selectedNodeCanonicalPath;
    private final HippoBean siteContentBaseBean;
    private final boolean showFacetNavigations;

    public RepoBasedMenuProvider(HippoBean siteContentBaseBean, HstRequest request) {
        this(siteContentBaseBean, false, request);
    }

    public RepoBasedMenuProvider(HippoBean siteContentBaseBean, boolean showFacetNavigations, HstRequest request) {
        this.request = request;
        this.siteContentBaseBean = siteContentBaseBean;
        this.showFacetNavigations = showFacetNavigations;
        HippoBean bean = request.getRequestContext().getContentBean();
        if (bean != null) {
            selectedNodeCanonicalPath = bean != null ? bean.getCanonicalPath() : null;
        } else {
            selectedNodeCanonicalPath = null;
        }
    }

    public EditableMenu addRepoBasedMenuItems(EditableMenu editableMenu) {
        List<EditableMenuItem> menuItems = editableMenu.getMenuItems();
        addRepoBasedMenuItems(menuItems);
        return editableMenu;
    }

    private void addRepoBasedMenuItems(List<EditableMenuItem> menuItems) {
        for (EditableMenuItem item : menuItems) {
            addRepoBasedMenuItems(item.getChildMenuItems());
            expandForcedExpandedItems(item);
            if (item.isRepositoryBased()) {
                List<String> locations = getParameterValues(Constants.HstParametersConstants.ROOT, item);
                HippoBean[] beanOfMenuItems = null;
                if (locations != null && !locations.isEmpty()) {
                    beanOfMenuItems = getBeans(locations);
                } else {
                    beanOfMenuItems = new HippoBean[] { getBeanOfMenuItem(item) };
                }
                for (HippoBean hippoBean : beanOfMenuItems) {
                    addSubitems(item, hippoBean, 1);
                }
            }
        }
    }

    private void expandForcedExpandedItems(EditableMenuItem item) {
        String value = getParameterValue(Constants.HstParametersConstants.EXPANDED, item);
        if (Constants.ValuesConstants.TRUE.equals(value)) {
            String expandeOnlyCurrentItem = getParameterValue(
                    Constants.HstParametersConstants.EXPAND_ONLY_CURRENT_ITEM, item);
            if (Constants.ValuesConstants.TRUE.equals(expandeOnlyCurrentItem)) {
                markOnlyCurrentItemAsExpanded(item);
            } else {
                markAsExpanded(item);
            }
        }
    }

    private HippoBean[] getBeans(List<String> locations) {
        HippoBean[] beanOfMenuItems;
        beanOfMenuItems = new HippoBean[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            beanOfMenuItems[i] = siteContentBaseBean.getBean(locations.get(i));
        }
        return beanOfMenuItems;
    }

    private void addSubitems(EditableMenuItem item, HippoBean indexPageBean, int depth) {
        if (item.getDepth() >= depth) {
            HippoBean childbearingBean = getFolderOrFacet(indexPageBean);
            if (childbearingBean != null) {
                List<HippoBean> childbearingChildren = getChildbearingChildren(childbearingBean);
                for (HippoBean childbearingChild : childbearingChildren) {
                    HippoBean foldersIndex = NavigationUtils.getIndexBean(childbearingChild);
                    if (foldersIndex != null) {
                        EditableMenuItem folderItem = addItem(item, foldersIndex, childbearingChild.getLocalizedName());
                        addSubitems(folderItem, foldersIndex, depth + 1);
                    }
                }
                List<HippoDocumentBean> documents = getNonChildbearingChilds(childbearingBean);
                for (final HippoBean document : documents) {
                    if (!document.getCanonicalPath().equals(indexPageBean.getCanonicalPath())) {
                        addItem(item, document, document.getLocalizedName());
                    }
                }
            }
        }
    }

    private List<HippoDocumentBean> getNonChildbearingChilds(HippoBean childbearingBean) {
        List<HippoDocumentBean> result;
        if (childbearingBean instanceof HippoFolderBean) {
            result = ((HippoFolderBean) childbearingBean).getDocuments();
        } else {
            result = new ArrayList<HippoDocumentBean>();
        }
        return result;
    }

    private List<HippoBean> getChildbearingChildren(HippoBean childbearingBean) {
        List<HippoBean> result;
        if (childbearingBean instanceof HippoFacetNavigation) {
            result = getChildbearingChildrenOfFacet((HippoFacetNavigation) childbearingBean);
        } else if (childbearingBean instanceof HippoFolderBean) {
            result = getChildbearingChildrenOfFolder(childbearingBean);
        } else {
            throw new IllegalArgumentException(
                    "Expect childbearingBean to be either a HippoFolderBean or a HippoFacetNavigationBean");
        }
        return result;
    }

    private List<HippoBean> getChildbearingChildrenOfFolder(HippoBean childbearingBean) {
        List<HippoBean> items = new ArrayList<HippoBean>();
        items.addAll(((HippoFolderBean) childbearingBean).getFolders());
        if (showFacetNavigations) {
            items.addAll(childbearingBean.getChildBeans(HippoFacetNavigation.class));
        }
        return items;
    }

    private List<HippoBean> getChildbearingChildrenOfFacet(HippoFacetNavigation facetNavigation) {
        List<HippoBean> result = new ArrayList<HippoBean>();
        if (facetNavigation instanceof HippoFacetsAvailableNavigation) {
            addChildrenOfType(result, facetNavigation, HippoFacetSubNavigation.class);
        } else {
            List<HippoFacetsAvailableNavigation> availableNavigation = facetNavigation
                    .getChildBeans(HippoFacetsAvailableNavigation.class);
            if (availableNavigation != null) {
                if (availableNavigation.size() == 1) {
                    HippoFacetsAvailableNavigation facetsAvailableNavigation = availableNavigation.get(0);
                    addChildrenOfType(result, facetsAvailableNavigation, HippoFacetSubNavigation.class);
                } else {
                    for (HippoFacetsAvailableNavigation fan : availableNavigation) {
                        result.add(fan);
                    }
                }
            }
        }

        return result;
    }

    private <T extends HippoFacetNavigation> void addChildrenOfType(List<HippoBean> result, HippoFacetNavigation facet,
            Class<T> clazz) {
        List<T> childrenBeans = facet.getChildBeans(clazz);
        for (T child : childrenBeans) {
            result.add(child);
        }
    }

    public static void markAsSeleted(EditableMenuItem item) {
        EditableMenuItem temp = item;
        if (temp instanceof SimpleEditableMenuItem) {
            ((SimpleEditableMenuItem) temp).setSelected(true);
        }
        markAsExpanded(temp);
    }

    public static void markAsExpanded(EditableMenuItem temp) {
        EditableMenuItem currentItem = temp;
        while (currentItem != null) {
            currentItem.setExpanded(true);
            currentItem = currentItem.getParentItem();
        }
    }

    private void markOnlyCurrentItemAsExpanded(EditableMenuItem item) {
        EditableMenuItem parentItem = item.getParentItem();
        if (parentItem != null) {
            boolean isParentItemExpanded;
            isParentItemExpanded = parentItem.isExpanded();
            item.setExpanded(true);
            parentItem.setExpanded(isParentItemExpanded);
        } else {
            item.setExpanded(true);
        }
    }

   

    private EditableMenuItem addItem(EditableMenuItem item, final HippoBean document, String localizedName) {
        HstLink hstLink = Essentials.createHstLink(document, request);
        EditableMenuItem repoMenuItem;
        if (document instanceof Page) {
            Boolean hideFromSitemap = ((Page) document).getHideFromSitemap();
            repoMenuItem = new SimpleEditableMenuItem(item, hstLink, localizedName, hideFromSitemap != null && hideFromSitemap);
        } else {
            repoMenuItem = new SimpleEditableMenuItem(item, hstLink, localizedName);
        }
        if (document instanceof HippoFacetsAvailableNavigation && repoMenuItem instanceof SimpleEditableMenuItem) {
            ((SimpleEditableMenuItem)repoMenuItem).setDisabled(true);
        }
        item.addChildMenuItem(repoMenuItem);
        if (selectedNodeCanonicalPath != null && selectedNodeCanonicalPath.equals(document.getCanonicalPath())) {
            markAsSeleted(repoMenuItem);
        }
        return repoMenuItem;
    }

    private HippoBean getFolderOrFacet(HippoBean bean) {
        HippoBean result = null;
        if (bean instanceof HippoFacetNavigation) {
            result = bean;
        } else {
            HippoBean itemBean = bean;
            while (itemBean != null) {
                if (itemBean.isHippoFolderBean()) {
                    result = itemBean;
                    break;
                }
                itemBean = itemBean.getParentBean();
            }
        }

        return result;
    }

    private HippoBean getBeanOfMenuItem(CommonMenuItem item) {
        HippoBean bean = null;
        ResolvedSiteMapItem resolveToSiteMapItem = item.resolveToSiteMapItem(request);
        if (resolveToSiteMapItem != null) {
            bean = siteContentBaseBean.getBean(resolveToSiteMapItem.getRelativeContentPath());
        }
        return bean;
    }

    public static String getParameterValue(String parameterName, EditableMenuItem menuItem) {
        String result = null;
        List<String> values = getParameterValues(parameterName, menuItem);
        if (!values.isEmpty()) {
            result = values.get(0);
        }
        return result;
    }

    public static List<String> getParameterValues(String parameterName, EditableMenuItem menuItem) {
        if (StringUtils.isBlank(parameterName) || menuItem == null) {
            throw new IllegalArgumentException("Both parameterName and menuItem are required.");
        }
        List<String> result = new ArrayList<String>();
        Map<String, Object> properties = menuItem.getProperties();
        String[] paramNames = (String[]) properties.get(HstNodeTypes.GENERAL_PROPERTY_PARAMETER_NAMES);
        String[] paramValues = (String[]) properties.get(HstNodeTypes.GENERAL_PROPERTY_PARAMETER_VALUES);
        if (paramNames != null && paramValues != null) {
            if (paramNames.length == paramValues.length) {
                for (int i = 0; i < paramNames.length; i++) {
                    String propName = paramNames[i];
                    if (parameterName.equals(propName)) {
                        result.add(paramValues[i]);
                    }
                }
            } else {
                LOG.warn("Parameter name array and parameter values arrays have different lengths");
            }
        }
        return result;
    }

    public static boolean getBooleanProperty(CommonMenuItem menuItem, String propertyName) {
        boolean result = false;
        Map<String, Object> properties = menuItem.getProperties();
        if (properties != null) {
            Object object = properties.get(propertyName);
            if (object instanceof String[] && ((String[]) object).length == 1) {
                result = ValuesConstants.TRUE.equalsIgnoreCase(((String[]) object)[0]);
            }
        }
        return result;
    }

    public static class SimpleEditableMenuItem extends EditableMenuItemImpl {

        private final HstLink hstLink;
        private final String localizedName;
        private final int depth;

        public SimpleEditableMenuItem(EditableMenuItem item, HstLink hstLink, String localizedName) {
            super(item);
            this.hstLink = hstLink;
            this.localizedName = localizedName;
            this.depth = item.getDepth();
            this.properties = new HashMap<String, Object>();
        }

        public SimpleEditableMenuItem(EditableMenuItem item, HstLink hstLink, String localizedName, boolean invisible) {
            this(item, hstLink, localizedName);
            if (invisible) {
                getProperties().put(HstParametersConstants.INVISIBLE, new String[] { "true" });
            }
        }

        @Override
        public HstLink getHstLink() {
            return hstLink;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public String getName() {
            return localizedName;
        }

        @Override
        public boolean isRepositoryBased() {
            return true;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        public boolean isDisabled() {
            return getBooleanProperty(this, HstParametersConstants.DISABLED);
        }

        public boolean isInvisible() {
            return getBooleanProperty(this, HstParametersConstants.DISABLED);
        }

        public void setDisabled(boolean disabled) {
            if (disabled) {
                this.properties.put(HstParametersConstants.DISABLED, new String[] { "true" });
            }
        }

    }

}
