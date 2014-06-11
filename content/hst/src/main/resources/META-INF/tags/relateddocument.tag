<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.RelatedDocumentItemBean" %>
<%@ attribute name="className" type="java.lang.String" rtexprvalue="true" required="false" %>

<c:set var="relationship" value="" />
<c:choose>
  <c:when test="${not empty content.externalLink.url }">
    <c:set var="link" value="${content.externalLink.url }" />
    <c:set var="relationship">rel="${content.externalLink.relationship }"</c:set>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty content.internalLink.deref }">
      <hst:link var="link" hippobean="${content.internalLink.deref }" />
    </c:if>
  </c:otherwise>
</c:choose>
<c:set var="classAttribute" value="" />
<c:if test="${not empty className }">
  <c:set var="classAttribute"> class="${className }"</c:set>
</c:if>
<a href="${link }" ${relationship } title="<c:out value="${content.label }" escapeXml="true" />" ${classAttribute }><c:out value="${content.label }" escapeXml="true" /></a>