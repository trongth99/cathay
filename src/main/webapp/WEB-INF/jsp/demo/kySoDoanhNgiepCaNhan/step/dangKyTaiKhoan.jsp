<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>

<%@include file="../layout/tolltip.jsp"%>

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
				<form action="${contextPath }/khach-hang/ky-so/mo-tai-khoan-2" style="width: 100%;" method="post" id="submitForm" enctype="multipart/form-data">
					<div class="col-md-12">
						<h4>Thông tin KH</h4>
					</div>
					<div class="col-md-12">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group ">
									<label class="form-label" style="font-weight: bold;">Thuộc đối tượng người cư trú </label>
									<input type="text" class="form-control" value="Có"  readonly="readonly"/>
								</div>
							</div>	
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Chủ sở hữu khác</label>
									<input type="text" class="form-control" value="Không"  readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Mục đích trong mối quan hệ với SeABank</label>
									<input type="text" class="form-control" value="Daily Banking"  readonly="readonly"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Thỏa thuận pháp lý khác</label>
									<input type="text" class="form-control" value="Không"readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Thông tin FATCA</label>
									<input type="text" class="form-control" value="Không"  readonly="readonly"/>
								</div>
							</div>
						</div>
					</div>	
					<div class="col-md-12">
						<h4>Thông tin đăng ký E-Banking</h4>
					</div>
					<div class="col-md-12">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group ">
									<label class="form-label" style="font-weight: bold;">Gói dịch vụ </label>
									<input type="text" class="form-control" value="Super"  name="goiDichVu" readonly="readonly"/>
								</div>
							</div>	
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Hạn mức TKTT</label>
									<input type="text" class="form-control" value="100 triệu VND/ ngày, 100 triệu VND/tháng"  name="hanMucTKTT" readonly="readonly" data-toggle="tooltip" title="100 triệu VND/ ngày, 100 triệu VND/tháng"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Phương thức xác thực</label>
									<input type="text" class="form-control" value="SMS"  name="phuongThucXacThuc" readonly="readonly"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Thông tin người giới thiệu</label>
									<input type="text" class="form-control" value="PTF"  name="thongTinNguoiGioiThieu" readonly="readonly"/>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col-md-12">
								<p>
									<label><input type="checkbox" name="dkdk" value="dongy" id="dongY"/> Đồng ý điều kiện điều khoản của SeABank </label>
								</p>
								<p style="color: orange;">
									<i>
										Bằng việc chọn xác nhận, tôi đồng ý giao kết thỏa thuận mở và sử dụng tài khoản thanh toán tại SeABank bằng phương thức điện tử và tuân thủ
										 "ĐIỀU KIỆN ĐIỀU KHOẢN MỞ VÀ SỬ DỤNG TÀI KHOẢN THANH TOÁN THEO PHƯƠNG THỨC ĐIỆN TỬ VÀ DỊCH VỤ EBANK ĐÍNH KÈM TẠI SEABANK" của SeABank.
										  Đồng ý cho PTF cung cấp thông tin của tôi cho SeABank để mở tài khoản thanh toán, đăng ký dịch vụ ngân hàng. Đồng thời, 
										  SeABank được quyền cung cấp các thông tin về tài khoản thanh toán, dịch vụ ngân hàng của tôi tại SeABank cho PTF để nhận tiền giải ngân (bao gồm cả thông tin tài khoản thanh toán của tôi đã mở tại SeABank)
									</i>
								</p>
								<p style="color: red;"> -->
								
 									<a href="${contextPath}/khach-hang/ky-so/chi-tiet-dieu-khoan" target="_blank">Click để xem chi tiết điều khoản mở tài khoản thanh toán theo phương thức điện tử.</a> 
								</p> 
							</div>
						</div>
						<input type="button" class="btnRegister" id="btnRegisters1" value="Tiếp tục" disabled="disabled"/>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$("#btnRegisters1").click(function() {
		$(".loading").show();
		$("#submitForm").submit();			
	});
	$("#dongY").click(function(){
		if($('#dongY').is(":checked")) {
			$("#btnRegisters1").prop('disabled', false);
		} else {
			$("#btnRegisters1").prop('disabled', true);
		}
	});
});
</script>

<%@include file="../layout/footer.jsp"%>
			
