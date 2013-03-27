<%@tag description="Set URL Query" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="item" required="true" type="java.lang.String" rtexprvalue="true"%>
<jsp:useBean id="queryBean" class="com.tdclighthouse.commons.utils.beans.QueryBean" scope="page" />
<jsp:setProperty name="queryBean" property="request" value="${pageContext.request}" />
<% queryBean.remove(item);%>
${queryBean.query}