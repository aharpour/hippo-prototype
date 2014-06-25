package com.tdclighthouse.prototype.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.junit.Assert;
import org.junit.Test;

import com.tdclighthouse.prototype.utils.FacetDeepLink.FacetDeepLinkExceptoin;
import com.tdclighthouse.prototype.utils.FacetDeepLink.NotFacetedPropertyExceptoin;
import com.tdclighthouse.prototype.utils.FacetMockUtils.FacetChild;

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
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(new FacetChild[] {}, FACET_NAMES, FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        Assert.assertEquals(facet, bean);
    }

    @Test
    public void invalidFilterTest() {
        Map<String, String[]> filter = new HashMap<String, String[]>();
        filter.put("hsl:subjecttags", new String[] { "over-hl" });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(new FacetChild[] {}, FACET_NAMES, FACETS);
        boolean exception = false;
        try {
            FacetDeepLink.getDeepLinkBean(facet, filter);
        } catch (FacetDeepLinkExceptoin e) {
            exception = true;
        }
        Assert.assertEquals(exception, true);
    }

    @Test
    public void basicTest() throws FacetDeepLinkExceptoin {
        Map<String, String[]> filter = new HashMap<String, String[]>();
        filter.put("hsl:subjecttags", new String[] { "over-hl" });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(new FacetChild[] { new FacetChild("Over",
                new FacetChild[] { new FacetChild("over-hl", new FacetChild[] {}) }) }, FACET_NAMES, FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Over/over-hl");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void inconsistencyTest() throws FacetDeepLinkExceptoin {
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(new FacetChild[] {}, new String[] { "Over",
                "Thema", "Periode" }, new String[] { "hsl:subjecttags", "hsl:thematags" });
        Map<String, String[]> filter = new HashMap<String, String[]>();
        boolean exception = false;
        try {
            FacetDeepLink.getDeepLinkBean(facet, filter);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        Assert.assertEquals(true, exception);
    }

    @Test
    public void inconsistencyTest2() throws FacetDeepLinkExceptoin {
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(new FacetChild[] {}, new String[] { "Over",
                "Thema" }, new String[] { "hsl:subjecttags", "hsl:thematags" });
        Map<String, String[]> filter = new HashMap<String, String[]>();
        filter.put("hsl:noneExistingProperty", new String[] { "value" });
        boolean exception = false;
        try {
            FacetDeepLink.getDeepLinkBean(facet, filter);
        } catch (NotFacetedPropertyExceptoin e) {
            exception = true;
        }
        Assert.assertEquals(true, exception);
    }

    @Test
    public void commonCaseTest() throws FacetDeepLinkExceptoin, IOException {
        @SuppressWarnings("unchecked")
        Map<String, String[]> filter = new LinkedMap();
        filter.put("hsl:subjecttags", new String[] { "over-hl", "hl-media" });
        filter.put("hsl:thematags", new String[] { "management" });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Over/over-hl/Over/hl-media/Thema/management");
        Assert.assertEquals(target, bean);
    }

    private HippoFacetNavigationBean fetchSubbean(HippoFacetNavigationBean bean, String path) {
        HippoFacetNavigationBean result = bean;
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        String[] segments = normalizedPath.split("/");
        for (String segment : segments) {
            result = result.getBean(segment);
        }
        return result;
    }

}