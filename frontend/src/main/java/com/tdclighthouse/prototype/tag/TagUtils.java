package com.tdclighthouse.prototype.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringEscapeUtils;
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
			validateHippoHtml(hippoHtml, hstRequest, hstResponse);

			SimpleContentRewriter contentRewriter = new SimpleContentRewriter();
			contentRewriter.setFullyQualifiedLinks(fullyQualifiedLinks);
			String html = hippoHtml.getContent();
			html = contentRewriter.rewrite(html, hippoHtml.getNode(), hstRequest.getRequestContext());

			if (html == null) {
				html = "";
			}
			out.print(html);
		} catch (IOException ioe) {
			throw new JspException(" Exception: cannot write to the output writer.");
		}

	}

	public static void printXmlEscaped(JspWriter out, String value) throws IOException {
		out.print(StringEscapeUtils.escapeXml(value));
	}

	private static void validateHippoHtml(HippoHtml hippoHtml, HstRequest hstRequest, HstResponse hstResponse)
			throws JspException {
		if (hstRequest == null || hstResponse == null) {
			throw new JspException(
					"Cannot continue HstHtmlTag because response/request not an instance of hst response/request");
		}
		if (hippoHtml == null || hippoHtml.getContent() == null || hippoHtml.getNode() == null) {
			throw new JspException("Node or content is null.");
		}
	}
	
	public static String getLink(HippoBean bean, HstRequestContext requestContext, boolean fullyQualified) {
		HstLink hstLink = requestContext.getHstLinkCreator().create(bean, requestContext);
		return hstLink.toUrlForm(requestContext, fullyQualified);
	}

}
