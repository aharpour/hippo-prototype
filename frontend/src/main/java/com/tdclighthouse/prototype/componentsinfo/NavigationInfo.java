/*
 *  Copyright 2012 Smile B.V.
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

/**
 * @author Ebrahim Aharpour
 * 
 */
public interface NavigationInfo extends SimpleNavigationInfo, LabelsInfo {

    public static final String SHOW_FACETED_NAVIGATION_DEFAULT = "false";
    public static final String SHOW_FACETED_NAVIGATION = "showFacetedNavigation";

    @Parameter(name = SHOW_FACETED_NAVIGATION, defaultValue = SHOW_FACETED_NAVIGATION_DEFAULT, displayName = "Show faceted navigation items in the site menu")
    public boolean isShowFacetedNavigation();

}
