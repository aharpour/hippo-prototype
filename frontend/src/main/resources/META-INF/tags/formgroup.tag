<%@tag description="Renders a com.tdclighthouse.commons.simpleform.html.FormItemGroup object" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" uri="http://open-web.nl/hippo/prototype" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="group" type="com.tdclighthouse.commons.simpleform.html.FormItemGroup" rtexprvalue="true" required="true" %>
<c:if test="${not empty group}">
<fieldset class="form-group ${(not empty group.groupType ? group.groupType : '') }" id="${group.name}">
<c:if test="${not empty group.label}">
	<legend><fmt:message key="group.label"/> </legend>
</c:if>
<c:choose>
	<c:when test="${fn:length(group.subgroups) > 0}">
		<c:forEach items="group.subgroups" var="subgroup">
			<tag:formgroup group="subgroup"/>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:forEach items="${group.items }" var="formItem">
		<c:choose>
			<c:when test="${formItem.type eq 'HIDDEN'}">
				<input type="hidden" name="${formItem.name}" id="${formItem.name}" value="${formItem.value}" />
			</c:when>
			<c:otherwise>
      <c:set var="fieldClass">${not empty formItem.errorMessage ? 'error' : ''}</c:set>
			<div class="form-row">
				<label for="${formItem.name}"><fmt:message key="${formItem.label}"/>${formItem.mandatory? ' *' : ''}</label>
				<tag:formitem formItem="${formItem}" fieldClass="${fieldClass }"/>
        <c:if test="${not empty formItem.errorMessage}">
          <span class="error"><fmt:message key="${formItem.errorMessage}"/></span>
        </c:if>
			</div>
			</c:otherwise>
		</c:choose>
		</c:forEach>
	</c:otherwise>
</c:choose>
</fieldset>
</c:if>