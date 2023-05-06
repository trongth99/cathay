<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%@include file="../layout/js.jsp"%>
<style type="text/css">
.help-block{
color: #dc3545;
}
</style>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2><spring:message code="sao_ke_ngan_hang_da_ky" /></h2>
	</div>
	<div class="modal-body">
			
		<div class="row clearfix">
		
			<div class="col-sm-12">
			<c:if test="${not empty path }">
				<hr/>
				<!-- <h3 style="margin-left: 13px;">Giấy chứng nhận</h3> -->
				<div class="col-sm-12">
					<iframe id="base64FileSeaBank" style="width: 100%; height: 400px; border: 0;"></iframe>
				</div>
			</c:if>
			</div>
			<div style="clear: both;"></div>
			
			<div style="clear: both;"></div>
			
		</div> 
		<div style="clear: both;"></div>
		<br/><br/>
		<div class="col-md-12 mb-0 text-right">
			<button class="btn btn-danger btn-sm" data-dismiss="modal">
				<i class="fa fa-times"></i>
				<span><spring:message code="thoat" /></span>
			</button>
		</div>
		<br/><br/>
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


var b64fileSeaBank = '${path }';
console.log(b64fileSeaBank)
var blofileSeaBank = b64toBlob(b64fileSeaBank, contentType);
var blobUrlfileSeaBank = URL.createObjectURL(blofileSeaBank);

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip()
	

	$("#base64FileSeaBank").attr("src", blobUrlfileSeaBank);
	
});


</script>
<%@include file="../layout/footerAjax.jsp"%>
