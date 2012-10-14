package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = QuotationBean.JCR_TYPE)
public class QuotationBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:Quotation";

	private String quote;
	private String author;

	public String getQuote() {
		if (this.quote == null) {
			this.quote = getProperty("tdc:quote");
		}
		return quote;
	}

	public String getAuthor() {
		if (this.author == null) {
			this.author = getProperty("tdc:author");
		}
		return author;
	}

}