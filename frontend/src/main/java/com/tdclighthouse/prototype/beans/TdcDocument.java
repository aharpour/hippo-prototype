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
package com.tdclighthouse.prototype.beans;

import java.util.Calendar;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.standard.HippoAvailableTranslationsBean;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.provider.jcr.JCRValueProvider;
import org.hippoecm.hst.util.PathUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tdclighthouse.prototype.beans.compounds.SelectionBean;
import com.tdclighthouse.prototype.beans.compounds.ValueListBean;
import com.tdclighthouse.prototype.utils.TdcUtils;

/**
 * @author Ebrahim Aharpour
 *
 */
public class TdcDocument extends HippoDocument {

	public Calendar getLastModificationDate() {
		return getProperty("hippostdpubwf:lastModificationDate");
	}

	public SelectionBean getSelectionBean(String propertyName, String listAbslutePath) {
		Object propertyValue = getProperty(propertyName);
		Map<String, String> labelsMap = getSelectionOptionsMap(listAbslutePath);
		return new SelectionBean(labelsMap, propertyValue);
	}

	protected Map<String, String> getSelectionOptionsMap(String path) {
		return TdcUtils.valueListBeanToMap(getValueListBean(path));
	}

	protected ValueListBean getValueListBean(String path) {
		try {
			ValueListBean valueListBean;
			Node listValueNode = getNode().getSession().getRootNode().getNode(PathUtils.normalizePath(path));
			Object object = getObjectConverter().getObject(listValueNode);
			if (object instanceof ValueListBean) {
				valueListBean = (ValueListBean) object;
				String localeString = getLocaleString();
				if (StringUtils.isNotBlank(localeString)) {
					HippoBean translation = valueListBean.getAvailableTranslationsBean().getTranslation(localeString);
					if (translation instanceof ValueListBean) {
						valueListBean = (ValueListBean) translation;
					}
				}
			} else {
				throw new HstComponentException("the path given \"" + path + "\" is invalid");
			}
			return valueListBean;
		} catch (ObjectBeanManagerException e) {
			throw new RuntimeException(e);
		} catch (PathNotFoundException e) {
			throw new RuntimeException(e);
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public Node getNode() {
		return super.getNode();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public <T extends HippoBean> T getCanonicalBean() {
		return super.getCanonicalBean();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public JCRValueProvider getValueProvider() {
		return super.getValueProvider();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public Map<String, Object> getProperties() {
		return super.getProperties();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public <T extends HippoBean> HippoAvailableTranslationsBean<T> getAvailableTranslationsBean() {
		return super.getAvailableTranslationsBean();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public HippoBean getParentBean() {
		return super.getParentBean();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public HippoBean getContextualBean() {
		return super.getContextualBean();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public HippoBean getContextualParentBean() {
		return super.getContextualParentBean();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public Map<Object, Object> getEqualComparator() {
		return super.getEqualComparator();
	}

	@Override
	@JsonIgnore
	@XmlTransient
	public ObjectConverter getObjectConverter() {
		return super.getObjectConverter();
	}

}