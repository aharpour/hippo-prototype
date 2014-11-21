package com.tdclighthouse.prototype.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.junit.Assert;
import org.junit.Test;

import com.tdclighthouse.prototype.utils.FacetMockUtils.FacetChild;
import com.tdclighthouse.prototype.utils.exceptions.FacetDeepLinkExceptoin;
import com.tdclighthouse.prototype.utils.exceptions.NotFacetedPropertyExceptoin;

public class FacetDeepLinkTest {

    private static final String[] FACET_NAMES = new String[] { "Over", "Thema", "Periode" };
    private static final String[] FACETS = new String[] {
            "hsl:subjecttags",
            "hsl:thematags",
            "hsl:eventDate$[{name:'this week', resolution:'week', begin:0, end:1}, {name:'next 30 days', resolution:'day', begin:0, end:30}, {name:'next 6 months', resolution:'month', begin:0, end:6}, {name:'next year', resolution:'year', begin:0, end:2}]", };

    @Test
    public void emptyFilterTest() throws FacetDeepLinkExceptoin {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(new FacetChild[] {}, FACET_NAMES, FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        Assert.assertEquals(facet, bean);
    }

    @Test
    public void invalidFilterTest() {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
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
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
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
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
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
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
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
        Map<String, Object[]> filter = new LinkedMap();
        filter.put("hsl:subjecttags", new String[] { "over-hl", "hl-media" });
        filter.put("hsl:thematags", new String[] { "management" });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Over/over-hl/Over/hl-media/Thema/management");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void dateFieldCaseTest() throws FacetDeepLinkExceptoin, IOException {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
        filter.put("hsl:eventDate", new Object[] { Calendar.getInstance() });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Periode/this week");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void dateFieldCase2Test() throws FacetDeepLinkExceptoin, IOException {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 8);
        filter.put("hsl:eventDate", new Object[] { calendar });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Periode/next 30 days");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void dateFieldCase3Test() throws FacetDeepLinkExceptoin, IOException {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 31);
        filter.put("hsl:eventDate", new Object[] { calendar });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Periode/next 6 months");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void dateFieldCase4Test() throws FacetDeepLinkExceptoin, IOException {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        filter.put("hsl:eventDate", new Object[] { calendar });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Periode/next year");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void dateFieldCase5Test() throws FacetDeepLinkExceptoin, IOException {
        Map<String, Object[]> filter = new HashMap<String, Object[]>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        filter.put("hsl:eventDate", new Object[] { calendar });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        Assert.assertEquals(facet, bean);
    }

    @Test
    public void dateFieldCase6Test() throws FacetDeepLinkExceptoin, IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> filter = new LinkedMap();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        filter.put("hsl:subjecttags", new String[] { "over-hl", "hl-media" });
        filter.put("hsl:thematags", new String[] { "management" });
        filter.put("hsl:eventDate", new Object[] { calendar });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet, "Over/over-hl/Over/hl-media/Thema/management");
        Assert.assertEquals(target, bean);
    }

    @Test
    public void dateFieldCase7Test() throws FacetDeepLinkExceptoin, IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object[]> filter = new LinkedMap();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 12);
        filter.put("hsl:subjecttags", new String[] { "over-hl", "hl-media" });
        filter.put("hsl:thematags", new String[] { "management" });
        filter.put("hsl:eventDate", new Object[] { calendar });
        HippoFacetNavigationBean facet = FacetMockUtils.createMockFacet(
                ClassLoader.getSystemResourceAsStream("com/tdclighthouse/prototype/utils/TestFacet.json"), FACET_NAMES,
                FACETS);
        HippoFacetNavigationBean bean = FacetDeepLink.getDeepLinkBean(facet, filter);
        HippoFacetNavigationBean target = fetchSubbean(facet,
                "Over/over-hl/Over/hl-media/Thema/management/Periode/next 30 days");
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