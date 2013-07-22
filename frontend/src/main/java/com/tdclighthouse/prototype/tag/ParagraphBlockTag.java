package com.tdclighthouse.prototype.tag;

import static com.tdclighthouse.prototype.tag.TagUtils.getLink;
import static com.tdclighthouse.prototype.tag.TagUtils.printXmlEscaped;
import static com.tdclighthouse.prototype.tag.TagUtils.renderHippoHtml;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstRequestUtils;

import com.tdclighthouse.prototype.beans.TdcImageSetBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphBean;

public class ParagraphBlockTag extends TagSupport {

	private static final String LEFT = "left";
	private static final String RIGHT = "right";

	private static final long serialVersionUID = 1L;

	private ParagraphBean content;
	private HstRequest hstRequest;
	private HstResponse hstResponse;

	public void setContent(ParagraphBean content) {
		this.content = content;
	}

	private void cleanUp() {
		hstRequest = null;
		hstResponse = null;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			printTitle(out);
			if (content.getImage() != null && content.getImage().getLinkBean() != null) {
				HippoBean imageBean = content.getImage().getLinkBean();
				if (imageBean instanceof TdcImageSetBean) {
					imageBean = ((TdcImageSetBean) imageBean).getParagraphImage();
				}

				out.print("<div class=\"image\">");
				out.print("<p class=\"image ");
				out.print(gethorizontalPosition());
				out.print("\">");
				out.print("<img src=\"");
				out.print(getLink(imageBean, getHstRequest().getRequestContext(), false));
				out.print("\" alt=\"");
				printXmlEscaped(out, content.getImage().getAlt());
				out.print("\" title=\"");
				printXmlEscaped(out, content.getImage().getAlt());
				out.println("\" />");
				if (StringUtils.isNotBlank(content.getImage().getCaption())) {
					out.print("<span>");
					printXmlEscaped(out, content.getImage().getCaption());
					out.println("</span>");
				}
				out.println("</p>");
				printContent(out);
				out.println("</div>");

			} else {
				printContent(out);
			}
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException(e.getMessage(), e);
		} finally {
			cleanUp();
		}

	}

	private String gethorizontalPosition() {
		String result = LEFT;
		if (content.getImage() != null && RIGHT.equals(content.getImage().getHorizontalPosition())) {
			result = RIGHT;
		}
		return result;
	}

	private void printTitle(JspWriter out) throws IOException {
		if (StringUtils.isNotBlank(content.getTitle())) {
			out.print("<h3>");
			printXmlEscaped(out, content.getTitle());
			out.print("</h3>");
		}
	}

	private void printContent(JspWriter out) throws JspException {
		if (content.getContent() != null && StringUtils.isNotBlank(content.getContent().getContent())) {
			renderHippoHtml(content.getContent(), out, false, getHstRequest(), getHstResponse());
		}
	}

	private HstRequest getHstRequest() {
		if (hstRequest == null) {
			HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
			hstRequest = HstRequestUtils.getHstRequest(servletRequest);
		}
		return hstRequest;
	}

	private HstResponse getHstResponse() {
		if (hstResponse == null) {
			HttpServletResponse servletResponse = (HttpServletResponse) pageContext.getResponse();
			hstResponse = HstRequestUtils.getHstResponse(getHstRequest(), servletResponse);
		}
		return hstResponse;
	}

	@Override
	public void release() {
		super.release();
		cleanUp();
	}

}
