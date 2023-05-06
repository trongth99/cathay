<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			Danh sách cấu hình
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active">Danh sách cấu hình</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              		<button class="btn btn-block btn-primary btn-xs"
	                             data-toggle="modal" data-target="#largeModal" type="button"
	                             onclick="loadAdd('${contextPath}/config/them-moi')">
	                             <i class="fa fa-plus" aria-hidden="true"></i>
	                             
	                         <span><spring:message code="them_moi" /></span>
	                     </button>
	              </h3>
	
	              <div class="box-tools">
	                <div class="form-inline input-group-sm" style="width: 100%;">
						<input class="form-control form-control-sm" type="text" value="<c:out value='${ma}'/>" name="ma" placeholder="Mã cấu hình" data-toggle="tooltip" title="Mã cấu hình"/>
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
								<th>Mã</th>
								<th>Giá trị</th>
								<th>Mô tả</th>
								<th style="width: 180px;">Trạng thái</th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${configs}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${status.index+1 }</th>
									<td><c:out value='${item.ma }'/></td>
									<td><c:out value='${item.giaTri }'/></td>
									<td><c:out value='${item.moTa }'/></td>
									<td>
										<c:if test="${item.trangThai eq 1 }">
											<span class="text-primary">Hoạt động</span>
										</c:if>
										<c:if test="${item.trangThai ne 1 }">
											<span class="text-danger">Không hoạt động</span>
										</c:if>
									</td>
									<td class="text-center">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/config/sua?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;">
											<i class="fa fa-edit"></i>
										</a>
										<a href="javascript:void(0)" onclick="deleteRC('${contextPath}/config/xoa?id=${item.id}')" class="text-danger" style="margin-left: 5px;">
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