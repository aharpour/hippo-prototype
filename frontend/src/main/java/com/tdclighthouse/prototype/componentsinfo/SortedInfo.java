package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

import com.tdclighthouse.prototype.utils.Constants;

public interface SortedInfo {

	@Parameter(name = "sortOrder", defaultValue = Constants.Values.DESCENDING)
	public String getSortOrder();

	@Parameter(name = "sortBy", defaultValue = Constants.FieldName.TDC_RELEASE_DATE)
	public String getSortBy();

}
