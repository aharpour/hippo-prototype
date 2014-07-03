/*
 *  Copyright 2014 Openweb IT Solutions.
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
package com.tdclighthouse.prototype.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jcr.RepositoryException;

import net.sourceforge.hstmixinsupport.DynamicProxyFactory;

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
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.PathUtils;

import com.tdclighthouse.prototype.beans.Page;
import com.tdclighthouse.prototype.beans.compounds.ValueListBean;
import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.LabelsInfo;
import com.tdclighthouse.prototype.componentsinfo.PaginatedInfo;
import com.tdclighthouse.prototype.utils.Constants;
import com.tdclighthouse.prototype.utils.PaginatorWidget;
import com.tdclighthouse.prototype.utils.TdcUtils;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class BaseComponent extends BaseHstComponent {

    private final DynamicProxyFactory dynamicProxyFactory = HstServices.getComponentManager().getComponent(
            DynamicProxyFactory.class);

    @SuppressWarnings("unchecked")
    public <T extends HippoBean> T getBean(String relativePath, HstRequest request) {
        T result = null;
        String path = PathUtils.normalizePath(relativePath);
        result = (T) request.getRequestContext().getSiteContentBaseBean().getBean(path);
        return result;
    }

    public HippoBean getMixinProxy(HippoBean bean) throws RepositoryException {
        return dynamicProxyFactory.getProxy(bean);
    }

    public Map<String, String[]> getPublicRequestParameterMap(HstRequest request) {
        String contextNamespaceReference = request.getRequestContext().getContextNamespace();
        if (contextNamespaceReference == null) {
            contextNamespaceReference = "";
        }
        return request.getParameterMap(contextNamespaceReference);
    }

    protected Map<String, String> getLabels(HstRequest request) {
        Map<String, String> labels = new HashMap<String, String>();
        Object parametersInfo = getComponentParametersInfo(request);
        if (parametersInfo instanceof LabelsInfo) {
            LabelsInfo parameters = (LabelsInfo) parametersInfo;
            String labelPaths = parameters.getLabelPaths();
            if (StringUtils.isNotBlank(labelPaths)) {
                String[] paths = labelPaths.split(",");
                for (String path : paths) {
                    labels.putAll(getSelectionOptionsMap(request, path, request.getLocale().getLanguage()));
                }
            }
        }
        return labels;
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
                .getParameter(Constants.HstParametersConstants.CONTENT_BEAN_PATH);
        if (StringUtils.isBlank(indexFilePath)) {
            indexFilePath = parametersInfo.getContentBeanPath();
        }
        if (StringUtils.isNotBlank(indexFilePath)) {
            if (indexFilePath.startsWith("/")) {
                try {
                    result = (T) request.getRequestContext().getObjectBeanManager().getObject(indexFilePath);
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
        String pageString = getPublicRequestParameter(request, Constants.ParametersConstants.PAGE);
        if (StringUtils.isNotBlank(pageString) && StringUtils.isNumeric(pageString)) {
            result = Integer.parseInt(pageString);
        }
        return result;
    }

    protected int getPageSize(HstRequest request) {
        int result = 25;
        String pageSzieString = getPublicRequestParameter(request, Constants.ParametersConstants.PAGE_SIZE);
        if (StringUtils.isNotBlank(pageSzieString) && StringUtils.isNumeric(pageSzieString)) {
            result = Integer.parseInt(pageSzieString);
        } else {
            Object parametersInfo = getComponentParametersInfo(request);
            if (parametersInfo instanceof PaginatedInfo) {
                result = ((PaginatedInfo) parametersInfo).getDefaultPageSzie();
            }
        }
        return result;
    }

    protected Map<String, String> getSelectionOptionsMap(HstRequest request, String path, String langauge) {
        ValueListBean valueList;

        try {
            ObjectBeanManager objectBeanManager = request.getRequestContext().getObjectBeanManager();
            Object object = objectBeanManager.getObject(path);
            if (object instanceof ValueListBean) {
                valueList = (ValueListBean) object;
                if (StringUtils.isNotBlank(langauge)) {
                    HippoBean translation = valueList.getAvailableTranslations().getTranslation(langauge);
                    if (translation instanceof ValueListBean) {
                        valueList = (ValueListBean) translation;
                    }
                }
            } else {
                throw new HstComponentException("the path: \"" + path + "\" is not valid path to a key value list");
            }
        } catch (ObjectBeanManagerException e) {
            throw new HstComponentException(e.getMessage(), e);
        }
        return TdcUtils.valueListBeanToMap(valueList);
    }

    protected HippoBean getWebDocumetBean(final HstRequest request) {
        Page result = null;
        HippoBean doc = request.getRequestContext().getContentBean();
        if (!(doc instanceof Page)) {
            Object parameters = getComponentParametersInfo(request);
            if (parameters instanceof ContentBeanPathInfo) {
                doc = getContentBeanViaParameters(request, (ContentBeanPathInfo) parameters);
            }
        }
        if (doc instanceof Page) {
            result = (Page) doc;
        }
        return result;
    }

    protected PaginatorWidget getPaginator(HstRequest request, int defaultPageSzie, int totalRows) {
        return new PaginatorWidget(totalRows, getPageNumber(request), defaultPageSzie);
    }

    protected String getLinkPath(HstRequest request, HippoBean bean) {
        HstLink link = getLink(request, bean);
        return request.getContextPath() + request.getRequestContext().getResolvedMount().getResolvedMountPath() + "/"
                + link.getPath();
    }

    protected HstLink getLink(HstRequest request, HippoBean bean) {
        HstRequestContext requestContext = request.getRequestContext();
        return requestContext.getHstLinkCreator().create(bean, requestContext);
    }

    protected static String getLanguage(HstRequest request) {
        String locale = request.getRequestContext().getResolvedMount().getMount().getLocale();
        return LocaleUtils.toLocale(locale).getLanguage();
    }

}
