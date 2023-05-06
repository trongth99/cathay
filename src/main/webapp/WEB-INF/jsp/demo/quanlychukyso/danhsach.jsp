<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../../layout/header2.jsp"%>
<%@include file="../../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			Quản lý chữ ký số
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active">Quản lý chữ ký số</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              </h3>
	
	              <div class="box-tools">
	              </div>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body table-responsive no-padding">
	              	<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="width: 50px;text-align: center;">#</th>
								<th>Mã chữ ký</th>
								<th>Mật khẩu</th>
								<th>Chữ ký mặc định</th>
								<th>Người đại diện</th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${quanLyChuKySos}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${(status.index+1) }</th>
									<td><c:out value="${item.ma }"></c:out> </td>
									<td><c:out value="${item.matKhau }"></c:out></td>
									<td>
										<c:if test="${item.macDinh eq '1' }">
											Sử dụng mặc định
										</c:if>
									</td>
									<td><c:out value="${item.nguoiDaiDien }"></c:out></td>
									<td class="text-center">
<%-- 										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/quan-ly-chu-ky-so/sua?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;"> --%>
<!-- 											<i class="fa fa-edit"></i> -->
<!-- 										</a> -->
										<a href="javascript:void(0)" onclick="alertRC('${contextPath}/quan-ly-chu-ky-so/mac-dinh?id=${item.id}', 'Bạn có chắc muốn thay đổi chữ ký mặc định')" class="text-info" style="margin-left: 5px;">
											<i class="fa fa-check-square"></i>
										</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					
					<%@include file="../../layout/paginate.jsp"%>
	            </div>
	            <!-- /.box-body -->
	          </div>
			
		</section>
	</form>
	<!-- /.content -->
</div>
<%@include file="../../layout/footer2.jsp"%>