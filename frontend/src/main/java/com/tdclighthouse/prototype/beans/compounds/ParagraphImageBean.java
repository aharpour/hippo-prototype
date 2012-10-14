package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;

@Node(jcrType = ParagraphImageBean.JCR_TYPE)
public class ParagraphImageBean extends ImageBean {

	public static final String JCR_TYPE = "tdc:ParagraphImage";

	private String horizontalPosition;
	private String verticalPosition;

	public String getHorizontalPosition() {
		if (this.horizontalPosition == null) {
			this.horizontalPosition = getProperty("tdc:horizontalPosition");
		}
		return horizontalPosition;
	}

	public String getVerticalPosition() {
		if (this.verticalPosition == null) {
			this.verticalPosition = getProperty("tdc:verticalPosition");
		}
		return verticalPosition;
	}

}