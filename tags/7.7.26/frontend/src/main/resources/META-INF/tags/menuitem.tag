<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype"%>
<%@ attribute name="siteMenuItem" type="org.hippoecm.hst.core.sitemenu.EditableMenuItem" rtexprvalue="true" required="true"%>

<%@ attribute name="depth" type="java.lang.Integer" rtexprvalue="true" required="false" %>
<%@ attribute name="selectedClass" type="java.lang.String" rtexprvalue="true" required="false"%>
<%@ attribute name="expandedClass" type="java.lang.String" rtexprvalue="true" required="false"%>




<c:if test="${empty depth }">
	<c:set var="depth" value="1"/>
</c:if>
<c:if test="${empty selectedClass }">
	<c:set var="selectedClass" value="selected"/>
</c:if>
<c:if test="${empty expandedClass }">
	<c:set var="expandedClass" value="expanded"/>
</c:if>

<c:choose>
  <c:when test="${siteMenuItem.selected}">
    <li class="${selectedClass}"><a href="<hst:link link="${siteMenuItem.hstLink}"/>"><c:out value="${siteMenuItem.name}"/></a>
  </c:when>
  <c:when test="${siteMenuItem.expanded}">
    <li class="${expandedClass}"><a href="<hst:link link="${siteMenuItem.hstLink}"/>"><c:out value="${siteMenuItem.name}"/></a>
  </c:when>
  <c:otherwise>
  	<c:choose>
  		<c:when test="${not empty siteMenuItem.hstLink }">
  			<hst:link var="link" link="${siteMenuItem.hstLink}"/>
  		</c:when>
  		<c:otherwise>
  			<c:set var="link" value="${siteMenuItem.externalLink}"/>
  		</c:otherwise>
  	</c:choose>
    <li><a href="${link}"><c:out value="${siteMenuItem.name}"/></a>
  </c:otherwise>
</c:choose>
<c:set var="depth" value="${depth - 1 }" />
<c:if test="${not empty siteMenuItem.childMenuItems and depth >= 0}">
		<ul>
		<c:forEach var="child" items="${siteMenuItem.childMenuItems}">
			<tdc:menuitem siteMenuItem="${child}" depth="${depth}" selectedClass="${selectedClass}" expandedClass="${expandedClass}"/>
		</c:forEach>
		</ul>
</c:if>
</li>