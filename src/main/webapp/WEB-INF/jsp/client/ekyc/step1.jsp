<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<form action="" class="form" id="forms" method="post">
	<%@include file="menu.jsp"%>
	<div class="step-forms step-forms-active">
		<div class="group-inputs">
			<label for="soDienThoai">Số điện thoại</label> <input type="text" name="soDienThoai" id="soDienThoai" />
		</div>
		<div class="group-inputs">
			<label for="email">Email</label> <input type="text" name="email" id="email" />
		</div>
		<div class="">
			<button type="submit" class="btn btn-next width-50 ml-auto">Next</button>
		</div>
	</div>
</form>
<%@include file="../layout/footer.jsp"%>