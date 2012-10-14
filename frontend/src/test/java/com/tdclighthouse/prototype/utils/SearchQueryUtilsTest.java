package com.tdclighthouse.prototype.utils;

import org.junit.Assert;
import org.junit.Test;

public class SearchQueryUtilsTest {

	@Test
	public void parseAndEscapeBadCharactersTest() {
		String testQuery = "\u00EB\u00E4\u00FC\u00FF\u00F6";
		String escapedTestQuery = SearchQueryUtils.parseAndEscapeBadCharacters(testQuery);
		System.out.println("Original query: " + testQuery + "\nEscaped query: " + escapedTestQuery);
		Assert.assertEquals("eauyo", escapedTestQuery);

		testQuery = "the quick fox jumped over the lazy brown dog.";
		escapedTestQuery = SearchQueryUtils.parseAndEscapeBadCharacters(testQuery);
		System.out.println("Original query: " + testQuery + "\nEscaped query: " + escapedTestQuery);
		Assert.assertEquals(testQuery, escapedTestQuery);
	}

}
