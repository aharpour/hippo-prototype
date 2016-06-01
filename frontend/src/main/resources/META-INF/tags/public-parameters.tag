<%@tag description="Set URL Query" pageEncoding="UTF-8" import="org.hippoecm.hst.core.component.HstRequest, java.util.Map" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ attribute name="parameterName" rtexprvalue="true" required="true" type="java.lang.String" %>
<%@ attribute name="var" rtexprvalue="true" required="true" type="java.lang.String" %>
<%  
HstRequest hstRequest = ((HstRequest)request);

Map<String, String []> namespaceLessParameters = hstRequest.getParameterMap("");
String [] paramValues = namespaceLessParameters.get(parameterName);
if (paramValues != null && paramValues.length > 0) {
	request.setAttribute(var, paramValues);
}
%>