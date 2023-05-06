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
			<h3 class="register-heading">Kiểm tra thông tin ocr</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/step20" style="width: 100%;" method="post" id="submitForm" enctype="multipart/form-data">
					<div class="col-md-12">
						<p style="color: red;">
							<i><b>Chú ý: </b>Vui lòng kiểm tra lại thông tin ocr đảm bảo thông tin chính xác để tránh việc phải làm lại hợp đồng. Nếu thông tin không đúng, 
							bấm nút "Quay lại" thực hiện việc chụp lại ảnh giấy tờ làm lại quá trình ocr.</i>
						</p>
					</div>
					<div class="col-md-12">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group ">
									<label class="form-label" style="font-weight: bold;">Số chứng minh thư </label>
									<input type="text" class="form-control" value="<c:out value='${ocr.soCmt }'/>"  name="soCmt" disabled="disabled"/>
								</div>
							</div>	
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Họ và tên </label>
									<input type="text" class="form-control" value="<c:out value='${ocr.hoVaTen }'/>"  name="hoVaTen" disabled="disabled"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Ngày hết hạn</label>
									<input type="text" class="form-control" value="<c:out value='${ocr.ngayHetHan }'/>"  name="ngayHetHan" disabled="disabled"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Ngày cấp</label>
									<input type="text" class="form-control" value="<c:out value='${ocr.ngayCap }'/>"  name="ngayCap" disabled="disabled"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Năm sinh</label>
									<input type="text" class="form-control" value="<c:out value='${ocr.namSinh }'/>"  name="namSinh" disabled="disabled"/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Nơi cấp</label>
									<input type="text" class="form-control" value="<c:out value='${ocr.noiCap }'/>"  name="noiCap" readonly="readonly" data-toggle="tooltip" title="<c:out value="${ocr.noiCap }"/>"/>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="form-label" style="font-weight: bold;">Nơi trú</label>
									<input type="text" class="form-control" value="<c:out value='${ocr.noiTru }'/>"  name="noiTru" id="noiTru" data-toggle="tooltip" title="<c:out value="${ocr.noiTru }"/>"/>
								</div>
							</div>
						</div>
						<input type="button" class="btnRegister" id="btnRegisters" value="Xác nhận thông tin ocr" />
						<button class="btnRegister" type="button" style="background: #CCC;color: black;width: 110px;" onclick="javascript:location.href='${contextPath }/khach-hang/ky-so/step2'">Quay lại</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$("#btnRegisters").click(function() {
		if($("#noiTru").val().trim() != "") {
			$(".loading").show();
			$("#submitForm").submit();			
		} else {
			toastr.error("Nơi trú không được để trống");
		}
	});
});
</script>

<%@include file="../layout/footer.jsp"%>
			
