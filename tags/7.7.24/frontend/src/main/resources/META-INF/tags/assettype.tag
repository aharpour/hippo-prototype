<%@tag trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="asset" rtexprvalue="true" required="true"
	type="org.hippoecm.hst.content.beans.standard.HippoResource"%>

<c:choose>
	<c:when test="${asset.mimeType eq ''}"><fmt:message key="file.type."/></c:when>
	<c:when test="${asset.mimeType eq 'application/pdf' or asset.mimeType eq 'application/x-pdf' or asset.mimeType eq 'application/acrobat' or asset.mimeType eq 'applications/vnd.pdf' or asset.mimeType eq 'text/x-pdf' or asset.mimeType eq 'text/pdf'}"><fmt:message key="file.type.pdf"/></c:when>
	<c:when test="${asset.mimeType eq 'application/msword'}"><fmt:message key="file.type.doc"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'}"><fmt:message key="file.type.docx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.wordprocessingml.template'}"><fmt:message key="file.type.dotx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-word.document.macroEnabled.12'}"><fmt:message key="file.type.docm"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-word.template.macroEnabled.12'}"><fmt:message key="file.type.dotm"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-excel'}"><fmt:message key="file.type.xls"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}"><fmt:message key="file.type.xlsx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.spreadsheetml.template'}"><fmt:message key="file.type.xltx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-excel.sheet.macroEnabled.12'}"><fmt:message key="file.type.xlsm"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-excel.template.macroEnabled.12'}"><fmt:message key="file.type.xltm"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-excel.addin.macroEnabled.12'}"><fmt:message key="file.type.xlam"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-excel.sheet.binary.macroEnabled.12'}"><fmt:message key="file.type.xlsb"/></c:when>
	<c:when test="${asset.mimeType eq 'application/mspowerpoint'}"><fmt:message key="file.type.ppt"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-powerpoint'}"><fmt:message key="file.type.ppt"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.presentationml.presentation'}"><fmt:message key="file.type.pptx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.presentationml.template'}"><fmt:message key="file.type.potx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.openxmlformats-officedocument.presentationml.slideshow'}"><fmt:message key="file.type.ppsx"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-powerpoint.addin.macroEnabled.12'}"><fmt:message key="file.type.ppam"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-powerpoint.presentation.macroEnabled.12'}"><fmt:message key="file.type.pptm"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-powerpoint.template.macroEnabled.12'}"><fmt:message key="file.type.potm"/></c:when>
	<c:when test="${asset.mimeType eq 'application/vnd.ms-powerpoint.slideshow.macroEnabled.12'}"><fmt:message key="file.type.ppsm"/></c:when>
	<c:when test="${asset.mimeType eq 'image/jpeg'}"><fmt:message key="file.type.jpeg"/></c:when>
	<c:when test="${asset.mimeType eq 'image/png'}"><fmt:message key="file.type.png"/></c:when>
	<c:when test="${asset.mimeType eq 'image/gif'}"><fmt:message key="file.type.gif"/></c:when>
	<c:when test="${asset.mimeType eq 'application/x-zip-compressed'}"><fmt:message key="file.type.zip"/></c:when>
	<c:otherwise><fmt:message key="file.type.binary"/></c:otherwise>
</c:choose>