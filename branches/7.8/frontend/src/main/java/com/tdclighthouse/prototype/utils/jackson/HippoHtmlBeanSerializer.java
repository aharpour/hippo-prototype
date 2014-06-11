/*
 *  Copyright 2013 Smile B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
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

import org.hippoecm.hst.content.beans.standard.HippoHtmlBean;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @author Ebrahim Aharpour
 *
 */
public class HippoHtmlBeanSerializer extends StdSerializer<HippoHtmlBean> {

	public HippoHtmlBeanSerializer() {
		super(HippoHtmlBean.class);
	}

	@Override
	public void serialize(HippoHtmlBean value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeString(value.getContent());
	}

}
