package com.tdclighthouse.prototype.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Weeks;
import org.joda.time.Years;

import com.tdclighthouse.prototype.utils.exceptions.FacetDeepLinkExceptoin;
import com.tdclighthouse.prototype.utils.exceptions.NodeNotFoundExceptoin;
import com.tdclighthouse.prototype.utils.exceptions.NotFacetedPropertyExceptoin;

/**
 * @author Ebrahim Aharpour
 *
 */
public class FacetDeepLink {

    private static final char DOLLAR_SIGN = '$';

    private FacetDeepLink() {
    }

    public static HippoFacetNavigationBean getDeepLinkBean(HippoFacetNavigationBean facet, Map<String, Object[]> filter)
            throws FacetDeepLinkExceptoin {
        HippoFacetNavigationBean targetNode = facet;
        String[] facetedNames = facet.getProperty(Constants.HippoFacetAttributesConstants.HIPPOFACNAV_FACETNODENAMES);
        String[] facets = facet.getProperty(Constants.HippoFacetAttributesConstants.HIPPOFACNAV_FACETS);
        validateInput(facetedNames, facets);
        Map<String, FacetItem> facetMap = getFacetMap(facetedNames, facets);
        for (Iterator<Entry<String, Object[]>> iterator = filter.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, Object[]> entry = iterator.next();
            String propertyName = entry.getKey();
            Object[] propertyValues = entry.getValue();
            FacetItem facetItem = facetMap.get(propertyName);
            if (facetItem != null && StringUtils.isNotBlank(facetItem.getName())) {
                for (Object value : propertyValues) {
                    if (value instanceof Calendar) {
                        targetNode = getFilterNode(targetNode, facetItem.getName(),
                                getStringValue(facetItem, (Calendar) value));
                    } else {
                        targetNode = getFilterNode(targetNode, facetItem.getName(), value.toString());
                    }
                }
            } else {
                throw new NotFacetedPropertyExceptoin("the given facet does not facet on the property: " + propertyName);
            }
        }
        return targetNode;
    }

    private static String getStringValue(FacetItem facetItem, Calendar calendar) {
        String value = null;
        FacetConfig[] config = facetItem.getConfig();
        if (config != null && config.length > 0) {
            for (FacetConfig fg : config) {
                int res = getResolutionAsInteger(fg.getResolution());
                int v = getTimeDiff(calendar, res);
                if ((fg.getBegin() == null || fg.getBegin() <= v) && (fg.getEnd() == null || fg.getEnd() > v)) {
                    value = fg.getName();
                    break;
                }
            }

        } else if (StringUtils.isNotEmpty(facetItem.getResolution())) {
            int res = getResolutionAsInteger(facetItem.getResolution());
            value = Integer.toString(calendar.get(res));
        } else {
            value = calendar.getTime().toString();
        }
        return value;
    }

    private static int getTimeDiff(Calendar calendar, int res) {
        int result = Integer.MIN_VALUE;
        DateTime then = new DateTime(calendar);
        DateTime now = new DateTime();
        if (Calendar.DAY_OF_MONTH == res) {
            result = Days.daysBetween(now, then).getDays();
        } else if (Calendar.WEEK_OF_YEAR == res) {
            result = Weeks.weeksBetween(now, then).getWeeks();
        } else if (Calendar.MONTH == res) {
            result = Months.monthsBetween(now, then).getMonths();
        } else if (Calendar.YEAR == res) {
            result = Years.yearsBetween(now, then).getYears();
        }
        return result;
    }

    private static int getResolutionAsInteger(String resolution) {
        int res;
        if ("year".equals(resolution)) {
            res = Calendar.YEAR;
        } else if ("month".equals(resolution)) {
            res = Calendar.MONTH;
        } else if ("week".equals(resolution)) {
            res = Calendar.WEEK_OF_YEAR;
        } else if ("day".equals(resolution)) {
            res = Calendar.DAY_OF_MONTH;
        } else {
            throw new IllegalArgumentException("unsupported resolution");
        }
        return res;
    }

