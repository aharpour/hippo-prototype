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
package com.tdclighthouse.prototype.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = TdcImageSetBean.JCR_TYPE)
public class TdcImageSetBean extends HippoGalleryImageSet {

    public static final String JCR_TYPE = "tdc:TdcImageSet";

    private HippoGalleryImageBean articleImage;
    private HippoGalleryImageBean paragraphImage;

    public HippoGalleryImageBean getArticleImage() {
        if (this.articleImage == null) {
            this.articleImage = getBean("tdc:articleImage");
        }
        return articleImage;
    }

    public HippoGalleryImageBean getParagraphImage() {
        if (this.paragraphImage == null) {
            this.paragraphImage = getBean("tdc:paragraphImage");
        }
        return paragraphImage;
    }

}
