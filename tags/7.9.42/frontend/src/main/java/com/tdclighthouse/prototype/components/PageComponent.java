package com.tdclighthouse.prototype.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.configuration.channel.ChannelInfo;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;

import com.tdclighthouse.prototype.utils.Constants.AttributesConstants;

public class PageComponent extends BaseHstComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        HstRequestContext context = request.getRequestContext();
        HippoBean contentBean = context.getContentBean();
        context.setAttribute(AttributesConstants.DOCUMENT, contentBean);
        request.setAttribute(AttributesConstants.DOCUMENT, contentBean);
        ChannelInfo channelInfo = context.getResolvedMount().getMount().getChannelInfo();
        context.setAttribute(AttributesConstants.CHANNEL_INFO, channelInfo);
        request.setAttribute(AttributesConstants.CHANNEL_INFO, channelInfo);
    }

}
