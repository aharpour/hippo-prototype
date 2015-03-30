package com.tdclighthouse.prototype.tag;

import static com.tdclighthouse.prototype.tag.TagUtils.getLink;
import static com.tdclighthouse.prototype.tag.TagUtils.renderHippoHtml;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import com.tdclighthouse.prototype.beans.TdcImageSetBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphBean;

public class ParagraphBlockTag extends PrototypeTagSupport {

    private static final String LEFT = "left";
    private static final String RIGHT = "right";

    private static final long serialVersionUID = 1L;

    private ParagraphBean content;

    public void setContent(ParagraphBean content) {
        this.content = content;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            renderParagraph(content, out, getHstRequest(), getHstResponse());
            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException(e.getMessage(), e);
        } finally {
            reset();
        }

    }

    public static void renderParagraph(ParagraphBean content, JspWriter out, HstRequest hstRequest,
            HstResponse hstResponse) throws IOException, JspException {
        printTitle(content, out);
        if (content.getImage() != null && content.getImage().getLinkBean() != null) {
            HippoBean imageBean = content.getImage().getLinkBean();
            if (imageBean instanceof TdcImageSetBean) {
                imageBean = ((TdcImageSetBean) imageBean).getParagraphImage();
            }

            out.print("<div class=\"image\">");
            out.print("<p class=\"image ");
            out.print(gethorizontalPosition(content));
            out.print("\">");
            out.print("<img src=\"");
            out.print(getLink(imageBean, hstRequest.getRequestContext(), false));
            out.print("\" alt=\"");
            out.print(escapeXml(content.getImage().getAlt()));
            out.print("\" title=\"");
            out.print(escapeXml(content.getImage().getTitle()));
            out.println("\" />");
            if (StringUtils.isNotBlank(content.getImage().getCaption())) {
                out.print("<span>");
                out.print(escapeXml(content.getImage().getCaption()));
                out.println("</span>");
            }
            out.println("</p>");
            printContent(content, out, hstRequest, hstResponse);
            out.println("</div>");

        } else {
            printContent(content, out, hstRequest, hstResponse);
        }
    }

    private static String gethorizontalPosition(ParagraphBean content) {
        String result = LEFT;
        if (content.getImage() != null && RIGHT.equals(content.getImage().getHorizontalPosition())) {
            result = RIGHT;
        }
        return result;
    }

    private static void printTitle(ParagraphBean content, JspWriter out) throws IOException {
        if (StringUtils.isNotBlank(content.getTitle())) {
            out.print("<h3>");
            out.print(escapeXml(content.getTitle()));
            out.print("</h3>");
        }
    }

    private static void printContent(ParagraphBean content, JspWriter out, HstRequest hstRequest,
            HstResponse hstResponse) throws JspException {
        if (content.getContent() != null && StringUtils.isNotBlank(content.getContent().getContent())) {
            renderHippoHtml(content.getContent(), out, false, hstRequest, hstResponse);
        }
    }

}
