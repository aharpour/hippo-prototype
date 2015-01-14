package com.tdclighthouse.prototype.utils;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocumentBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.core.component.HstRequest;

public class ComponentUtils {

    private ComponentUtils() {
    }

    public static Locale getLocale(HippoBean hippoBean) {
        Locale result = null;
        if (hippoBean instanceof HippoDocumentBean) {
            result = ((HippoDocumentBean) hippoBean).getLocale();
        } else if (hippoBean instanceof HippoFolderBean) {
            result = ((HippoFolderBean) hippoBean).getLocale();
        }
        return result;
    }

    public static String getLanguage() {
        String locale = RequestContextProvider.get().getResolvedMount().getMount().getLocale();
        return LocaleUtils.toLocale(locale).getLanguage();
    }

    public static Map<String, String[]> getPublicRequestParameterMap(HstRequest request) {
        String contextNamespaceReference = request.getRequestContext().getContextNamespace();
        if (contextNamespaceReference == null) {
            contextNamespaceReference = "";
        }
        return request.getParameterMap(contextNamespaceReference);
    }

    public static String getComponentSpecificParameterName(HstRequest request, String name) {
        return name + "_" + request.getReferenceNamespace();
    }
}
