package com.tdclighthouse.prototype.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.tdclighthouse.prototype.beans.compounds.LatexBean;

public class LatexBlockTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private LatexBean content;

    @Override
    public int doEndTag() throws JspException {
        try {
            HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
            renter(content, servletRequest.getContextPath(), pageContext.getOut());
            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException(e.getMessage(), e);
        }
    }

    public static void renter(LatexBean latexBean, String contextPath, JspWriter out) throws IOException {
        if (latexBean != null && StringUtils.isNotBlank(latexBean.getLatex())) {
            out.print("<img src=\"");
            out.print(contextPath);
            out.print("/latex/?latex=");
            out.print(latexBean.getEncodeLatex());
            out.print("\" alt=\"");
            out.print(StringEscapeUtils.escapeXml(latexBean.getLatex()));
            out.print("\" title=\"");
            out.print(StringEscapeUtils.escapeXml(latexBean.getLatex()));
            out.println("\"/>");
            if (StringUtils.isNotBlank(latexBean.getCaption())) {
                out.print("<p class=\"note\">");
                out.print(StringEscapeUtils.escapeXml(latexBean.getCaption()));
                out.println("</p>");
            }
        }

    }

    @Override
    public void release() {
        super.release();
        content = null;
    }

    public void setContent(LatexBean content) {
        this.content = content;
    }

}
