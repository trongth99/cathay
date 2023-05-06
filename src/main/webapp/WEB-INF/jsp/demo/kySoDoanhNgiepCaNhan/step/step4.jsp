<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<style type="text/css">
.sweet-alert p {
font-size: 12px;
}
</style>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Kiểm tra hợp đồng</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/step3" style="width: 100%;" method="post" id="submitForm">
					<div class="col-sm-12">
						<h2>Hợp đồng vay</h2>
						<hr/>
						<iframe id="base64File" style="width: 100%; height: 500px; border: 0;" src="${LINK_ADMIN }/viewpdf?file=${LINK_ADMIN }/viewpdf/byte/${file}"></iframe>
						<hr/>	
						<c:if test="${not empty fileBaoHiem }">
							<h2>Hợp đồng bảo hiểm</h2>
							<hr/>
							<iframe id="base64FileBaoHiem" style="width: 100%; height: 500px; border: 0;" src="${LINK_ADMIN }/viewpdf?file=${LINK_ADMIN }/viewpdf/byte/${fileBaoHiem}"></iframe>
						</c:if>	 
						<c:if test="${not empty fileSeaBank }">
							<h2>Hợp đồng SeABank</h2>
							<hr/>
							<iframe id="base64FileSeabank" style="width: 100%; height: 500px; border: 0;" src="${LINK_ADMIN }/viewpdf?file=${LINK_ADMIN }/viewpdf/byte/${fileSeaBank}"></iframe>
						</c:if>	
						<hr/>
						<p style="color: red;">
							<a href="${LINK_DKDK }" target="_blank">Xem điều khoản điều kiện</a>
						</p>
						<p style="color: orange;">
							<i>Chú ý: Click vào tôi đồng ý với điều khoản điều kiện để tiếp tục thực hiện ký số</i>
						</p>
						<p>
							<label><input type="checkbox" name="dkdk" value="1" id="dongY"/> Tôi đồng ý với điều khoản và điều kiện </label>
						</p>
					</div>
					<div id="maOtpKySo" style="display: none;margin-top: 20px;">
						<label class="control-label"><spring:message code="ekycdn.nha_otp_ky_so" /></label>
						<input maxlength="200" type="text" required="required" class="form-control" id="otpKySo" name="otpKySo"/>
						<input  type="hidden" class="form-control" id="agreementUUID" name="agreementUUID" value='<c:out value="${agreementUUID }"></c:out>'/>
						<input  type="hidden" class="form-control" id="maKy" name="maKy" value='<c:out value="${maKy }"></c:out>'/>
						<input  type="hidden" class="form-control" id="soCmt" name="soCmt" value="<c:out value='${ocr.soCmt }'/>"/>
						<input  type="hidden" class="form-control" id="kiemTraGuiMaOTP" name="kiemTraGuiMaOTP"/>
					</div>
					<input type="button" class="btnRegister" id="btnRegisterDangKy" value="Ký số" onclick="dangKyKySo(this)" style="margin-left: 5px;display: none;"/>
					<button class="btnRegister" type="button" style="background: #CCC;color: black;width: 110px;" onclick="javascript:location.href='${contextPath }/khach-hang/ky-so/step1'">Quay lại</button>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var kiemTraGuiMaOTP = '<c:out value="${kiemTraGuiMaOTP}"/>'
if(kiemTraGuiMaOTP && kiemTraGuiMaOTP == 'true') {
	nhapMaOtp();
	$("#kiemTraGuiMaOTP").val('true');
}
$(document).ready(function(){
	$("#dongY").click(function(){
		if($('#dongY').is(":checked")) {
			$("#btnRegisterDangKy").show();
		} else {
			$("#btnRegisterDangKy").hide();
		}
	});
});
function nhapMaOtp() {
	swal({
	  title: "Nhập mã OTP!",
	  text: "Vui lòng nhập mã OTP được gửi vào số điện thoại của bạn.\n\nOTP này là chữ kí cho cả hợp đồng vay và hợp đồng mở tài khoản SeABank.\n\nVới khách hàng thực hiện mở TKTT SeABank thành công, quý khách có tối đa 60 phút để nhập OTP, nếu quá 60 phút hệ thống chưa nhận được xác thực OTP thì TKTT SeABank sẽ bị đóng/ngừng giao dịch.",
	  type: "input",
	  showCancelButton: true,
	  closeOnConfirm: false,
	  confirmButtonText: "Ký số",
	  cancelButtonText: "Đóng",
	  inputPlaceholder: "OTP"
	}, function (inputValue) {
		  if (inputValue === false) {
			  $("#btnRegisterDangKy").prop("disabled",false);
			  return false;
		  }
		  if (inputValue === "") {
		    swal.showInputError("Nhập vào mã OTP!");
		    return false
		  }
		  $("#otpKySo").val(inputValue);
		  $("#submitForm").submit();
		  $(".confirm").prop("disabled",true);
	});
}
function dangKyKySo(obj) {
	if(kiemTraDaChonUyQuyen()) {
		$("#btnRegisterDangKy").prop("disabled",true);
		var base64AnhMatTruoc = $("#base64AnhMatTruoc").html();
		var base64AnhMatSau = $("#base64AnhMatSau").html();
		var base64Pdf = $("#contentFilePdf").html();
		var data = {
			"soCmt": '${ocr.soCmt}'
// 			"uyQuyen": $("input[name='uyQuyen']:checked").val()
		};
		$(obj).attr('value', 'Đang xử lý thông tin ...');
		$.ajax({
			url:'${contextPath}/khach-hang/ky-so/dang-ky',
		    data: JSON.stringify(data),
		    type: 'POST',
		    processData: false,
		    contentType: 'application/json'
		}).done(function(data) {
			console.log(data)
			if(data.status == 200) {
				$("#agreementUUID").val(data.agreementUUID);
				$("#maKy").val(data.maKy);
				$("#kiemTraGuiMaOTP").val('true');
				nhapMaOtp();
//	 			$("#btnRegister").attr("type", "submit");
//	 			$("#btnRegister").attr("onclick", "").unbind("click");
//	 			toastr.info ("Click vào nút ký số để tiến hành ký");
			} else if(data.status == 405) {
				location.href = '${contextPath}/khach-hang/ky-so/step1';
			} else {
				toastr.error(data.message);
				$("#btnRegisterDangKy").prop("disabled",false);
			}
			
			$(obj).attr('value', 'Ký số');
		}).fail(function(data) {
			$("#btnRegisterDangKy").prop("disabled",false);
			toastr.error("Lỗi kiểm tra thông tin");
			$(obj).attr('value', 'Ký số');
		});
	} else {
		toastr.error("Bạn vui lòng chọn có ủy quyền hoặc không ủy quyền.");
	}
}
function kiemTraDaChonUyQuyen() {
// 	if (!$("#dongY").prop("checked") && !$("#khongDongY").prop("checked")) {
// 		return false;
// 	}
	return true;
}
</script>
<%@include file="../layout/footer.jsp"%>
			
