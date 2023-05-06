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
			<h3 class="register-heading">Tải lên ảnh giấy tờ</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/step2" style="width: 100%;" method="post" id="submitForm" enctype="multipart/form-data">
					<div class="col-md-12">
						<div class="row" style="margin-top: 20px;">
							<div class="col-sm-6" style="width: 99%;">
								<div style="text-align: center">CMND/CCCD mặt trước</div>

								<div class="capture-image">
									<div class="capture-image-container" style="overflow: hidden;">
										<div class="capture-image2-container" >
											<div class="camera_result content-row-inline" style="width: 100%">
												<div style="" id="frameFront">
													<img id="blahFront" src="" alt="your image" class="imgID" style="display: none;" >
													<video id="videoTakePhotoFront" width="100%" style="object-fit: cover; " playsinline></video>
													<canvas id="canvasTakePhotoFront" style="display: none" width="1280" height="960"></canvas>
													<textarea style="display: none;" id="frontBase64" name="anhMatTruocBase64"></textarea>
												</div>
												<div id="container-btn-front">
													<div class="row d-none d-xl-block" style="display: block !important;;">
														<div style="left: 40%; position: absolute; bottom: 20px;" class="buttonPic">
															<button type="button" class="font-Roboto  btn btn-warning" style="margin-top: 156px;" id="btn-capture">Chụp ảnh</button>
														</div>
													</div>
												</div>
											</div>
										</div>

									</div>

								</div>
							</div>
							<input type="hidden" name="code" value="${code }"/>
							<div class="col-sm-6" style="width: 99%; margin-bottom: 16px;">
								<div style="text-align: center">CMND/CCCD mặt sau</div>
								<div class="capture-image">
									<div class="capture-image-container" style="overflow: hidden;">
										<div class="capture-image2-container" ">
											<div class="camera_result content-row-inline" style="width: 100%">
												<div style=" overflow: hidden;" id="frameBack">
													<img id="blahBack" src="" alt="your image" class="imgID" style="display: none">
													<textarea style="display: none;" id="backBase64" name="anhMatSauBase64"></textarea>
												</div>
												<div id="container-btn-back">
													<div class="row d-none d-xl-block" style="display: block !important;;">
														<div style="left: 40%; position: absolute; bottom: 20px;" class="buttonPic">
															<button type="button" class="font-Roboto  btn btn-warning" style="margin-top: 156px" id="btn-capture1">Chụp ảnh</button>
														</div>
													</div>
												</div>
											</div>
										</div>

									</div>

								</div>
							</div>
						</div>
						<input type="button" class="btnRegister" id="btnRegisters" value="Tải lên ảnh giấy tờ" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	$(".buttonPic").css("left", (($("#container-btn-front").width()-$("#btn-capture").width())/2-10)+"px");
	
	$("#btnRegisters").click(function() {
		$("#frontBase64").html($("#blahFront").attr("src").replace("data:image/jpeg;base64,", ""));
		$("#backBase64").html($("#blahBack").attr("src").replace("data:image/jpeg;base64,", ""));
		var check = 0;
		if($("#frontBase64").html() == "") check++;
		if($("#backBase64").html() == "") check++;
		if(check == 0) {
			$(".loading").show();
			$("#submitForm").submit();			
		} else {
			toastr.error("Vui lòng Chụp/ Tải ảnh mặt trước và sau giấy tờ của quý khách");
		}
	});
	
	createVideoBack1('videoTakePhotoFront', "btn-capture1", "blahBack", "canvasTakePhotoFront", 'frameBack');
	createVideoBack1('videoTakePhotoFront', "btn-capture", "blahFront", "canvasTakePhotoFront", 'frameFront');
});
</script>

<%@include file="../layout/footer.jsp"%>
			
