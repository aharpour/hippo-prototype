package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType = ParagraphBean.JCR_TYPE)
public class ParagraphBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:Paragraph";

	private String title;
	private HippoHtml content;
	private ParagraphImageBean image;
	

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public HippoHtml getContent() {
		if (this.content == null) {
			this.content = getHippoHtml("tdc:content");
		}
		return content;
	}

	public ParagraphImageBean getImage() {
		if (this.image == null) {
			this.image = getBean("tdc:image");
		}
		return image;
	}
}