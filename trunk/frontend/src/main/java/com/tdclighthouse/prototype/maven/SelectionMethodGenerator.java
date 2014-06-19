package com.tdclighthouse.prototype.maven;

/*
 *    Copyright 2013 Smile B.V
 *
 *   Licensed under the Apache License, Version 2.0 (the "License")
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.mavenhippo.gen.AnnotationGenerator;
import net.sourceforge.mavenhippo.gen.ClassReference;
import net.sourceforge.mavenhippo.gen.MethodGenerator;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Item;
import net.sourceforge.mavenhippo.utils.FreemarkerUtils;
import net.sourceforge.mavenhippo.utils.NammingUtils;
import net.sourceforge.mavenhippo.utils.exceptions.GeneratorException;

/**
 * @author Ebrahim Aharpour
 *
 */
public class SelectionMethodGenerator implements MethodGenerator {

    private final Item item;
    private final String valueListPath;
    private final ClassReference returnType;

    public SelectionMethodGenerator(Item item, String valueListPath, ClassReference returnType) {
        this.item = item;
        this.valueListPath = valueListPath;
        this.returnType = returnType;
    }

    @Override
    public String getFragment() throws GeneratorException {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            String fieldName = item.getSimpleName();
            model.put("fieldName", fieldName);
            model.put("propertyName", item.getRelativePath());
            model.put("methodName", NammingUtils.stringToClassName(fieldName));
            model.put("valueListPath", valueListPath);
            model.put("type", returnType);
            return FreemarkerUtils.renderTemplate("com/tdclighthouse/prototype/maven/selection-method-generator.ftl",
                    model, this.getClass());
        } catch (Exception e) {
            throw new GeneratorException(e.getMessage(), e);
        }
    }

    @Override
    public List<AnnotationGenerator> getAnnotations() {
        return new ArrayList<AnnotationGenerator>();
    }

}
