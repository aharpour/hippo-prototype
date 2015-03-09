/*
 *  Copyright 2013 Smile B.V.
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
package com.tdclighthouse.prototype.utils.jackson;

import java.io.IOException;

import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author Ebrahim Aharpour
 *
 */
public class HippoGalleryImageSetBeanSerializer extends StdSerializer<HippoGalleryImageSetBean> {

    public HippoGalleryImageSetBeanSerializer() {
        super(HippoGalleryImageSetBean.class);
    }

    @Override
    public void serialize(HippoGalleryImageSetBean value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.getCanonicalPath());
    }

}
