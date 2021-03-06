package com.tdclighthouse.prototype.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;

public class PathUtils {

    private static final Pattern INITIAL_OR_TRAILING_SLASHES = Pattern.compile("\\A/*|/*\\z");

    private PathUtils() {
    }

    public static String getLinkPath(HstRequest request, HippoBean bean) {
        HstLink link = getLink(bean);
        return request.getContextPath() + request.getRequestContext().getResolvedMount().getResolvedMountPath() + "/"
                + link.getPath();
    }

    public static HstLink getLink(HippoBean bean) {
        HstRequestContext requestContext = RequestContextProvider.get();
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
    
    public static String normalize(String facetsParameter) {
        Matcher matcher = INITIAL_OR_TRAILING_SLASHES.matcher(facetsParameter);
        return matcher.replaceAll("");
    }

}
