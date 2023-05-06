<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.12/ace.js" type="text/javascript" charset="utf-8"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			Đối soát kiểm tra
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active">Đối soát kiểm tra</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
	            <div class="box-header">
	              <h3 class="box-title">
	              		<a class="btn btn-block btn-primary btn-xs" type="button" id="export" href="javascript:void(0)">
	                         <span>Export</span>
	                     </a>
	              </h3>
	
	              <div class="box-tools">
	                <div class="form-inline input-group-sm" style="width: 100%;">
						<input class="form-control form-control-sm" type="text" value="<c:out value="${maLanGoi}"/>" name="maLanGoi" placeholder="Mã lần gọi" data-toggle="tooltip" title="Mã lần gọi" autocomplete="off"/>
						<select class="form-control form-control-sm" name="khachHang" data-toggle="tooltip" title="Khách hàng" id="khachHang">
							<c:forEach items="${quanLyKhachHangs }" var="item">
								<option value="<c:out value="${item.code}"/>" <c:if test="${khachHang eq item.code }">selected</c:if>><c:out value="${item.name}"/></option>
							</c:forEach>
						</select>
						<select class="form-control form-control-sm" name="loai" data-toggle="tooltip" title="Loại kiểm tra" id="loai">
							<option value="" ${empty maGiayTo ? 'selected' : ''}>Tất cả</option>
							<option value="kiemTraDuLieuDinhDanh" ${loai eq 'kiemTraDuLieuDinhDanh' ? 'selected': ''}>Kiểm tra dữ liệu định danh</option>
							<option value="kiemTraMaBhxh" ${loai eq 'kiemTraMaBhxh' ? 'selected': ''}>Kiểm tra BHXH</option>
							<option value="kiemTraThongTinDoanhNghiep" ${loai eq 'kiemTraThongTinDoanhNghiep' ? 'selected': ''}>Kiểm tra thông tin doanh nghiệp</option>
						</select>
						<input class="form-control form-control-sm datepicker" type="text" value="<c:out value="${fromDate}"/>" id="fromDate" name="fromDate" placeholder="Từ ngày" data-toggle="tooltip" title="Từ ngày" autocomplete="off"/>
						<input class="form-control form-control-sm datepicker" type="text" value="<c:out value="${toDate}"/>" id="toDate" name="toDate" placeholder="Đến ngày" data-toggle="tooltip" title="Đến ngày" autocomplete="off"/>
	                    <button type="submit" class="btn btn-sm btn-primary"><i class="fa fa-search"></i></button>
	                </div>
	              </div>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body table-responsive no-padding">
	              	<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="max-width: 40px;text-align: center;">#</th>
								<th>Mã lần gọi</th>
								<th>Loại kiểm tra</th>
								<th style="width: 180px;">Ngày tạo</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${logApis}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${ (currentPage-1)*10+(status.index+1) }</th>
									<td style="padding-left: 5px;"><a href="javascript:void(0)" onclick="loadEdit('${contextPath}/doi-soat-kiem-tra/xem?id=${item.id}&loaiGoi=<c:out value="${loaiGoi }"/>')" data-toggle="modal" data-target="#largeModal" class="text-info"><c:out value="${item.maLanGoi }"/></a></td>
									<td><c:out value="${item.loai }"/></td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.ngayTao }" /></td>
									<td class="text-center">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-log/xem?logId=${item.requestId}&time=<fmt:formatDate pattern = "yyyy-MM-dd" value = "${item.ngayTao }" />')" data-toggle="modal" data-target="#largeModal" class="text-info">
											<i class="fa fa-eye"></i>
										</a>
										<a style="margin-left: 5px;" href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-log/unzip?time=<fmt:formatDate pattern = "yyyy-MM-dd" value = "${item.ngayTao }" />')" data-toggle="modal" data-target="#largeModal" class="text-info">
											<i class="fa fa-sticky-note"></i>
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
		$("#export").click(function(){
			window.location.href="${contextPath}/doi-soat-kiem-tra/export?toDate="+$("#toDate").val()+"&fromDate="+$("#fromDate").val()+"&khachHang="+$("#khachHang").val()+"&loai="+$("#loai").val();
		});
		$('.datepicker').datepicker({
		     autoclose: true,
		     format: 'dd/mm/yyyy'
		})
	});
</script>	

<%@include file="../layout/footer2.jsp"%>