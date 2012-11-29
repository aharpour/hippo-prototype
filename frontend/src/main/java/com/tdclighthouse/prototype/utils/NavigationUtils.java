package com.tdclighthouse.prototype.utils;

import java.util.List;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;

public class NavigationUtils {

	private NavigationUtils() {
	}

	public static HippoBean getIndexBean(HippoFolderBean folder) {
		HippoBean result;
		result = folder.getBean(Constants.NodeName.INDEX);
		// when there is no index document in a folder then the first
		// document is selected instead
		if (result == null) {
			List<HippoDocumentBean> documents = folder.getDocuments();
			result = (documents.size() > 0 ? documents.get(0) : null);
		}
		return result;
	}
}
