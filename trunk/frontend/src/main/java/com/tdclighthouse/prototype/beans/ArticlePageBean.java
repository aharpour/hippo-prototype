package com.tdclighthouse.prototype.beans;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;

import com.tdclighthouse.prototype.beans.compounds.ImageBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphImageBean;

@Node(jcrType = ArticlePageBean.JCR_TYPE)
public class ArticlePageBean extends WebDocumentBean {
	public static final String JCR_TYPE = "tdc:ArticlePage";

	private List<HippoBean> flexibleBlock;

	public List<HippoBean> getFlexibleBlock() {
		if (flexibleBlock == null) {
			flexibleBlock = getChildBeansByName("tdc:flexibleBlock");
		}
		return flexibleBlock;
	}

	public ImageBean getImage() {
		ImageBean result = null;
		List<HippoBean> list = getFlexibleBlock();
		for (HippoBean hippoBean : list) {
			if (hippoBean instanceof ParagraphBean) {
				ParagraphBean paragraph = (ParagraphBean) hippoBean;
				ParagraphImageBean image = paragraph.getImage();
				if (image != null && image.getLink().getDeref() != null && image.getLink().getDeref() != null) {
					result = image;
					break;
				}
			}
		}
		return result;
	}

}
