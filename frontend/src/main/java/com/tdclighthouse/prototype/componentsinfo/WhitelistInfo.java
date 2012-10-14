package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface WhitelistInfo {

	@Parameter(name = "whitelist", displayName = "Whiteist", defaultValue = "", description = "a comma separated list of whitelisted nodes")
	public String getWhiteisted();

}
