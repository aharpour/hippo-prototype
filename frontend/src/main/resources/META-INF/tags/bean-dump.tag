<%@tag import="com.tdclighthouse.commons.utils.objectdump.ObjectDump" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="object" required="true" type="java.lang.Object" rtexprvalue="true" %>
<%@ attribute name="name" required="true" type="java.lang.String" rtexprvalue="true" %>
<%@ attribute name="depth" required="false" type="java.lang.Integer" rtexprvalue="true" %>
<% 
if (depth == null || depth == 0) {
	depth = 5;		
}
%>
<link rel="stylesheet" href="http://jqueryui.com/themes/base/jquery.ui.all.css">

<script type="text/javascript" src="http://www.google.com/jsapi"></script> 
<script type="text/javascript">
	if (typeof jQuery == 'undefined') {
		//Use Google's CDN
		google.load("jquery", "1.6.2");
	}
</script>
 
<script src="http://jqueryui.com/external/jquery.bgiframe-2.1.2.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.core.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.widget.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.mouse.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.draggable.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.position.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.resizable.js"></script> 
<script src="http://jqueryui.com/ui/jquery.ui.dialog.js" type="text/javascript"></script>

<script type="text/javascript">
<!--
	var json = <%=ObjectDump.dump(object, depth.intValue())%>;
	console.log(json);
	
//-->
</script>
<div id="dialog" title="Basic dialog">
	<%=ObjectDump.PrintJSONObject(ObjectDump.dump(object, depth.intValue()), name)%>
</div>
