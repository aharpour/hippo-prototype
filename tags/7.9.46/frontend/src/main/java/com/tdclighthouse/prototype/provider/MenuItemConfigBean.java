package com.tdclighthouse.prototype.provider;

import org.hippoecm.hst.content.beans.standard.HippoBean;

class MenuItemConfigBean implements MenuItemConfig {
    private HippoBean bean;
    private boolean showFacetNavigations;
    private boolean invisible;
    private boolean disabled;

    public MenuItemConfigBean(HippoBean bean, boolean showFacetNavigations, boolean invisible, boolean disabled) {
        this.bean = bean;
        this.showFacetNavigations = showFacetNavigations;
        this.invisible = invisible;
        this.disabled = disabled;
    }

    public HippoBean getBean() {
        return bean;
    }

    public boolean isShowFacetNavigations() {
        return showFacetNavigations;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean isDisabled() {
        return disabled;
    }

}