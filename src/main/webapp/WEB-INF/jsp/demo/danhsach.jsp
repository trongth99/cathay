<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			Danh sách khách hàng
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active">Danh sách khách hàng</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              		<button class="btn btn-block btn-primary btn-xs"
	                             type="button"
	                             onclick="loadAdd('${contextPath}/demo-check-in/them-moi')">
	                             <i class="fa fa-plus" aria-hidden="true"></i>
	                             
	                         <span><spring:message code="them_moi" /></span>
	                     </button>
	              </h3>
	
	              <div class="box-tools">
	                <div class="form-inline input-group-sm" style="width: 100%;">
						<input class="form-control form-control-sm" type="text" value="${name}" name="name" placeholder="Tên khách hàng" data-toggle="tooltip" title="Tên khách hàng"/>
	                    <button type="submit" class="btn btn-sm btn-primary"><i class="fa fa-search"></i></button>
	                </div>
	              </div>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body table-responsive no-padding">
	              	<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="width: 50px;text-align: center;">#</th>
								<th>Tên khách hàng</th>
								<th>Số giấy tờ</th>
								<th>Ngày sinh</th>
								<th>Face Id</th>
								<th>Điện thoại</th>
								<th>Facebook Id</th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${demoUsers}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${status.index+1 }</th>
									<td>${item.hoVaTen }</td>
									<td>${item.soGiayTo }</td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy" value = "${item.namSinh }" /></td>
									<td>${item.faceId }</td>
									<td>${item.dienThoai }</td>
									<td>${item.facebookId }</td>
									<td class="text-center">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/demo-check-in/xem?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;">
											<i class="fa fa-eye"></i>
										</a>
										<a href="javascript:void(0)" onclick="deleteRC('${contextPath}/demo-check-in/xoa?id=${item.id}')" class="text-danger" style="margin-left: 5px;">
											<i class="fa fa-trash"></i>
										</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					
					<%@include file="../layout/paginate.jsp"%>
	            </div>
	            <!-- /.box-body -->
	          </div>
			
		</section>
	</form>
	<!-- /.content -->
</div>
<%@include file="../layout/footer2.jsp"%>