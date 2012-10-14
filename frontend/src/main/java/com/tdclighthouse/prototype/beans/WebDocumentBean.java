package com.tdclighthouse.prototype.beans;

import java.util.GregorianCalendar;

import org.hippoecm.hst.content.beans.Node;

@Node(jcrType = WebDocumentBean.JCR_TYPE)
public class WebDocumentBean extends BaseDocumentBean {

	public static final String JCR_TYPE = "tdc:WebDocument";

	private String browserTitle;
	private String description;
	private String keywords;
	private String author;
	private Boolean hideFromSitemap;
	private GregorianCalendar releaseDate;

	public String getBrowserTitle() {
		if (this.browserTitle == null) {
			this.browserTitle = getProperty("tdc:browserTitle");
		}
		return browserTitle;
	}

	public String getDescription() {
		if (this.description == null) {
			this.description = getProperty("tdc:description");
		}
		return description;
	}

	public String getKeywords() {
		if (this.keywords == null) {
			this.keywords = getProperty("tdc:keywords");
		}
		return keywords;
	}

	public String getAuthor() {
		if (this.author == null) {
			this.author = getProperty("tdc:author");
		}
		return author;
	}

	public Boolean getHideFromSitemap() {
		if (this.hideFromSitemap == null) {
			this.hideFromSitemap = getProperty("tdc:hideFromSitemap");
		}
		return hideFromSitemap;
	}

	public GregorianCalendar getReleaseDate() {
		if (this.releaseDate == null) {
			this.releaseDate = getProperty("tdc:releaseDate");
		}
		return releaseDate;
	}

}
