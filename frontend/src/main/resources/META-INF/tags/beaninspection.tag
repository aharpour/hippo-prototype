<%@tag import="com.tdclighthouse.prototype.tag.BeanInspectionTagSupport" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="bean" rtexprvalue="true" required="true" type="java.lang.Object" %>
<%@ attribute name="depth" rtexprvalue="true" required="false" type="java.lang.Integer" %>
<%@ attribute name="applyBlackList" rtexprvalue="true" required="false" type="java.lang.Boolean" %>
<c:choose>
<c:when test="${not empty bean}">
<h4>Inspection of bean of type ${bean.class.name}</h4>
<pre>
<%
BeanInspectionTagSupport.inspectProperties(bean, out, applyBlackList, depth);
%>
</pre>
</c:when>
<c:otherwise>
<h4>the given bean attribute has null value.</h4>
</c:otherwise>
</c:choose>