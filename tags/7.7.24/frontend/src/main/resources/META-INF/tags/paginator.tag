<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="paginator" type="com.tdclighthouse.prototype.utils.PaginatorWidget" rtexprvalue="true" required="true" %>
<%@ attribute name="pageParamerter" type="java.lang.String" rtexprvalue="true" required="false" %>
<%@ attribute name="rowsPerPageParameter" type="java.lang.String" rtexprvalue="true" required="false" %>
<c:if test="${empty pageParamerter}">
  <c:set var="pageParamerter" value="page"/>
</c:if>
<c:if test="${paginator.firstShownPage < paginator.lastShownPage}">
<div class="pager">
  <ul>
  <c:choose>
    <c:when test="${1 < paginator.page}">
      <c:set var="url">
        <tdc:add-query-param key="${pageParamerter}" value="${1}"/>
      </c:set>
      <li class="pager-first first"><a href="${url}" title="Ga naar eerste pagina">&laquo; <fmt:message key="paginator.first" /></a></li>
    </c:when>
    <c:otherwise>
      <li class="pager-first first"><span>&laquo; <fmt:message key="paginator.first" /></span></li>
    </c:otherwise>
  </c:choose>
  <c:choose>
    <c:when test="${paginator.page > 1}">
      <c:set var="url">
        <tdc:add-query-param key="${pageParamerter}" value="${paginator.page - 1}"/>
      </c:set>
      <li class="pager-previous"><a href="${url}" title="Ga naar vorige pagina">&lsaquo; <fmt:message key="paginator.previous" /></a></li>
    </c:when>
    <c:otherwise>
      <li class="pager-previous"><span>&lsaquo; vorige</span></li>
    </c:otherwise>
  </c:choose>
  <c:forEach begin="${paginator.firstShownPage}" end="${paginator.lastShownPage}" var="index">
    <c:choose>
      <c:when test="${index == paginator.page}">
        <li><span>${index}</span></li>
      </c:when>
      <c:otherwise>
        <c:set var="url">
          <tdc:add-query-param key="${pageParamerter}" value="${index}"/>
        </c:set>
        <li><a href="${url}">${index}</a></li>
      </c:otherwise>
    </c:choose>
  </c:forEach>
  
  <c:choose>
    <c:when test="${paginator.numberOfPages > paginator.page}">
      <c:set var="url">
        <tdc:add-query-param key="${pageParamerter}" value="${paginator.page + 1}"/>
      </c:set>
      <li class="pager-next"><a href="${url}" title="Volgende"><fmt:message key="paginator.next" /> &rsaquo;</a></li>
    </c:when>
    <c:otherwise>
      <li class="pager-next"><span>volgende &rsaquo;</span></li>
    </c:otherwise>
  </c:choose>
  <c:choose>
    <c:when test="${paginator.numberOfPages != paginator.page}">
      <c:set var="url">
        <tdc:add-query-param key="${pageParamerter}" value="${paginator.numberOfPages}"/>
      </c:set>
      <li class="pager-last last"><a href="${url}" title="Ga naar laatste pagina"><fmt:message key="paginator.last" /> &raquo;</a></li>
    </c:when>
    <c:otherwise>
      <li class="pager-last last"><span><fmt:message key="paginator.last" /> &raquo;</span></li>
    </c:otherwise>
  </c:choose>
  </ul>
</div>
</c:if>