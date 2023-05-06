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


<form method="POST" action="${contextPath }/danh-sach-khach-hang-bh/upload-file" id='submitFormModal' enctype="multipart/form-data">
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<div class="modal-header">
			<h2>
				Tải danh sách ( <a href="${contextPath }/danh-sach-khach-hang-bh/template" style="color: red;">Download template</a> )
			</h2>
		</div>
		<div class="modal-body">
			<div class="row clearfix">
				<div class="col-md-12 mb-0">
					<label>Tải danh sách (*)</label>
					<div class="form-group form-float">
						<div class="form-line">
							<input type="file" class="form-control" value="" name="file">
						</div>
					</div>
				</div>
			</div>
				<div class="col-md-12 mb-0 text-right" style="margin-bottom: 20px;">
					<div id="ajax-load" class="preloader pl-size-xs hidden">
						<div class="spinner-layer pl-red-grey">
							<div class="circle-clipper left">
								<div class="circle"></div>
							</div>
							<div class="circle-clipper right">
								<div class="circle"></div>
							</div>
						</div>
					</div>
					<button type="submit" class="btn btn-primary btn-sm">
						<i class="fa fa-save"></i> <span><spring:message code="luu" /></span>
					</button>
					<button class="btn btn-danger btn-sm" data-dismiss="modal">
						<i class="fa fa-times"></i> <span><spring:message code="dong" /></span>
					</button>
				</div>
			</div>
		</div>
</form>
<script type="text/javascript">
$(document).ready(function() {

	$("#submitFormModal").validate({
		rules : {
			file : {
				required : true
			}
		}
	});
});
</script>

<%@include file="../../layout/footerAjax.jsp"%>
