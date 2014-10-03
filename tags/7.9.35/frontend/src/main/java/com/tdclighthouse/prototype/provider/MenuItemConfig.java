package com.tdclighthouse.prototype.provider;

import org.hippoecm.hst.content.beans.standard.HippoBean;

interface MenuItemConfig {

    public abstract boolean isShowFacetNavigations();

    public abstract HippoBean getBean();

    public boolean isDisabled();

    public boolean isInvisible();

}