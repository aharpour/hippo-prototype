package com.tdclighthouse.prototype.derived;

import java.util.Map;

import javax.jcr.Value;

import org.hippoecm.repository.ext.DerivedDataFunction;

public class GenericDerivedDataFunction extends DerivedDataFunction{

	static final long serialVersionUID = 1;

	@Override
	public Map<String, Value[]> compute(Map<String, Value[]> parameters) {
		return parameters;
	}

}