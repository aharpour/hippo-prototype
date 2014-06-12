package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.Constants.PikcerTypes;

public interface TemplateInfo {

    @JcrPath(pickerConfiguration = PikcerTypes.ASSET_PICKER, isRelative = false)
    @Parameter(name = Constants.HstParameters.TEMPLATE, displayName = "Template", required = true)
    public String getTemplate();

}
