<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<style type="text/css" media="screen">
#editor {
	position: absolute;
	top: 0;
	right: 0;
	bottom: 0;
	left: 0;
}
</style>
<script>
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

var contentType = '${contentType}';
var b64Data = '${file }';

var blob = b64toBlob(b64Data, contentType);
var blobUrl = URL.createObjectURL(blob);
$(document).ready(function(){
		$("#base64File").attr("src", blobUrl);	
});
var json = '${thongKeOcr.noiDungOcr}';

var editor = ace.edit("editor");
editor.getSession().setMode("ace/mode/json");
editor.setTheme("ace/theme/monokai");
editor.getSession().setTabSize(2);
editor.getSession().setUseWrapMode(true);
editor.setValue(JSON.stringify(JSON.parse(json), null, '\t'));
</script>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Xem log <c:out value="${thongKeOcr.maLanGoi }"/> (<c:out value="${thongKeOcr.maGiayTo }"/>)</h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
			<c:if test="${contentType eq 'application/pdf' }">
				<iframe id="base64File"
					style="width: 100%; height: 400px; border: 0;"></iframe>
			</c:if>
			<c:if test="${contentType ne 'application/pdf' }">
			<div style="width: 100%; height: 400px; overflow: auto;">
				<img alt="" src="data:image/jpeg;base64,${file }" id="myImg">
			</div>
			</c:if>
			<hr />
			<div
				style='clear: both; position: relative; width: 100%; min-height: 600px;'>
				<div id="editor"></div>
			</div>
		</div>
		<hr />
		<div class="col-md-12 mb-0 text-right">
			<button class="btn btn-danger btn-sm" data-dismiss="modal">
				<i class="fa fa-times"></i> <span>Đóng</span>
			</button>
		</div>
		<div style="clear: both;"></div>
	</div>
</div>
<%@include file="../layout/footerAjax.jsp"%>
