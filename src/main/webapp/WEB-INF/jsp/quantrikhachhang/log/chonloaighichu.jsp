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
<springForm:form method="POST" action="${contextPath }/danh-sach-khach-hang/ky-so/ghi-chu" id='submitFormModal'>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<div class="modal-header">
			<h2>Chọn ghi chú</h2>
		</div>
		<div class="modal-body">
			<div class="row clearfix">
				<div class="col-sm-12">
					<select class="form-control show-tick" name="ghiChu">
						<c:forEach items="${danhSachGhiChu}" var="item" varStatus="status">
							<option value='<c:out value="${item }"></c:out>'><c:out value="${item }"></c:out></option>
						</c:forEach>
					</select>
					<input type="hidden" name="id" value="${id }"/>
				</div>
			</div>
			<div style="clear: both;"></div>
			<br/><br/>
			<div class="row clearfix">
				<div class="col-md-12 mb-0">
					<button type="submit" class="btn btn-primary btn-sm" id='buttonSub'>
						<i class="fa fa-save"></i> 
						<span>Ghi chú</span>
					</button>
					<button class="btn btn-danger btn-sm" data-dismiss="modal">
						<i class="fa fa-times"></i>
						<span><spring:message code="thoat" /></span>
					</button>
				</div>
			</div>
			<br/><br/>
		</div>
	</div>
</springForm:form>
<script type="text/javascript">

</script>
<%@include file="../../layout/footerAjax.jsp"%>
