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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ebrahim Aharpour
 *
 */
public class SelectionBean {

    private final Map<String, ListItemBean> itemsMap;
    private final List<SelectionItemBean> items = new ArrayList<SelectionItemBean>();

    public SelectionBean(Map<String, ListItemBean> itemsMap, Object propertyValue) {
        this.itemsMap = itemsMap;
        if (propertyValue != null) {
            if (propertyValue.getClass().isArray()) {
                String[] keys = (String[]) propertyValue;
                for (String key : keys) {
                    addSelectionItemBean(itemsMap, key);
                }
            } else {
                String key = (String) propertyValue;
                addSelectionItemBean(itemsMap, key);
            }
        }
    }

    private void addSelectionItemBean(Map<String, ListItemBean> labelsMap, String key) {
        items.add(new SelectionItemBean(key, labelsMap.get(key)));
    }

    public Map<String, ListItemBean> getItemsMap() {
        return itemsMap;
    }

    public List<SelectionItemBean> getItems() {
        return items;
    }

    public SelectionItemBean getFirstItem() {
        SelectionItemBean result = null;
        if (items.isEmpty()) {
            result = items.get(0);
        }
        return result;
    }

}
