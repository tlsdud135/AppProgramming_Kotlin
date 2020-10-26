<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%request.setCharacterEncoding("UTF-8"); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="member" class="mysns.member.Member" />
<jsp:setProperty name="member" property="*" />
<jsp:useBean id="mdao" class="mysns.member.MemberDAO" />
<%
	String action = request.getParameter("action");

	if(action.equals("new")) {
		if(mdao.addMember(member))
			out.println("<script>alert('등록 완료');opener.window.location.reload();window.close();</script>");
		else
			out.println("<script>alert('동일한 아이디 존재');history.go(-1);</script>");
		session.setAttribute("uid",member.getUid());
	}

	else if(action.equals("login")) {
		if(mdao.login(member.getUid(),member.getPasswd())) {
			session.setAttribute("uid",member.getUid());
			response.sendRedirect("sns_control.jsp?action=getall");
		}
		else {
			out.println("<script>alert('아이디 또는 비밀번호 오류!');history.go(-1);</script>");
		}
	} 
	else if(action.equals("out")) {
		if(mdao.out(member.getUid(),member.getPasswd())) {
			session.removeAttribute("uid");
			out.println("<script>alert('탈퇴 완료');opener.window.location.reload();window.close();</script>");
		}
		else {
			out.println("<script>alert('비밀번호 오류!');history.go(-1);</script>");
		}
	} 
	else if(action.equals("logout")) {
		session.removeAttribute("uid");
		response.sendRedirect("sns_control.jsp?action=getall");		
	}
%>