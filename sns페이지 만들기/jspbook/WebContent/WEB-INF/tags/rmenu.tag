<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="rid" %>
<%@ attribute name="ruid" %>
<%@ attribute name="curmsg" %>

<script>
	function remsg(URL) {
		window.open(
			URL,
			"수정",
			"titlebar=no,location=no,scrollbars=no,resizeable=no,menubar=no,toolbar=no,width=515,height=150");
	}
</script>

<c:if test="${uid == ruid}">
[<a href="sns_control.jsp?action=delreply&rid=${rid}&curmsg=${curmsg}&cnt=${cnt}&suid=${suid}">삭제</a>] 
[<a href="javascript:remsg('rerply.jsp?&rid=${rid}')">수정</a>] 
</c:if>