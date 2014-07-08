package com.tdclighthouse.prototype.utils;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;

public class PathUtils {

    private PathUtils() {
    }

    public static String getLinkPath(HstRequest request, HippoBean bean) {
        HstLink link = getLink(request, bean);
        return request.getContextPath() + request.getRequestContext().getResolvedMount().getResolvedMountPath() + "/"
                + link.getPath();
    }

    public static HstLink getLink(HstRequest request, HippoBean bean) {
        HstRequestContext requestContext = request.getRequestContext();
        return requestContext.getHstLinkCreator().create(bean, requestContext);
    }

    public static String absolutToRelativePath(String absolutPath, HstRequest request) {
        String result;
        String basePath = request.getRequestContext().getResolvedMount().getMount().getContentPath();
        if (absolutPath.startsWith(basePath)) {
            result = org.hippoecm.hst.util.PathUtils.normalizePath(absolutPath.substring(basePath.length()));
        } else {
            throw new IllegalArgumentException("the given path is not in the current mount");
        }
        return result;
    }

}
