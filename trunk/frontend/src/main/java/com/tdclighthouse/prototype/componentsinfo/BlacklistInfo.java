package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

public interface BlacklistInfo {

	@Parameter(name = "blacklist", displayName = "Blacklist", defaultValue = "", description = "a comma separated list of blacklisted nodes")
	public String getBlacklisted();

}
