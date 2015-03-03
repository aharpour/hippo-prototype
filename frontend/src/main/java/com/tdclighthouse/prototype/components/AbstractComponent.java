package com.tdclighthouse.prototype.components;

import java.util.HashMap;
import java.util.Map;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import com.tdclighthouse.prototype.utils.Constants.AttributesConstants;

/**
 * @author Ebrahim
 *
 */
public abstract class AbstractComponent extends BaseHstComponent {

    private static final String NOT_MAP_ERROR_MESSAGE = "an object which is not of the type Map has already been added to request.";

    public abstract Map<String, Object> getModel(HstRequest request, HstResponse response);

    @Override
    public final void doBeforeRender(HstRequest request, HstResponse response) {
        addToModel(getModel(request, response), request);
    }

    protected Object addToModel(String key, Object value, HstRequest request) {
        Map<String, Object> model = getModelObject(request);
        return model.put(key, value);
    }

    protected void addToModel(Map<String, Object> map, HstRequest request) {
        Map<String, Object> model = getModelObject(request);
        if (map != null) {
            model.putAll(map);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getModelObject(HstRequest request) {
        Map<String, Object> model;
        Object attribute = request.getAttribute(AttributesConstants.MODEL);
        if (attribute == null) {
            model = new HashMap<String, Object>();
            request.setAttribute(AttributesConstants.MODEL, model);
        } else if (attribute instanceof Map) {
            model = (HashMap<String, Object>) attribute;
        } else {
            throw new HstComponentException(NOT_MAP_ERROR_MESSAGE);
        }
        return model;
    }

}
