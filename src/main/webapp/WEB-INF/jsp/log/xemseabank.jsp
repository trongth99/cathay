<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style type="text/css" media="screen">
    #matTruoc,#matSau,#param,#response { 
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
    }
</style>
<script>
	var params= '${params}';
	var response= '${response}';
	
	function viewJson (json, id) {
		var editor = ace.edit(id);
		editor.getSession().setMode("ace/mode/json");
		editor.setTheme("ace/theme/monokai");
		editor.getSession().setTabSize(2);
		editor.getSession().setUseWrapMode(true);
		editor.setValue(JSON.stringify(JSON.parse(json), null, '\t'));
	}
	
	if(params && params != '')
		viewJson('${params}', 'param');
	if(response && response != '')
		viewJson('${response}', 'response');
</script>
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Xem log <c:out value="${logId }"/></h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
		<%-- <c:if test="${logApiSeabank.status  eq 200 || logApiSeabank.status  eq 201}"> --%>
		    <div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Họ và tên</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${logApiSeaBank.fullName }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Số chứng minh thư</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${logApiSeaBank.idCard }"/>" />
				</div>
			</div>
			<div class="col-sm-6" style="margin-bottom: 10px;">
				<div class="form-group  has-feedback">
					<label class="control-label">Số điện thoại</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${logApiSeaBank.phone }"/>" />
				</div>
			</div>  
			
			<div class="col-sm-12">
			
			 
			</div>
			<%-- </c:if> --%>
			<br/>
			</div>
		<div class="row clearfix">
			<div style="word-break: break-all;">
				Params: <div style='position: relative;width: 100%;height: 300px;'><div id='param'></div></div>
				<hr style="border-top: 1px solid #CCC;"/>
             	Response:<div style='position: relative;width: 100%;height: 300px;'><div id='response'></div></div>			
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
