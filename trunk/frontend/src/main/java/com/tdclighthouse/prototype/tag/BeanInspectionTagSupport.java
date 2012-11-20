/*
 *  Copyright 2012 Finalist B.V.
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
package com.tdclighthouse.prototype.tag;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Ebrahim Aharpour
 *
 */
public class BeanInspectionTagSupport {

	public static void inspectProperties(Object bean, JspWriter out, Boolean applyHippoBlackList,
			Integer inspectionDepth) {
		try {
			int depth = (inspectionDepth == null ? 5 : inspectionDepth);
			PropertyInspector inspector = new PropertyInspector(out, applyHippoBlackList);
			inspector.recursivePropertyInspector(bean, "", depth);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static class PropertyInspector {

		public static Set<String> hippoBlackList = getBlackList();

		private final JspWriter out;
		private final boolean applyHippoBlackList;

		public PropertyInspector(JspWriter out, Boolean applyHippoBlackList) {
			this.out = out;
			if ((applyHippoBlackList != null) && (applyHippoBlackList == false)) {
				this.applyHippoBlackList = false;
			} else {
				this.applyHippoBlackList = true;
			}
		}

		public void recursivePropertyInspector(Object bean, String relativePath, int inspectionDepth)
				throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
			if (inspectionDepth > 0) {
				PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(bean);
				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					if ((propertyDescriptor.getReadMethod() != null)
							&& (!applyHippoBlackList || isNotBlackListed(propertyDescriptor))) {
						Class<?> propertyType = PropertyUtils.getPropertyType(bean, propertyDescriptor.getName());
						String path = relativePath + (StringUtils.isNotBlank(relativePath) ? "." : "")
								+ propertyDescriptor.getName();
						try {
							Object propertyValue = PropertyUtils.getProperty(bean, propertyDescriptor.getName());
							if (propertyValue != null) {
								doWithProperty(propertyType, propertyValue, path, inspectionDepth - 1);
							} else {
								print(path, propertyValue);
							}
						} catch (Exception e) {
							print(path,
									"<span style=\"color: red;\">an exeption of type " + e.getClass().getSimpleName()
											+ " was throw with the following message: " + e.getMessage() + "</span>");
						}
					}
				}
			}
		}

		private boolean isNotBlackListed(PropertyDescriptor propertyDescriptor) {
			return !hippoBlackList.contains(propertyDescriptor.getName());
		}

		private void doWithProperty(Class<?> propertyType, Object propertyValue, String path, int inspectionDepth)
				throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
			if (!(propertyType.isAssignableFrom(Class.class))) {
				if (ClassUtils.isPrimitiveOrWrapper(propertyType) || String.class.isAssignableFrom(propertyType)
						|| Date.class.isAssignableFrom(propertyType)) {
					print(path, propertyValue);
				} else if (Calendar.class.isAssignableFrom(propertyType)) {
					print(path, ((Calendar) propertyValue).getTime());
				} else if (propertyType.isArray()) {
					int length = Array.getLength(propertyValue);
					for (int i = 0; i < length; i++) {
						Object object = Array.get(propertyValue, i);
						doWithProperty(object.getClass(), object, path + "[" + i + "]", inspectionDepth);
					}
				} else if (Collection.class.isAssignableFrom(propertyType)) {
					Collection<?> collection = (Collection<?>) propertyValue;
					int i = 0;
					for (Object object : collection) {
						if (object != null) {
							doWithProperty(object.getClass(), object, path + "[" + i + "]", inspectionDepth);
						} else {
							print(path + "[" + i + "]", object);
						}
						i++;
					}

				} else {
					recursivePropertyInspector(propertyValue, path, inspectionDepth);
				}
			}
		}

		private void print(String path, Object value) throws IOException {
			out.println("<strong style=\"color: blue;\">" + path + "</strong> = " + value);
		}

		private static HashSet<String> getBlackList() {
			HashSet<String> result = new HashSet<String>();
			result.add("canonicalPath");
			result.add("canonicalHandleUUID");
			result.add("canonicalHandlePath");
			result.add("canonicalUUID");
			result.add("hippoFolderBean");
			result.add("hippoDocumentBean");
			result.add("leaf");
			result.add("localizedName");
			result.add("localeString");
			result.add("parentBean");
			result.add("availableTranslationsBean");
			result.add("contextualParentBean");
			result.add("contextualBean");
			result.add("UUID");
			result.add("node");
			result.add("valueProvider");
			result.add("equalComparator");
			result.add("canonicalBean");
			result.add("property");
			return result;
		}

	}
}
