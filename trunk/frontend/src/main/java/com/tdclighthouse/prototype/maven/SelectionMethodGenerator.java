package com.tdclighthouse.prototype.maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.mavenhippo.gen.AnnotationGenerator;
import net.sourceforge.mavenhippo.gen.MethodGenerator;
import net.sourceforge.mavenhippo.model.ContentTypeBean.Item;
import net.sourceforge.mavenhippo.utils.FreemarkerUtils;
import net.sourceforge.mavenhippo.utils.NammingUtils;

public class SelectionMethodGenerator implements MethodGenerator {
	
	private final Item item; 
	private final String valueListPath;

	public SelectionMethodGenerator(Item item, String valueListPath) {
		this.item = item;
		this.valueListPath = valueListPath;
	}

	@Override
	public String getFragment() {
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("fieldName", item.getSimpleName());
			String propertyName = item.getRelativePath();
			model.put("propertyName", propertyName);
			model.put("methodName", NammingUtils.stringToClassName(propertyName));
			model.put("valueListPath", valueListPath);
			return FreemarkerUtils.renderTemplate("com/tdclighthouse/prototype/maven/selection-method-generator.ftl",
					model);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<AnnotationGenerator> getAnnotations() {
		return new ArrayList<AnnotationGenerator>();
	}

}
