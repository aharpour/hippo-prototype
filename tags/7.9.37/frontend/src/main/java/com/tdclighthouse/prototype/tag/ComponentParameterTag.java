package com.tdclighthouse.prototype.tag;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;

import com.tdclighthouse.prototype.utils.ComponentUtils;

public class ComponentParameterTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private String var;
    private String name;

    public ComponentParameterTag() {
        init();
    }

    private void init() {
        name = var = null;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            if (StringUtils.isNotBlank(name)) {

                String parameterName = ComponentUtils.getComponentSpecificParameterName(getHstRequest(), name);
                if (StringUtils.isNotBlank(var)) {
                    pageContext.setAttribute(var, parameterName);
                } else {
                    pageContext.getOut().write(parameterName);
                }
            } else {
                throw new JspException("Name argument is requred.");
            }
            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException(e.getMessage(), e);
        }
    }

    private HstRequest getHstRequest() throws JspException {
        ServletRequest request = pageContext.getRequest();
        if (!(request instanceof HstRequest)) {
            throw new JspException("request could not be cast to HstRequest.");
        }
        return (HstRequest) request;
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
