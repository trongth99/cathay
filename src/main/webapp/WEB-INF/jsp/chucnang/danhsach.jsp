<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			<spring:message code="danh_sach_chuc_nang" text="Danh sách chức năng" />
		</h1>
		<ol class="breadcrumb">
			<li>
				<a href="${contextPath}/">
					<spring:message code="trang_chu" />
				</a>
			</li>
			<li class="active">
				<spring:message code="danh_sach_chuc_nang" text="Danh sách chức năng" />
			</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
				<div class="box-header">
					<h3 class="box-title">
						<button class="btn btn-block btn-primary btn-xs" data-toggle="modal" data-target="#largeModal" type="button" onclick="loadAdd('${contextPath}/chuc-nang/them-moi')">
							<i class="fa fa-plus" aria-hidden="true"></i>

							<span>
								<spring:message code="them_moi" />
							</span>
						</button>
					</h3>

					<div class="box-tools">
						<div class="form-inline input-group-sm" style="width: 100%;">
							<input class="form-control form-control-sm" type="text" value='<c:out value="${url }"/>' name="url" placeholder="Url" data-toggle="tooltip" title="Url" />
							<button type="submit" class="btn btn-sm btn-primary">
								<i class="fa fa-search"></i>
							</button>
						</div>
					</div>
				</div>
				<!-- /.box-header -->
				<div class="box-body table-responsive no-padding">
					<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="width: 50px; text-align: center;">#</th>
								<th>
									<spring:message code="ten_chuc_nang" text="Tên chức năng" />
								</th>
								<th>
									<spring:message code="url" text="Url" />
								</th>
								<th style="width: 180px;">
									<spring:message code="trang_thai" />
								</th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${userModules }" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${ (currentPage-1)*20+(status.index+1) }</th>
									<td>
										<c:out value="${item.name }" />
									</td>
									<td>
										<c:out value="${item.url }" />
									</td>
									<td>
										<c:if test="${item.status eq 1 }">
											<span class="text-primary">
												<spring:message code="hoat_dong" />
											</span>
										</c:if>
										<c:if test="${item.status ne 1 }">
											<span class="text-danger">
												<spring:message code="khong_hoat_dong" />
											</span>
										</c:if>
									</td>
									<td class="text-center">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/chuc-nang/sua?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info">
											<i class="fa fa-edit"></i>
										</a>
										<a href="javascript:void(0)" onclick="deleteRC('${contextPath}/chuc-nang/xoa?id=${item.id}')" class="text-danger" style="margin-left: 5px;">
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

<!-- /.content-wrapper -->
<%@include file="../layout/footer2.jsp"%>