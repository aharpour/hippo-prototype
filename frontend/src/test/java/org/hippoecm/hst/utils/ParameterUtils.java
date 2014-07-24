package org.hippoecm.hst.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.hippoecm.hst.core.component.HstComponent;
import org.hippoecm.hst.core.component.HstParameterValueConversionException;
import org.hippoecm.hst.core.component.HstParameterValueConverter;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.ComponentConfiguration;

public class ParameterUtils {
    public static final String MY_MOCK_PARAMETER_INFO = "my_mock_parameterinfo";
    
    public static final HstParameterValueConverter DEFAULT_HST_PARAMETER_VALUE_CONVERTER = new HstParameterValueConverter(){
        @Override
        public Object convert(String parameterValue, Class<?> returnType) {
            // ConvertUtils.convert cannot handle Calendar as returnType, however, we support it. 
            // that's why we first convert to Date
            try {
                if (returnType.equals(Calendar.class)) {
                    Date date = (Date) ConvertUtils.convert(parameterValue, Date.class);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    return cal;
                }
                return ConvertUtils.convert(parameterValue, returnType);
            } catch (ConversionException e) {
                throw new HstParameterValueConversionException(e);
            }
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> T getParametersInfo(HstComponent component, final ComponentConfiguration componentConfig, final HstRequest request) {
        return (T) request.getAttribute(MY_MOCK_PARAMETER_INFO);
    }
}
