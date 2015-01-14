/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
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

import org.hippoecm.hst.util.SearchInputParsingUtils;

/**
 * @author Ebrahim Aharpour
 *
 */
public class SearchQueryUtils {

    private SearchQueryUtils() {
    }

    public static String parseAndEscapeBadCharacters(String query) {
        String q = SearchInputParsingUtils.parse(query, true);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q.length(); i++) {
            char c = q.charAt(i);
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
