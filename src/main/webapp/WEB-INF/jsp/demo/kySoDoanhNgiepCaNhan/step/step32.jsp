<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header.jsp"%>
<%@include file="../layout/js.jsp"%>

<div class="col-md-12 register-right">
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
			<h3 class="register-heading">Đăng ký cấp mới chứng thư số FPT-CA và <br/> điều khoản sử dụng</h3>
			<div class="row register-form">
				<form action="${contextPath }/khach-hang/ky-so/step32" style="width: 100%;" method="post" id="submitForm">
					<div id="pdfFrom">
						<div class="col-sm-12">
							<iframe id="base64File1" style="width: 100%; height: 700px; border: 0;" src="${LINK_ADMIN }/viewpdf?file=${LINK_ADMIN }/viewpdf/byte/${pdfSign}"></iframe>
							<textarea id="chuKySo" name="chuKySo" style="display: none;"></textarea>
						</div>
						<hr/>
						<h5>Vui lòng ký xác nhận</h5>
						<div class="col-sm-12" id="divCanvas">
							<canvas id="myCanvas" style="border:2px solid black; width: 100%;height: 300px;"></canvas>
							<input type="button" class="" id="" value="Làm mới" onclick="clearArea()"/>
							<input type="button" class="" id="btnRegister" value="Đồng ý sử dụng" onclick="save()" disabled="disabled"/>
						</div>
					</div>
				</form>
				
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
var mousePressed = false;
var lastX, lastY;
var ctx;
var check = 0;
function InitThis() {
	canv = document.getElementById('myCanvas');
    ctx = canv.getContext("2d");
// 	$("#myCanvas").width($("#divCanvas").width());
    canv.width  = $("#divCanvas").width();
    canv.height = 300;
    $('#myCanvas').mousedown(function (e) {
        mousePressed = true;
        Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, false);
    });

    $('#myCanvas').mousemove(function (e) {
        if (mousePressed) {
            Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, true);
        }
    });

    $('#myCanvas').mouseup(function (e) {
        mousePressed = false;
    });
    
	$('#myCanvas').mouseleave(function (e) {
        mousePressed = false;
    });
	
	$('#myCanvas').on({ 'touchstart' : function(e){ 
	    e.preventDefault();
	    mousePressed = true;
        Draw(e.pageX - $(this).offset().left, e.pageY - $(this).offset().top, false);
	} });  
	var canvas = document.getElementById("myCanvas");
	$('#myCanvas').on({ 'touchmove' : function(e){ 
	    mousePressed = true;
	    mousePos = getTouchPos(canvas, e);
	    if (mousePressed) {
            Draw(mousePos.x , mousePos.y, true);
        }
	} });    
	$('#myCanvas').on({ 'touchend' : function(e){ 
	    e.preventDefault();
	    mousePressed = false;
	} }); 
}
function getTouchPos(canvasDom, touchEvent) {
  var rect = canvasDom.getBoundingClientRect();
  return {
    x: touchEvent.touches[0].clientX - rect.left,
    y: touchEvent.touches[0].clientY - rect.top
  };
}
function Draw(x, y, isDown) {
    if (isDown) {
    	check ++;
    	if(check > 40) {
    		$("#btnRegister").prop("disabled", false);
    	}
        ctx.beginPath();
        ctx.strokeStyle = 'black';
        ctx.lineWidth = '1';
        ctx.lineJoin = "round";
        ctx.moveTo(lastX, lastY);
        ctx.lineTo(x, y);
        ctx.closePath();
        ctx.stroke();
    }
    lastX = x; lastY = y;
}
function save() {
	var canvas = document.getElementById("myCanvas");
	 var type = "image/png"
     var dataURL = canvas.toDataURL(type);
    dataURL = dataURL.replace('data:' + type + ';base64,', '');
    $("#chuKySo").html(dataURL);
    $("#submitForm").submit();
}
function clearArea() {
	check = 0;
	$("#btnRegister").prop("disabled", true);
    // Use the identity matrix while clearing the canvas
    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
}

</script>
        
<script type="text/javascript">
$(document).ready(function(){
	InitThis();
});
</script>

<%@include file="../layout/footer.jsp"%>
			
