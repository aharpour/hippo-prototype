package com.tdclighthouse.prototype.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.rewriter.impl.SimpleContentRewriter;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;

public class TagUtils {

    private TagUtils() {
    }

    public static void renderHippoHtml(HippoHtml hippoHtml, JspWriter out, boolean fullyQualifiedLinks,
            HstRequest hstRequest, HstResponse hstResponse) throws JspException {
        try {
            out.print(hippoHtmlToString(hippoHtml, fullyQualifiedLinks, hstRequest));
        } catch (IOException ioe) {
            throw new JspException(" Exception: cannot write to the output writer.", ioe);
        } catch (IllegalArgumentException e) {
            throw new JspException(e.getMessage(), e);
        }

    }

    public static String hippoHtmlToString(HippoHtml hippoHtml, boolean fullyQualifiedLinks, HstRequest hstRequest) {
        validateHippoHtml(hippoHtml, hstRequest);
        SimpleContentRewriter contentRewriter = new SimpleContentRewriter();
        contentRewriter.setFullyQualifiedLinks(fullyQualifiedLinks);
        String html = hippoHtml.getContent();
        html = contentRewriter.rewrite(html, hippoHtml.getNode(), hstRequest.getRequestContext());

        if (html == null) {
            html = "";
        }
        return html;
    }

    private static void validateHippoHtml(HippoHtml hippoHtml, HstRequest hstRequest) {
        if (hstRequest == null) {
            throw new IllegalArgumentException(
                    "Cannot continue HstHtmlTag because response/request not an instance of hst response/request");
        }
        if (hippoHtml == null || hippoHtml.getContent() == null || hippoHtml.getNode() == null) {
            throw new IllegalArgumentException("Node or content is null.");
        }
    }

    public static String getLink(HippoBean bean, HstRequestContext requestContext, boolean fullyQualified) {
        HstLink hstLink = requestContext.getHstLinkCreator().create(bean, requestContext);
        return hstLink.toUrlForm(requestContext, fullyQualified);
    }

}
