package com.tdclighthouse.prototype.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;

import com.tdclighthouse.prototype.componentsinfo.SimpleNavigationInfo;
import com.tdclighthouse.prototype.utils.Constants;

@ParametersInfo(type = SimpleNavigationInfo.class)
public class SimpleNavigation extends BaseHstComponent {

	@Override
	public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		String menuName = this.<SimpleNavigationInfo> getParametersInfo(request).getMenuName();
		HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu(menuName);
		request.setAttribute(Constants.Attributes.MENU, menu);
	}

}
