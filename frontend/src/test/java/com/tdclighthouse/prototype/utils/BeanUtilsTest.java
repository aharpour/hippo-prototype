package com.tdclighthouse.prototype.utils;

import org.easymock.EasyMock;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.configuration.hosting.VirtualHost;
import org.hippoecm.hst.configuration.hosting.VirtualHosts;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectBeanManager;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.junit.Assert;
import org.junit.Test;

public class BeanUtilsTest {

    private static final String BEAN_PATH = "/bean/path";
    private static final String SITE = "/site";

    @Test
    public void getBeanViaAbsolutionPathTest() throws IllegalStateException, ObjectBeanManagerException {

        MockHstRequest requestMock = new MockHstRequest();
        requestMock.setRequestContext(createMockHstRequestContext(BEAN_PATH, false, null, true));
        HippoBean selectedBean = BeanUtils.getBeanViaAbsolutionPath(BEAN_PATH, requestMock);
        Assert.assertEquals(BEAN_PATH, selectedBean.getPath());
    }

    @Test
    public void getBeanViaAbsolutionPathExceptionTest() throws IllegalStateException, ObjectBeanManagerException {
        HippoBean selectedBean = null;
        try {
            MockHstRequest requestMock = new MockHstRequest();
            requestMock.setRequestContext(createMockHstRequestContext(BEAN_PATH, true, null, false));
            selectedBean = BeanUtils.getBeanViaAbsolutionPath(BEAN_PATH, requestMock);
        } catch (HstComponentException e) {
            Assert.assertEquals(selectedBean, null);
        }
    }

    private ObjectBeanManager createObjectBeanManagerMock(String contentBeanPath, boolean throwException)
            throws ObjectBeanManagerException {
        ObjectBeanManager mock = EasyMock.createMock(ObjectBeanManager.class);

        if (!throwException) {
            HippoBean scopeBean = getMockContentBean(contentBeanPath);
            EasyMock.expect(mock.getObject(contentBeanPath)).andReturn(scopeBean).anyTimes();
        } else {
            EasyMock.expect(mock.getObject(contentBeanPath)).andThrow(new ObjectBeanManagerException());
        }

        EasyMock.replay(mock);
        return mock;
    }

    private HstRequestContext createMockHstRequestContext(String beanPath, boolean throwException, String urlForm,
            Boolean contextPathInUrl) throws IllegalStateException, ObjectBeanManagerException {

        HstRequestContext mock = EasyMock.createMock(HstRequestContext.class);

        EasyMock.expect(mock.getObjectBeanManager()).andReturn(createObjectBeanManagerMock(beanPath, throwException))
                .anyTimes();

        if (urlForm != null) {
            HstLinkCreator hstLinkCreatorMock = getHstLinkCreatorMock(mock, urlForm);
            EasyMock.expect(mock.getHstLinkCreator()).andReturn(hstLinkCreatorMock).anyTimes();
            EasyMock.expect(mock.getResolvedMount()).andReturn(getResolvedMountMock(contextPathInUrl));

        }

        EasyMock.replay(mock);
        return mock;
    }

    private HstLinkCreator getHstLinkCreatorMock(HstRequestContext reqContext, String urlForm) {
        HstLinkCreator mock = EasyMock.createMock(HstLinkCreator.class);

        HstLink hstLinkMock = createHstLinkMock(reqContext, urlForm);

        EasyMock.expect(mock.create(EasyMock.anyObject(HippoBean.class), EasyMock.anyObject(HstRequestContext.class)))
                .andReturn(hstLinkMock).anyTimes();

        EasyMock.replay(mock);
        return mock;
    }

    private HstLink createHstLinkMock(HstRequestContext reqContext, String urlForm) {
        HstLink mock = EasyMock.createMock(HstLink.class);
        EasyMock.expect(mock.toUrlForm(reqContext, false)).andReturn(urlForm).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    private HippoBean getMockContentBean(String contentBeanPath) {
        HippoBean mock = EasyMock.createMock(HippoBean.class);
        EasyMock.expect(mock.getPath()).andReturn(contentBeanPath).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    private ResolvedMount getResolvedMountMock(Boolean contextPathInUrl) {
        ResolvedMount resolvedMountMock = EasyMock.createMock(ResolvedMount.class);
        Mount mountMock = EasyMock.createMock(Mount.class);

        EasyMock.expect(mountMock.isContextPathInUrl()).andReturn(contextPathInUrl);
        EasyMock.expect(mountMock.getVirtualHost()).andReturn(getVirtualHostMock(SITE, true));
        EasyMock.expect(resolvedMountMock.getMount()).andReturn(mountMock);

        EasyMock.replay(resolvedMountMock, mountMock);
        return resolvedMountMock;
    }

    private VirtualHost getVirtualHostMock(String defaultCtxPath, boolean ctxPathInUrl) {
        VirtualHost virtualHostMock = EasyMock.createMock(VirtualHost.class);
        VirtualHosts virtualHostsMock = EasyMock.createMock(VirtualHosts.class);

        EasyMock.expect(virtualHostsMock.getDefaultContextPath()).andReturn(defaultCtxPath);
        EasyMock.expect(virtualHostsMock.isContextPathInUrl()).andReturn(ctxPathInUrl);
        EasyMock.expect(virtualHostMock.getVirtualHosts()).andReturn(virtualHostsMock);

        EasyMock.replay(virtualHostMock, virtualHostsMock);
        return virtualHostMock;
    }

}
