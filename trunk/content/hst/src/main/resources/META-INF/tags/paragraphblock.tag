<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.ParagraphBean" %>

<c:set var="horizontalPosition" value="left" />
<c:if test="${content.image.horizontalPosition == 'right' }">
  <c:set var="horizontalPosition" value="right" />
</c:if>
<c:if test="${not empty content.title }">
  <h3><c:out value="${content.title }" escapeXml="true" /> </h3>
</c:if>
<c:choose>
  <c:when test="${not empty content.image.link.deref }">
    <div class="image">
      <p class="image ${horizontalPosition }">
        <img src="<hst:link hippobean="${content.image.link.deref.paragraphImage }" />" alt="<c:out value="${content.image.alt }" escapeXml="true" />" title="<c:out value="${content.image.alt }" escapeXml="true" />" />
        <c:if test="${not empty content.image.caption }">
          <span><c:out value="${content.image.caption }" escapeXml="true" /></span>
        </c:if>
      </p>
      <c:if test="${not empty content.content }">
        <hst:html hippohtml="${content.content }" />
      </c:if>
    </div>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty content.content }">
      <hst:html hippohtml="${content.content }" />
    </c:if>
  </c:otherwise>
</c:choose>