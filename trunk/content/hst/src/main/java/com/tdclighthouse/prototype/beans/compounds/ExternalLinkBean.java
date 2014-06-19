/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
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
package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;

import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = ExternalLinkBean.JCR_TYPE)
public class ExternalLinkBean extends TdcDocument {

	public static final String JCR_TYPE = "tdc:ExternalLink";

	private String url;
	private String relationship;

	public String getUrl() {
		if (this.url == null) {
			this.url = getProperty("tdc:url");
		}
		return url;
	}

	public String getRelationship() {
		if (this.relationship == null) {
			this.relationship = getProperty("tdc:relationship");
		}
		return relationship;
	}

}
