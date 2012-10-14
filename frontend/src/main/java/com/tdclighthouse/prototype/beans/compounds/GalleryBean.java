package com.tdclighthouse.prototype.beans.compounds;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = GalleryBean.JCR_TYPE)
public class GalleryBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:Gallery";

	private String title;
	private List<ImageBean> images;

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public List<ImageBean> getImages() {
		if (this.images == null) {
			this.images = getChildBeans(ImageBean.JCR_TYPE);
		}
		return images;
	}

}