package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface MixinEnabled {
    
    @Parameter(name = "tryToUseMixin", displayName = "Try to use mixin", defaultValue = "off")
    public Boolean getUseMixin();
    
}
