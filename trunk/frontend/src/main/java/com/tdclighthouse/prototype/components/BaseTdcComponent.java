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
package com.tdclighthouse.prototype.components;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectBeanManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentIterator;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.PathUtils;

import com.tdclighthouse.prototype.beans.WebDocumentBean;
import com.tdclighthouse.prototype.beans.compounds.ValueListBean;
import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.PaginatedInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.PaginatorWidget;
import com.tdclighthouse.prototype.utils.TdcUtils;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class BaseTdcComponent extends BaseHstComponent {

	@SuppressWarnings("unchecked")
	public <T extends HippoBean> T getBean(String relativePath, HstRequest request) {
		T result = null;
		String path = PathUtils.normalizePath(relativePath);
		result = (T) getSiteContentBaseBean(request).getBean(path);
		return result;
	}

	@Override
	public HippoBean getContentBean(HstRequest request) {
		HippoBean contentBean = super.getContentBean(request);
		processAnnotations(contentBean, request);
		return contentBean;
	}

	@Override
	public <T extends HippoBean> T getContentBean(HstRequest request, Class<T> beanMappingClass) {
		T contentBean = super.getContentBean(request, beanMappingClass);
		processAnnotations(contentBean, request);
		return contentBean;
	}

	public Map<String, String[]> getPublicRequestParameterMap(HstRequest request) {
		String contextNamespaceReference = request.getRequestContext().getContextNamespace();
		if (contextNamespaceReference == null) {
			contextNamespaceReference = "";
		}
		return request.getParameterMap(contextNamespaceReference);
	}

	protected String absolutToRelativePath(String absolutPath, HstRequest request) {
		String result;
		String basePath = request.getRequestContext().getResolvedMount().getMount().getContentPath();
		if (absolutPath.startsWith(basePath)) {
			result = PathUtils.normalizePath(absolutPath.substring(basePath.length()));
		} else {
			throw new IllegalArgumentException("the given path is not in the current mount");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <T extends HippoBean> T getContentBeanViaParameters(HstRequest request, ContentBeanPathInfo parametersInfo) {
		T result = null;
		String indexFilePath = request.getRequestContext().getResolvedSiteMapItem()
				.getParameter(Constants.HstParameters.CONTENT_BEAN_PATH);
		if (StringUtils.isBlank(indexFilePath)) {
			indexFilePath = parametersInfo.getContentBeanPath();
		}
		if (StringUtils.isNotBlank(indexFilePath)) {
			if (indexFilePath.startsWith("/")) {
				try {
					result = (T) getObjectBeanManager(request).getObject(indexFilePath);
				} catch (ObjectBeanManagerException e) {
					// ignore
				}
			} else {
				result = (T) getBean(indexFilePath, request);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> getItemsFromHippoBeanIterator(HippoBeanIterator documentIterator,
			PaginatorWidget paginatorWidget) {
		List<T> items = new ArrayList<T>(paginatorWidget.getRowsPerPage());
		int start = (paginatorWidget.getPage() - 1) * paginatorWidget.getRowsPerPage();
		documentIterator.skip(start);
		for (int i = 0; (i < paginatorWidget.getRowsPerPage()) && documentIterator.hasNext(); i++) {
			items.add((T) documentIterator.next());
		}
		return items;
	}

	protected <T> List<T> getItemsFromHippoDocumentIterator(HippoDocumentIterator<T> documentIterator,
			PaginatorWidget paginatorWidget) {
		List<T> items = new ArrayList<T>(paginatorWidget.getRowsPerPage());
		int start = (paginatorWidget.getPage() - 1) * paginatorWidget.getRowsPerPage();
		documentIterator.skip(start);
		for (int i = 0; (i < paginatorWidget.getRowsPerPage()) && documentIterator.hasNext(); i++) {
			items.add(documentIterator.next());
		}
		return items;
	}

	protected List<HippoDocumentBean> getItemsFromResultSet(HippoFolderBean resultSet, PaginatorWidget paginatorWidget) {
		HippoDocumentIterator<HippoDocumentBean> documentIterator = resultSet
				.getDocumentIterator(HippoDocumentBean.class);
		return getItemsFromHippoDocumentIterator(documentIterator, paginatorWidget);
	}

	protected <T> List<T> getItemsFromResultSet(HstQueryResult resultSet, PaginatorWidget paginatorWidget) {
		HippoBeanIterator hippoBeans = resultSet.getHippoBeans();
		return this.<T> getItemsFromHippoBeanIterator(hippoBeans, paginatorWidget);
	}

	protected Locale getLocale(HippoBean hippoBean) {
		Locale result = null;
		if (hippoBean instanceof HippoDocumentBean) {
			result = ((HippoDocumentBean) hippoBean).getLocale();
		} else if (hippoBean instanceof HippoFolderBean) {
			result = ((HippoFolderBean) hippoBean).getLocale();
		}
		return result;
	}

	protected int getPageNumber(HstRequest request) {
		int result = 1;
		String pageString = getPublicRequestParameter(request, Constants.Parameters.PAGE);
		if (StringUtils.isNotBlank(pageString) && StringUtils.isNumeric(pageString)) {
			result = Integer.parseInt(pageString);
		}
		return result;
	}

	protected int getPageSize(HstRequest request) {
		int result = 25;
		String pageSzieString = getPublicRequestParameter(request, Constants.Parameters.PAGE_SIZE);
		if (StringUtils.isNotBlank(pageSzieString) && StringUtils.isNumeric(pageSzieString)) {
			result = Integer.parseInt(pageSzieString);
		} else {
			Object parametersInfo = getParametersInfo(request);
			if (parametersInfo instanceof PaginatedInfo) {
				result = ((PaginatedInfo) parametersInfo).getDefaultPageSzie();
			}
		}
		return result;
	}

	protected Map<String, String> getSelectionOptionsMap(HstRequest request, String path, String langauge) {
		ValueListBean valueList;

		try {
			ObjectBeanManager objectBeanManager = getObjectBeanManager(request);
			Object object = objectBeanManager.getObject(path);
			if (object instanceof ValueListBean) {
				valueList = (ValueListBean) object;
				if (StringUtils.isNotBlank(langauge)) {
					HippoBean translation = valueList.getAvailableTranslationsBean().getTranslation(langauge);
					if (translation instanceof ValueListBean) {
						valueList = (ValueListBean) translation;
					}
				}
			} else {
				throw new HstComponentException("the path: \"" + path + "\" is not valid path to a key value list");
			}
		} catch (ObjectBeanManagerException e) {
			throw new RuntimeException(e);
		}
		return TdcUtils.valueListBeanToMap(valueList);
	}

	protected HippoBean getWebDocumetBean(final HstRequest request) {
		WebDocumentBean result = null;
		HippoBean doc = getContentBean(request);
		if (!(doc instanceof WebDocumentBean)) {
			Object parameters = getParametersInfo(request);
			if (parameters instanceof ContentBeanPathInfo) {
				doc = getContentBeanViaParameters(request, (ContentBeanPathInfo) parameters);
			}
		}
		if (doc instanceof WebDocumentBean) {
			result = (WebDocumentBean) doc;
		}
		return result;
	}

	protected void processAnnotations(HippoBean hippoBean, HstRequest request) {

	}

	protected PaginatorWidget setPaginator(HstRequest request, int defaultPageSzie, int totalRows) {
		PaginatorWidget paginator = new PaginatorWidget(totalRows, getPageNumber(request), defaultPageSzie);
		request.setAttribute(Constants.Attributes.PAGINATOR, paginator);
		return paginator;
	}

	protected String getLinkPath(HstRequest request, HippoBean bean) {
		HstLink link = getLink(request, bean);
		String path = request.getContextPath() + request.getRequestContext().getResolvedMount().getResolvedMountPath()
				+ "/" + link.getPath();
		return path;
	}

	protected HstLink getLink(HstRequest request, HippoBean bean) {
		HstRequestContext requestContext = request.getRequestContext();
		return requestContext.getHstLinkCreator().create(bean, requestContext);
	}

	protected static String getLanguage(HstRequest request) {
		String locale = request.getRequestContext().getResolvedMount().getMount().getLocale();
		return LocaleUtils.toLocale(locale).getLanguage();
	}

	protected abstract class ProcessFieldsAnnotations<FieldType, AnnotationType extends Annotation> {
		private final Class<FieldType> fieldClass;
		private final Class<AnnotationType> annotationClass;

		public ProcessFieldsAnnotations(Class<FieldType> fieldClass, Class<AnnotationType> annotationClass) {
			this.fieldClass = fieldClass;
			this.annotationClass = annotationClass;
		}

		public abstract FieldType getValue(HstRequest request, AnnotationType annotation, HippoBean hippoBean);

		public void process(HippoBean hippoBean, HstRequest request) {
			try {
				Field[] declaredFields = hippoBean.getClass().getDeclaredFields();
				for (Field field : declaredFields) {
					Class<?> type = field.getType();
					if (fieldClass.isAssignableFrom(type)) {
						AnnotationType annotation = field.getAnnotation(annotationClass);
						field.setAccessible(true);

						field.set(hippoBean, getValue(request, annotation, hippoBean));
					}
				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
