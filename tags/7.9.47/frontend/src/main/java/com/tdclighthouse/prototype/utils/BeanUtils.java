package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;

import net.sourceforge.hstmixinsupport.DynamicProxyFactory;
import net.sourceforge.hstmixinsupport.HstMinxinSupportInfo;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectBeanManager;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.PathUtils;
import org.onehippo.forge.selection.hst.contentbean.ValueList;

import com.tdclighthouse.prototype.beans.Page;
import com.tdclighthouse.prototype.componentsinfo.ContentBeanPathInfo;
import com.tdclighthouse.prototype.componentsinfo.LabelsInfo;

public class BeanUtils {

    private BeanUtils() {
    }

    private static final DynamicProxyFactory DYNAMIC_PROXY_FACTORY = HstServices.getComponentManager().getComponent(
            DynamicProxyFactory.class, HstMinxinSupportInfo.MODULE_NAME);

    public static HippoBean getMixinProxy(HippoBean bean) throws RepositoryException {
        return DYNAMIC_PROXY_FACTORY.getProxy(bean);
    }

    public static <T extends HippoBean> T getContentBeanViaParameters(ContentBeanPathInfo parametersInfo) {
        T result = null;
        String indexFilePath = RequestContextProvider.get().getResolvedSiteMapItem()
                .getParameter(Constants.HstParametersConstants.CONTENT_BEAN_PATH);
        if (StringUtils.isBlank(indexFilePath)) {
            indexFilePath = parametersInfo.getContentBeanPath();
        }
        result = getContentBeanFromParameter(indexFilePath);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HippoBean> T getContentBeanFromParameter(String indexFilePath) {
        T result = null;
        if (StringUtils.isNotBlank(indexFilePath)) {
            if (indexFilePath.startsWith("/")) {
                try {
                    result = (T) RequestContextProvider.get().getObjectBeanManager().getObject(indexFilePath);
                } catch (ObjectBeanManagerException e) {
                    // ignore
                }
            } else {
                result = (T) getBean(indexFilePath);
            }
        }
        return result;
    }

    public static HippoBean getWebDocumetBean(Object parametersInfo) {
        Page result = null;
        HippoBean doc = RequestContextProvider.get().getContentBean();
        if (!(doc instanceof Page)) {
            if (parametersInfo instanceof ContentBeanPathInfo) {
                doc = getContentBeanViaParameters((ContentBeanPathInfo) parametersInfo);
            }
        }
        if (doc instanceof Page) {
            result = (Page) doc;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HippoBean> T getBean(String relativePath) {
        T result = null;
        String path = PathUtils.normalizePath(relativePath);
        result = (T) RequestContextProvider.get().getSiteContentBaseBean().getBean(path);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T extends HippoBean> T getBeanViaAbsolutePath(String absolutePath) {
        try {
            return (T) RequestContextProvider.get().getObjectBeanManager().getObject(absolutePath);
        } catch (ObjectBeanManagerException e) {
            throw new HstComponentException(e.getMessage(), e);
        }
    }

    public static Map<String, String> getLabels(Object parametersInfo) {
        Map<String, String> labels = new HashMap<String, String>();
        if (parametersInfo instanceof LabelsInfo) {
            LabelsInfo parameters = (LabelsInfo) parametersInfo;
            String labelPaths = parameters.getLabelPaths();
            if (StringUtils.isNotBlank(labelPaths)) {
                String[] paths = labelPaths.split(",");
                for (String path : paths) {
                    labels.putAll(getSelectionOptionsMap(path, RequestContextProvider.get().getPreferredLocale().getLanguage()));
                }
            }
        }
        return labels;
    }

    public static Map<String, String> getSelectionOptionsMap(String path, String langauge) {
        ValueList valueList;

        try {
            ObjectBeanManager objectBeanManager = RequestContextProvider.get().getObjectBeanManager();
            Object object = objectBeanManager.getObject(path);
            if (object instanceof ValueList) {
                valueList = (ValueList) object;
                if (StringUtils.isNotBlank(langauge)) {
                    HippoBean translation = valueList.getAvailableTranslations().getTranslation(langauge);
                    if (translation instanceof ValueList) {
                        valueList = (ValueList) translation;
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
