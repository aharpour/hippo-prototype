package com.tdclighthouse.prototype.maven;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.mavenhippo.gen.ClassReference;
import net.sourceforge.mavenhippo.gen.ContentTypeItemHandler;
import net.sourceforge.mavenhippo.gen.HandlerResponse;
import net.sourceforge.mavenhippo.gen.ImportRegistry;
import net.sourceforge.mavenhippo.gen.MethodGenerator;
import net.sourceforge.mavenhippo.gen.PackageHandler;
import net.sourceforge.mavenhippo.gen.PropertyGenerator;
import net.sourceforge.mavenhippo.gen.annotation.Weight;
import net.sourceforge.mavenhippo.gen.impl.ContentTypeItemAnalyzer.AnalyzerResult;
import net.sourceforge.mavenhippo.gen.impl.ContentTypeItemAnalyzer.Type;
import net.sourceforge.mavenhippo.gen.impl.DefaultPropertyGenerator;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Item;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Template;
import net.sourceforge.mavenhippo.model.HippoBeanClass;

import com.tdclighthouse.prototype.beans.compounds.SelectionBean;

@Weight(value = -10.0)
public class SelectionHandler extends ContentTypeItemHandler {

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
				if (pathToValueList != null) {
					ClassReference reutrnType = new ClassReference(SelectionBean.class);
					importRegistry.register(reutrnType);
					List<PropertyGenerator> propertyGenerators = Collections
							.singletonList((PropertyGenerator) new DefaultPropertyGenerator(new AnalyzerResult(Type.PROPERTY, reutrnType), item, importRegistry));
					List<MethodGenerator> methodGenerators = Collections
							.singletonList((MethodGenerator) new SelectionMethodGenerator(item, pathToValueList));

					result = new HandlerResponse(propertyGenerators, methodGenerators);
					
				}
			}
		}
		return result;
	}

}
