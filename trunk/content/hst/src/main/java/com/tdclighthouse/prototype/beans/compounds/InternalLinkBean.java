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
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = InternalLinkBean.JCR_TYPE)
public class InternalLinkBean extends TdcDocument {

	public static final String JCR_TYPE = "tdc:InternalLink";

	private String label;
	private HippoMirror link;
	private HippoDocument linkBean;

	public String getLabel() {
		if (this.label == null) {
			this.label = getProperty("tdc:label");
		}
		return label;
	}

	public HippoMirror getLink() {
		if (this.link == null) {
			this.link = getBean("tdc:link");
		}
		return link;
	}

	public HippoDocument getLinkBean() {
		if (this.linkBean == null) {
			this.linkBean = getLinkedBean("tdc:link", HippoDocument.class);
		}
		return linkBean;
	}

}
