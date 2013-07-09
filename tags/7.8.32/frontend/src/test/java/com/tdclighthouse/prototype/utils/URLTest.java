package com.tdclighthouse.prototype.utils;

import org.junit.Assert;
import org.junit.Test;

import com.tdclighthouse.prototype.utils.URL;

public class URLTest {

	private static final String NEW_PARAM = "token";
	private static final String NEW_PARAM_VALUE = "myToken";

	@Test
	public void parsingTest() {
		URL url = new URL(
				"https://www.google.com/search?q=test&oq=test&aqs=chrome.0.59j57j60l3j65.329&sourceid=chrome&ie=UTF-8#test");
		url.setQueryParameter(NEW_PARAM, NEW_PARAM_VALUE);
		String urlAsString = url.toString();
		Assert.assertEquals(true, urlAsString.startsWith("https://www.google.com/search?"));
		Assert.assertEquals(true, urlAsString.endsWith("#test"));
		Assert.assertEquals(true, urlAsString.contains("q=test"));
		Assert.assertEquals(true, urlAsString.contains("oq=test"));
		Assert.assertEquals(true, urlAsString.contains("aqs=chrome.0.59j57j60l3j65.329"));
		Assert.assertEquals(true, urlAsString.contains("sourceid=chrome"));
		Assert.assertEquals(true, urlAsString.contains("ie=UTF-8"));
		Assert.assertEquals(true, urlAsString.contains(NEW_PARAM + "=" + NEW_PARAM_VALUE));
	}
}
