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
package com.tdclighthouse.prototype.gallery;

import org.hippoecm.hst.core.linking.AbstractResourceContainer;

import com.tdclighthouse.prototype.beans.TdcImageSetBean;

/**
 * @author Ebrahim Aharpour
 *
 */
public class TdcGalleryResourceContainer extends AbstractResourceContainer {

    @Override
    public String getNodeType() {
        return TdcImageSetBean.JCR_TYPE;
    }

}
