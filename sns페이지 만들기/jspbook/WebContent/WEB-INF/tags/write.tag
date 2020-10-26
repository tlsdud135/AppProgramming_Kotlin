<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ attribute name="type" %>

<c:if test="${uid != null}">
<c:choose> 
	<c:when test="${type == 'msg'}"><input  type="text" name="msg" maxlength="100" size="50"></c:when>
	<c:when test="${type == 'rmsg'}">댓글달기 <input  type="text" name="rmsg" maxlength="50" size="60"></c:when>
	<c:when test="${type == 'remsg'}"><input  type="text" name="remsg" maxlength="100"></c:when>
	<c:when test="${type == 'rerply'}"><input  type="text" name="rerply" maxlength="100"></c:when>
</c:choose>
</c:if>

<c:if test="${uid == null}">
<c:choose> 
	<c:when test="${type == 'msg'}"><input type="text" name="msg" maxlength="100" size="50" disabled="disabled"  value="로그인 필요!!"></c:when>
	<c:when test="${type == 'rmsg'}">댓글달기 <input type="text" name="rmsg" maxlength="50" size="60" disabled="disabled" value="로그인 필요!!"></c:when>
</c:choose>
</c:if>