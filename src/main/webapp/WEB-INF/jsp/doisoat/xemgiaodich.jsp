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
<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2>Xem chi tiết giao dịch <c:out value="${maGiaoDich }"/></h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
			<div class="card-body table-responsive p-0">
	            <c:if test="${not empty logApis }">
	            	<table class="table table-bordered table-sm table-striped">
						<thead>
							<tr>
								<th style="max-width: 40px;">#</th>
								<th>Uri</th>
								<th>Trạng thái</th>
								<th style="width: 180px;">Thời gian</th>
								<th>Log ID</th>
								<th>Kết quả</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${logApis}" var="item" varStatus="status">
								<tr>
									<th scope="row">${ (status.index+1) }</th>
									<td style="padding-left: 5px;">${item.uri }</td>
									<td>
										<c:if test="${item.status  eq 200}">
											<b style="color: blue;">${item.status}</b>
										</c:if>
										<c:if test="${item.status  ne 200}">
											<b style="color: red;">${item.status}</b>
										</c:if>
									</td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.date }" /></td>
									<td><c:out value="${item.logId }"/></td>
									<td><c:out value="${item.response }"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
	            </c:if>
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
