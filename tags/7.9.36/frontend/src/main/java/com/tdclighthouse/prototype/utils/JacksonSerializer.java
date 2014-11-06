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
import java.util.List;

import org.apache.commons.lang3.SerializationException;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * @author Ebrahim Aharpour
 *
 */
public class JacksonSerializer implements ObjectSerializer {
    private ObjectMapper objectMapper;
    private List<JsonSerializer<?>> serializers;

    @Override
    public void serialize(Object source, OutputStream outputStream) {
        try {
            objectMapper.writeValue(outputStream, source);
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setSerializers(List<JsonSerializer<?>> serializers) {
        this.serializers = serializers;
    }

    public void initialize() {
        SimpleModule module = new SimpleModule();
        for (JsonSerializer<?> jsonSerializer : serializers) {
            module.addSerializer(jsonSerializer);
        }
        objectMapper.registerModule(module);
    }

}
