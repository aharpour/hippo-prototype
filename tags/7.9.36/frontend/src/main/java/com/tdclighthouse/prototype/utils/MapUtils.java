package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtils {

    private MapUtils() {
    }

    public static <K, V> Map<V, K> reverseMap(Map<K, V> map) {
        Map<V, K> result = null;
        if (map != null) {
            result = new HashMap<V, K>(map.size());

            for (Iterator<Entry<K, V>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
                Entry<K, V> entry = iterator.next();
                result.put(entry.getValue(), entry.getKey());
            }
        }
        return result;
    }

}
