package com.tdclighthouse.prototype.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstComponentException;

import com.tdclighthouse.commons.mail.util.MailClient;

public class EmailUtils {

	private final static Map<String, MailClient> mailClientRegistary = new Hashtable<String, MailClient>();

	public static MailClient getMailClient(final String sessionName) {
		MailClient result = mailClientRegistary.get(sessionName);
		if (result == null) {
			Session mailSession = getMailSession(sessionName);
			result = new MailClient(mailSession);
			mailClientRegistary.put(sessionName, result);
		}
		return result;
	}

	public static Session getMailSession(final String sessionName) {
		Session result = null;
		InitialContext initialContext = null;
		try {
			initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:comp/env");
			result = (Session) context.lookup(sessionName);
		} catch (NamingException e) {
			throw new HstComponentException(e);
		} finally {
			try {
				if (initialContext != null) {
					initialContext.close();
				}
			} catch (NamingException e) {
				throw new HstComponentException(e);
			}
		}

		return result;
	}

	public static String[] parseToAddress(String to) {
		List<String> emails = new ArrayList<String>();
		String[] split = to.split("(\\s*[;,]\\s*)+");
		for (String email : split) {
			if (StringUtils.isNotBlank(email)) {
				emails.add(email);
			}
		}
		return emails.toArray(new String[emails.size()]);
	}

}
