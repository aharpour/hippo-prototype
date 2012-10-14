package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

/**
 * @author Ebrahim Aharpour
 * 
 */
@Node(jcrType = ListItemBean.JCR_NODE)
public class ListItemBean extends HippoDocument {

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
