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
package com.tdclighthouse.prototype.beans;

import org.hippoecm.hst.content.beans.Node;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = BaseDocumentBean.JCR_TYPE)
public class BaseDocumentBean extends TdcDocument {
	
	public static final String JCR_TYPE = "tdc:basedocument"; 
	
	private String title;
	private String subtitle;
	private String introduction;

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public String getSubtitle() {
		if (this.subtitle == null) {
			this.subtitle = getProperty("tdc:subtitle");
		}
		return subtitle;
	}

	public String getIntroduction() {
		if (this.introduction == null) {
			this.introduction = getProperty("tdc:introduction");
		}
		return introduction;
	}

}
