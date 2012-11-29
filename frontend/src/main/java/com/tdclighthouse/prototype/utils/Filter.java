package com.tdclighthouse.prototype.utils;

public interface Filter<T> {
	public boolean accept(T bean);
}