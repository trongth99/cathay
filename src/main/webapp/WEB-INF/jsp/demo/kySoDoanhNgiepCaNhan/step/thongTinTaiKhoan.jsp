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
			<h3 class="register-heading">Mở TKTT SeABank</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/mo-tai-khoan-3" style="width: 100%;" method="post" id="submitForm" enctype="multipart/form-data">
					<div class="col-md-12">
						<h4>Thông tin TKTT SeABank</h4>
					</div>
					
				<div class="col-md-12">
				<c:forEach items="${danhSachTaiKhoan}" var="item" varStatus="status">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group ">
									<label class="form-label"><b>Số TKTT:</b> <c:out value='${item.soTaiKhoan }'/></label>
									<input type="hidden" name="soTK" value="<c:out value='${item.soTaiKhoan }'/> "/>
								</div>
							</div>	
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label"><b>Tên chủ tài khoản:</b> <c:out value='${item.hoVaTen }'/></label>
									 <input type="hidden" name="tenTK" value="<c:out value='${item.hoVaTen }'/> "/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label"><b>Số CMND:</b> <c:out value='${item.identityNumber }'/></label>
								 <input type="hidden" name="soCmt" value="<c:out value='${item.identityNumber }'/> "/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label"><b>Chi nhánh mở TK:</b> <c:out value='${item.coCodeFull }'/></label>
									 <input type="hidden" name="coCode" value="<c:out value='${item.coCode }'/> "/>
								</div>
							</div>
						</div>
						<div class="row">
								<div class="col-md-12">
									<div class="form-group">
										<label class="form-label" ><b>Ngày bắt đầu hoạt động:</b> <c:out value='${item.createDate }'/></label>
								    </div>
						        </div>
						</div>
					</c:forEach>
						<div class="row">
							<div class="col-md-12">
								<p style="color: orange;">
									<i>
										-	Số tài khoản chỉ bắt đầu có hiệu lực sử dụng từ sau thời điểm KH nhập OTP xác thực giao kết với Ngân hàng.<br/>
                                        -	<b>Tên đăng nhập Ebanking hiển thị trên Hợp đồng mở và sử dụng TKTT SeABank. Mật khẩu đăng nhập Ebanking sẽ được Ngân hàng gửi qua SMS sau khi khách hàng xác thực OTP thành công.</b><br/>
                                        -	Quý khách có TỐI ĐA 60 PHÚT ĐỂ XÁC THỰC OTP, sau thời gian này nếu hệ thống không nhận được xác thực thì tài khoản của quý khách sẽ bị ngừng/ không thể giao dịch theo quy định hiện hành tại ngân hàng.
									</i>
								</p>
							</div>
						</div>
						<input type="button" class="btnRegister" id="btnRegisters"  value="Tiếp theo"/>
					</div> 
				</form>
			</div>
		</div>
	</div>
</div>
<script>
 $(document).ready(function(){
	$("#btnRegisters").click(function() {
		
			$(".loading").show();
			$("#submitForm").submit();			
		 
	});
/* 	$("#dongY").click(function(){
		if($('#dongY').is(":checked")) {
			$("#btnRegisters").prop('disabled', false);
		} else {
			$("#btnRegisters").prop('disabled', true);
		}
	}); */
});
</script>

<%@include file="../layout/footer.jsp"%>
			
