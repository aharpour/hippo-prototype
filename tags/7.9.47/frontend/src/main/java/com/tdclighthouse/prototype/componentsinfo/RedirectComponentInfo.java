package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface RedirectComponentInfo {

    @Parameter(name = "statusCode", displayName = "Redirection status code", defaultValue = "302", description = "Response status code 301 or 302")
    public int getStatusCode();
    
    @Parameter(name = "redirectTo", displayName = "Redirect to", defaultValue = "", description = "Redirect to this page.")
    public String getRedirectTo();

    @Parameter(name = "preservePath", displayName = "Preserve path", defaultValue = "on", description = "Whether to preserver the context path or not.")
    public boolean isPreservePath();
    
    @Parameter(name = "path", displayName = "Path", description = "Path")
    public String getPath();
    
}
