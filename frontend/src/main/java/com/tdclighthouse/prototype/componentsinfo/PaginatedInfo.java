package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface PaginatedInfo {

	@Parameter(name = "defaultPageSize", displayName = "Default page size", defaultValue = "25", description = "Default Number of items per page")
	public int getDefaultPageSzie();

	@Parameter(name = "showPaginaotr", displayName = "Show paginator", defaultValue = "true", description = "Whether to show paginator or not")
	@DropDownList(value = { "true", "false" })
	public boolean getShowPaginator();

	@Parameter(name = "numberOfPagesShow", displayName = "Number of pages shown", description = "Number of pages shown in the paginator", defaultValue = "9")
	public int getNumberOfPagesShow();

}
