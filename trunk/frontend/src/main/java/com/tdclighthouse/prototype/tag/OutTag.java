package com.tdclighthouse.prototype.tag;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;

public class OutTag extends OutSupport {

    private static final long serialVersionUID = 1L;

    public void setValue(Object value) {
        this.value = value;
    }
    
    public void setExpression(String expression) {
        try {
            HstRequest request = (HstRequest) pageContext.getRequest();
            Map<String, Object> attributeMap = request.getAttributeMap();
            String property = getProperty(attributeMap, expression);
            if (property == null) {
                Map<String, Object> attributes = request.getRequestContext().getAttributes();
                property = getProperty(attributes, expression);
            }
            value = property;
        } catch (InvocationTargetException e) {
            throw new HstComponentException(e.getMessage(), e);
        }
    }

    private String getProperty(Map<String, Object> bean, String expression) throws InvocationTargetException {
        String property = null;
        try {
            property = BeanUtils.getProperty(bean, expression);
        } catch (IllegalAccessException e) {
            // igonre
        } catch (NoSuchMethodException e) {
            // ignore
        } catch (NestedNullException e) {
            // ignore
        }
        return property;
    }

    public void setDefault(String def) {
        this.def = def;
    }

    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }
}