<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="asset" rtexprvalue="true" required="true"
	type="org.hippoecm.hst.content.beans.standard.HippoResource"%>

<c:choose>
	<c:when test="${asset.lengthMB > 1}">
		<fmt:formatNumber maxFractionDigits="2" value="${asset.lengthMB}"/> MB
	</c:when>
	<c:otherwise>
		<fmt:formatNumber maxFractionDigits="0" value="${asset.lengthKB}"/> KB
	</c:otherwise>
</c:choose>