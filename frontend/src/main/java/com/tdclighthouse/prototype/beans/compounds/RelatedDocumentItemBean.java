package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

@Node(jcrType = RelatedDocumentItemBean.JCR_TYPE)
public class RelatedDocumentItemBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:RelatedDocumentItem";

	private String label;
	private HippoMirror internalLink;
	private ExternalLinkBean externalLink;

	public String getLabel() {
		if (this.label == null) {
			this.label = getProperty("tdc:label");
		}
		return label;
	}

	public HippoMirror getInternalLink() {
		if (this.internalLink == null) {
			this.internalLink = getBean("tdc:internalLink");
		}
		return internalLink;
	}

	public ExternalLinkBean getExternalLink() {
		if (this.externalLink == null) {
			this.externalLink = getBean("tdc:externalLink");
		}
		return externalLink;
	}

}