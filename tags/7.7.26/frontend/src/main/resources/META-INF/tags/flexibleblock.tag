<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" type="java.util.List" rtexprvalue="true" required="true" %>
<%@ attribute name="flexibleblockid" type="java.lang.String" rtexprvalue="true" required="false" %>

<c:set var="tagcount" value="0" />

<c:forEach var="block" items="${content }">
	<c:choose>
		<c:when test="${block.class.simpleName == 'LatexBean' }">
			<tdc:latexblock content="${block }"/>
		</c:when>
		<c:when test="${block.class.simpleName == 'QuotationBean' }">
			<tdc:quotationblock content="${block }"/>
		</c:when>
		<c:when test="${block.class.simpleName == 'ParagraphBean' }">
			<tdc:paragraphblock content="${block }"/>
		</c:when>
		<c:when test="${block.class.simpleName == 'ImageBean' }">
			<tdc:imageblock content="${block }"/>
		</c:when>
		<c:when test="${block.class.simpleName == 'AudioBean' }">
			<tdc:audioblock content="${block }" count="${tagcount }" flexibleblockid="${flexibleblockid }" />
		</c:when>
		<c:when test="${block.class.simpleName == 'VideoBean' }">
			<tdc:videoblock content="${block }" count="${tagcount }" flexibleblockid="${flexibleblockid }" />
		</c:when>
		<c:when test="${block.class.simpleName == 'RelatedDocumentsBean' }">
			<tdc:relateddocumentsblock content="${block }"/>
		</c:when>
	</c:choose>
	<c:set var="tagcount" value="${tagcount + 1 }" />
</c:forEach>