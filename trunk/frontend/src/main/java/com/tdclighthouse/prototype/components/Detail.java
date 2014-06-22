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
package com.tdclighthouse.prototype.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import com.tdclighthouse.prototype.utils.Constants;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class Detail extends BaseComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        HippoBean contentBean = request.getRequestContext().getContentBean();
        if (contentBean != null) {
            request.setAttribute(Constants.AttributesConstants.DOCUMENT, contentBean);
        }
    }

}
