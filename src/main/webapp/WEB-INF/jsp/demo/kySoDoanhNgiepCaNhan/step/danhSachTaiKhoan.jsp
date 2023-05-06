<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${contextPath}/css/ZCommon_0.css?v=20211004-01">
<style type="text/css">
.btn-warning {
	background: #00A950;
	color: white;
}
</style>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel"
			aria-labelledby="home-tab">
			<h3 class="register-heading">Danh sách tài khoản</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/mo-tai-khoan-3"
					style="width: 100%;" method="post" id="submitForm"
					enctype="multipart/form-data">
					<div class="col-md-12">
						<p>Quý khách đã có TKTT SeABank. Vui lòng chọn TKTT từ danh
							sách dưới đây. TKTT quý khách chọn từ danh sách được sử dụng để
							giải ngân khoản vay của Quý khách hàng tại PTF.</p>
					</div>
					<div class="col-md-12">
						<c:forEach items="${danhSachTaiKhoan}" var="item"
							varStatus="status">
							<div class="row">
								<div class="col-md-12">
									<label><input type="radio" name="index" value="<c:out value='${item.soTaiKhoan }'/>"/> TKTT: <c:out value='${item.soTaiKhoan }'/></label>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									Chủ tài khoản :<label> <c:out value='${item.hoVaTen }'/></label>
								</div>
							</div>
						</c:forEach>
						<input type="button" class="btnRegister" id="btnRegisters" value="Tiếp theo" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		$("#btnRegisters").click(function() {
			var check = false;
			$("input[name='index']").each(function(){
				if ($(this).prop("checked")) {
					check = true;
				}
			});
			if(check) {
				$(".loading").show();
				$("#submitForm").submit();				
			} else {
				toastr.error("Vui lòng chọn 1 tài khoản");
			}
		});
	});
</script>

<%@include file="../layout/footer.jsp"%>

