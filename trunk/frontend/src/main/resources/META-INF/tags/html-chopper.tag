<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<%@ attribute name="content" required="true" type="org.hippoecm.hst.content.beans.standard.HippoHtml" rtexprvalue="true"%>
<%@ attribute name="maxLength" required="true" type="java.lang.Integer" rtexprvalue="true"%>
<%@ attribute name="showReadMore" required="false" type="java.lang.Boolean" rtexprvalue="true"%>
<%@ attribute name="showDots" required="false" type="java.lang.Boolean" rtexprvalue="true"%>
<%@ attribute name="allowedLengthTolerance" required="false" type="java.lang.Integer" rtexprvalue="true"%>

<jsp:useBean id="StringChopper" class="com.tdclighthouse.commons.utils.beans.StringChopperBean" scope="page" />

<hst:html hippohtml="${content }" var="string" />

<jsp:setProperty name="StringChopper" property="targetString" value="${string }" />
<jsp:setProperty name="StringChopper" property="maxLength" value="${maxLength }" />

<c:choose>
  <c:when test="${not empty showDots }">
    <jsp:setProperty name="StringChopper" property="suffix" value="..." />
  </c:when>
  <c:otherwise>
    <jsp:setProperty name="StringChopper" property="suffix" value="" />
  </c:otherwise>
</c:choose>

<c:if test="${allowedLengthTolerance != null}">
	<jsp:setProperty name="StringChopper" property="punctuationSearchIntervalPercentage" value="${allowedLengthTolerance}" />
</c:if>
${StringChopper.result }
<c:if test="${showReadMore}">&nbsp;<a href="<hst:link hippobean="${bean}"/>"><fmt:message key="global.readmore" /></a></c:if>
