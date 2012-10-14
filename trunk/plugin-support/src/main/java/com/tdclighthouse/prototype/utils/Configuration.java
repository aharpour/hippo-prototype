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

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ebrahim Aharpour
 *
 */
public class Configuration {
	private static final String TIME_FORMAT_STRING = "HH:mm";
	public static Logger log = LoggerFactory.getLogger(Configuration.class);
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT_STRING);
	private final Properties properties;

	public Configuration(Properties properties) {
		this.properties = properties;
	}

	public Configuration(String propertiesName) throws IOException {
		InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(propertiesName);
		properties = new Properties();
		properties.load(inStream);
	}

	public String getString(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public Date getTime(String key, Date defaultValue) {
		Date result = defaultValue;
		String stringDate = properties.getProperty(key);
		try {
			if (StringUtils.isNotBlank(stringDate)) {

				Calendar calendar = new GregorianCalendar();
				calendar.setTime(TIME_FORMAT.parse(stringDate));
				Calendar now = new GregorianCalendar();
				calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
						now.get(Calendar.DAY_OF_MONTH));
				if (calendar.before(now)) {
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				result = calendar.getTime();
			}
		} catch (ParseException e) {
			log.error("the given value \"" + stringDate + "\" of the key \"" + key
					+ "\" is not of the format \"" + TIME_FORMAT_STRING
					+ "\" so we default back to the default value");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T getItem(String key, T defaultValue, Class<T> clazz) {
		T result = defaultValue;
		String stringValue = properties.getProperty(key);
		Object converted = ConvertUtils.convert(stringValue, clazz);
		if (clazz.isAssignableFrom(converted.getClass())) {
			result = (T) converted;
		} else {
			log.error("the given value \"" + stringValue + "\" of the key \"" + key
					+ "\" is not convertable to " + clazz.getSimpleName() + ". So we default back to the default value");
		}
		return result;
	}
}
