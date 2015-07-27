package com.tdclighthouse.prototype.beans;

import java.util.List;

public class FacetLinkConfigBean {
    private List<String> propertyNames;

    public FacetLinkConfigBean() {
    }

    public FacetLinkConfigBean(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    public List<String> getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

}