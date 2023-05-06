<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title>Admin Register</title>
<!-- Favicon-->
<link rel="icon" href="${contextPath}/favicon.ico" type="image/x-icon">

<!-- Google Fonts -->
<link href="${contextPath}/css/google-fonts.css" rel="stylesheet">
<link href="${contextPath}/css/google-icon.css" rel="stylesheet">

<!-- Bootstrap Core Css -->
<link href="${contextPath}/plugins/bootstrap/css/bootstrap.css" rel="stylesheet">

<!-- Waves Effect Css -->
<link href="${contextPath}/plugins/node-waves/waves.css" rel="stylesheet" />

<!-- Animation Css -->
<link href="${contextPath}/plugins/animate-css/animate.css" rel="stylesheet" />

<!-- Custom Css -->
<link href="${contextPath}/css/style.css" rel="stylesheet">
</head>

<body class="signup-page">
	<div class="signup-box">
		<div class="logo">
			<a href="javascript:void(0);"> Quản trị </a>
			<small>Công tác đầu tư</small>
		</div>
		<div class="card">
			<div class="body">
				<form id="sign_up" method="POST">
					<div class="msg">Đăng ký thành viên</div>
					<div class="input-group">
						<span class="input-group-addon">
							<i class="material-icons">person</i>
						</span>
						<div class="form-line">
							<input type="text" class="form-control" name="namesurname" placeholder="Tên đăng nhập" required autofocus>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon">
							<i class="material-icons">email</i>
						</span>
						<div class="form-line">
							<input type="email" class="form-control" name="email" placeholder="Email" required>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon">
							<i class="material-icons">lock</i>
						</span>
						<div class="form-line">
							<input type="password" class="form-control" name="password" minlength="6" placeholder="Mật khẩu" required>
						</div>
					</div>
					<div class="input-group">
						<span class="input-group-addon">
							<i class="material-icons">lock</i>
						</span>
						<div class="form-line">
							<input type="password" class="form-control" name="confirm" minlength="6" placeholder="Nhập lại mật khẩu" required>
						</div>
					</div>
<!-- 					<div class="form-group"> -->
<!-- 						<input type="checkbox" name="terms" id="terms" class="filled-in chk-col-pink"> -->
<!-- 						<label for="terms"> -->
<!-- 							I read and agree to the -->
<!-- 							<a href="javascript:void(0);">terms of usage</a> -->
<!-- 							. -->
<!-- 						</label> -->
<!-- 					</div> -->

					<button class="btn btn-block btn-lg bg-pink waves-effect" type="submit">Đăng ký</button>

					<div class="m-t-25 m-b--5 align-center">
						<a href="${contextPath}/login">Nếu bạn đã là thành viên?</a>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Jquery Core Js -->
	<script src="${contextPath}/plugins/jquery/jquery.min.js"></script>

	<!-- Bootstrap Core Js -->
	<script src="${contextPath}/plugins/bootstrap/js/bootstrap.js"></script>

	<!-- Waves Effect Plugin Js -->
	<script src="${contextPath}/plugins/node-waves/waves.js"></script>

	<!-- Validation Plugin Js -->
	<script src="${contextPath}/plugins/jquery-validation/jquery.validate.js"></script>

	<!-- Custom Js -->
	<script src="${contextPath}/js/admin.js"></script>
	<script src="${contextPath}/js/pages/examples/sign-up.js"></script>
</body>

</html>