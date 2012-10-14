package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

@Node(jcrType = VideoImageBean.JCR_TYPE)
public class VideoImageBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:VideoImage";

	private String url;
	private HippoMirror image;

	public String getUrl() {
		if (this.url == null) {
			this.url = getProperty("tdc:url");
		}
		return url;
	}

	public HippoMirror getImage() {
		if (this.image == null) {
			this.image = getBean("tdc:image");
		}
		return image;
	}

}