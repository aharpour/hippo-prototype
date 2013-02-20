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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;

import com.tdclighthouse.prototype.beans.compounds.ListItemBean;
import com.tdclighthouse.prototype.beans.compounds.ValueListBean;

/**
 * @author Ebrahim Aharpour
 *
 */
public class TdcUtils {

	private static final String GMT = "GMT";
	private static final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'",
			new Locale("en")) {
		private static final long serialVersionUID = 1L;

		{
			setTimeZone(TimeZone.getTimeZone(GMT));
		}
	};

	public static String dateToRFC1123(Date date) {
		String result = dateFormat.format(date);
		return result;
	}
	
	public static String getContextPath(HstRequest request) {
		String contextPath = request.getRequestContext().getVirtualHost().getVirtualHosts().getDefaultContextPath();
		boolean contextPathInUrl = request.getRequestContext().getVirtualHost().getVirtualHosts().isContextPathInUrl();
		if (StringUtils.isBlank(contextPath)) {
			contextPath = request.getContextPath();
		}
		return contextPathInUrl ? contextPath : "";
	}

	public static String getExpiresDate(int hours) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.HOUR, hours);
		Date date = calendar.getTime();
		return dateToRFC1123(date);

	}

	public static void main(String[] args) {
		System.out.println(getExpiresDate(2));
	}

	public static Map<String, String> valueListBeanToMap(ValueListBean valueList) {
		List<ListItemBean> listItem = valueList.getListItem();
		Map<String, String> result = new HashMap<String, String>(listItem.size());
		for (ListItemBean listItemBean : listItem) {
			result.put(listItemBean.getKey(), listItemBean.getLabel());
		}
		return result;
	}

	public static String getCurrentPagePath(HstRequest request) {
		HstRequestContext requestContext = request.getRequestContext();
		return request.getContextPath() + requestContext.getResolvedMount().getResolvedMountPath() + "/"
				+ requestContext.getResolvedSiteMapItem().getPathInfo();
	}

	public static String encode(String param) throws UnsupportedEncodingException {
		return URLEncoder.encode(param, Constants.Encodings.UTF8);
	}

	public static boolean collectionContains(@SuppressWarnings("rawtypes") Collection collection, Object object) {
		return collection.contains(object);
	}

	public static boolean mapContainsKey(@SuppressWarnings("rawtypes") Map map, Object key) {
		return map.containsKey(key);
	}

	public static boolean mapContainsValue(@SuppressWarnings("rawtypes") Map map, Object value) {
		return map.containsValue(value);
	}

	public static String getCurrentUrl(HttpServletRequest request) {
		String result = request.getRequestURL().toString();
		String queryString = request.getQueryString(); // d=789
		if (queryString != null) {
			result += "?" + queryString;
		}
		return result;
	}

}
