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
package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

/**
 * @author Ebrahim Aharpour
 * 
 */
@Node(jcrType = ListItemBean.JCR_NODE)
public class ListItemBean extends HippoCompound {

    public static final String JCR_NODE = "selection:listitem";

    private String key;
    private String label;

    public String getKey() {
        if (this.key == null) {
            this.key = getProperty("selection:key");
        }
        return key;
    }

    public String getLabel() {
        if (this.label == null) {
            this.label = getProperty("selection:label");
        }
        return label;
    }

}