    private static HippoFacetNavigationBean getFilterNode(HippoFacetNavigationBean targetNode, String facetName,
            String value) throws NodeNotFoundExceptoin {
        HippoFacetNavigationBean result = targetNode.getBean(facetName);
        if (result != null) {
            result = result.getBean(value);
        }
        if (result == null) {
            if (value != null) {
                throw new NodeNotFoundExceptoin(value);
            } else {
                result = targetNode;
            }
        }
        return result;
    }

    public static Map<String, FacetItem> getFacetMap(String[] facetedNames, String[] facets) {
        Map<String, FacetItem> facetMap = new HashMap<String, FacetItem>();
        for (int i = 0; i < facets.length; i++) {
            FacetItem facetItem = createFacetItem(facets[i], facetedNames[i]);
            facetMap.put(getBeforeDollarSign(facets[i]), facetItem);
        }
        return facetMap;
    }

    private static FacetItem createFacetItem(String facent, String facetName) {
        FacetItem facetItem;
        String name = getBeforeDollarSign(facetName);
        String property = getBeforeDollarSign(facent);
        int dsi = facent.indexOf(DOLLAR_SIGN);
        if (dsi > 0) {
            String rangeConfig = facent.substring(dsi + 1);
            FacetConfig[] config = getConfig(rangeConfig);
            String resolution = getResolution(rangeConfig);
            if (config == null) {
                facetItem = new FacetItem(name, property, resolution);
            } else {
                facetItem = new FacetItem(name, property, config);
            }
        } else {
            facetItem = new FacetItem(name, property);
        }
        return facetItem;
    }

    public static void main(String[] args) {
        getConfig("ns:test${'name': 'somathing'}");
    }

    private static String getResolution(String value) {
        String result = null;
        if (!value.startsWith("[")) {
            result = value;
        }
        return result;
    }

    private static FacetConfig[] getConfig(String value) {
        FacetConfig[] result = null;
        if (value.startsWith("[")) {
            Object array = JSONArray.toArray(JSONArray.fromObject(value), FacetConfig.class);
            if (array instanceof FacetConfig[]) {
                result = (FacetConfig[]) array;
            }
        }
        return result;
    }

    private static String getBeforeDollarSign(String facetName) {
        String result = facetName;
        int indexOfExpression = facetName.indexOf(DOLLAR_SIGN);
        if (indexOfExpression > 0) {
            result = facetName.substring(0, indexOfExpression);
        }
        return result;
    }

    private static void validateInput(String[] facetedNames, String[] facets) {
        if (facetedNames.length != facets.length) {
            throw new IllegalArgumentException(
                    "\"hippofacnav:facetnodenames\" and \"hippofacnav:facets\" has to have the same lenght.");
        }
    }

    public static class FacetItem {
        private final String name;
        private final String resolution;
        private final FacetConfig[] config;
        private final String property;

        private FacetItem(String name, String property) {
            this.name = name;
            this.property = property;
            this.config = null;
            this.resolution = null;
        }

        private FacetItem(String name, String property, FacetConfig[] config) {
            this.name = name;
            this.config = config;
            this.property = property;
            this.resolution = null;
        }

        private FacetItem(String name, String property, String resolution) {
            this.name = name;
            this.property = property;
            this.resolution = resolution;
            this.config = null;
        }

        public String getName() {
            return name;
        }

        public FacetConfig[] getConfig() {
            return config.clone();
        }

        public String getProperty() {
            return property;
        }

        public String getResolution() {
            return resolution;
        }

    }

    public static class FacetConfig {
        private String name;
        private String resolution;
        private Double begin;
        private Double end;

        public FacetConfig() {
            super();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public Double getBegin() {
            return begin;
        }

        public void setBegin(Double begin) {
            this.begin = begin;
        }

        public Double getEnd() {
            return end;
        }

        public void setEnd(Double end) {
            this.end = end;
        }

    }

}
