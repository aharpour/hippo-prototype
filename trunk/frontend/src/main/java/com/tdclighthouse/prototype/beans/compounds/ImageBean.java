package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

@Node(jcrType = ImageBean.JCR_TYPE)
public class ImageBean extends HippoDocument {
	
	public static final String JCR_TYPE = "tdc:Image";
	
	private String title;
	private String alt;
	private String caption;
	private HippoMirror link;
	private String credit;

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public String getAlt() {
		if (this.alt == null) {
			this.alt = getProperty("tdc:alt");
		}
		return alt;
	}

	public String getCaption() {
		if (this.caption == null) {
			this.caption = getProperty("tdc:caption");
		}
		return caption;
	}

	public HippoMirror getLink() {
		if (this.link == null) {
			this.link = getBean("tdc:link");
		}
		return link;
	}
	
	public String getCredit() {
		if (this.credit == null) {
			this.credit = getProperty("tdc:credit");
		}
		return credit;
	}

}