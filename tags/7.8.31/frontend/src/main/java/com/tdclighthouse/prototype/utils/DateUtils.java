package com.tdclighthouse.prototype.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	private DateUtils() {
	}

	public static boolean isSameMonth(Date date1, Date date2) {
		return isSameMonth(dateToCalendar(date1), dateToCalendar(date2));
	}

	public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
				.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
	}

	public static boolean isSameYear(Date date1, Date date2) {
		return isSameYear(dateToCalendar(date1), dateToCalendar(date2));
	}

	public static boolean isSameYear(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR));
	}


	private static Calendar dateToCalendar(Date date) {
		Calendar cal = null;
		if (date != null) {
			cal = Calendar.getInstance();
			cal.setTime(date);
		}
		return cal;
	}
}
