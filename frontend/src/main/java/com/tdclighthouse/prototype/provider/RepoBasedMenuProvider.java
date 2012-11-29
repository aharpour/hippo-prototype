package com.tdclighthouse.prototype.provider;

import java.util.List;
import java.util.Map;

import org.hippoecm.hst.configuration.HstNodeTypes;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
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
import com.tdclighthouse.prototype.utils.NavigationUtils;

public class RepoBasedMenuProvider {
	public static final Logger log = LoggerFactory.getLogger(RepoBasedMenuProvider.class);

	private final HstRequest request;
	private final String selectedNodeCanonicalPath;
	private final HippoBean siteContentBaseBean;

	public RepoBasedMenuProvider(HippoBean siteContentBaseBean, HstRequest request) {
		this.request = request;
		this.siteContentBaseBean = siteContentBaseBean;
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
			if (item.isRepositoryBased()) {
				String root = getRootParameterValue(item.getProperties());
				HippoBean beanOfMenuItem = null;
				if (root != null) {
					beanOfMenuItem = siteContentBaseBean.getBean(root);
				} else {
					beanOfMenuItem = getBeanOfMenuItem(item);
				}
				addSubitems(item, beanOfMenuItem, 1);
			}
		}
	}

	private void addSubitems(EditableMenuItem item, HippoBean indexPageBean, int depth) {
		if (item.getDepth() >= depth) {
			HippoFolderBean folderBean = getFolder(indexPageBean);
			if (folderBean != null) {
				for (HippoFolderBean folder : folderBean.getFolders()) {
					HippoBean foldersIndex = NavigationUtils.getIndexBean(folder);
					if (foldersIndex != null) {
						EditableMenuItem folderItem = addItem(item, foldersIndex, folder.getLocalizedName());
						addSubitems(folderItem, foldersIndex, depth + 1);
					}

				}
				for (final HippoBean document : folderBean.getDocuments()) {
					if (!document.getCanonicalPath().equals(indexPageBean.getCanonicalPath())) {
						addItem(item, document);
					}
				}
			}
		}
	}

	private void setSelected(EditableMenuItem item) {
		EditableMenuItem temp = item;
		if (temp instanceof SimpleEditableMenuItem) {
			((SimpleEditableMenuItem) temp).setSelected(true);
		}
		while (temp != null) {
			temp.setExpanded(true);
			temp = temp.getParentItem();
		}
	}

	private EditableMenuItem addItem(EditableMenuItem item, final HippoBean document) {
		String localizedName = document.getLocalizedName();
		return addItem(item, document, localizedName);
	}

	private EditableMenuItem addItem(EditableMenuItem item, final HippoBean document, String localizedName) {
		HstLink hstLink = Essentials.createHstLink(document, request);
		EditableMenuItem repoMenuItem = new SimpleEditableMenuItem(item, hstLink, localizedName);
		item.addChildMenuItem(repoMenuItem);
		if (selectedNodeCanonicalPath != null && selectedNodeCanonicalPath.equals(document.getCanonicalPath())) {
			setSelected(repoMenuItem);
		}
		return repoMenuItem;
	}

	

	private HippoFolderBean getFolder(HippoBean bean) {
		HippoFolderBean result = null;
		while (bean != null) {
			if (bean.isHippoFolderBean()) {
				result = (HippoFolderBean) bean;
				break;
			}
			bean = bean.getParentBean();
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

	private String getRootParameterValue(Map<String, Object> properties) {
		String root = null;
		String[] paramNames = (String[]) properties.get(HstNodeTypes.GENERAL_PROPERTY_PARAMETER_NAMES);
		String[] paramValues = (String[]) properties.get(HstNodeTypes.GENERAL_PROPERTY_PARAMETER_VALUES);
		if (paramNames != null && paramValues != null) {

			if (paramNames.length == paramValues.length) {
				for (int i = 0; i < paramNames.length; i++) {
					String propName = paramNames[i];
					if ("root".equals(propName)) {
						root = paramValues[i];
						break;
					}
				}
			} else {
				log.warn("Parameter name array and parameter values arrays have different lengths");
			}
		}
		return root;
	}

	public static class SimpleEditableMenuItem extends EditableMenuItemImpl {

		private HstLink hstLink;
		private String localizedName;
		private int depth;

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