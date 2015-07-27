package com.tdclighthouse.prototype.tag;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.tdclighthouse.prototype.beans.compounds.QuotationBean;
import com.tdclighthouse.prototype.utils.FreemarkerUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class QuotationBlockTag extends PrototypeTagSupport {

    private static final long serialVersionUID = 1L;

    private static final String TEMPLATE_PATH = "/com/tdclighthouse/prototype/tag/quotationblock.ftl";
    private static final Template template = FreemarkerUtils.getTemplate(TEMPLATE_PATH, ParagraphBlockTag.class);

    private QuotationBean content;

    public void setContent(QuotationBean content) {
        this.content = content;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            renderQuotation(content, out);
            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException(e.getMessage(), e);
        } catch (TemplateException e) {
            throw new JspException(e.getMessage(), e);
        } finally {
            reset();
        }

    }

    public static void renderQuotation(QuotationBean content, JspWriter out) throws IOException, TemplateException {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("content", content);
        template.process(model, out);
    }

}
