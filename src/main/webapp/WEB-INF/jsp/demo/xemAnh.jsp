<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="../layout/js.jsp"%>
<style type="text/css">
.help-block{
color: #dc3545;
}
</style>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Xem ảnh khách hàng</h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Ảnh khách hàng</label>
					<img src="${anhKhachHang }" style="width: 100%;"/>
				</div>
			</div>
		</div>
		<div class="col-md-12 mb-0 text-right">
			<button class="btn btn-danger btn-sm" data-dismiss="modal">
				<i class="fa fa-times"></i>
				<span>Đóng</span>
			</button>
		</div>
	</div>
</div>

<%@include file="../layout/footerAjax.jsp"%>
