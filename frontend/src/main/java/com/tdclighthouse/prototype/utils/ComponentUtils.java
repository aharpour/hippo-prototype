package com.tdclighthouse.prototype.utils;

import org.hippoecm.hst.core.component.HstRequest;

public class ComponentUtils {

    private ComponentUtils() {
    }

    public static String getComponentSpecificParameterName(HstRequest request, String name) {
        return name + "_" + request.getReferenceNamespace();
    }
}
