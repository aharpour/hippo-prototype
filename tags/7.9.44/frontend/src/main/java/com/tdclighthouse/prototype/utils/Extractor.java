package com.tdclighthouse.prototype.utils;

public interface Extractor<T, R> {
    public R extract(T t);
}