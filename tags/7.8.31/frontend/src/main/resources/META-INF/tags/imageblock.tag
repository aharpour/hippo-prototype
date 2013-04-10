<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.ImageBean" %>

<img src="<hst:link hippobean="${content.link.deref.articleImage}" />" alt="<c:out value="${content.alt }" escapeXml="true" />" title="<c:out value="${content.title }" escapeXml="true" />" />
<c:if test="${not empty content.caption }">
	<p class="note">
		<c:out value="${content.caption }" escapeXml="true" />
		<c:if test="${not empty content.credit }">
			<span class="source"><c:out value="${content.credit }" escapeXml="true" /></span>
		</c:if>
	</p>
</c:if>