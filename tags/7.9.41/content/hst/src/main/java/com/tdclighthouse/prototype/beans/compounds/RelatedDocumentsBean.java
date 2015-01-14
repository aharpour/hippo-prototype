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

import java.util.List;

import org.hippoecm.hst.content.beans.Node;

import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = RelatedDocumentsBean.JCR_TYPE)
public class RelatedDocumentsBean extends TdcDocument {

	public static final String JCR_TYPE = "tdc:RelatedDocuments";

	private String title;
	private List<RelatedDocumentItemBean> relateddocumentitem;

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public List<RelatedDocumentItemBean> getRelateddocumentitem() {
		if (this.relateddocumentitem == null) {
			this.relateddocumentitem = getChildBeans(RelatedDocumentItemBean.JCR_TYPE);
		}
		return relateddocumentitem;
	}

}
