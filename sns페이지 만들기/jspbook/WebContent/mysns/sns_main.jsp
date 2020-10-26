<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*, mysns.sns.*, mysns.member.*"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sns"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="main.css" type="text/css" media="screen"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>메인화면</title>
<script>
	function newuser() {
		window.open(
			"new_user.jsp",
			"회원가입",
			"titlebar=no,location=no,scrollbars=no,resizeable=no,menubar=no,toolbar=no,width=270,height=200");
	}
	function deluser(URL) {
		window.open(
			URL,
			"회원탈퇴",
			"titlebar=no,location=no,scrollbars=no,resizeable=no,menubar=no,toolbar=no,width=270,height=145");
	}
</script>
</head>
<body>
<header>

<h1>Flow-my sns</h1>

</header>
<nav>
<div>
<ul>
<li><a href="index.jsp">Home</a></li>
<c:if test="${uid==null }">
<li><a href="javascript:newuser()">New User</a></li>
</c:if>
<c:if test="${uid!=null }">
<li><a href="javascript:deluser('out.jsp?uid=${uid}')">회원 탈퇴</a></li>
</c:if>
<li><a href="sns_control.jsp?action=getall">전체글보기</a></li>
<li><sns:login /></li>

</ul>
</div>
</nav>
<section>
<h2>내 소식</h2>
<form method="post" action="sns_control.jsp?action=newmsg">
	<input type="hidden" name="uid" value="${uid}">
	<sns:write type="msg"/>
	<button type="submit">등록</button>
</form>
<h2>hot 게시판</h2>
<div>
	<c:forEach varStatus="mcnt" var="msgs" items="${datas}">
	<c:set value="30" var="favline" />
	<c:set value="20" var="msgline" />
	<c:if test="${(msgs.message.favcount>=favline)||(msgs.message.replycount>=msgline)}">
	<details>
		<c:set var="m" value="${msgs.message}"/>
		<summary>[${m.uid}]${m.msg}  <br>[좋아요 ${m.favcount} | 댓글 ${m.replycount}]
		
			
			 _ <sns:smenu mid="${m.mid}" auid="${m.uid}" curmsg="${mcnt.index}"/> _ 작성일: ${m.date}
			</summary>
			<div class="reply">
			<br>
			<ul >
				<c:forEach  var="r" items="${msgs.rlist}">
					<li class="re">[${r.uid }] ${r.rmsg} | ${r.date} <sns:rmenu curmsg="${mcnt.index}" rid="${r.rid}" ruid="${r.uid}"/></li>
				</c:forEach>
			</ul>
						
			<form action="sns_control.jsp?action=newreply&cnt=${cnt}" method="post">
				<input type="hidden" name="mid" value="${m.mid}">
				<input type="hidden" name="uid" value="${uid}">
				<input type="hidden" name="suid" value="${suid}">
				<input type="hidden" name="curmsg" value="${mcnt.index}">
				<br/>				
				<sns:write type="rmsg"/>
				<button type="submit">등록</button>
			</form>
			<br>
		</div>
		
	</details>
	<br/>
</c:if>
	</c:forEach>
</div>		
<h2>새 소식</h2>
<div>
	<c:forEach varStatus="mcnt" var="msgs" items="${datas}">
	<details>
		<c:set var="m" value="${msgs.message}"/>
		<summary>[${m.uid}]${m.msg}  <br>[좋아요 ${m.favcount} | 댓글 ${m.replycount}]	

			
			 _ <sns:smenu mid="${m.mid}" auid="${m.uid}" curmsg="${mcnt.index}"/> _ 작성일: ${m.date}
			</summary>	
			<div class="reply">
			<br>
			<ul >
				<c:forEach  var="r" items="${msgs.rlist}">
					<li class="re">[${r.uid }] ${r.rmsg} | ${r.date} <sns:rmenu curmsg="${mcnt.index}" rid="${r.rid}" ruid="${r.uid}"/></li>
				</c:forEach>
			</ul>
						
			<form action="sns_control.jsp?action=newreply&cnt=${cnt}" method="post">
				<input type="hidden" name="mid" value="${m.mid}">
				<input type="hidden" name="uid" value="${uid}">
				<input type="hidden" name="suid" value="${suid}">
				<input type="hidden" name="curmsg" value="${mcnt.index}">
				<br/>				
				<sns:write type="rmsg"/>
				<button type="submit">등록</button>
				
			</form>
			<br>
		</div>
		
	</details>
	<br/>
	</c:forEach>
</div>		
<div><a href="sns_control.jsp?action=getall&cnt=${cnt+5}&suid=${suid}">더보기&gt;&gt;</a></div>
<br>
</section>
<aside>
<h2>회원 목록</h2>
<c:forEach items="${nusers}" var="n">
	<ul>
		<li><a href="sns_control.jsp?action=getall&suid=${n}">${n}</a></li>
	</ul>
</c:forEach>
</aside>
<footer>
<ul>
	<li id="left"><a href="https://www.duksung.ac.kr/">덕성여자대학교</a></li>
	<li>20190971_김신영</li>
	<li id="right">2020.07.03.</li>
</ul>
</footer>
</body>
</html>