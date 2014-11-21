<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.VideoBean" %>
<%@ attribute name="count" rtexprvalue="true" required="true" type="java.lang.Integer" %>
<%@ attribute name="flexibleblockid" type="java.lang.String" rtexprvalue="true" required="false" %>

<hst:setBundle basename="nl.openweb.prototype.Messages"/>

<hst:headContribution keyHint="generic.swfobject.js">
	<script type="text/javascript" src="<hst:link path="/js/swfobject.js" />"></script>
</hst:headContribution>

<hst:link var="pathToPlayer" path="/swf/flvplayer.swf"/>
<c:if test="${not empty content.threeGp.binary.deref}">
	<hst:link var="threeGpPath" hippobean="${content.threeGp.binary }" />
	<c:set var="videoPath" value="${threeGpPath}"/>
</c:if>
<c:if test="${not empty content.wmv.binary.deref}">
	<hst:link var="wmvPath" hippobean="${content.wmv.binary }" />
	<c:set var="videoPath" value="${wmvPath}"/>
</c:if>
<c:if test="${not empty content.flv.binary.deref}">
	<hst:link var="flvPath" hippobean="${content.flv.binary }" />
	<c:set var="videoPath" value="${flvPath}"/>
</c:if>
<c:if test="${not empty content.mp4.binary.deref}">
	<hst:link var="mp4Path" hippobean="${content.mp4.binary }" />
	<c:set var="videoPath" value="${mp4Path}"/>
</c:if>
<c:if test="${not empty content.srt.binary.deref}">
	<hst:link var="srtPath" hippobean="${content.srt.binary }" />
</c:if>
<c:if test="${not empty content.image.image.deref}">
	<hst:link var="imagePath" hippobean="${content.image.image }" />
</c:if>
<c:if test="${not empty content.audio.binary.deref}">
	<hst:link var="audioPath" hippobean="${content.audio.binary }" />
</c:if>
<div class="video block clearfix">
	<c:if test="${not empty content.title }">
		<h3><c:out value="${content.title }" escapeXml="true" /></h3>
	</c:if>
	<div class="mediaspace">
		<div id='mediaspace-${flexibleblockid }${count }'>
      		<hst:html hippohtml="${content.transcript }" />
		</div>
	</div>
	<script type='text/javascript'>
		var s${flexibleblockid }${count } = new SWFObject("${pathToPlayer}","single","584","300","7");
		s${flexibleblockid }${count }.addParam("allowfullscreen","true");
		s${flexibleblockid }${count }.addVariable("file","${videoPath }");
		<c:if test="${not empty imagePath}">
			s${flexibleblockid }${count }.addVariable("image","${imagePath }");
		</c:if>
		s${flexibleblockid }${count }.addVariable("width","584");
		s${flexibleblockid }${count }.addVariable("height","300");
		<c:if test="${not empty audioPath }">
			s${flexibleblockid }${count }.addVariable("audio","${audioPath }");
		</c:if>
		<c:if test="${not empty srtPath }">
		s${flexibleblockid }${count }.addVariable("captions","${srtPath }");
		</c:if>
		s${flexibleblockid }${count }.write("mediaspace-${flexibleblockid }${count }"); 
	</script>
	<div class="note">
		<c:out value="${content.caption }" escapeXml="true" />
	</div>
	<c:if test="${not empty threeGpPath or not empty wmvPath or not empty mp4Path or not empty srtPath or not empty audioPath}">
		<p><fmt:message key="media.alternative.text"/></p>
		<ul class="link-list">
			<c:if test="${not empty threeGpPath }">
				<li><a href="${threeGpPath }" rel="external"><span><fmt:message key="media.alternative.3gp"/></span></a></li>
			</c:if>
			<c:if test="${not empty wmvPath }">
				<li><a href="${wmvPath }" rel="external"><span><fmt:message key="media.alternative.wmv"/></span></a></li>
			</c:if>
			<c:if test="${not empty mp4Path }">
				<li><a href="${mp4Path }" rel="external"><span><fmt:message key="media.alternative.mp4"/></span></a></li>
			</c:if>
			<c:if test="${not empty srtPath}">
				<li><a href="${srtPath }" rel="external"><span><fmt:message key="media.alternative.srt"/></span></a></li>
			</c:if>
			<c:if test="${not empty audioPath}">
				<li><a href="${audioPath }" rel="external"><span><fmt:message key="media.alternative.mp3"/></span></a></li>
			</c:if>
		</ul>
	</c:if>
</div>