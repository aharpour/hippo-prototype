package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface ValueListAjaxInfo extends BlacklistInfo, ContentBeanPathInfo {

	@Parameter(name = "cacheTime", defaultValue = "1", displayName = "Cache time", description = "cache time in hours")
	public double getCacheTime();

}
