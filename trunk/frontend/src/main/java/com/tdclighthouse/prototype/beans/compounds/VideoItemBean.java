package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

@Node(jcrType = VideoItemBean.JCR_TYPE)
public class VideoItemBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:VideoItem";

	private String url;
	private HippoMirror binary;

	public String getUrl() {
		if (this.url == null) {
			this.url = getProperty("tdc:url");
		}
		return url;
	}

	public HippoMirror getBinary() {
		if (this.binary == null) {
			this.binary = getBean("tdc:binary");
		}
		return binary;
	}

}