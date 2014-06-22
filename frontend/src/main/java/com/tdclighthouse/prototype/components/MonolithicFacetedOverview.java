package com.tdclighthouse.prototype.components;

import java.util.Map;

import org.hippoecm.hst.content.beans.standard.HippoFacetChildNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;

import com.tdclighthouse.prototype.componentsinfo.FacetedOverviewPageInfo;
import com.tdclighthouse.prototype.utils.Constants;

@ParametersInfo(type = FacetedOverviewPageInfo.class)
public class MonolithicFacetedOverview extends FacetedOverview {

    @Override
    public Map<String, Object> getModel(HstRequest request, HstResponse response) {
        Map<String, Object> model = super.getModel(request, response);
        setLabels(request, model);
        setChildNavAttribute(model);
        return model;
    }

    private void setChildNavAttribute(Map<String, Object> model) {
        if (model.get(Constants.AttributesConstants.FACET_BEAN) instanceof HippoFacetChildNavigationBean) {
            model.put(Constants.AttributesConstants.CHILDNAV, "true");
        }
    }

    private void setLabels(HstRequest request, Map<String, Object> model) {
        Map<String, String> labels = getLabels(request);
        model.put(Constants.AttributesConstants.LABELS, labels);
    }
}
