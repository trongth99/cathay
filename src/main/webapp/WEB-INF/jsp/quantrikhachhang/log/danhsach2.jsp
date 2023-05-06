<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../../layout/header2.jsp"%>
<%@include file="../../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			Danh sách khách hàng ký 
		</h1>
		<ol class="breadcrumb">
			<li>
				<a href="${contextPath}/">
					<spring:message code="trang_chu" />
				</a>
			</li>
			<li class="active">
				Danh sách khách hàng ký 
			</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
				<div class="box-header">
					<h3 class="box-title">
						<button class="btn btn-block btn-primary btn-xs" type="button" onclick="kySo()">
	                         <i class="fa fa-send" aria-hidden="true"></i>
	                         <span>
								Gửi
							</span>
	                    </button>
	                  <%--   <button class="btn btn-block btn-primary btn-xs" data-toggle="modal" data-target="#largeModal" type="button" onclick="loadAdd('${contextPath}/danh-sach-khach-hang-bh/upload-file')">
							<i class="fa fa-plus" aria-hidden="true"></i>

							<span>
								Upload Danh sách
							</span>
						</button> --%>
					</h3>

					<div class="box-tools">
						<div class="form-inline input-group-sm" style="width: 100%;">
							<input class="form-control form-control-sm" type="text" value="<c:out value="${hoVaTen}"/>" name="hoVaTen" placeholder="<spring:message code="ho_va_ten" />" data-toggle="tooltip" title="<spring:message code="ho_va_ten" />" />
							<input class="form-control form-control-sm" type="text" value="<c:out value="${soDienThoai}"/>" name="soDienThoai" placeholder="<spring:message code="so_dien_thoai" />" data-toggle="tooltip" title="<spring:message code="so_dien_thoai" />" />
							<input class="form-control form-control-sm" type="text" value="<c:out value="${soCmt}"/>" name="soCmt" placeholder="<spring:message code="so_cmt" />" data-toggle="tooltip" title="<spring:message code="so_cmt" />" />
							<input class="form-control form-control-sm datepicker" type="text" value="<c:out value="${fromDate}"/>" name="fromDate" placeholder="<spring:message code="tu_ngay" />" data-toggle="tooltip" title="<spring:message code="tu_ngay" />" autocomplete="off"/>
							<input class="form-control form-control-sm datepicker" type="text" value="<c:out value="${toDate}"/>" name="toDate" placeholder="<spring:message code="den_ngay" />" data-toggle="tooltip" title="<spring:message code="den_ngay" />" autocomplete="off"/>
							<select class="form-control show-tick" name="trangThai" id="trangThai" data-toggle="tooltip" title="<spring:message code="trang_thai" />">
								<option value="" ${trangThai eq '2' ? 'selected': ''}><spring:message code="tat_ca" /></option>
								
								<option value="2" ${trangThai eq '2' ? 'selected': ''}>Đã ký</option>
							</select>
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
								<th style="width: 50px; text-align: center;">
									<input type="checkbox" id="checkAll"/>
								</th>
								
								<th style="width: 50px; text-align: center;">#</th>
								<th>
									Số hợp đồng
								</th>
								<%-- <th>
									<spring:message code="so_giay_to" />
								</th> --%>
								<%-- <th>
									<spring:message code="ho_va_ten" />
								</th> --%>
								<%-- <th>
									<spring:message code="dien_thoai" />
								</th> --%>
								<th>
									<spring:message code="email" />
								</th>
								<th>
									Trạng Thái hợp đồng
								</th>
								<th>
									Trạng Thái hồ sơ
								</th>
								<th>
									<spring:message code="ngay_tao" />
								</th>
								<th style="width: 100px;"></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ekycKysos}" var="item" varStatus="status">
								<tr>
									 <th style="width: 50px; text-align: center;">
										<input type="checkbox" class="checkAll" name="check" value="${item.id}"/>
									</th>
									<th scope="row" style="text-align: center;">${status.index+1 }</th>
									<td><c:out value="${item.sohd }"/></td>
									<%-- <td><c:out value="${item.soCmt }"/></td>
									<td><c:out value="${item.hoVaTen }"/></td>
									<td><c:out value="${item.soDienThoai }"/></td> --%>
									<td><c:out value="${item.email }"/></td>
									<td>
										<c:if test="${item.trangThai eq '2' }"><span style="color: green;">Đã ký</span></c:if>
										<c:if test="${item.trangThai eq '1' }"><span style="color: blue">Chưa ký</span></c:if>
										<c:if test="${item.trangThai eq '0' or empty item.trangThai }"><span style="color: red;">Chưa ký</span></c:if>
									</td>
									<td>
										<c:if test="${item.trangThaiGui eq '2' }"><span style="color: green;">Đã gửi</span></c:if>
										<c:if test="${item.trangThaiGui eq '1' }"><span style="color: blue">Chưa gửi</span></c:if>
										<c:if test="${item.trangThaiGui eq '0' or empty item.trangThai }"><span style="color: red;">Chưa ký</span></c:if>
									</td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.ngayTao }" /></td>
									<td>
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang-bh/xem?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;">
												<i class="fa fa-eye"></i>
										</a>
										<c:if test="${item.trangThai eq '2' }">
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang-bh/ky-so/gui-mail/chon?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Gửi email">
												<i class="fa  fa-send"></i>
										</a>
										</c:if>
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
<script type="text/javascript">
	$(document).ready(function(){
		$('.datepicker').datepicker({
		     autoclose: true,
		     format: 'dd/mm/yyyy'
		})
		$("#checkAll").click(function(){
			if($('#checkAll').is(":checked")) {
				$(".checkAll").prop("checked", true);
			} else {
				$(".checkAll").prop("checked", false);
			}
		});
	});
	var check = false;
	function kySo() {
		swal({
			title: "Bạn có chắc muốn gửi mail tất cả các bản ghi được chọn",
	        text: "",
	        type: "warning",
	        showCancelButton: true,
	        confirmButtonColor: "#DD6B55",
	        confirmButtonText: '<spring:message code="dong_y" />',
	        cancelButtonText: '<spring:message code="thoat" />',
	        closeOnConfirm: true
	    }, function () {
	    	check = true;
	    });
		console.log(check)
		if(check == false){
			console.log("hjdbfhjdsbfhj")
			var list = "";
			$('.checkAll:checked').each(function() {
				list +=$(this).val()+"-";
			});
			location.href='${contextPath}/danh-sach-khach-hang-bh/ky-so/gui-mail?list='+list;
		}
	}
	
	

	
</script>
<%@include file="../../layout/footer2.jsp"%>