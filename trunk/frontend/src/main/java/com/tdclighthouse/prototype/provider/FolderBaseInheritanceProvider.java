package com.tdclighthouse.prototype.provider;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;

import com.tdclighthouse.prototype.utils.Extractor;
import com.tdclighthouse.prototype.utils.Filter;
import com.tdclighthouse.prototype.utils.NavigationUtils;

public class FolderBaseInheritanceProvider<T> {
	private final Extractor<HippoBean, T> extractor;

	public FolderBaseInheritanceProvider(Extractor<HippoBean, T> extractor) {
		this.extractor = extractor;
	}

	public T getInheritedItem(HippoBean bean, Filter<T> filter) {
		if (bean == null) {
			throw new IllegalArgumentException("the given bean should not be null");
		}
		T result = null;
		T extracted = extractor.extract(bean);
		while (bean != null && !filter.accept(extracted)) {
			if (bean.isHippoFolderBean()) {
				bean = handelHippoFolderCase(bean);
			} else {
				bean = handelHippoDocumentCase(bean);
			}
			extracted = extractor.extract(bean);
		}
		if (filter.accept(extracted)) {
			result = extracted;
		}
		return result;
	}

	private HippoBean handelHippoDocumentCase(HippoBean bean) {
		HippoBean parentBean = bean.getParentBean();
		if (parentBean.isHippoFolderBean()) {
			HippoBean indexBean = NavigationUtils.getIndexBean((HippoFolderBean) parentBean);
			if (indexBean == null || indexBean.getCanonicalPath().equals(bean.getCanonicalPath())) {
				bean = parentBean.getParentBean();
			} else {
				bean = indexBean;
			}
		}
		return bean;
	}

	private HippoBean handelHippoFolderCase(HippoBean bean) {
		HippoBean indexBean = NavigationUtils.getIndexBean((HippoFolderBean) bean);
		if (indexBean != null) {
			bean = indexBean;
		} else {
			bean = bean.getParentBean();
		}
		return bean;
	}

}
