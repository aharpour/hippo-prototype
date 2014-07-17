package org.hippoecm.hst.utils;

import org.hippoecm.hst.core.component.HstComponent;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.ComponentConfiguration;

public class ParameterUtils {
    public static final String MY_MOCK_PARAMETER_INFO = "my_mock_parameterinfo";

    @SuppressWarnings("unchecked")
    public static <T> T getParametersInfo(HstComponent component, final ComponentConfiguration componentConfig, final HstRequest request) {
        return (T) request.getAttribute(MY_MOCK_PARAMETER_INFO);
    }
}
