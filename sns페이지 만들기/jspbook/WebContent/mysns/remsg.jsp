<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, mysns.sns.*, mysns.member.*" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="msg" class="mysns.sns.Message"/>
<jsp:setProperty name="msg" property="*" />
<%@ taglib tagdir="/WEB-INF/tags" prefix="sns"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="re.css" type="text/css" media="screen"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>수정</title>
</head>
<body>
<div>
<h2>수정</h2>
</div>

<form method="post" action="sns_control.jsp?action=remsg&mid=${msg.mid}">
	<sns:write type="remsg"/>
	<button type="submit">수정</button>
</form>
</body>
</html>

