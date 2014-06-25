package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetNavigation;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetSubNavigation;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetsAvailableNavigation;
import org.junit.Assert;
import org.junit.Test;

import com.tdclighthouse.prototype.utils.Constants.HippoFacetAttributesConstants;
import com.tdclighthouse.prototype.utils.FacetDeepLink.FacetDeepLinkExceptoin;

public class FacetDeepLinkTest {

    private static final String[] FACET_NAMES = new String[] { "Over", "Thema", "Periode", "Year",
            "Month${after:'Year', hide:'Year'}" };
    private static final String[] FACETS = new String[] {
            "hsl:subjecttags",
            "hsl:thematags",
            "hsl:eventDate$[{name:'deze week', resolution:'week', begin:-1, end:0}, {name:'binnen 30 dagen', resolution:'day', begin:0, end:30}, {name:'binnen 6 maanden', resolution:'day', begin:0, end:183}]",
            "hsl:eventDate$year", "hsl:eventDate$month" };

    @Test
    public void emptyFilterTest() throws FacetDeepLinkExceptoin {
        Map<String, String[]> filter = new HashMap<String, String[]>();
        HippoFacetNavigationBean facet = createMockFacet(new FacetChild[] {}, FACET_NAMES, FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        Assert.assertEquals(facet, bean);
    }

    @Test
    public void invalidFilterTest() {
        Map<String, String[]> filter = new HashMap<String, String[]>();
        filter.put("hsl:subjecttags", new String[] { "over-hl" });
        HippoFacetNavigationBean facet = createMockFacet(new FacetChild[] {}, FACET_NAMES, FACETS);
        boolean exception = false;
        try {
            FacetDeepLink.getDeepLinkBean(facet, filter);
        } catch (FacetDeepLinkExceptoin e) {
            exception = true;
        }
        Assert.assertEquals(exception, true);
    }

    @Test
    public void commonCaseTest() throws FacetDeepLinkExceptoin {
        Map<String, String[]> filter = new HashMap<String, String[]>();
        filter.put("hsl:subjecttags", new String[] { "over-hl" });
        HippoFacetNavigationBean facet = createMockFacet(new FacetChild[] { new FacetChild("Over",
                new FacetChild[] { new FacetChild("over-hl", new FacetChild[] {}) }) }, FACET_NAMES, FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = ((HippoFacetNavigationBean) facet.getBean("Over")).getBean("over-hl");
        Assert.assertEquals(target, bean);
    }

    private HippoFacetNavigation createMockFacet(FacetChild[] children, String[] facetNames, String[] facets) {
        HippoFacetNavigation mock = EasyMock.createMock(HippoFacetNavigation.class);
        EasyMock.expect(mock.getProperty(HippoFacetAttributesConstants.HIPPOFACNAV_FACETNODENAMES))
                .andReturn(facetNames).anyTimes();
        EasyMock.expect(mock.getProperty(HippoFacetAttributesConstants.HIPPOFACNAV_FACETS)).andReturn(facets)
                .anyTimes();
        for (FacetChild child : children) {
            EasyMock.expect(mock.getBean(child.name))
                    .andReturn(mockHippoFacetsAvailableNavigation(child.getChildren())).anyTimes();
        }
        EasyMock.expect(mock.getBean(EasyMock.anyString())).andReturn(null).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    private HippoFacetsAvailableNavigation mockHippoFacetsAvailableNavigation(FacetChild[] children) {
        HippoFacetsAvailableNavigation mock = EasyMock.createMock(HippoFacetsAvailableNavigation.class);
        for (FacetChild child : children) {
            EasyMock.expect(mock.getBean(child.name)).andReturn(mockHippoFacetSubNavigation(child.getChildren()))
                    .anyTimes();
        }
        EasyMock.expect(mock.getBean(EasyMock.anyString())).andReturn(null).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    private HippoFacetSubNavigation mockHippoFacetSubNavigation(FacetChild[] children) {
        HippoFacetSubNavigation mock = EasyMock.createMock(HippoFacetSubNavigation.class);
        for (FacetChild child : children) {
            EasyMock.expect(mock.getBean(child.getName()))
                    .andReturn(mockHippoFacetsAvailableNavigation(child.getChildren())).anyTimes();
        }
        EasyMock.expect(mock.getBean(EasyMock.anyString())).andReturn(null).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    private static class FacetChild {

        private final String name;
        private final FacetChild[] children;

        private FacetChild(String name, FacetChild[] children) {
            this.name = name;
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public FacetChild[] getChildren() {
            return children;
        }

    }

}