package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapUtils {

	private MapUtils() {
	}

	public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
		Map<V, K> result = null;
		if (map != null) {
			result = new HashMap<V, K>(map.size());
			for (Iterator<K> iterator = map.keySet().iterator(); iterator.hasNext();) {
				K oldKey = iterator.next();
				V oldValue = map.get(oldKey);
				result.put(oldValue, oldKey);
			}
		}
		return result;
	}

}
