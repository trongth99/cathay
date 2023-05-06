<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="layout/header.jsp"%>
<%@include file="layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Thông báo</h3>
			<div class="row register-form text-center">
				<form action="" style="width: 100%;" method="post" id="submitForm">
					<h1 style="color: red;">Có lỗi trong quá trình xử lý, bạn vui lòng thực hiện lại.</h1>
					<a href="${contextPath }/khach-hang/ky-so/step1" style="font-size: 20px;">Trở về</a>
				</form>
			</div>
		</div>
	</div>
</div>

<%@include file="layout/footer.jsp"%>
			
