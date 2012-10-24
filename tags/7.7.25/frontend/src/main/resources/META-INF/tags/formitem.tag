<%@tag description="Renders a com.tdclighthouse.commons.simpleform.html.FormItemGroup object" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>

<%@ attribute name="formItem" type="com.tdclighthouse.commons.simpleform.html.FormItem" rtexprvalue="true" required="true" %>
<%@ attribute name="fieldClass" type="java.lang.String" rtexprvalue="true" required="false" %>

<c:choose>
	<c:when test="${formItem.type eq 'TEXT' and formItem.disabled}">					
		<input type="text" name="${formItem.name}" id="${formItem.name}" value="${formItem.value}" ${formItem.disabled == true ? 'disabled="disabled"' : '' } class="${fieldClass }" />
	</c:when>
	<c:when test="${formItem.type eq 'TEXT'}">
		<input type="text" name="${formItem.name}" id="${formItem.name}" value="${formItem.value}" class="${formItem.dataType == 'DATEFIELD'? 'pickdate': ''} textfield validate-email ${fieldClass }" />
	</c:when>
	<c:when test="${formItem.type eq 'TEXTFIELD'}">
		<textarea name="${formItem.name}" id="${formItem.name}" cols="20" rows="10" class="textarea ${fieldClass }" ${formItem.disabled == true ? 'disabled="disabled"' : '' }>${formItem.value}</textarea>
	</c:when>
	<c:when test="${formItem.type eq 'HIDDEN'}">
		<input type="hidden" name="${formItem.name}" id="${formItem.name}" value="${formItem.value}" />
	</c:when>
	<c:when test="${formItem.type eq 'SELECT'}">
		<c:if test="${formItem.value != NULL}">
			<input type="hidden" name="${formItem.name}_selected" id="${formItem.name}_selected" value="${formItem.value}" />
		</c:if>
    	<select rel="${formItem.value}" id="${formItem.name}" name="${formItem.name}"${formItem.disabled == true ? 'disabled="disabled"' : '' } class="${fieldClass }">
	   		<c:forEach items="${formItem.options}" var="option">
    			<option ${formItem.value == option.value ? 'selected="selected"' : ''} value="${option.value}" ${formItem.disabled == true ? 'disabled="disabled"' : '' }>${option.text}</option>
    		</c:forEach>
    	</select>
    </c:when>
</c:choose>

