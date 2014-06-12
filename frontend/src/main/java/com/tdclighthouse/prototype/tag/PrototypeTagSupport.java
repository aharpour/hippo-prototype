package com.tdclighthouse.prototype.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.HstRequestUtils;

public abstract class PrototypeTagSupport extends TagSupport {

    private static final long serialVersionUID = 1L;

    private HstRequest hstRequest;
    private HstResponse hstResponse;

    protected void reset() {

        hstRequest = null;
        hstResponse = null;
    }

    protected HstRequest getHstRequest() {
        if (hstRequest == null) {
            HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
            hstRequest = HstRequestUtils.getHstRequest(servletRequest);
        }
        return hstRequest;
    }

    protected HstResponse getHstResponse() {
        if (hstResponse == null) {
            HttpServletResponse servletResponse = (HttpServletResponse) pageContext.getResponse();
            hstResponse = HstRequestUtils.getHstResponse(getHstRequest(), servletResponse);
        }
        return hstResponse;
    }

    protected static String escapeXml(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

    @Override
    public void release() {
        super.release();
        reset();
    }

}
