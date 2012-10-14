/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.beans;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;

import com.tdclighthouse.prototype.beans.compounds.ImageBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphImageBean;

/**
 * @author Ebrahim Aharpour
 *
 */
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
