/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.componentsinfo;

import org.hippoecm.hst.core.parameters.Parameter;

/**
 * @author Ebrahim Aharpour
 *
 */
public interface PaginatedInfo {

    @Parameter(name = "defaultPageSize", displayName = "Default page size", defaultValue = "25", description = "Default Number of items per page")
    public int getDefaultPageSzie();

    @Parameter(name = "showPaginaotr", displayName = "Show paginator", defaultValue = "true", description = "Whether to show paginator or not")
    public boolean getShowPaginator();

    @Parameter(name = "numberOfPagesShow", displayName = "Number of pages shown", description = "Number of pages shown in the paginator", defaultValue = "9")
    public int getNumberOfPagesShow();

}
