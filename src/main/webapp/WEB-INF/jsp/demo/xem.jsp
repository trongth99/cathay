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
		<h2>Xem thông tin khách hàng</h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Tên khách hàng</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.hoVaTen }" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Số điện thoại</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.dienThoai }" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Số giấy tờ</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.soGiayTo }" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Giới tính</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.gioiTinh }" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Năm sinh</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<fmt:formatDate pattern = "dd/MM/yyyy" value = "${userInfo.namSinh }" />" />
				</div>
			</div>
			<div class="col-sm-6 clearfix">
				<div class="form-group  has-feedback">
					<label class="control-label">Quê quán</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.queQuan }" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Nơi cấp</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.noiCap }" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label">Nơi trú</label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="${userInfo.noiTru }" />
				</div>
			</div>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Ảnh khách hàng</label>
					<img src="${anhKhachHang }" style="width: 100%;"/>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Facebook Id</label>
					<a href="https://www.facebook.com/${userInfo.facebookId }" target="_blank">${userInfo.facebookId }</a>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Ảnh facebook</label>
					<c:forEach items="${images}" var="item" varStatus="status">
						<img src="${item }" style="width: 100%;"/>
					</c:forEach>
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
