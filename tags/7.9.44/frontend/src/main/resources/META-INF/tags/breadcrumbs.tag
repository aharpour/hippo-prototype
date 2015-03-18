<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tag" uri="http://open-web.nl/hippo/prototype"%>
<%@ attribute name="menu" type="org.hippoecm.hst.core.sitemenu.AbstractMenu" rtexprvalue="true" required="true"%>
<%@ attribute name="lastItemClass" type="java.lang.String" rtexprvalue="true" required="false"%>
<c:if test="${empty lastItemClass }">
	<c:set var="lastItemClass" value="last"/>
</c:if>
<ul>
<c:forEach items="${menu.menuItems}" var="item">
<c:choose>
<c:when test="${item.selected}">
	<li class="${lastItemClass}"><a	href="<hst:link link="${item.hstLink}"/>"><c:out value="${item.name}" /></a></li>
</c:when>
<c:when test="${item.expanded}">
	<li><a href="<hst:link link="${item.hstLink}"/>"><c:out	value="${item.name}" /></a></li>
	<tag:breadcrumbitem parentItem="${item}" lastItemClass="${lastItemClass} " />
</c:when>
</c:choose>
</c:forEach>
</ul>