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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tdclighthouse.prototype.beans.compounds.SelectionBean;

import net.sourceforge.mavenhippo.gen.*;
import net.sourceforge.mavenhippo.gen.ContentTypeItemAnalyzer.AnalyzerResult;
import net.sourceforge.mavenhippo.gen.ContentTypeItemAnalyzer.Type;
import net.sourceforge.mavenhippo.gen.annotation.Weight;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Item;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Template;
import net.sourceforge.mavenhippo.model.HippoBeanClass;

import org.apache.commons.lang3.StringUtils;



/**
 * @author Ebrahim Aharpour
 *
 */
@Weight(value = -10.0)
public class SelectionHandler extends ContentTypeItemHandler {

    private static final String VALUELIST_OPTIONS = "valuelist.options";

    public SelectionHandler(Map<String, HippoBeanClass> beansOnClassPath, Map<String, HippoBeanClass> beansInProject,
                            Set<String> namespaces, PackageHandler packageHandler) {
        super(beansOnClassPath, beansInProject, namespaces, packageHandler);
    }

    @Override
    public HandlerResponse handle(Item item, ImportRegistry importRegistry) {
        HandlerResponse result = null;
        if ("DynamicDropdown".equals(item.getType())) {
            Template template = item.getContentType().getTemplate(item);
            if (template != null) {
                String pathToValueList = template.getOptionsValue("source");
                if (StringUtils.isBlank(pathToValueList)) {
                    pathToValueList = template.getSubnodePropertyValue(VALUELIST_OPTIONS, "source");
                }
                if (pathToValueList != null) {
                    ClassReference returnType = new ClassReference(SelectionBean.class);
                    importRegistry.register(returnType);
                    List<PropertyGenerator> propertyGenerators = Collections
                            .singletonList(new SelectionPropertyGenerator(new AnalyzerResult(
                                    Type.PROPERTY, returnType), item, importRegistry));
                    List<MethodGenerator> methodGenerators = Collections
                            .singletonList(new SelectionMethodGenerator(item, pathToValueList,
                                    returnType));

                    result = new HandlerResponse(propertyGenerators, methodGenerators);

                }
            }
        }
        return result;
    }
}
