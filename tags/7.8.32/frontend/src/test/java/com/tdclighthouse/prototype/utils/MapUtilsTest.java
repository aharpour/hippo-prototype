package com.tdclighthouse.prototype.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class MapUtilsTest {

	@Test
	public void reverseMapTest() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(null, "value");
		map.put("key", null);
		Map<String, String> reversedMap = MapUtils.reverseMap(map);
		Assert.assertEquals("key", reversedMap.get(null));
		Assert.assertEquals(null, reversedMap.get("value"));

	}

}
