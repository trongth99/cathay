<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>
<link rel="stylesheet" type="text/css" href="${contextPath}/css/ZCommon_0.css?v=20211004-01">
<style type="text/css">
.btn-warning {
background: #00A950;
color: white;
}
</style>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading"></h3>
			<div class="row register-form">
			
					<div class="col-md-12" style="text-align: center;">
						<h4>QK vui lòng chọn Tiếp tục để thử lại hoặc tải ứng dụng ngân hàng điện tử SeAMobile để mở lại TKTT online?</h4>
					</div>
					<form action="${contextPath }/khach-hang/ky-so/mo-tai-khoan-2" style="width: 100%;" method="post" id="submitForm" enctype="multipart/form-data">
					
					</form>
					<div class="col-md-12">
						<div class="row">
							<div class="col-md-12">
								<div class=" " style=" display: flex;justify-content: center;">
									<button class="btnRegister"  type="button" style="background: #CCC;color: black;width: 200px;height: 65px;" id="tieptuc">Tiếp tục</button>
						            <button class="btnRegister" type="button" style="background: #CCC;color: black;width: 250px;height: 65px;" onclick="javascript:location.href='${contextPath }/khach-hang/ky-so/thong-tin-giai-ngan'">Nhập TKTT ngân hàng khác</button>
								</div>
							</div>	
						</div>
					
					</div> 
				
			</div>
		</div>
	</div>
</div>
<script>
 $(document).ready(function(){
	var dongTaiKhoan = '${errorPopup}';
	if(dongTaiKhoan && dongTaiKhoan!='') {
		swal("Thông báo", dongTaiKhoan);
	}
	 
	$("#tieptuc").click(function() {
		$(".loading").show();
		$("#submitForm").submit();			
	});
});
</script>

<%@include file="../layout/footer.jsp"%>
			
