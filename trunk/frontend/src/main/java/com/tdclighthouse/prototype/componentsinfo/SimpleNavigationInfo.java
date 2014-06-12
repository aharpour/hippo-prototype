package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface SimpleNavigationInfo {

    @Parameter(name = "menuName", defaultValue = "main", displayName = "Menu name")
    public String getMenuName();

}
