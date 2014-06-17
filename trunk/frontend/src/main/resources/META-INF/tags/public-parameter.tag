<%@tag description="Set URL Query" pageEncoding="UTF-8" import="org.hippoecm.hst.core.component.HstRequest, java.util.Map, org.apache.commons.lang3.StringUtils" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ attribute name="parameterName" rtexprvalue="true" required="true" type="java.lang.String" %>
<%@ attribute name="var" rtexprvalue="true" type="java.lang.String" %>
<%  
HstRequest hstRequest = ((HstRequest)request);

String contextNamespaceReference = hstRequest.getRequestContext().getContextNamespace();

if (contextNamespaceReference == null) {
    contextNamespaceReference = "";
}

Map<String, String []> namespaceLessParameters = hstRequest.getParameterMap(contextNamespaceReference);
String [] paramValues = namespaceLessParameters.get(parameterName);

if (paramValues != null && paramValues.length > 0) {
	String value = paramValues[0];
	if (StringUtils.isNotBlank(var)) {
		request.setAttribute(var, value);		
	} else {
    	out.print(value);
	}
} else if (StringUtils.isNotBlank(var)){
	request.removeAttribute(var);		
}
%>