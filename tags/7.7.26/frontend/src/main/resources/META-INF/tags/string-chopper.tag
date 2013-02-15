<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="bean" required="true" type="org.hippoecm.hst.content.beans.standard.HippoBean" rtexprvalue="true"%>
<%@ attribute name="stringPath" required="true" type="java.lang.String" rtexprvalue="true"%>
<%@ attribute name="maxLength" required="true" type="java.lang.Integer" rtexprvalue="true"%>
<%@ attribute name="showReadMore" required="false" type="java.lang.Boolean" rtexprvalue="true"%>
<%@ attribute name="showDots" required="false" type="java.lang.Boolean" rtexprvalue="true"%>
<%@ attribute name="allowedLengthTolerance" required="false" type="java.lang.Integer" rtexprvalue="true"%>

<jsp:useBean id="StringChopper" class="com.tdclighthouse.commons.utils.beans.StringChopperBean" scope="page" />
<jsp:setProperty name="StringChopper" property="targetString" value="${bean[stringPath]}" />
<jsp:setProperty name="StringChopper" property="maxLength" value="${maxLength}" />

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