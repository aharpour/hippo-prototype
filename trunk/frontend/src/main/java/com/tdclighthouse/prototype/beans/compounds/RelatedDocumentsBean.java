package com.tdclighthouse.prototype.beans.compounds;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType = RelatedDocumentsBean.JCR_TYPE)
public class RelatedDocumentsBean extends HippoDocument {

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
