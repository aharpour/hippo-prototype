<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>
<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.LatexBean" %>
<c:if test="${not empty content.latex }" >
  <hst:link path="/latex" var="contextPath"/>
  <img src="${contextPath}/?latex=${content.encodeLatex}" alt="${content.latex}" title="${content.latex}"/>
  <c:if test="${not empty content.caption }">
    <p class="note"><c:out value="${content.caption }" escapeXml="true" /></p>
  </c:if>
</c:if>