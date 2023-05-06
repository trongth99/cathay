<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>
<%@include file="../layout/notCopy.jsp"%>
<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Xác thực khuôn mặt</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/step31" style="width: 100%;" method="post" id="submitForm">
					<div style="width: 100%;margin: auto;margin-bottom: 20px;text-align: center;">
						<c:if test="${not empty anhVideo }">
							<button class="btnRegister" id="tieptuc" type="button" style="float: none;width: 190px;margin-top: 10px;">Tiếp tục</button><br/><br/>
							<img src="data:image/jpeg;base64,${anhVideo }" width="100%"/>
						</c:if>
						<c:if test="${empty anhVideo }">
							<button class="btnRegister" type="button" style="background: #CCC;color: black;width: 110px;margin-top: 10px;float: none;" onclick="javascript:location.href='${contextPath }/khach-hang/ky-so/step1'">Quay lại</button>
							<button class="btnRegister" id="startAndStop" type="button" style="float: none;width: 190px;margin-top: 10px;">Xác thực khuôn mặt</button>
							<p style="color: red;"><i>Giữ thẳng khuôn mặt, sau khi bấm vào nút "Xác thực khuôn mặt" quay đầu từ từ qua trái hoặc qua phải</i></p>
						</c:if>
					</div>
					
					<div class="col-md-12">
						<c:if test="${empty anhVideo }">
							<video id="videoInput" width="100%" playsinline autoplay></video>
	    					<canvas id="canvasOutput" width="640" height="480" style="display:none;"></canvas>
	    				</c:if>
	    				<textarea name="listImage" id="listImage" style="display:none;">${anhVideo }</textarea>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<c:if test="${not empty anhVideo }">
	<script type="text/javascript">
	$(document).ready(function(){
		$("#tieptuc").click(function(){
			$(".loading").show();
			$("#submitForm").submit();
		});
		
	});
	</script>
</c:if>
<c:if test="${empty anhVideo }">
<script type="text/javascript">

'use strict';

function detectWebcam(callback) {
  let md = navigator.mediaDevices;
  if (!md || !md.enumerateDevices) return callback(false);
  md.enumerateDevices().then(devices => {
    callback(devices.some(device => 'videoinput' === device.kind));
  })
}

//Put variables in global scope to make them available to the browser console.
const video = document.querySelector('video');
const canvas = window.canvas = document.querySelector('canvas');
canvas.width = 640;
canvas.height = 480;
var arrImg = [];
var count = 0;
var countMinus = 4;
$(document).ready(function(){
	$("#startAndStop").click(function(){
		detectWebcam(function(hasWebcam) {
			if(hasWebcam) {
				$("#startAndStop").prop("disabled",true);
				$("#startAndStop").html("Đang xử lý ("+countMinus+") ...");
				process();	
			} else {
				toastr.error("Vui lòng bật camera và cấp quyền sử dụng camera để thực hiện quá trình xác thực.");
			}
		  console.log('Webcam: ' + (hasWebcam ? 'yes' : 'no'));
		})
	});
	
});
function process() {
	try {
		canvas.width = video.videoWidth;
		canvas.height = video.videoHeight;
		canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
		
		postCanvasToURL();
		
		if(countMinus > 0)
			countMinus --;
		
		$("#startAndStop").html("Đang xử lý ( "+countMinus+" ) ...");
		
		setTimeout(process, 1500);
	} catch (err) {
		console.log(err);
	}
}

function postCanvasToURL() { 
	if(count < 4) {
		var type = "image/jpeg"
	    var data = document.getElementById("canvasOutput").toDataURL(type);
	    data = data.replace('data:' + type + ';base64,', ''); 
	    if(arrImg.length < 3)
			arrImg.push(data)
		if(count == 3) {
			$("#listImage").html(arrImg.toString());
			$(".loading").show();
			$("#submitForm").submit();
		}
		count ++;
	}
}

const constraints = {
	audio: false,
	video: true
};

function handleSuccess(stream) {
	window.stream = stream; 
	video.srcObject = stream;
}

function handleError(error) {
	toastr.error("Vui lòng bật camera và cấp quyền sử dụng camera để thực hiện quá trình xác thực.");
	console.log('navigator.MediaDevices.getUserMedia error: ', error.message, error.name);
}

navigator.mediaDevices.getUserMedia(constraints).then(handleSuccess).catch(handleError);
</script>
</c:if>
<%@include file="../layout/footer.jsp"%>
		