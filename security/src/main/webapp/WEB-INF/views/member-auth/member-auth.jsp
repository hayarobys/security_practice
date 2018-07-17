<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/views/common/common-head.jsp" flush="false"/>
	<title>MEMBER-AUTH 관리</title>
	
	<link rel="stylesheet" href="<c:url value='/resources/scripts/jqwidgets/styles/jqx.base.css'/>" />
	<link rel="stylesheet" href="<c:url value='/resources/css/ui/member-auth/member-auth.css'/>" />
	
	<script src="<c:url value='/resources/scripts/jqwidgets/jqx-all.js'/>"></script>
	<script src="<c:url value='/resources/scripts/ui/member-auth/member-auth.js'/>"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	
	<div id="contents">
		<jsp:include page="/WEB-INF/views/common/nav.jsp"></jsp:include>
		
		<section id="box" class="box">
			<div class="data_box">
				<header class="title">
					<span class="title_font">MEMBER</span>
				</header>
				
				<section id="data_member" class="data_body">
					
				</section>
				
				<input type="button" value="reload" onclick="javascript:reloadMemberGrid();" />
			</div>
			
			<div class="data_box">
				<header class="title">
					<span class="title_font">AUTH</span>
				</header>
				
				<section id="data_auth" class="data_body">
					
				</section>
				
				<input type="button" value="save" onclick="javascipt:save();">
				<br/>
				<span>
					※ 권한 미 선택시, 해당 계정은 로그인 조차 할 수 없습니다.
				</span>
			</div>
		</section>
	</div>
	
	<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
</body>
</html>









