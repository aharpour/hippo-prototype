package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

@Node(jcrType = InternalLinkBean.JCR_TYPE)
public class InternalLinkBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:InternalLink";

	private String label;
	private HippoMirror link;

	public String getLabel() {
		if (this.label == null) {
			this.label = getProperty("tdc:label");
		}
		return label;
	}

	public HippoMirror getLink() {
		if (this.link == null) {
			this.link = getBean("tdc:link");
		}
		return link;
	}

}
