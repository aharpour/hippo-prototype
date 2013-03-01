package com.tdclighthouse.prototype.provider;

import java.util.ArrayList;
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
import com.tdclighthouse.prototype.beans.WebDocumentBean;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.NavigationUtils;

/**
 * @author Ebrahim Aharpour
 * 
 *         this component only shows sub-items of a faceted navigation item only
 *         if there is just one facetsavailablenavigation under the facet
 * 
 */
public class RepoBasedMenuProvider {

	public static final Logger log = LoggerFactory.getLogger(RepoBasedMenuProvider.class);

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
		String relativeContentPath = request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath();
		if (relativeContentPath != null) {
			selectedNodeCanonicalPath = siteContentBaseBean.<HippoBean> getBean(relativeContentPath).getCanonicalPath();
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
				List<String> locations = getParameterValues(Constants.HstParameters.ROOT, item);
				HippoBean[] beanOfMenuItems = null;
				if (locations != null && locations.size() > 0) {
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
		String value = getParameterValue(Constants.HstParameters.EXPANDED, item);
		if (Constants.Values.TRUE.equals(value)) {
			String expandeOnlyCurrentItem = getParameterValue(Constants.HstParameters.EXPAND_ONLY_CURRENT_ITEM, item);
			if (Constants.Values.TRUE.equals(expandeOnlyCurrentItem)) {
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
				List<? extends HippoBean> childbearingChildren = getChildbearingChildren(childbearingBean);
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
						addItem(item, document);
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

	private List<? extends HippoBean> getChildbearingChildren(HippoBean childbearingBean) {
		List<? extends HippoBean> result;
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

	private List<? extends HippoBean> getChildbearingChildrenOfFacet(HippoFacetNavigation facetNavigation) {
		List<? extends HippoBean> result;
		List<HippoFacetsAvailableNavigation> availableNavigation = facetNavigation
				.getChildBeans(HippoFacetsAvailableNavigation.class);
		if (availableNavigation == null || availableNavigation.size() != 1) {
			result = new ArrayList<HippoBean>();
		} else {
			result = availableNavigation.get(0).getChildBeans(HippoFacetSubNavigation.class);
		}
		return result;
	}

	public static void markAsSeleted(EditableMenuItem item) {
		EditableMenuItem temp = item;
		if (temp instanceof SimpleEditableMenuItem) {
			((SimpleEditableMenuItem) temp).setSelected(true);
		}
		temp = markAsExpanded(temp);
	}

	public static EditableMenuItem markAsExpanded(EditableMenuItem temp) {
		while (temp != null) {
			temp.setExpanded(true);
			temp = temp.getParentItem();
		}
		return temp;
	}
	
	private void markOnlyCurrentItemAsExpanded(EditableMenuItem item) {
		EditableMenuItem parentItem = item.getParentItem();
		if (parentItem != null) {
			boolean isParentItemExpanded;
			isParentItemExpanded = parentItem.isExpanded();
			item.setExpanded(true);
			item.setExpanded(isParentItemExpanded);
		} else {
			item.setExpanded(true);
		}
	}

	private void addItem(EditableMenuItem item, final HippoBean document) {
		String localizedName = document.getLocalizedName();
		if (document instanceof WebDocumentBean) {
			if (((WebDocumentBean) document).getHideFromSitemap()) {
				if (selectedNodeCanonicalPath != null && selectedNodeCanonicalPath.equals(document.getCanonicalPath())) {
					markAsExpanded(item);
				}
			} else {
				addItem(item, document, localizedName);
			}
		} else {
			addItem(item, document, localizedName);
		}
	}

	private EditableMenuItem addItem(EditableMenuItem item, final HippoBean document, String localizedName) {
		HstLink hstLink = Essentials.createHstLink(document, request);
		EditableMenuItem repoMenuItem = new SimpleEditableMenuItem(item, hstLink, localizedName);
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
			while (bean != null) {
				if (bean.isHippoFolderBean()) {
					result = bean;
					break;
				}
				bean = bean.getParentBean();
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
		if (values.size() > 0) {
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
				log.warn("Parameter name array and parameter values arrays have different lengths");
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
			depth = item.getDepth();
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
	}

}
