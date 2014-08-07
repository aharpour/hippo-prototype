<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tag" uri="http://open-web.nl/hippo/prototype" %>

<%@ attribute name="paginator" type="com.tdclighthouse.prototype.utils.PaginatorWidget" rtexprvalue="true" required="true" %>
<%@ attribute name="pageParamerter" type="java.lang.String" rtexprvalue="true" required="false" %>
<%@ attribute name="sizeParamerter" type="java.lang.String" rtexprvalue="true" required="false" %>
<%@ attribute name="namespaced" type="java.lang.Boolean" rtexprvalue="true" required="false" %>

<hst:setBundle basename="nl.openweb.prototype.Messages"/>


<c:if test="${empty pageParamerter}">
  <c:set var="pageParamerter" value="page"/>
</c:if>
<c:if test="${empty sizeParamerter}">
  <c:set var="sizeParamerter" value="size"/>
</c:if>
<c:if test="${empty namespaced}">
  <c:set var="namespaced" value="${true}"/>
</c:if>
<c:if test="${paginator.numberOfPages > 1}">
<div class="pager">
  <ul>
  <fmt:message key="paginator.go.to.previous" var="goToPrevious" />
  <c:choose>
    <c:when test="${paginator.page > 1}">
      <c:choose>
        <c:when test="${namespaced}">
          <hst:renderURL var="url">
      	    <hst:param name="${pageParamerter}" value="${paginator.page - 1}"/>
      	    <hst:param name="${sizeParamerter}" value="${paginator.rowsPerPage}"/>
          </hst:renderURL>
        </c:when>
        <c:otherwise>
          <hst:link var="url" navigationStateful="true">
        	<hst:param name="${pageParamerter}" value="${paginator.page - 1}"/>
          </hst:link>
        </c:otherwise>
      </c:choose>
      <li class="pager-previous"><a href="${url}" title="${goToPrevious}">&lsaquo; <fmt:message key="paginator.previous" /></a></li>
    </c:when>
    <c:otherwise>
      <li class="pager-previous"><span>&lsaquo; <fmt:message key="paginator.previous" /></span></li>
    </c:otherwise>
  </c:choose>
  <fmt:message key="paginator.go.to.next" var="goToNext" />
  <c:choose>
    <c:when test="${paginator.numberOfPages > paginator.page}">
      <c:choose>
        <c:when test="${namespaced}">
          <hst:renderURL var="url">
            <hst:param name="${pageParamerter}" value="${paginator.page + 1}"/>
            <hst:param name="${sizeParamerter}" value="${paginator.rowsPerPage}"/>
          </hst:renderURL>
        </c:when>
        <c:otherwise>
          <hst:link var="url" navigationStateful="true">
          <hst:param name="${pageParamerter}" value="${paginator.page + 1}"/>
          </hst:link>
        </c:otherwise>
      </c:choose>
      <li class="pager-next"><a href="${url}" title="${goToNext}"><fmt:message key="paginator.next" /> &rsaquo;</a></li>
    </c:when>
    <c:otherwise>
      <li class="pager-next"><span><fmt:message key="paginator.next" /> &rsaquo;</span></li>
    </c:otherwise>
  </c:choose>
  <fmt:message key="paginator.go.to.last" var="goToLast" />
  </ul>
</div>
</c:if>