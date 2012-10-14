package com.tdclighthouse.prototype.beans.compounds;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = ValueListBean.JCR_NODE)
public class ValueListBean extends HippoDocument {

	public static final String JCR_NODE = "selection:valuelist";
	private List<ListItemBean> listItem;

	public List<ListItemBean> getListItem() {
		if (listItem == null) {
			listItem = getChildBeansByName("selection:listitem");
		}
		return listItem;
	}
}
