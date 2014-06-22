/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
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

import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 *
 */
public interface SortedInfo {

    @Parameter(name = "sortOrder", defaultValue = Constants.ValuesConstants.DESCENDING)
    public String getSortOrder();

    @Parameter(name = "sortBy", defaultValue = Constants.FieldNameConstants.TDC_RELEASE_DATE)
    public String getSortBy();

}
