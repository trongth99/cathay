<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->

<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1><spring:message code="sao_ke_ngan_hang_da_ky" /></h1>
		<%-- <ol class="breadcrumb">
			<li>
				<a href="${contextPath}/">
					<spring:message code="trang_chu" />
				</a>
			</li>
			<li class="active">
				Số hợp đồng
			</li>
		</ol> --%>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
				<div class="box-header">
					<h3 class="box-title">
						<section class="content-header" style="display: flex;flex-direction: column;">

							
							<ol class="path breadcrumb" style="position: static;font-size: 15px;">
								<li><a href="${contextPath}/sao-ke-ngan-hang-da-ky"><spring:message code="sao_ke_ngan_hang_da_ky" /></a></li>
								<li class="active"><spring:message code="chi_tiet_hop_dong" /></li>
							</ol>


						</section>

					</h3>


				</div>
				<!-- /.box-header -->
				<div class="box-body table-responsive no-padding">
					<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="width: 50px; text-align: center;">#</th>
								<th><spring:message code="ten_file" /></th>

								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${soHopDongs }" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${ (currentPage-1)*20+(status.index+1) }</th>
									<td>
									      <a href="javascript:void(0)" onclick="loadEdit('${contextPath}/sao-ke-ngan-hang-da-ky/xem-file?pathfile=${item.tenFolder}&tenfile=${item.tenFile}')" data-toggle="modal" data-target="#largeModal" class="text-info">
											<c:out value="${item.tenFile }" />
										  </a>  
									
									</td>

									<td class="text-center">
										  <a href="javascript:void(0)" onclick="loadEdit('${contextPath}/sao-ke-ngan-hang-da-ky/xem-file?pathfile=${item.tenFolder}&tenfile=${item.tenFile}')" data-toggle="modal" data-target="#largeModal" class="text-info">
											<i class="fa fa-eye"></i>
										  </a>  
										<%-- 	<a href="javascript:void(0)" onclick="alertRCDl('${contextPath}/sao-ke-ngan-hang-da-ky/dowloand?hd=${item.soHD}', 'Bạn có chắc chắn muốn tải xuống hợp đồng')" class="text-info" style="margin-left: 5px;" title="">
										<i class="fa fa-download" aria-hidden="true"></i>
										</a> --%>

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

<script type="text/javascript">
	
</script>