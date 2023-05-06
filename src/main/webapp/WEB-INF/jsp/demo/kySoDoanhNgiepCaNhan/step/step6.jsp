<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Complete</h3>
			<div class="row register-form">
				<form action="" style="width: 100%;" method="post" id="submitForm">
					<div class="col-md-12">
						<h1>Sucessfully register</h1>
						<button class="btnRegister" type="button" style="background: #CCC;color: black;width: 110px;" onclick="javascript:location.href='${contextPath }/khach-hang/ky-so/step1'">Back</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<%@include file="../layout/footer.jsp"%>
			
