package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface SimpleNavigationInfo {

    public static final String MENU_NAME_DEFAULT = "main";
    public static final String MENU_NAME = "menuName";

    @Parameter(name = MENU_NAME, defaultValue = MENU_NAME_DEFAULT, displayName = "Menu name")
    public String getMenuName();

}
