<%@ page contentType="text/html; charset=UTF-8"%>
<div class="panel panel-primary setup-content" id="step-2">
	<div class="panel-heading">
		<h3 class="panel-title">Định nghĩa vị trí ký cho doanh nghiệp và cá nhân</h3>
	</div>
	<div class="panel-body">
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Từ khóa vị trí ký doanh nghiệp</label>
					<input  type="text"  class="form-control input-sm" id="viTriKyDn" name="viTriKyDn" value="nganhangky"/>
				</div>
			</div>
			<!-- <div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Từ khóa vị trí ký cá nhân</label>
					<input  type="text"  class="form-control input-sm" id="viTriKyCaNhan" name="viTriKyCaNhan" value="benvayky"/>
				</div>
			</div> -->
		</div>	
		<div class="row">
			<!-- <div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Từ khóa vị trí ký bảo hiểm</label>
					<input  type="text"  class="form-control input-sm" id="viTriKyBaoHiem" name="viTriKyBaoHiem" value="baohiemky"/>
				</div>
			</div> -->
<!-- 			<div class="col-sm-6"> -->
<!-- 				<div class="form-group  has-feedback"> -->
<!-- 					<label class="control-label">Từ khóa vị trí ký cá nhân</label> -->
<!-- 					<input  type="text"  class="form-control input-sm" id="viTriKyCaNhan" name="viTriKyCaNhan"/> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="col-sm-6"> -->
<!-- 				<div class="form-group  has-feedback"> -->
<!-- 					<label class="control-label">Trang ký cá nhân</label> -->
<!-- 					<input  type="number"  class="form-control input-sm" id="trangKyCaNhan" name="trangKyCaNhan"/> -->
<!-- 				</div> -->
<!-- 			</div> -->
		</div>	
		<h2>Hợp đồng vay</h2>
		<iframe id="iframeid" style="width: 100%; height: 400px; border: 0;"></iframe>
		<hr/>
	<!-- 	<h2>Hợp đồng bảo hiểm</h2>
		<iframe id="iframeidBaoHiem" style="width: 100%; height: 400px; border: 0;display: none;"></iframe> -->
		<button class="btn btn-default btn-sm" type="button" onclick="prevStep(this)">Quay lại</button>
		<button class="btn btn-primary nextBtn pull-right btn-sm" type="button" onclick="validateStep2(this)" data-loading-text="<i class='fa fa-circle-o-notch fa-spin'></i> Đang xử lý">Lưu và ký hợp đồng vay</button>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	
});
function validateStep2(obj) {
	var check = 0;
	
	if($("#viTriKyDn").val().trim() == "") {
		check ++;	
	}


		luuNoiDungKy($("#base64TaiFile").html(), obj);		
	
}
function luuNoiDungKy(base64, obj) {
	if(base64 != "") {
		
		
			var data = {
					"noiDungFile": base64,
					
					"tenFile": $("#nameTaiFile").val().split('\\').pop(),
					/* "soCmt": $("#soCmt").val().trim(),
					"email": $("#email").val().trim(),
					"hoVaTen":$("#hoVaTen").val().trim(),
					"soDienThoai":$("#soDienThoai").val().trim(), */
					"viTriKyDn":$("#viTriKyDn").val().trim(),
					
					
					"trangKydn":$("#trangKydn").val(),
					
					
					
					
					"id":'${ekycKyso.id}',
					"thayDoiFileKy":thayDoiFileKy,
					
			}
		
		
		$(obj).button('loading');
		$.ajax({
			url:'${contextPath}/danh-sach-khach-hang/ky-so/luu',
		    data: JSON.stringify(data),
		    type: 'POST',
		    processData: false,
		    contentType: 'application/json'
		}).done(function(data) {
			console.log(data)
			if(data.status == "200") {
				nextStep(obj);
			} else {
				toastr.error(data.message);
			}
			$(obj).button('reset');;
		}).fail(function(data) {
			toastr.error("Lỗi ký số");
			$(obj).button('reset');;
		});
	} else {
		toastr.error("Lỗi ký số");
	}
}
</script>