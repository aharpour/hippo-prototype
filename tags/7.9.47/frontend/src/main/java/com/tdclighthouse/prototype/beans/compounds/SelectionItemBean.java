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

import org.onehippo.forge.selection.hst.contentbean.ValueListItem;

/**
 * @author Ebrahim Aharpour
 *
 */
public class SelectionItemBean {

    protected String key;
    protected ValueListItem item;

    public SelectionItemBean(String key, ValueListItem item) {
        this.key = key;
        this.item = item;
    }

    public final String getKey() {
        return key;
    }

    public final String getLabel() {
        return item != null ? item.getLabel() : null;
    }

    public ValueListItem getItem() {
        return item;
    }

}
