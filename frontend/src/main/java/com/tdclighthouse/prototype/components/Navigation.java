package com.tdclighthouse.prototype.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.sitemenu.EditableMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.componentsinfo.SimpleNavigationInfo;
import com.tdclighthouse.prototype.provider.RepoBasedMenuProvider;
import com.tdclighthouse.prototype.utils.Constants;

@ParametersInfo(type = SimpleNavigationInfo.class)
public class Navigation extends BaseHstComponent {

	
	protected static Logger log = LoggerFactory.getLogger(Navigation.class);

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		String menuName = this.<SimpleNavigationInfo> getParametersInfo(request).getMenuName();
		HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(menuName);
		if (menu != null) {
			EditableMenu editableMenu = menu.getEditableMenu();
			new RepoBasedMenuProvider(getSiteContentBaseBean(request), request).addRepoBasedMenuItems(editableMenu);
			request.setAttribute(Constants.Attributes.MENU, editableMenu);
		}
	}
	
}
