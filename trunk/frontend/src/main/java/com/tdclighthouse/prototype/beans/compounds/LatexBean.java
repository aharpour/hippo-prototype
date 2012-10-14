package com.tdclighthouse.prototype.beans.compounds;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = LatexBean.JCR_TYPE)
public class LatexBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:Latex";

	private String latex;
	private String caption;

	public String getLatex() {
		if (this.latex == null) {
			this.latex = getProperty("tdc:latex");
		}
		return latex;
	}

	public String getEncodeLatex() {
		try {
			String escaped = StringEscapeUtils.escapeXml(getLatex());
			return URLEncoder.encode(escaped, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getCaption() {
		if (this.caption == null) {
			this.caption = getProperty("tdc:caption");
		}
		return caption;
	}

}
