<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading" style="color: blue;">Xác thực khuôn mặt thành công</h3>
			<div class="row register-form" style="padding-top: 30px;">
				<form action="${contextPath }/khach-hang/ky-so/mo-tai-khoan-1" style="width: 100%;" method="post" id="submitForm">
					<div class="col-md-12 text-center">
						<button class="btnRegister" type="submit" style="float: none;width: 190px;margin-top: 10px;margin-bottom: 10px;">Mở TKTT SeABank</button>
						<p>Quý khách đã có tài khoản tại SeABank vui lòng vẫn thao tác chọn mở TKTT SeABank, hệ thống sẽ kiểm tra và lấy ra thông tin tài khoản đang tồn tại của Quý khách.” </p>
					</div>
					<div class="col-md-12" style="margin-top: 10px;color: red;">
						<h5>LƯU Ý:</h5>
						<p>- Quý khách được miễn phí chuyển khoản giải ngân bằng tài khoản thanh toán mở tại SeABank.</p>
						<p>- Trường hợp QK giải ngân vào tài khoản ngân hàng khác thì các khoản chi phí chuyển khoản phát sinh do Bên Vay thanh toán.</p>
<!-- 						<p>- Trường hợp khách hàng đã có TKTT SeABank thì khi click chọn "Mở TKTT SeABank" hệ thống sẽ kiểm tra và lấy lên danh sách TKTT SeABank của Quý khách</p> -->
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

</script>

<%@include file="../layout/footer.jsp"%>
			
