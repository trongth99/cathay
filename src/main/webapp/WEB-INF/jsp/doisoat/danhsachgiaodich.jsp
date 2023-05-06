<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/ace/1.4.12/ace.js" type="text/javascript" charset="utf-8"></script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			Đối soát giao dịch
		</h1>
		<ol class="breadcrumb">
			<li><a href="${contextPath}/"><spring:message code="trang_chu" /></a></li>
			<li class="active">Đối soát giao dịch</li>
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
	                	<input class="form-control form-control-sm" type="text" value="<c:out value="${soDienThoai}"/>" name="soDienThoai" id="soDienThoai" placeholder="Số điện thoại" data-toggle="tooltip" title="Số điện thoại" autocomplete="off"/>
	                	<input class="form-control form-control-sm" type="text" value="<c:out value="${soCmt}"/>" name="soCmt" id="soCmt" placeholder="Số chứng minh thư" data-toggle="tooltip" title="Số chứng minh thư" autocomplete="off"/>
	                	<input class="form-control form-control-sm" type="text" value="<c:out value="${soHopDong}"/>" name="soHopDong" id="soHopDong" placeholder="Số hợp đồng" data-toggle="tooltip" title="Số hợp đồng" autocomplete="off"/>
	                	<input class="form-control form-control-sm" type="text" value="<c:out value="${hoVaTen}"/>" name="hoVaTen" id="hoVaTen" placeholder="Họ và tên" data-toggle="tooltip" title="Họ và tên" autocomplete="off"/>
						<input class="form-control form-control-sm" type="text" value="<c:out value="${maGiaoDich}"/>" name="maGiaoDich" id="maGiaoDich" placeholder="Mã giao dịch" data-toggle="tooltip" title="Mã giao dịch" autocomplete="off"/>
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
								<th>Mã giao dịch</th>
								<th style="width: 180px;">Ngày tạo</th>
								<th>Số điện thoại</th>
								<th>Họ và tên</th>
								<th>Số cmt</th>
								<th>Số hợp đồng</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${logApis}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${ (currentPage-1)*10+(status.index+1) }</th>
									<td style="padding-left: 5px;"><a href="javascript:void(0)" onclick="loadEdit('${contextPath}/doi-soat-giao-dich/xem?maGiaoDich=<c:out value="${item.codeTransaction}"/>')" data-toggle="modal" data-target="#largeModal" class="text-info"><c:out value="${item.codeTransaction }"/></a></td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.date }" /></td>
									<td><c:out value="${item.phone }"/></td>
									<td><c:out value="${item.fullName }"/></td>
									<td><c:out value="${item.idCard }"/></td>
									<td><c:out value="${item.idContract }"/></td>
									<td class="text-center">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/doi-soat-giao-dich/xem?maGiaoDich=<c:out value="${item.codeTransaction }"/>')" data-toggle="modal" data-target="#largeModal" class="text-info">
											<i class="fa fa-eye"></i>
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
			window.location.href="${contextPath}/doi-soat-giao-dich/export?toDate="+$("#toDate").val()+"&fromDate="+$("#fromDate").val()
					+"&maGiaoDich="+$("#maGiaoDich").val()
					+"&soDienThoai="+$("#soDienThoai").val()
					+"&soCmt="+$("#soCmt").val()
					+"&soHopDong="+$("#soHopDong").val()
					+"&hoVaTen="+$("#hoVaTen").val()
					;
		});
		$('.datepicker').datepicker({
		     autoclose: true,
		     format: 'dd/mm/yyyy'
		})
	});
</script>	

<%@include file="../layout/footer2.jsp"%>