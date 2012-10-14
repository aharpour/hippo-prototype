package com.tdclighthouse.prototype.beans;

import org.hippoecm.hst.content.beans.Node;

@Node(jcrType = BaseDocumentBean.JCR_TYPE)
public class BaseDocumentBean extends TdcDocument {
	
	public static final String JCR_TYPE = "tdc:basedocument"; 
	
	private String title;
	private String subtitle;
	private String introduction;

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public String getSubtitle() {
		if (this.subtitle == null) {
			this.subtitle = getProperty("tdc:subtitle");
		}
		return subtitle;
	}

	public String getIntroduction() {
		if (this.introduction == null) {
			this.introduction = getProperty("tdc:introduction");
		}
		return introduction;
	}

}
