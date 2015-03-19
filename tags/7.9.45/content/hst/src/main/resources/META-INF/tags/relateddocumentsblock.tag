<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.RelatedDocumentsBean" %>

<c:if test="${not empty content.title }">
	<h3><c:out value="${content.title }" escapeXml="true" /> </h3>
</c:if>
<ul class="link-list">
	<c:forEach var="related" items="${content.relateddocumentitem }">
		<li><tdc:relateddocument content="${related }" /></li>
	</c:forEach>
</ul>