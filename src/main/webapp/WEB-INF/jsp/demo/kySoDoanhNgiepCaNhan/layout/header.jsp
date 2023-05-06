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
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Ptf</title>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<link rel='SHORTCUT ICON' href='${contextPath }/img/favicon.png' type='image/x-icon' />
<link href="${contextPath }/css/style.css" rel="stylesheet">

<link rel="stylesheet" href="${contextPath }/plugins/toastr/toastr.min.css">
<script src="${contextPath }/plugins/toastr/toastr.min.js"></script>
<script src="${contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<link href="${contextPath}/plugins/sweetalert/sweetalert.css" rel="stylesheet" />
<script src="${contextPath}/plugins/sweetalert/sweetalert.min.js"></script>

<!------ Include the above in your HEAD tag ---------->
</head>
<body>
	<div class="container register">
		<div class="row">
