package com.tdclighthouse.prototype.maven;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.mavenhippo.gen.*;
import net.sourceforge.mavenhippo.model.ContentTypeBean;
import net.sourceforge.mavenhippo.utils.exceptions.GeneratorException;

public class SelectionPropertyGenerator implements PropertyGenerator {
    private final ClassReference type;
    private final String fieldName;
    private final ContentTypeItemAnalyzer.Type propertyType;

    public SelectionPropertyGenerator(ContentTypeItemAnalyzer.AnalyzerResult analyzerResult, ContentTypeBean.Item item, ImportRegistry importRegistry) {
        this.type = analyzerResult.getReturnType();
        this.propertyType = analyzerResult.getType();
        this.fieldName = item.getSimpleName();
        importRegistry.register(type);
    }

    @Override
    public String getFragment() throws GeneratorException {
        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("multiple", false);
            model.put("type", type);
            model.put("fieldName", fieldName);
            model.put("basicType", ContentTypeItemAnalyzer.Type.PROPERTY == propertyType);
            return FreemarkerUtils.renderTemplate("net/sourceforge/mavenhippo/gen/default-property-generator.ftl",
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
