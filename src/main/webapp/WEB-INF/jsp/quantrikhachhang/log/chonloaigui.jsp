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
<springForm:form method="POST" action="${contextPath }/danh-sach-khach-hang-bh/ky-so/gui-mail" id='submitFormModal'>
	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
		<div class="modal-header">
			<h2>Chọn hình thức gửi link ký cho khách hàng</h2>
		</div>
		<div class="modal-body">
			<div class="row clearfix">
				<div class="col-sm-12">
					<div class="form-group  has-feedback">
						<label class="control-label"><input type="checkbox" value="email" name="loaiGui"/>&nbsp;&nbsp; Gửi qua email</label><br/>
						<label class="control-label"><input type="checkbox" value="sms" name="loaiGui"/>&nbsp;&nbsp; Gửi qua SMS</label>
						<input type="hidden" name="id" value="${id }"/>
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
			<br/><br/>
			<div class="col-md-12 mb-0">
				 <button type="submit" class="btn btn-primary btn-sm" id='buttonSub'>
					<i class="fa fa-save"></i> 
					<span>Gửi hợp đồng</span>
				</button>
				<button class="btn btn-danger btn-sm" data-dismiss="modal">
					<i class="fa fa-times"></i>
					<span><spring:message code="thoat" /></span>
				</button>
			</div>
			<br/><br/>
		</div>
	</div>
</springForm:form>
<script type="text/javascript">

</script>
<%@include file="../../layout/footerAjax.jsp"%>
