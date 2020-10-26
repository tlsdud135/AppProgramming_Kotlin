<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, mysns.member.*" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="member" class="mysns.member.Member" />
<jsp:setProperty name="member" property="*" />
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="re.css" type="text/css" media="screen"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>탈퇴</title>
</head>
<body>
<div class="join">
<h2 >회원 탈퇴</h2>
</div>
<form class="j" method="post" action="user_control.jsp?action=out&uid=${member.uid}">
비밀번호 입력<br>
<input type="password" name="passwd" size="10" required> 
<input type="submit" value="탈퇴">
</form>
</body>
</html>