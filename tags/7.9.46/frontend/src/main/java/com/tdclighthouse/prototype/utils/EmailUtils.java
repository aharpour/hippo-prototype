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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.commons.mail.util.MailClient;

/**
 * @author Ebrahim Aharpour
 *
 */
public class EmailUtils {

    private static final Logger LOG = LoggerFactory.getLogger(EmailUtils.class);
    private static final Map<String, MailClient> MAIL_CLIENT_REGISTARY = new Hashtable<String, MailClient>();

    private EmailUtils() {
    }

    public static MailClient getMailClient(final String sessionName) {
        MailClient result = MAIL_CLIENT_REGISTARY.get(sessionName);
        if (result == null) {
            Session mailSession = getMailSession(sessionName);
            result = new MailClient(mailSession);
            MAIL_CLIENT_REGISTARY.put(sessionName, result);
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
                LOG.error(e.getMessage(), e);
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
