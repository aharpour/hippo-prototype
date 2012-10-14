package com.tdclighthouse.prototype.gallery;

import org.hippoecm.hst.core.linking.AbstractResourceContainer;

import com.tdclighthouse.prototype.beans.TdcImageSetBean;

public class TdcGalleryResourceContainer extends AbstractResourceContainer {

	@Override
	public String getNodeType() {
		return TdcImageSetBean.JCR_TYPE;
	}

}
