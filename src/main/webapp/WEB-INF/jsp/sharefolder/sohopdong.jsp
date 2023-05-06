<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../layout/header2.jsp"%>
<%@include file="../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			<spring:message code="sao_ke_ngan_hang_da_ky" />
		</h1>
		<ol class="breadcrumb">
			<li>
				<a href="${contextPath}/">
					<spring:message code="trang_chu" />
				</a>
			</li>
			<li class="active">
				<spring:message code="sao_ke_ngan_hang_da_ky" />
			</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
				<div class="box-header">
					<h3 class="box-title">
						
					</h3>

					
				</div>
				<!-- /.box-header -->
				<div class="box-body table-responsive no-padding">
					<table class="table table-striped table-hover table-bordered">
						<thead>
							<tr>
								<th style="width: 50px; text-align: center;">#</th>
								<th>
									
									<spring:message code="hop_dong_sao_ke" />
								</th>
								
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${soHD }" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${ (currentPage-1)*20+(status.index+1) }</th>
									<td>
										<c:out value="${item.soHD }" />
									</td>
									
									<td class="text-center">
										
											<i class="fa fa-eye text-info" onclick="javascript:window.location='${contextPath}/sao-ke-ngan-hang-da-ky/xem?hd=${item.soHD}'"></i>
										
										
										
										<a href="javascript:void(0)" onclick="alertRCDl('${contextPath}/sao-ke-ngan-hang-da-ky/dowloand?hd=${item.soHD}', 'Bạn có chắc chắn muốn tải xuống hợp đồng')" class="text-info" style="margin-left: 5px;" title="">
										<i class="fa fa-download" aria-hidden="true"></i>
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

<script type="text/javascript">
	

</script>