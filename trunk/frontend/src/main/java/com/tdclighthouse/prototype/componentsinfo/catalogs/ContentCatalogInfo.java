package com.tdclighthouse.prototype.componentsinfo.catalogs;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.TemplateInfo;
import com.tdclighthouse.prototype.utils.Constants;

public interface ContentCatalogInfo extends ContentBeanPathInfo, TemplateInfo {

    @JcrPath
    @Parameter(name = Constants.HstParametersConstants.CONTENT_BEAN_PATH, displayName = "Second document")
    public String getContentBeanPath();

}
