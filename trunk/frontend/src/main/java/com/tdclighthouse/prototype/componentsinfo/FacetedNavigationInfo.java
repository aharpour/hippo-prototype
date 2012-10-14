package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface FacetedNavigationInfo {
	@Parameter(name = "labelPaths", description = "A comma separated list of paths to selection dropdowns", defaultValue = "")
	public String getLabelPaths();
}
