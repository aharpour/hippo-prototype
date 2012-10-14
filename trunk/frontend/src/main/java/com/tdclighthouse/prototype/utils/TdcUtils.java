package com.tdclighthouse.prototype.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;

import com.tdclighthouse.prototype.beans.compounds.ListItemBean;
import com.tdclighthouse.prototype.beans.compounds.ValueListBean;

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

}
