<%@tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="tdc" uri="http://www.tdclighthouse.com/hippo/prototype" %>

<%@ attribute name="content" rtexprvalue="true" required="true" type="com.tdclighthouse.prototype.beans.compounds.AudioBean" %>
<%@ attribute name="count" rtexprvalue="true" required="true" type="java.lang.Integer" %>
<%@ attribute name="flexibleblockid" type="java.lang.String" rtexprvalue="true" required="false" %>

<hst:link var="pathToPlayer" path="/themes/mediaplayer/flvplayer.swf"/>
<hst:link var="pathToMp3" hippobean="${content.audio.binary.deref }" />

<div class="audio block mediaspace">
	<c:if test="${not empty content.title }">
		<h3><c:out value="${content.title }" escapeXml="true" /></h3>
	</c:if>
	<div class="mediaspace">
		<div id='mediaspace-${flexibleblockid }${count }'><hst:html hippohtml="${content.transcript }" /></div>
	</div>
	
	<script type='text/javascript'>
		var s${flexibleblockid }${count } = new SWFObject("${pathToPlayer}","single","430","20","7");
		s${flexibleblockid }${count }.addParam("allowfullscreen","false");
		s${flexibleblockid }${count }.addVariable("file","${pathToMp3 }");
		s${flexibleblockid }${count }.addVariable("width","430");
		s${flexibleblockid }${count }.addVariable("height","20");
		s${flexibleblockid }${count }.write("mediaspace-${flexibleblockid }${count }"); 
	</script>
	<div class="note">
		<c:out value="${content.caption }" escapeXml="true" />
	</div>
</div>