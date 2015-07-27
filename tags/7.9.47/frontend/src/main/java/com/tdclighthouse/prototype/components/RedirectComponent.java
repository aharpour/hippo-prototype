package com.tdclighthouse.prototype.components;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;

import com.tdclighthouse.prototype.componentsinfo.RedirectComponentInfo;
import com.tdclighthouse.prototype.utils.Constants.ValuesConstants;
import com.tdclighthouse.prototype.utils.PathUtils;

@ParametersInfo(type = RedirectComponentInfo.class)
public class RedirectComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        try {
            RedirectComponentInfo paramInfo = getComponentParametersInfo(request);
            String redirectTo = paramInfo.getRedirectTo();
            if (StringUtils.isNotBlank(redirectTo)) {
                if (paramInfo.isPreservePath()) {
                    redirectTo = appendWithPath(request, paramInfo, redirectTo);
                }
                setResponseStatusCode(response, paramInfo);
                response.sendRedirect(redirectTo);
            }
        } catch (IOException e) {
            throw new HstComponentException(e.getMessage(), e);
        }
    }

    private void setResponseStatusCode(HstResponse response, RedirectComponentInfo paramInfo) {
        if (paramInfo.getStatusCode() == HttpServletResponse.SC_MOVED_TEMPORARILY
                || paramInfo.getStatusCode() == HttpServletResponse.SC_MOVED_PERMANENTLY) {
            response.setStatus(paramInfo.getStatusCode());
        }
    }

    private String appendWithPath(HstRequest request, RedirectComponentInfo paramInfo, String redirectTo) {
        String result = redirectTo;
        String path = PathUtils.normalize(paramInfo.getPath());
        if (StringUtils.isNotBlank(path) && !getHomePage(request).equals(path)) {
            result = result.endsWith(ValuesConstants.SLASH) ? redirectTo + path : redirectTo + ValuesConstants.SLASH
                    + path;
        }
        return result;
    }

    private String getHomePage(HstRequest request) {
        return request.getRequestContext().getResolvedMount().getMount().getHomePage();
    }

}
