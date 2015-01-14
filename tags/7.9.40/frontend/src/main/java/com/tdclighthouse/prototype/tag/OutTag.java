package com.tdclighthouse.prototype.tag;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutTag extends OutSupport {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(OutTag.class);

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
            LOG.debug(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            // ignore
            LOG.debug(e.getMessage(), e);
        } catch (NestedNullException e) {
            // ignore
            LOG.debug(e.getMessage(), e);
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