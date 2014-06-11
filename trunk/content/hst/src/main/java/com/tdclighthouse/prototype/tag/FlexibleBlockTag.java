package com.tdclighthouse.prototype.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.beans.compounds.AudioBean;
import com.tdclighthouse.prototype.beans.compounds.GalleryBean;
import com.tdclighthouse.prototype.beans.compounds.ImageBean;
import com.tdclighthouse.prototype.beans.compounds.LatexBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphBean;
import com.tdclighthouse.prototype.beans.compounds.QuotationBean;
import com.tdclighthouse.prototype.beans.compounds.RelatedDocumentItemBean;
import com.tdclighthouse.prototype.beans.compounds.VideoBean;

import freemarker.template.TemplateException;

public class FlexibleBlockTag extends PrototypeTagSupport {

	private static Logger log = LoggerFactory.getLogger(FlexibleBlockTag.class);
	private static final long serialVersionUID = 1L;
	
	
	private List<HippoBean> content;
	private String flexibleblockid;
	
	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			for (int i = 0; i < content.size(); i++) {
				HippoBean hippoBean = content.get(i);
				if (hippoBean instanceof ParagraphBean) {
					ParagraphBlockTag.renderParagraph((ParagraphBean) hippoBean, out, getHstRequest(), getHstResponse());
				} else if (hippoBean instanceof ImageBean) {
					ImageBlockTag.renderImageBean((ImageBean) hippoBean, out, getHstRequest());
				} else if (hippoBean instanceof RelatedDocumentItemBean) {
					out.println("<h3>RelatedDocumentItemBean is not supported yet.</h3>");//TODO
				} else if (hippoBean instanceof VideoBean) {
					out.println("<h3>VideoBean is not supported yet.</h3>");//TODO
				} else if (hippoBean instanceof AudioBean) {
					out.println("<h3>AudioBean is not supported yet.</h3>");//TODO
				} else if (hippoBean instanceof LatexBean) {
					HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
					LatexBlockTag.renter((LatexBean) hippoBean, servletRequest.getContextPath(), out);
				} else if (hippoBean instanceof QuotationBean) {
					QuotationBlockTag.renderQuotation((QuotationBean) hippoBean, out);
				} else if (hippoBean instanceof GalleryBean) {
					out.println("<h3>GalleryBean is not supported yet.</h3>");//TODO
				} else {
					if (hippoBean != null) {
						log.error("flexibleblock tag does not support items of type \"" + hippoBean.getClass().getName() + "\"");
					}
				}
				
			}
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException("A IOException while trying to write to jsp out writer.", e);
		} catch (TemplateException e) {
			throw new JspException(e.getMessage(), e);
		}finally {
			reset();
		}
	}
	
	@Override
	protected void reset() {
		super.reset();
		content = null;
		flexibleblockid = null;
	}
	
	public void setContent(List<HippoBean> content) {
		this.content = content;
	}
	public void setFlexibleblockid(String flexibleblockid) {
		this.flexibleblockid = flexibleblockid;
	}
	
	

}
