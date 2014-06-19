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

import java.util.List;

import org.hippoecm.hst.content.beans.Node;

import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = ValueListBean.JCR_NODE)
public class ValueListBean extends TdcDocument {

	public static final String JCR_NODE = "selection:valuelist";
	private List<ListItemBean> listItem;

	public List<ListItemBean> getListItem() {
		if (listItem == null) {
			listItem = getChildBeansByName("selection:listitem");
		}
		return listItem;
	}
}
