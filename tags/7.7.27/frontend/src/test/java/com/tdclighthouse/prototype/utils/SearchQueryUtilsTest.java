/*
 *  Copyright 2012 Finalist B.V.
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
package com.tdclighthouse.prototype.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ebrahim Aharpour
 *
 */
public class SearchQueryUtilsTest {

	@Test
	public void parseAndEscapeBadCharactersTest() {
		String testQuery = "\u00EB\u00E4\u00FC\u00FF\u00F6";
		String escapedTestQuery = SearchQueryUtils.parseAndEscapeBadCharacters(testQuery);
		Assert.assertEquals("eauyo", escapedTestQuery);

		testQuery = "the quick fox jumped over the lazy brown dog.";
		escapedTestQuery = SearchQueryUtils.parseAndEscapeBadCharacters(testQuery);
		Assert.assertEquals(testQuery, escapedTestQuery);
	}

}
