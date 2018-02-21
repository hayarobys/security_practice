<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="com.suph.security.core.userdetails.MemberInfo" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%	
	// 로그인 정보를 가져오기 위해 스프링 시큐리티에서 제공하는 방법
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Object principal = auth.getPrincipal();
	String name = "";
	if(principal != null && principal instanceof MemberInfo){
		name = ((MemberInfo)principal).getName();
		// 미인증 상태에선 "anonymousUser" 가 반환된다.
	}
	
	/* 
	// 로그인 정보를 가져오기 위해 Servlet Spec에서 제공하는 방법
	Authentication auth = (Authentication)request.getUserPrincipal();
	String name = "";
	if(auth == null){
		
	}else{
		Object principal = auth.getPrincipal();
		if(principal != null && principal instanceof MemberInfo){
			name = ((MemberInfo)principal).getName();
		}
	}
	 */
	// 로그인 유저라면 양쪽 모두 UsernamePasswordAuthenticationToken 객체를 반환한다.
	// 미로그인 유저라면 스프링은 AnonymousAuthenticationToken 반환, 서블릿은 null을 반환한다.
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	
	<!-- Ajax, Json에서 사용하기 위한 CSRF 메타 태그 -->
	<!-- default header name is X-XSRF-TOKEN -->
	<%-- <meta name="_csrf_header" content="${_csrf.headerName}" /> --%>
	<%-- <meta name="_csrf" content="${_csrf.token}" /> --%>
	<sec:csrfMetaTags /> <!-- 로 대체 가능 -->
	
	<title>메인화면</title>
	
	<link rel="shorcut icon" href="./resources/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="./resources/css/ui/main/main.css" />
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="./resources/scripts/ui/main/main.js"></script>
</head>
<body>
	<h3>메인화면 입니다.</h3>
	<div style="width:200px; float:left;">
		<sec:authorize access="isAnonymous()">
			<!-- 스프링 시큐리티 4.x 버전부터는 action 경로가 login으로, name들은 더 명확한걸로 변경되었음에 주의. -->
			<form id="loginfrm" name="loginfrm" method="POST" action="./login_check">
				<table>
					<tr>
						<td style="width:50px;">id</td>
						<td style="width:150px;">
							<input style="width:145px;" type="text" id="loginid" name="loginid" value=""/>
						</td>
					</tr>
					<tr>
						<td>pwd</td>
						<td>
							<input style="width:145px;" type="password" id="loginpwd" name="loginpwd" value=""/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" id="loginbtn" value="로그인"/>
							<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
							<sec:csrfInput /> <!-- 을 사용하는 방법도 존재 -->
						</td>
					</tr>
				</table>
				
				<!-- 로그인 성공시 이동할 경로 지정 -->
				<!-- loginRedirect 값은 CustomAuthenticationFailureHandler에서 다룸 -->
				<input type="hidden" name="loginRedirect" value="${loginRedirect}"/>
			</form>
		</sec:authorize>
		<sec:authorize access="isAuthenticated()">
			<%=name%>님 반갑습니다<br/>
			<%-- <a href="<%=request.getContextPath()%>/j_spring_security_logout">로그아웃</a> --%>
			
			<%-- <c:url var="logoutUrl" value="/j_spring_security_logout"/> --%>
			<c:url var="logoutUrl" value="/logout"/>
			<form action="${logoutUrl}" method="post">
				<input type="submit" value="로그아웃" />
				<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
				<sec:csrfInput /> <!-- 을 사용하는 방법도 존재 -->
			</form>
			
			
		</sec:authorize>
		<ul>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<li><a href="./resource-auth/edit">관리자 화면(URL권한 추가)</a></li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')">
				<li>매니저 화면</li>
			</sec:authorize>
			<sec:authorize access="permitAll">
				<li>비회원 게시판</li>
			</sec:authorize>
			<sec:authorize access="isAuthenticated()">
				<li>준회원 게시판</li>
			</sec:authorize>
			<sec:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MANAGER')">
				<li>정회원 게시판</li>
			</sec:authorize>
		</ul>
		<p>
			메인 컨텐츠1
			<input type="button" value="ajax요청(반가워! 전송)" onclick="javascript:getHelloMessage()"/>
			<article id="message">
			
			</article>
		</p>
		<p>
			메인 컨텐츠2
			<input type="button" value="현재 닉네임 읽기" onclick="javascript:getNickname()"/>
			<article id="nickname">
			
			</article>
		</p>
	</div>
	
	
	
</body>
</html>