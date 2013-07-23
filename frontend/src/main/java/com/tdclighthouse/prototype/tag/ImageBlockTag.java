package com.tdclighthouse.prototype.tag;

import static com.tdclighthouse.prototype.tag.TagUtils.getLink;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;
import org.hippoecm.hst.core.component.HstRequest;

import com.tdclighthouse.prototype.beans.TdcImageSetBean;
import com.tdclighthouse.prototype.beans.compounds.ImageBean;

public class ImageBlockTag extends PrototypeTagSupport {

	private static final long serialVersionUID = 1L;

	private ImageBean content;

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			printImageBean(content, out, getHstRequest());

			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException(e.getMessage(), e);
		} finally {
			reset();
		}
	}

	private static void printImageBean(ImageBean imageBean, JspWriter out, HstRequest hstRequest) throws IOException {
		HippoGalleryImageSetBean linkBean = imageBean.getLinkBean();
		if (linkBean != null) {
			HippoBean image = getImageBean(linkBean);
			out.print("<img src=\"");
			out.print(getLink(image, hstRequest.getRequestContext(), false));
			out.print("\" alt=\"");
			out.print(escapeXml(imageBean.getAlt()));
			out.print("\" title=\"");
			out.print(escapeXml(imageBean.getTitle()));
			out.println("\" />");
			if (StringUtils.isNotBlank(imageBean.getCaption()) || StringUtils.isNotBlank(imageBean.getCredit())) {
				out.println("<p class=\"note\">");
				if (StringUtils.isNotBlank(imageBean.getCaption())) {
					out.print(escapeXml(imageBean.getCaption()));
				}
				if (StringUtils.isNotBlank(imageBean.getCredit())) {
					out.print("<span class=\"source\">");
					out.print(escapeXml(imageBean.getCredit()));
					out.println("</span>");
				}
				out.println("</p>");
			}
		}
	}

	private static HippoBean getImageBean(HippoGalleryImageSetBean linkBean) {
		HippoBean imageBean;
		if (linkBean instanceof TdcImageSetBean) {
			imageBean = ((TdcImageSetBean) linkBean).getArticleImage();
		} else {
			imageBean = linkBean;
		}
		return imageBean;
	}

	public void setContent(ImageBean content) {
		this.content = content;
	}
}
