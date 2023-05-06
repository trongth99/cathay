<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>

<%@include file="../layout/tolltip.jsp"%>

<style type="text/css">
.btnRegister {
margin-top: 20px;
}
</style>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Thông tin giải ngân</h3>
			<div class="row register-form">
			 	<form action="${contextPath }/khach-hang/ky-so/mo-tai-khoan-4" style="width: 100%;" method="post" id="submitForm" enctype="multipart/form-data"> 
					<div class="col-md-12">
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group ">
									<label class="control-label">Phương thức giải ngân</label><br/>
									<input type="text" class="form-control" value="Tài khoản SeABank"  name="phuongThucGiaiNgan" id="phuongThucGiaiNgan" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group  ">
									<label class="control-label">Tên ngân hàng</label><br/>
									<input type="text" class="form-control" value="<c:out value='${tenNganHang }'/>"  name="tenNganHang" id="tenNganHang" readonly="readonly" data-toggle="tooltip" title="<c:out value='${tenNganHang }'/>"/>
								</div>
							</div>
						</div>
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group  ">
									<label class="control-label">Tỉnh/thành phố</label><br/>
									 <input type="text" class="form-control" value="<c:out value='${khuVuc }'/>"  name="khuVuc" id="khuVuc" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group  ">
									<label class="control-label">Chi nhánh</label><br/>
									<input type="text" class="form-control" value="<c:out value='${chiNhanh }'/>"  name="chiNhanh" id="chiNhanh"  readonly="readonly" data-toggle="tooltip" title="<c:out value='${chiNhanh }'/>"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" >Bank code</label>
									<input type="text" class="form-control" value="<c:out value='${bankCode }'/>"  name="bankCode" id="bankCode" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" >Số tài khoản</label>
									<input type="text" class="form-control" value="<c:out value='${soTK }'/>"  name="stk" id="stk" readonly="readonly" data-toggle="tooltip" title="<c:out value='${soTK }'/>"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" >Tên tài khoản</label>
									<input type="text" class="form-control" value="<c:out value='${tenTK }'/>"  name="ttk" id="ttk" readonly="readonly" data-toggle="tooltip" title="<c:out value='${tenTK }'/>"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<c:if test="${spHanMuc eq 'Yes' }">
										<label class="form-label" >Số tiền giải ngân</label>
										<input type="text" class="form-control" value=""  name="soTienGiaiNgan" id="soTienGiaiNgan"  />
									</c:if>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div style="text-align: center;">
									<a href="${contextPath}/khach-hang/ky-so/thong-tin-luu-y" target="_blank" style="color: red;">Thông tin lưu ý?</a> 
								</div>
							</div>
						</div>
						<input type="hidden" name="btnGiaiNgan" id="btnGiaiNgan"/>
						<div class="d-flex justify-content-center">
						  <c:if test="${spHanMuc eq 'Yes' }">
						 	 <input type="button" class="btnRegister" id="btnChuaGiaiNgan"  style="background-color: black;border-radius: 5px; width: 55%;" value="Chưa giải ngân" />
						  </c:if>						</div>
						<div class="  d-flex justify-content-center">
					      	<input type="button" class="btnRegister" id="btnYeuCauGiaiNgan"  style="background-color: red;border-radius: 5px; width: 55%;" value="Yêu cầu giải ngân" />
						</div>
					</div>
				 </form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function () {
		var appLoanAmount = '${appLoanAmount}';
		$("#soTienGiaiNgan").keyup(function(){
			$(this).val(numberWithCommas($(this).val()));
		});
		$("#soTienGiaiNgan").blur(function(){
			$(this).val(numberWithCommas($(this).val()));
		});
	    $(".btnRegister").click(function(){
	    	$("#btnGiaiNgan").val("Chưa giải ngân");
			 var message = "";
			 if($("#ttk").val() == "") {
				 message = "Tên tài khoản không được để trống";
			 }
			 if($("#stk").val() == "") {
				 message = "Số tài khoản không được để trống";
			 }
			 if($("#bankCode").val() == "") {
				 message = "Bank code không được để trống";
			 }
			 if($("#chiNhanh").val() == "") {
				 message = "Chi nhánh không được để trống";
			 }
			 if($("#khuVuc").val() == "") {
				 message = "Tỉnh/thành phố không được để trống";
			 }
			 if($("#tenNganHang").val() == "") {
				 message = "Tên ngân hàng không được để trống";
			 }
			 if($("#phuongThucGiaiNgan").val() == "") {
				 message = "Phương thức giải ngân không được để trống";
			 }
			 if($(this).attr("id") == "btnYeuCauGiaiNgan") {
				 $("#btnGiaiNgan").val("Yêu cầu giải ngân");
				 if($("#soTienGiaiNgan").val() == "") {
					 message = "Số tiền giải ngân không được để trống";					 
				 }
				 if($("#soTienGiaiNgan").val()) {
					 if(parseInt(tonumber($("#soTienGiaiNgan").val())) < 1000000)
						 message = "Số tiền giải ngân phải lớn hơn 1,000,000";
				 }
				 if(appLoanAmount && $("#soTienGiaiNgan").val()) {
					 if(parseInt(tonumber($("#soTienGiaiNgan").val())) > parseInt(appLoanAmount))
						 message = "Số tiền giải ngân không được vượt quá số tiền phê duyệt";
				 }
			 }
			 
			 if(message == "") {
	 			 $("#submitForm").submit();
			 } else {
				 swal(message);
			 }
	 	});
	});
</script>

<%@include file="../layout/footer.jsp"%>
			
