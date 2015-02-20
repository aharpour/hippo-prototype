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
package com.tdclighthouse.prototype.utils;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

/**
 * @author Ebrahim Aharpour
 *
 */
public class XStreamSerializer implements ObjectSerializer {

	private XStream xStream;
	private Map<Class<?>, List<String>> exclusionMap;
	private List<Converter> converters;

	@Override
	public void serialize(Object source, OutputStream outputStream) {
		xStream.toXML(source, outputStream);
	}

	public void setxStream(XStream xStream) {
		this.xStream = xStream;
	}

	public void setExclusionMap(Map<Class<?>, List<String>> exclusionMap) {
		this.exclusionMap = exclusionMap;
	}

	public void setConverters(List<Converter> converters) {
		this.converters = converters;
	}

	public void initialize() throws Exception {

		for (Converter converter : converters) {
			xStream.registerConverter(converter);
		}

		for (Iterator<Class<?>> iterator = exclusionMap.keySet().iterator(); iterator.hasNext();) {
			Class<?> clazz = iterator.next();
			List<String> fields = exclusionMap.get(clazz);
			for (String field : fields) {
				xStream.omitField(clazz, field);
			}
		}

	}
}
