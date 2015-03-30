/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.beans.compounds;

import javax.xml.bind.annotation.XmlTransient;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;
import org.hippoecm.hst.content.beans.standard.HippoMirror;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = ImageBean.JCR_TYPE)
public class ImageBean extends TdcDocument {

    public static final String JCR_TYPE = "tdc:Image";

    private String title;
    private String alt;
    private String caption;
    private HippoMirror link;
    private HippoGalleryImageSetBean linkBean;
    private String credit;

    public String getTitle() {
        if (this.title == null) {
            this.title = getProperty("tdc:title");
        }
        return title;
    }

    public String getAlt() {
        if (this.alt == null) {
            this.alt = getProperty("tdc:alt");
        }
        return alt;
    }

    public String getCaption() {
        if (this.caption == null) {
            this.caption = getProperty("tdc:caption");
        }
        return caption;
    }

    @JsonIgnore
    @XmlTransient
    public HippoMirror getLink() {
        if (this.link == null) {
            this.link = getBean("tdc:link");
        }
        return link;
    }

    public HippoGalleryImageSetBean getLinkBean() {
        if (this.linkBean == null) {
            this.linkBean = getLinkedBean("tdc:link", HippoGalleryImageSetBean.class);
        }
        return linkBean;
    }

    public String getCredit() {
        if (this.credit == null) {
            this.credit = getProperty("tdc:credit");
        }
        return credit;
    }

}