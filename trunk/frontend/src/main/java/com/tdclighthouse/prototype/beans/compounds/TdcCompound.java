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
package com.tdclighthouse.prototype.beans.compounds;

import java.util.Map;

import javax.jcr.Node;
import javax.xml.bind.annotation.XmlTransient;

import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.standard.HippoAvailableTranslationsBean;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.provider.jcr.JCRValueProvider;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Ebrahim Aharpour
 *
 */
public class TdcCompound extends HippoCompound {

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