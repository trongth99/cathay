<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			<spring:message code="danh_sach_nguoi_dung" />
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active"><spring:message code="danh_sach_nguoi_dung" /></li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              		<button class="btn btn-primary btn-xs"
	                             data-toggle="modal" data-target="#largeModal" type="button"
	                             onclick="loadAdd('${contextPath}/nguoi-dung/them-moi')">
	                             <i class="fa fa-plus" aria-hidden="true"></i>
	                             
	                         <span><spring:message code="them_moi" /></span>
	                     </button>
	                    <a class="btn btn-primary btn-xs" type="button" id="export" href="javascript:void(0)">
	                        <span><i class="fa fa-download"></i> Export</span>
	                    </a>
	              </h3>
	
	              <div class="box-tools">
	                <div class="form-inline input-group-sm" style="width: 100%;">
						<input class="form-control form-control-sm" type="text" value="<c:out value='${s_uname}'/>" name="s_uname" placeholder="<spring:message code="ten_dang_nhap" />" data-toggle="tooltip" title="<spring:message code="ten_dang_nhap" />"/>
						<input class="form-control form-control-sm" type="text" value="<c:out value='${s_fname}'/>" name="s_fname" placeholder="<spring:message code="ho_va_ten" />" data-toggle="tooltip" title="<spring:message code="ho_va_ten" />"/>
						<input class="form-control form-control-sm" type="text" value="<c:out value='${s_email}'/>" name="s_email" placeholder="<spring:message code="email" />" data-toggle="tooltip" title="<spring:message code="email" />"/>
						<select class="form-control form-control-sm" name="s_status" data-toggle="tooltip" title="<spring:message code="trang_thai" />">
							<option value="" ${empty s_status ? 'selected' : ''}><spring:message code="tat_ca" /></option>
							<option value="0" ${s_status eq '0' ? 'selected': ''}><spring:message code="khong_hoat_dong" /></option>
							<option value="1" ${s_status eq '1' ? 'selected': ''}><spring:message code="hoat_dong" /></option>
						</select>
	                    <button type="button" class="btn btn-sm btn-primary" id="search"><i class="fa fa-search"></i></button>
	                </div>
	              </div>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body table-responsive no-padding">
	              	<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="width: 50px;text-align: center;">#</th>
								<th><spring:message code="ten_dang_nhap" /></th>
								<th><spring:message code="ho_va_ten" /></th>
								<th><spring:message code="email" /></th>
								<th>Khu vực</th>
								<th>Nhóm người dùng</th>
								<th style="width: 180px;"><spring:message code="trang_thai" /></th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${userInfos}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${ (currentPage-1)*20+(status.index+1) }</th>
									<td><c:out value='${item.username }'/></td>
									<td><c:out value='${item.fullName }'/></td>
									<td><c:out value='${item.email }'/></td>
									<td><c:out value='${item.khuVuc }'/></td>
									<td><c:out value='${item.nhomNguoiDung }'/></td>
									<td>
										<c:if test="${item.status eq 1 }">
											<span class="text-primary"><spring:message code="hoat_dong" /></span>
										</c:if>
										<c:if test="${item.status ne 1 }">
											<span class="text-danger"><spring:message code="khong_hoat_dong" /></span>
										</c:if>
									</td>
									<td class="text-center">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/nguoi-dung/sua?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;">
											<i class="fa fa-edit"></i>
										</a>
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/nguoi-dung/dat-mat-khau?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;">
											<i class="fa fa-key"></i>
										</a>
										<a href="javascript:void(0)" onclick="deleteRC('${contextPath}/nguoi-dung/xoa?id=${item.id}')" class="text-danger" style="margin-left: 5px;">
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
<script type="text/javascript">
$(document).ready(function(){
	$('.datepicker').datepicker({
	     autoclose: true,
	     format: 'dd/mm/yyyy'
	})
	$("#export").click(function(){
		$("#submitForm").attr("action", "${contextPath}/nguoi-dung/report");
		$("#submitForm").submit();
	});
	$("#search").click(function(){
		$("#submitForm").attr("action", "${contextPath}/nguoi-dung");
		$("#submitForm").submit();
	});
});
</script>
<%@include file="../layout/footer2.jsp"%>