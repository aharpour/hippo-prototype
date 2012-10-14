package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = ExternalLinkBean.JCR_TYPE)
public class ExternalLinkBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:ExternalLink";

	private String url;
	private String relationship;

	public String getUrl() {
		if (this.url == null) {
			this.url = getProperty("tdc:url");
		}
		return url;
	}

	public String getRelationship() {
		if (this.relationship == null) {
			this.relationship = getProperty("tdc:relationship");
		}
		return relationship;
	}

}
