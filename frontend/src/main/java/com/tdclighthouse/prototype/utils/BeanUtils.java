package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import net.sourceforge.hstmixinsupport.DynamicProxyFactory;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectBeanManager;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.PathUtils;

import com.tdclighthouse.prototype.beans.Page;
import com.tdclighthouse.prototype.beans.compounds.ValueListBean;
import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.LabelsInfo;

public class BeanUtils {
    private BeanUtils() {
    }

    private static final DynamicProxyFactory DYNAMIC_PROXY_FACTORY = HstServices.getComponentManager().getComponent(
            DynamicProxyFactory.class);

    public static HippoBean getMixinProxy(HippoBean bean) throws RepositoryException {
        return DYNAMIC_PROXY_FACTORY.getProxy(bean);
    }

    @SuppressWarnings("unchecked")
    public static <T extends HippoBean> T getContentBeanViaParameters(HstRequest request,
            ContentBeanPathInfo parametersInfo) {
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

    public static HippoBean getWebDocumetBean(final HstRequest request, Object parametersInfo) {
        Page result = null;
        HippoBean doc = request.getRequestContext().getContentBean();
        if (!(doc instanceof Page)) {
            if (parametersInfo instanceof ContentBeanPathInfo) {
                doc = getContentBeanViaParameters(request, (ContentBeanPathInfo) parametersInfo);
            }
        }
        if (doc instanceof Page) {
            result = (Page) doc;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HippoBean> T getBean(String relativePath, HstRequest request) {
        T result = null;
        String path = PathUtils.normalizePath(relativePath);
        result = (T) request.getRequestContext().getSiteContentBaseBean().getBean(path);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HippoBean> T getBeanViaAbsolutePath(String absolutePath, HstRequest request) {
        try {
            return (T) request.getRequestContext().getObjectBeanManager().getObject(absolutePath);
        } catch (ObjectBeanManagerException e) {
            throw new HstComponentException(e.getMessage(), e);
        }
    }

    public static Map<String, String> getLabels(HstRequest request, Object parametersInfo) {
        Map<String, String> labels = new HashMap<String, String>();
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

    public static Map<String, String> getSelectionOptionsMap(HstRequest request, String path, String langauge) {
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
        return TdcUtils.valueListBeanToLabelMap(valueList);
    }
}
