<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="k" uri="/META-INF/kenes.tld"%>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype"%>
<%@ taglib prefix="k" uri="/META-INF/kenes.tld"%>
<%@ attribute name="parentItem" type="org.hippoecm.hst.core.sitemenu.EditableMenuItem" rtexprvalue="true" required="true"%>
<%@ attribute name="lastItemClass" type="java.lang.String" rtexprvalue="true" required="false"%>
<c:if test="${empty lastItemClass }">
	<c:set var="lastItemClass" value="last"/>
</c:if>
<c:set var="hasExpandedItem" value="${false}"/>
<c:forEach items="${parentItem.childMenuItems}" var="item">
<c:choose>
<c:when test="${item.selected}">
	<li class="${lastItemClass}"><c:out value="${item.name}"/></li>
	<c:set var="hasExpandedItem" value="${true}"/>
</c:when>
<c:when test="${item.expanded}">
	<li><a href="<hst:link link="${item.hstLink}"/>"><c:out value="${item.name}"/></a></li>
	<tdc:breadcrumbitem parentItem="${item}"/>
	<c:set var="hasExpandedItem" value="${true}"/>
</c:when>
</c:choose>
</c:forEach>
<c:if test="${not hasExpandedItem  and not empty document}">
	<c:catch>	
		<li class="${lastItemClass}"><c:out value="${document.localizedName}"/></li>
	</c:catch>
</c:if>