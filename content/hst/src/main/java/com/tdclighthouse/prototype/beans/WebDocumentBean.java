/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.beans;

import java.util.GregorianCalendar;

import org.hippoecm.hst.content.beans.Node;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = WebDocumentBean.JCR_TYPE)
public class WebDocumentBean extends BaseDocumentBean implements WebPage {

	public static final String JCR_TYPE = "tdc:WebDocument";

	private String browserTitle;
	private String description;
	private String keywords;
	private String author;
	private Boolean hideFromSitemap;
	private GregorianCalendar releaseDate;
	private Boolean hideFromSearch;

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
	
	public Boolean getHideFromSearch() {
		if (this.hideFromSearch == null) {
			this.hideFromSearch = getProperty("tdc:hideFromSearch");
		}
		return hideFromSearch;
	}

}
