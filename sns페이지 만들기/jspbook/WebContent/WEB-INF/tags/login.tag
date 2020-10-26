<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form name="loginform" method="post" action="user_control.jsp">
<c:choose>
<c:when test="${uid != null}">
	<li><a href="sns_control.jsp?action=getall&suid=${uid}">${uid}님 글 모아보기</a></li>
	<input type="hidden" name="action" value="logout">
	<li><input type="submit" value="로그아웃"></li>
</c:when>
<c:otherwise>
		<li>Login
			<input type="hidden" name="action" value="login">
		&nbsp;<input type="text" name="uid" size="10">
		 <input type="password" name="passwd" size="10">
		 <input type="submit" value="로그인">
		</li>
</c:otherwise>
</c:choose>
</form>