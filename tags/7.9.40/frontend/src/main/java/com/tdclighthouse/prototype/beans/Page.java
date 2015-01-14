package com.tdclighthouse.prototype.beans;

import java.util.Calendar;

import org.hippoecm.hst.content.beans.standard.HippoBean;

public interface Page extends HippoBean {

    String getTitle();

    Boolean getHideFromSitemap();

    Calendar getReleaseDate();

}
