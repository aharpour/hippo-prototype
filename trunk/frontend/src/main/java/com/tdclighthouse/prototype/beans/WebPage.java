package com.tdclighthouse.prototype.beans;

import java.util.GregorianCalendar;

import org.hippoecm.hst.content.beans.standard.HippoBean;

public interface WebPage extends HippoBean {

    String getTitle();
    
    Boolean getHideFromSitemap();
    
    GregorianCalendar getReleaseDate();

}
