package com.tdclighthouse.prototype.utils;

import org.hippoecm.hst.util.SearchInputParsingUtils;

public class SearchQueryUtils {

	public static String parseAndEscapeBadCharacters(String query) {
		query = SearchInputParsingUtils.parse(query, true);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < query.length(); i++) {
			char c = query.charAt(i);
			switch (c) {
			case '\u00EB':
				sb.append('e');
				break;
			case '\u00E4':
				sb.append('a');
				break;
			case '\u00FC':
				sb.append('u');
				break;
			case '\u00FF':
				sb.append('y');
				break;
			case '\u00F6':
				sb.append('o');
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}
}
