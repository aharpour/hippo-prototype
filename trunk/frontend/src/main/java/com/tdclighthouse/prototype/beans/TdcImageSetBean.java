package com.tdclighthouse.prototype.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

@Node(jcrType = TdcImageSetBean.JCR_TYPE)
public class TdcImageSetBean extends HippoGalleryImageSet {

	public static final String JCR_TYPE = "tdc:TdcImageSet";

	private HippoGalleryImageBean articleImage;
	private HippoGalleryImageBean paragraphImage;

	public HippoGalleryImageBean getArticleImage() {
		if (this.articleImage == null) {
			this.articleImage = getBean("tdc:articleImage");
		}
		return articleImage;
	}

	public HippoGalleryImageBean getParagraphImage() {
		if (this.paragraphImage == null) {
			this.paragraphImage = getBean("tdc:paragraphImage");
		}
		return paragraphImage;
	}

}
