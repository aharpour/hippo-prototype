package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface FacetDeepLinkNavInfo {

    @Parameter(name = "facetPath")
    public String getFacetPath();

    @Parameter(name = "configuration")
    public String getConfiguration();

}
