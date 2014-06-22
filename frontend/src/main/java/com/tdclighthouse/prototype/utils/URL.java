package com.tdclighthouse.prototype.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.Parameters;

import com.tdclighthouse.prototype.utils.exceptions.UrlRuntimeException;

public class URL {

    private final String originalUrl;
    private final String pagePath;
    private final Map<String, String[]> queryParameters;
    private final String anchor;

    public URL(String url) {
        this.originalUrl = url;
        int indexOfQuestionMark = url.indexOf('?');
        int indexOfHash = url.lastIndexOf('#');
        queryParameters = stringParameterToMap(getQueryString(indexOfQuestionMark, indexOfHash));
        anchor = getAnchor(indexOfHash);
        pagePath = getPagePath(indexOfQuestionMark, indexOfHash);
    }

    public void setQueryParameter(String name, String value) {
        queryParameters.put(name, new String[] { value });
    }

    public String[] getQueryParameter(String name) {
        String[] result = null;
        String[] values = queryParameters.get(name);
        if (values != null) {
            result = new String[values.length];
            System.arraycopy(values, 0, result, 0, values.length);
        }
        return result;
    }

    public void setQueryParameter(String name, String[] values) {
        queryParameters.put(name, values);
    }

    public String getUrlAsString() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder(this.pagePath);
        if (!this.queryParameters.isEmpty()) {
            result.append('?').append(getQueryString());

        }
        if (StringUtils.isNotBlank(this.anchor)) {
            result.append('#').append(this.anchor);
        }
        return result.toString();
    }

    private String getAnchor(int indexOfHash) {
        String result = null;
        if (indexOfHash != -1) {
            result = originalUrl.substring(indexOfHash + 1);
        }
        return result;
    }

    private String getQueryString(int indexOfQuestionMark, int indexOfHash) {
        String result = null;
        if (indexOfQuestionMark != -1) {
            int i = indexOfHash;
            if (indexOfHash == -1) {
                i = originalUrl.length();
            }
            result = originalUrl.substring(indexOfQuestionMark + 1, i);
        }
        return result;
    }

    private String getPagePath(int indexOfQuestionMark, int indexOfHash) {
        String result;
        if (indexOfQuestionMark > 0) {
            result = originalUrl.substring(0, indexOfQuestionMark);
        } else if (indexOfHash > 0) {
            result = originalUrl.substring(0, indexOfHash);
        } else {
            result = originalUrl;
        }
        return result;
    }

    @Override
    public String toString() {
        try {
            return getUrlAsString();
        } catch (UnsupportedEncodingException e) {
            throw new UrlRuntimeException(e.getMessage(), e);
        }
    }

    private String getQueryString() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        Iterator<String> iterator = this.queryParameters.keySet().iterator();
        boolean firstTime = true;
        while (iterator.hasNext()) {
            String key = iterator.next();
            String[] values = this.queryParameters.get(key);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    if (firstTime) {
                        firstTime = false;
                    } else {
                        result.append("&");
                    }
                    result.append(URLEncoder.encode(key, Constants.EncodingsConstants.UTF8));
                    result.append('=');
                    result.append(URLEncoder.encode(values[i], Constants.EncodingsConstants.UTF8));
                }
            }
        }
        return result.toString();
    }

    private Map<String, String[]> stringParameterToMap(String queryString) {
        Map<String, String[]> result = new HashMap<String, String[]>();
        Parameters parameters = new Parameters();
        MessageBytes messagebytes = MessageBytes.newInstance();
        messagebytes.setString(queryString);
        parameters.processParameters(messagebytes, Constants.EncodingsConstants.UTF8);
        Enumeration<String> parameterNames = parameters.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            result.put(parameterName, parameters.getParameterValues(parameterName));
        }
        return result;
    }
}
