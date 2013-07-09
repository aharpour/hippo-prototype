<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.QuotationBean" %>

<c:if test="${not empty content.quote }">
	<blockquote class="quoted">
		<div class="content">
			<span class="top"></span>
			<span class="bottom"></span>
			<c:out value="${content.quote }" escapeXml="true" />
		</div>
		<c:if test="${not empty content.author }">
			<div>
				<span class="source">- <c:out value="${content.author }" escapeXml="true" /></span>
			</div>
		</c:if>
	</blockquote>
</c:if>