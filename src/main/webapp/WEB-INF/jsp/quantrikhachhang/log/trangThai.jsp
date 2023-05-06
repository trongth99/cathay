<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@include file="../../layout/js.jsp"%>
<style type="text/css">
.help-block{
color: #dc3545;
}
</style>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Cập nhật trạng thái eKYC</h2>
	</div>
	<div class="modal-body">
		<form id="submitFormModal" action="${contextPath}/danh-sach-khach-hang/trang-thai?id=${id }" method="post" enctype="multipart/form-data">
			<div class="row clearfix">
				<div class="row">	
					<div class="form-group col-sm-12">
						<input type="file" class="form-control-file" name="anhCaNhanKtt" id="anhCaNhanKtt" accept=".jpg,.jpeg,.png" style="display: none;"/>
						<button class="btn btn-default btn-lg" onclick="document.getElementById('anhCaNhanKtt').click(); this.blur();" style="width: 100%" type="button">
			                <span class="glyphicon glyphicon-cloud-upload"></span>
			                <spring:message code="ekycdn.anh_ca_nhan" />                                       
			            </button>
			            <small id="nameAnhCaNhanKtt" style="display: none;"></small><br/>
			            <c:if test="${empty anhCaNhan }">
			            	<img src="" style="display: none;max-width: 100%;max-height: 200px" id="base64AnhCaNhanKttImg" class="img-thumbnail"/>
			            </c:if>
			            <c:if test="${not empty anhCaNhan }">
			            	<img src="data:image/jpeg;base64,<c:out value="${anhCaNhan }"/>" style="max-width: 100%;max-height: 200px" id="base64AnhCaNhanKttImg" class="img-thumbnail"/>
			            </c:if>
						<textarea id="base64AnhCaNhanKtt" style="display: none;" name="base64AnhCaNhan"><c:out value="${anhCaNhan }"/></textarea>
					</div>
				</div>
				<div style="clear: both;"></div>
				<br/><br/>
				<div class="col-md-12 mb-0 text-right">
					<button type="button" class="btn btn-primary btn-sm" id='buttonSub'>
						<i class="fa fa-save"></i> 
						<span>Cập nhật trạng thái eKYC</span>
					</button>
					<button class="btn btn-danger btn-sm" data-dismiss="modal">
						<i class="fa fa-times"></i>
						<span><spring:message code="thoat" /></span>
					</button>
				</div>
				<br/><br/>
			</div>	
		</form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	addEventFile("anhCaNhanKtt", "base64AnhCaNhanKtt", "nameAnhCaNhanKtt");
	$("#buttonSub").click(function(){
		if ($("#base64AnhCaNhanKtt").html() == "") {
			toastr.error("Tải lên ảnh cá nhân")
		} else {
			$("#submitFormModal").submit();
		}
	});
});
function addEventFile(fileInput, idBase64, idNameShow) {
	$("#"+fileInput).change(function(){
		if(ValidateSingleInput(this)) {
			$("#"+idBase64).html("");
			var file = document.querySelector('#'+fileInput).files[0];
			getBase641(file, idBase64);
		}
		$("#"+idNameShow).html($("#"+fileInput).val());
		if($("#"+idNameShow).html()!="") {
			$("#"+idNameShow).show();
		}
	});
	$("#"+fileInput).click(function(){
		$("#"+fileInput).val("");
		$("#"+idNameShow).hide();
		$("#"+idBase64+"Img").attr("src", "");
		$("#"+idBase64+"Img").hide();
	})
}

function getBase641(file, idImg) {
	if (file) {
		if(file.type.match(/image.*/)) {
			console.log('An image has been loaded');
			var reader = new FileReader();
			reader.onload = function (readerEvent) {
				var image = new Image();
				image.onload = function (imageEvent) {
					var canvas = document.createElement('canvas'),
					max_size = 1000,// TODO : pull max size from a site config
					width = image.width,
					height = image.height;
					if (width > height) {
						if (width > max_size) {
							height *= max_size / width;
							width = max_size;
						}
					} else {
						if (height > max_size) {
							width *= max_size / height;
							height = max_size;
						}
					}
					canvas.width = width;
					canvas.height = height;
					canvas.getContext('2d').drawImage(image, 0, 0, width, height);
					var dataUrl = canvas.toDataURL('image/jpeg');
				
					$("#"+idImg).html( dataUrl.replace("data:image/jpeg;base64,", ""));
					$("#"+idImg+"Img").attr("src", dataUrl);
					$("#"+idImg+"Img").show();
				}
				image.src = readerEvent.target.result;
			}
			reader.readAsDataURL(file);
		}

	}
}
</script>
<%@include file="../../layout/footerAjax.jsp"%>
