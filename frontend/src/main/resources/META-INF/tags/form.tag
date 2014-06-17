<%@tag description="Renders a com.tdclighthouse.commons.simpleform.html.Form object" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" uri="http://open-web.nl/hippo/prototype" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="form" type="com.tdclighthouse.commons.simpleform.html.Form" rtexprvalue="true" required="true" %>
<%@ attribute name="method" type="java.lang.String" rtexprvalue="true" required="false" description="Http method, default value is Post" %>
<c:if test="${empty method}">
	<c:set value="POST" var="method"/>
</c:if>
<c:set var="action" value="${form.action}"/>
<c:if test="${empty action}">
	<c:choose>
		<c:when test="${fn:containsIgnoreCase(method, 'post')}">
			<hst:actionURL var="action"/>
		</c:when>
		<c:otherwise>
			<c:set var="action" value=""/>
		</c:otherwise>
	</c:choose>
</c:if>
<c:set var="submitButtonLabel"><fmt:message key="form.submit.button.label"/></c:set>
<c:set value="${form.group}" var="group"/>
<form action="${action}" method="${method}" class="simpleform">
	<input type="hidden" name="formname" value="${form.name}">
	<tag:formgroup group="${group}"/>
  <div class="ef-buttons">
	 <input type="submit" id="submit_button" value="${submitButtonLabel}"/>
  </div>
</form>
