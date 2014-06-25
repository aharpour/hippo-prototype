package com.tdclighthouse.prototype.utils;

import java.io.IOException;
import java.io.InputStream;

import org.easymock.EasyMock;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetNavigation;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetSubNavigation;
import org.hippoecm.hst.content.beans.standard.facetnavigation.HippoFacetsAvailableNavigation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdclighthouse.prototype.utils.Constants.HippoFacetAttributesConstants;

public class FacetMockUtils {

    private FacetMockUtils() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static HippoFacetNavigation createMockFacet(InputStream json, String[] facetNames, String[] facets)
            throws IOException {
        FacetChild[] children = OBJECT_MAPPER.readValue(json, FacetChild[].class);
        return createMockFacet(children, facetNames, facets);
    }

    public static HippoFacetNavigation createMockFacet(FacetChild[] children, String[] facetNames, String[] facets) {
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

    private static HippoFacetsAvailableNavigation mockHippoFacetsAvailableNavigation(FacetChild[] children) {
        HippoFacetsAvailableNavigation mock = EasyMock.createMock(HippoFacetsAvailableNavigation.class);
        for (FacetChild child : children) {
            EasyMock.expect(mock.getBean(child.name)).andReturn(mockHippoFacetSubNavigation(child.getChildren()))
                    .anyTimes();
        }
        EasyMock.expect(mock.getBean(EasyMock.anyString())).andReturn(null).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    private static HippoFacetSubNavigation mockHippoFacetSubNavigation(FacetChild[] children) {
        HippoFacetSubNavigation mock = EasyMock.createMock(HippoFacetSubNavigation.class);
        for (FacetChild child : children) {
            EasyMock.expect(mock.getBean(child.getName()))
                    .andReturn(mockHippoFacetsAvailableNavigation(child.getChildren())).anyTimes();
        }
        EasyMock.expect(mock.getBean(EasyMock.anyString())).andReturn(null).anyTimes();
        EasyMock.replay(mock);
        return mock;
    }

    public static class FacetChild {

        private String name;
        private FacetChild[] children;

        public FacetChild() {
        }

        public FacetChild(String name, FacetChild[] children) {
            this.name = name;
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public FacetChild[] getChildren() {
            return children;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChildren(FacetChild[] children) {
            this.children = children;
        }

    }

}
