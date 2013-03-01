package com.tdclighthouse.prototype.utils;

import org.hippoecm.hst.core.component.HstRequest;

public interface Filter<T> {
	public boolean accept(T bean, HstRequest request);
}