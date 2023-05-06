
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Admin | <spring:message code="lay_lai_mat_khau" /></title>
<!-- Tell the browser to be responsive to screen width -->
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Font Awesome -->
<link rel="stylesheet" href="${contextPath}/plugins/fontawesome-free/css/all.min.css">
<!-- Ionicons -->
<!-- <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"> -->
<!-- icheck bootstrap -->
<link rel="stylesheet" href="${contextPath}/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${contextPath}/css/adminlte.min.css">
<!-- Google Font: Source Sans Pro -->
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="${contextPath}/">
				<b><spring:message code="lay_lai_mat_khau" /></b>
			</a>
		</div>
		<!-- /.login-logo -->
		<div class="card">
			<div class="card-body login-card-body">
				<c:if test="${not empty message }">
					<p class="login-box-msg" style="color: red;">${message }</p>
				</c:if>
				<c:if test="${not empty success }">
					<p class="login-box-msg" style="color: blue;">${success }</p>
				</c:if>
				<form id="sign_in" method="POST" action="${contextPath}/lay-lai-mat-khau">
					<div class="input-group mb-3">
						<input type="text" class="form-control" name="username" placeholder="<spring:message code="ten_dang_nhap" />" required autofocus>
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-user"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="text" class="form-control" name="email" placeholder="Email" required>
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-phone"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<input type="text" class="form-control" name="captcha" placeholder="Captcha" required autocomplete="off">
						<div class="input-group-append">
							<div class="input-group-text">
								<span class="fas fa-key"></span>
							</div>
						</div>
					</div>
					<div class="input-group mb-3">
						<img src="${contextPath}/captcha" id='captcha'/> <a href="javascript:void(0)" style="margin-left: 10px;margin-top: 11px;" id="reloadCaptcha"><i class="fas fa-sync"></i></a>
					</div>
					<div class="row">
						<div class="col-5">
							<a href="${contextPath}/login"><spring:message code="quay_lai" /></a>
						</div>
						<!-- /.col -->
						<div class="col-7">
							<button type="submit" class="btn btn-primary btn-block"><spring:message code="lay_lai_mat_khau" /></button>
						</div>
						<!-- /.col -->
					</div>
				</form>
			</div>
			<!-- /.login-card-body -->
		</div>
	</div>
	<!-- /.login-box -->

	<!-- jQuery -->
	<script src="${contextPath}/plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap 4 -->
	<script src="${contextPath}/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- AdminLTE App -->
	<script src="${contextPath}/js/adminlte.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#reloadCaptcha").click(function(){
			$("#captcha").attr("src", "${contextPath}/captcha?reload="+Math.random());
		});
	});
	</script>
</body>
</html>
