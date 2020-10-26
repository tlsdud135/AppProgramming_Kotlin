<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ attribute name="mid" %>
<%@ attribute name="auid" %>
<%@ attribute name="curmsg" %>

<script>
	function remsg(URL) {
		window.open(
			URL,
			"수정",
			"titlebar=no,location=no,scrollbars=no,resizeable=no,menubar=no,toolbar=no,width=515,height=150");
	}
</script>

<c:if test="${uid == auid}">
[<a href="sns_control.jsp?action=delmsg&mid=${mid}&curmsg=${curmsg}&cnt=${cnt}&suid=${suid}">삭제</a>] 
[<a href="javascript:remsg('remsg.jsp?&mid=${mid}')">수정</a>] 

</c:if>
[<a href="sns_control.jsp?action=fav&mid=${mid}&curmsg=${curmsg}&cnt=${cnt}&suid=${suid}">좋아요</a>]