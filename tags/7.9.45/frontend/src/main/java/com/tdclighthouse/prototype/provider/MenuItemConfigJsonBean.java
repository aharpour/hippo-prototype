package com.tdclighthouse.prototype.provider;

import org.hippoecm.hst.content.beans.standard.HippoBean;

class MenuItemConfigJsonBean implements MenuItemConfig {

    private String relativeContentPath;
    private boolean showFacetNavigations;
    private boolean invisible;
    private boolean disabled;
    private HippoBean siteContentBaseBean;

    public String getRelativeContentPath() {
        return relativeContentPath;
    }

    public void setRelativeContentPath(String relativeContentPath) {
        this.relativeContentPath = relativeContentPath;
    }

    public boolean isShowFacetNavigations() {
        return showFacetNavigations;
    }

    public void setShowFacetNavigations(boolean showFacetNavigations) {
        this.showFacetNavigations = showFacetNavigations;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setSiteContentBaseBean(HippoBean siteContentBaseBean) {
        this.siteContentBaseBean = siteContentBaseBean;
    }

    @Override
    public HippoBean getBean() {
        return siteContentBaseBean.getBean(relativeContentPath, HippoBean.class);
    }

}