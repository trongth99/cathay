<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="../../layout/js.jsp"%>
<style type="text/css">
.help-block{
color: #dc3545;
}
</style>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Xem thông tin TKTT SeABank</h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
		
		
			<div class="col-sm-12 clearfix">
				<div class="form-group  has-feedback">
					<label class="control-label">Số tài khoản</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${thongtingiaingan.soTaiKhoan }"/>" data-toggle="tooltip" title="<c:out value="${thongtingiaingan.soTaiKhoan }"/>"/>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Tên tài khoản</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${thongtingiaingan.hoVaTen }"/>" data-toggle="tooltip" title="<c:out value="${thongtingiaingan.hoVaTen }"/>"/>
				</div>
			</div>
				<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Chi nhánh</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${thongtingiaingan.coName }"/>" data-toggle="tooltip" title="<c:out value="${thongtingiaingan.coName }"/>"/>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">coCode</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${thongtingiaingan.coCode }"/>" data-toggle="tooltip" title="<c:out value="${thongtingiaingan.coCode }"/>"/>
				</div>
			</div>
		
		<br/><br/>
	</div>
</div>
</div>
<script type="text/javascript">
var b64toBlob = (b64Data, contentType='', sliceSize=512) => {
	var byteCharacters = atob(b64Data);
	var byteArrays = [];
	
	for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
		var slice = byteCharacters.slice(offset, offset + sliceSize);
		
		var byteNumbers = new Array(slice.length);
		for (let i = 0; i < slice.length; i++) {
			byteNumbers[i] = slice.charCodeAt(i);
		}
		
		var byteArray = new Uint8Array(byteNumbers);
		byteArrays.push(byteArray);
	}
	
	var blob = new Blob(byteArrays, {type: contentType});
	return blob;
}

var contentType = 'application/pdf';
var b64Data = '${file }';
var blob = b64toBlob(b64Data, contentType);
var blobUrl = URL.createObjectURL(blob);

var b64DataBaoHiem = '${fileHdBaoHiem }';
var blobBaoHiem = b64toBlob(b64DataBaoHiem, contentType);
var blobUrlBaoHiem = URL.createObjectURL(blobBaoHiem);

var b64DataanhTinNhan = '${anhTinNhan }';
var blobanhTinNhan = b64toBlob(b64DataanhTinNhan, contentType);
var blobUrlanhTinNhan = URL.createObjectURL(blobanhTinNhan);

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip();
	$("#base64File").attr("src", blobUrl);
	$("#base64FileAnhTinNhan").attr("src", blobUrlanhTinNhan);
	$("#base64FileBaoHiem").attr("src", blobUrlBaoHiem);	
});
</script>
<%@include file="../../layout/footerAjax.jsp"%>
