package com.tdclighthouse.prototype.components;

import javax.servlet.http.HttpServletResponse;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

public class NotFound extends BaseHstComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
