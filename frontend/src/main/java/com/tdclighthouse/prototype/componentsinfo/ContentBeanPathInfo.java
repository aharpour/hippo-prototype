package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

import com.tdclighthouse.prototype.utils.Constants;

public interface ContentBeanPathInfo {

	/**
	 * you can both use, a relative path from the site content base or an absolute path
	 * 
	 */
	@Parameter(name = Constants.HstParameters.CONTENT_BEAN_PATH, displayName = "Index file Path")
	public String getContentBeanPath();
}
