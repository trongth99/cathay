<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>

<script src="${contextPath}/js/select2.min.js"></script>
<link href="${contextPath}/css/select2.min.css" rel="stylesheet" />

<style type="text/css">
.btn-warning {
background: #00A950;
color: white;
}
  .select2-container--default .select2-selection--single{
height: 37px;
} 
.select2-container--default .select2-selection--single .select2-selection__rendered{
line-height: 35px;
}  
.toast .toast-error {
background: red;
}
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
									<input type="text" class="form-control" value="Tài khoản ngân hàng khác"  name="phuongThucGiaiNgan" id="phuongThucGiaiNgan" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group  ">
									<label class="control-label">Tên ngân hàng</label><br/>
									<select style="width: 100%;" class="form-control "  name="tenNganHang" id="tenNganHang" placeholder="Search...">
										<c:forEach var="item" items="${listBank }">
										    <option value="<c:out value='${item.value }'/>" ><c:out value='${item.name }'/></option>
										</c:forEach>
									</select>
								</div>
								<input type="hidden"  value="${branch }"  name="branch" id="branch" />
							</div>
						</div>
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group  ">
									<label class="control-label">Tỉnh/thành phố</label><br/>
									<select style="width: 100%;" class="form-control "  name="khuVuc" id="khuVuc" placeholder="Search...">
										<option value="">-- Chọn thành phố --</option>
										<c:forEach var="item" items="${listCity }">
										    <option value="<c:out value='${item.value }'/>" ><c:out value='${item.name }'/></option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<div class="row">
								<div class="col-sm-12">
								<div class="form-group  ">
									<label class="control-label">Chi nhánh</label><br/>
									<select style="width: 100%;" class="form-control " value="<c:out value='${chiNhanh }'/>" name="chiNhanh" id="chiNhanh" placeholder="Search...">
									
									</select> 
								</div>
							</div>
							<input type="hidden"  value="<c:out value='${bankBranch }'/>"  name="bankBranch" id="bankBranch" />
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" >Bank code</label>
									<input type="text" class="form-control"  name="bankCode" id="bankCode" readonly="readonly"/>
									<input type="hidden" class="form-control"  name="id" id="id" readonly="readonly"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" >Số tài khoản</label>
									<input type="number" class="form-control" value=""  name="stk" id="stk" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" >Tên tài khoản</label>
									<input type="text" class="form-control" value="${hoTen}"  name="ttk" id="ttk" />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<c:if test="${spHanMuc eq 'Yes' }">
										<label class="form-label" >Số tiền giải ngân (VNĐ)</label>
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
						<div class="  d-flex justify-content-center">
						  	<c:if test="${spHanMuc eq 'Yes' }">
						  		<input type="button" class="btnRegister" id="btnChuaGiaiNgan" style="background-color: black;border-radius: 5px; width: 55%;" value="Chưa giải ngân" />
						  	</c:if>						
						</div>
						<div class="  d-flex justify-content-center">
					      	<input type="button" class="btnRegister" id="btnYeuCauGiaiNgan"  style="background-color: red;border-radius: 5px; width: 55%;" value="Yêu cầu giải ngân" />
						</div>
					</div>
				 </form>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function () {
		var dongTaiKhoan = '${dongTaiKhoan}';
		if(dongTaiKhoan && dongTaiKhoan=='true') {
			swal("Thông báo", "Do quá thời gian xác nhận OTP theo quy định tại SeABank nên TKTT SeABank đã bị ngừng/ không không thể giao dịch. Quý khách có thể tải ứng dụng ngân hàng điện tử SeAMobile để mở TKTT SeABank online.");
		}
		
		var appLoanAmount = '${appLoanAmount}';
		$("#soTienGiaiNgan").keyup(function(){
			$(this).val(numberWithCommas($(this).val()));
		});
		$("#soTienGiaiNgan").blur(function(){
			$(this).val(numberWithCommas($(this).val()));
		});
		$("select").select2();
		var data = {
				"tenNganHang": $("#tenNganHang").val(),
	 			"khuVuc":$("#khuVuc").val()
		};
   		$.ajax({
   			url:'${contextPath }/khach-hang/ky-so/list-branch',
   			data: JSON.stringify(data),
			type: 'POST',
			processData: false,
			contentType: 'application/json'
   		}).done(function(data) {
   			console.log(data)
   			var str = "<option value=''>-- Chọn chi nhánh --</option>";
   			for ( x in data.chiNhanh) {
   				  str += "<option value='"+data.chiNhanh[x].name+"'>"+data.chiNhanh[x].name+"</option>"
   			} 
   			$("#chiNhanh").html(str);
   		}).fail(function(data) {
   			toastr.error("Lỗi kiểm tra thông tin");
   			$(obj).button('reset');
   		});
	    	
	    $("#tenNganHang").change(function(){
	    	var data = {
						"tenNganHang": $("#tenNganHang").val(),
			 			"khuVuc":$("#khuVuc").val()
			};
	    	$("#bankCode").val("");
	    	$("#stk").val("");
	    	$('#khuVuc').val("").trigger('change');
	    	$.ajax({
	    		url:'${contextPath }/khach-hang/ky-so/list-branch',
	    		data: JSON.stringify(data),
				type: 'POST',
				processData: false,
				contentType: 'application/json'
	    	}).done(function(data) {
	    			var str = "<option value=''>-- Chọn chi nhánh --</option>";
	    			for ( x in data.chiNhanh) {
	    				str += "<option value='"+data.chiNhanh[x].name+"'>"+data.chiNhanh[x].name+"</option>"
	    			} 
	    			$("#chiNhanh").html(str);
	    	}).fail(function(data) {
	    			toastr.error("Lỗi kiểm tra thông tin");
	    			$(obj).button('reset');
	    	});
	    });

	    $("#khuVuc").change(function(){
	    	  var data = {
						"tenNganHang": $("#tenNganHang").val(),
			 			"khuVuc":$("#khuVuc").val()
					};
	    	  console.log( $("#tenNganHang").val())
	    	  console.log( $("#khuVuc").val())
	    		$.ajax({
	    			url:'${contextPath }/khach-hang/ky-so/list-branch',
	    			   data: JSON.stringify(data),
					    type: 'POST',
					    processData: false,
					    contentType: 'application/json'
	    		}).done(function(data) {
	    			console.log(data)
	    			var str = "<option value=''>-- Chọn chi nhánh --</option>";
	    			for ( x in data.chiNhanh) {
	    				  str += "<option>"+data.chiNhanh[x].name+"</option>"
	    			} 
	    			$("#chiNhanh").html(str);
	    		}).fail(function(data) {
	    			toastr.error("Lỗi kiểm tra thông tin");
	    			$(obj).button('reset');
	    		});
	    	});
	      $("#chiNhanh").change(function(){
	    	  var data1 = {
						"chiNhanh": $("#chiNhanh").val()
					};
	    	  $.ajax({
	    			url:'${contextPath }/khach-hang/ky-so/bankCode',
	    			data: JSON.stringify(data1),
					type: 'POST',
					processData: false,
					contentType: 'application/json'
	    		}).done(function(data) {
	    			console.log(data.bankCode);
	    			$("#bankCode").val(data.bankCode);
	    			$("#id").val(data.id);
	    			
	    		}).fail(function(data) {
	    			toastr.error("Lỗi kiểm tra thông tin");
	    			$(obj).button('reset');;
	    		});
	 
	    	});
	      $("#stk").blur(function(){
	    	  $(".loading").show();
					var data = {
							"stk": $("#stk").val(),
				 			"bankCode":$("#bankCode").val()
						};
					$.ajax({
						url:'${contextPath }/khach-hang/ky-so/bankacc',
						data: JSON.stringify(data),
						type: 'POST',
						processData: false,
						contentType: 'application/json'
					}).done(function(data) {
						console.log(data)
						if(data.error == 'false') {
							$("#ttk").val(data.accountName)
						} else {
							toastr.error(data.message);
						}
						$(".loading").hide();
					}).fail(function(data) {
						toastr.error("Lỗi kiểm tra thông tin");
						$(".loading").hide();
					});
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
			
