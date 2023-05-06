<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style type="text/css" media="screen">
    #matTruoc,#matSau,#ocr,#response { 
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
    }
</style>
<script>
	var matTruoc= '${matTruoc}';
	var matSau= '${matSau}';
	var ocr= '${ocr}';
	var response= '${response}';
	if(matTruoc != "")
		viewJson('${matTruoc}', 'matTruoc');
	if(matSau != "")
		viewJson('${matSau}', 'matSau');
	if(ocr != "")
		viewJson('${ocr}', 'ocr');
	if(response != "")
		viewJson('${response}', 'response');
	
	function viewJson (json, id) {
		var editor = ace.edit(id);
		editor.getSession().setMode("ace/mode/json");
		editor.setTheme("ace/theme/monokai");
		editor.getSession().setTabSize(2);
		editor.getSession().setUseWrapMode(true);
		editor.setValue(JSON.stringify(JSON.parse(json), null, '\t'));
	}
</script>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Xem log <c:out value="${logId }"/></h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
			<div style="word-break: break-all;">
${logs }				
			</div>
		</div>
		<div class="col-md-12 mb-0 text-right">
			<button class="btn btn-danger btn-sm" data-dismiss="modal">
				<i class="fa fa-times"></i>
				<span>Đóng</span>
			</button>
		</div>
		<div style="clear: both;"></div>
	</div>
</div>
<%@include file="../layout/footerAjax.jsp"%>
