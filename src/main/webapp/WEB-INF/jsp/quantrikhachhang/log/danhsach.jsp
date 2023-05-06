<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="../../layout/header2.jsp"%>
<%@include file="../../layout/js.jsp"%>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<section class="content-header">
		<h1>
			<spring:message code="danh_sach_khach_hang" />
		</h1>
		<ol class="breadcrumb">
			<li>
				<a href="${contextPath}/">
					<spring:message code="trang_chu" />
				</a>
			</li>
			<li class="active">
				<spring:message code="danh_sach_khach_hang" />
			</li>
		</ol>
	</section>

	<!-- Main content -->
	<form id="submitForm" action="" method="get">
		<section class="content container-fluid">
			<div class="box box-danger">
				<div class="box-header">
					<h3 class="box-title">
						<c:if test="${themMoiHopDong eq true }">
							<button class="btn  btn-primary btn-xs" type="button" onclick="javascript:window.location='${contextPath}/danh-sach-khach-hang/ky-so'">
		                          <i class="fa fa-plus" aria-hidden="true"></i>
		                         <span><spring:message code="them_moi" /></span>
		                     </button>
	                     </c:if>
	                     <button class="btn  btn-primary btn-xs" type="button" id="download" >
	                         <i class="fa fa-download" aria-hidden="true"></i>
	                         <span>Tải danh sách</span>
	                     </button>
					</h3>

					<div class="box-tools" style="position: relative;text-align: right;">
						<div class="form-inline input-group-sm" style="width: 100%;">
							<input style='margin-top:10px;' class="form-control form-control-sm" type="text" value="<c:out value="${soHopDong}"/>" name="soHopDong" placeholder="Số hợp đồng" data-toggle="tooltip" title="Số hợp đồng" />
							<input style='margin-top:10px;' class="form-control form-control-sm" type="text" value="<c:out value="${hoVaTen}"/>" name="hoVaTen" placeholder="<spring:message code="ho_va_ten" />" data-toggle="tooltip" title="<spring:message code="ho_va_ten" />" />
							<input style='margin-top:10px;' class="form-control form-control-sm" type="text" value="<c:out value="${soDienThoai}"/>" name="soDienThoai" placeholder="<spring:message code="so_dien_thoai" />" data-toggle="tooltip" title="<spring:message code="so_dien_thoai" />" />
							<input style='margin-top:10px;' class="form-control form-control-sm" type="text" value="<c:out value="${soCmt}"/>" name="soCmt" placeholder="<spring:message code="so_cmt" />" data-toggle="tooltip" title="<spring:message code="so_cmt" />" />
							<input style='margin-top:10px;' class="form-control form-control-sm" type="text" value="<c:out value="${nguoiTao}"/>" name="nguoiTao" placeholder="Người tạo" data-toggle="tooltip" title="Người tạo" />
							<input style='margin-top:10px;' class="form-control form-control-sm datepicker" type="text" value="<c:out value="${fromDate}"/>" name="fromDate" placeholder="<spring:message code="tu_ngay" />" data-toggle="tooltip" title="<spring:message code="tu_ngay" />" autocomplete="off"/>
							<input style='margin-top:10px;' class="form-control form-control-sm datepicker" type="text" value="<c:out value="${toDate}"/>" name="toDate" placeholder="<spring:message code="den_ngay" />" data-toggle="tooltip" title="<spring:message code="den_ngay" />" autocomplete="off"/>
							<input style='margin-top:10px;' class="form-control form-control-sm datepicker" type="text" value="<c:out value="${fromDateSign}"/>" name="fromDateSign" placeholder="Từ ngày khách hàng ký" data-toggle="tooltip" title="Từ ngày khách hàng ký" autocomplete="off"/>
							<input style='margin-top:10px;' class="form-control form-control-sm datepicker" type="text" value="<c:out value="${toDateSign}"/>" name="toDateSign" placeholder="Đến ngày khách hàng ký" data-toggle="tooltip" title="Đến ngày khách hàng ký" autocomplete="off"/>
							<select style='margin-top:10px;' class="form-control show-tick" name="trangThai" id="trangThai" data-toggle="tooltip" title="<spring:message code="trang_thai" />">
								<option value="" ${trangThai eq '' ? 'selected': ''}><spring:message code="tat_ca" /></option>
								<option value="0" ${trangThai eq '0' ? 'selected': ''}>Chưa ký</option>
								<option value="4" ${trangThai eq '4' ? 'selected': ''}>Chờ ký</option>
								<option value="1" ${trangThai eq '1' ? 'selected': ''}>Khách hàng ký</option>
								<option value="2" ${trangThai eq '2' ? 'selected': ''}>Bảo hiểm ký</option>
								<option value="3" ${trangThai eq '3' ? 'selected': ''}>Hoàn thành</option>
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="trangThaiGuiChungTu" id="trangThaiGuiChungTu" data-toggle="tooltip" title="Trạng thái gửi chứng tử mở TKTT SB">
								<option value="" ${trangThaiGuiChungTu eq '' ? 'selected': ''}>-- Chọn trạng thái gửi chứng tử mở TKTT SB --</option>
								<option value="Thành công" ${trangThaiGuiChungTu eq 'Thành công' ? 'selected': ''}>Thành công</option>
								<option value="Thất bại" ${trangThaiGuiChungTu eq 'Thất bại' ? 'selected': ''}>Thất bại</option>
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="ghiChu">
								<option value="" ${ghiChu eq '' ? 'selected': ''}>-- Chọn loại ghi chú --</option>
								<c:forEach items="${danhSachGhiChu}" var="item" varStatus="status">
									<option value='<c:out value="${item }"></c:out>' ${ghiChu eq item ? 'selected': ''}><c:out value="${item }"></c:out></option>
								</c:forEach>
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="khuVuc" id="khuVuc" data-toggle="tooltip" title="Khu vực">
								<option value=''>-- Chọn khu vực --</option>
								<c:forEach items="${khuVucs}" var="item" varStatus="status">
									<option value='<c:out value="${item }"></c:out>' ${khuVuc eq item ? 'selected': ''}><c:out value="${item }"></c:out></option>
								</c:forEach>
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="moTKTTSB" id="moTKTTSB" data-toggle="tooltip" title="Mở TKTT SeABank" >
								<option value="" }><spring:message code="tat_ca" /></option>
								<option value="Thành công" >Thành công</option>
								<option value="Không thành công" >Không thành công</option>
								<option value="Đã có TKTT" >Đã có TKTT</option>
								
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="ttTKTT" id="ttTKTT" data-toggle="tooltip" title="Trạng thái TKTT " >
								<option value="" }><spring:message code="tat_ca" /></option>
								<option value="Active" >Active</option>
								<option value="Inactive" >Inactive</option>
						
								
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="spHanMuc" id="spHanMuc" data-toggle="tooltip" title="Sản phẩm hạn mức" >
								<option value="" }><spring:message code="tat_ca" /></option>
								<option value="Yes" >Yes</option>
								<option value="No" >No</option>
						
								
							</select>
							<select style='margin-top:10px;' class="form-control show-tick" name="yeuCauGiaiNgan" id="yeuCauGiaiNgan" data-toggle="tooltip" title="Yêu cầu giải ngân" >
								<option value="" }><spring:message code="tat_ca" /></option>
								<option value="Có" >Có</option>
								<option value="Không" >Không</option>
						
								
							</select>
							<input style='margin-top:10px;' class="form-control form-control-sm datepicker" type="text" value="<c:out value="${fromNgayYC}"/>" name="fromDateSign" placeholder="Từ ngày yêu cầu mở TKTT SeABank" data-toggle="tooltip" title="Từ ngày yêu cầu mở TKTT SeABank" autocomplete="off"/>
							<input style='margin-top:10px;' class="form-control form-control-sm datepicker" type="text" value="<c:out value="${toDateNgayYC}"/>" name="toDateSign" placeholder="Đến ngày yêu cầu mở TKTT SeABank" data-toggle="tooltip" title="Đến ngày yêu cầu mở TKTT SeABank" autocomplete="off"/>
							<button style='margin-top:10px;' type="button" class="btn btn-sm btn-primary" id="search">
								<i class="fa fa-search"></i>
							</button>
						</div>
					</div>
				</div>
				<!-- /.box-header -->
				<div class="box-body table-responsive no-padding">
					<table class="table table-striped table-hover table-bordered" style="table-layout: fixed;">
						<thead>
							<tr>
								<th style="width: 50px; text-align: center;">#</th>
								<th style="width: 170px;">Chức năng</th>
								<th style="width: 80px">Ghi chú</th>
								<th style="width: 180px">
									Số hợp đồng
								</th>
								<th style="width: 150px">
									<spring:message code="so_giay_to" />
								</th>
								<th style="width: 150px">
									<spring:message code="ho_va_ten" />
								</th>
								<th style="width: 100px">
									<spring:message code="dien_thoai" />
								</th>
								<th style="width: 200px">
									<spring:message code="email" />
								</th>
								<th style="width: 150px">
									Mật khẩu
								</th>
								<th style="width: 150px">
									Trạng Thái
								</th>
								<th style="width: 150px">
									Trạng Thái eKYC
								</th>
								<th style="width: 200px">
									Lỗi eKYC
								</th>
								<th style="width: 100px">
									Bảo Hiểm
								</th>
								<th style="width: 150px">
									<spring:message code="ngay_tao" />
								</th>
								<th style="width: 100px">
									Khu vực
								</th>
								<th style="width: 100px">
									Người tạo
								</th>
								<th style="width: 150px">
									Ngày khách hàng ký
								</th>
								<th style="width: 150px">
									Ngày bảo hiểm ký
								</th>
								<th style="width: 100px">
									Gửi hợp đồng
								</th>
								<th style="width: 150px">
									Nội dung ghi chú
								</th>
								<th style="width: 150px">
									Ngày yêu cầu mở TKTT Seabank
								</th>
								<th style="width: 150px">
									Mở TKTT SeABank
								</th>
								<th style="width: 150px">
									Trạng thái TKTT
								</th>
								<th style="width: 150px">
									Trạng thái gửi chứng tử mở TKTT SB
								</th>
								<th style="width: 150px">
									Sản phẩm hạn mức
								</th>
								<th style="width: 150px">
									Yêu cầu giải ngân
								</th>
								<th style="width: 150px">
									Số tiền giải ngân
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${ekycKysos}" var="item" varStatus="status">
								<tr>
									<th scope="row" style="text-align: center;">${status.index+1 }</th>
									<td >
										<c:if test="${guiThongTin eq true }">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang/ky-so/gui-mail/chon?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Gửi email">
												<i class="fa  fa-send"></i>
											</a>
										</c:if>
										<c:if test="${ekycTruyenThong eq true }">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang/trang-thai?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Cập nhật ekyc">
												<i class="fa fa-user"></i>
											</a>
										</c:if>
										<c:if test="${xemChiTiet eq true }">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang/xem?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Xem chi tiết">
												<i class="fa fa-eye"></i>
											</a>
										</c:if>	
										<c:if test="${item.trangThai eq '0' or empty item.trangThai  }">
											<a href="${contextPath}/danh-sach-khach-hang/sua?id=${item.id}" style="margin-left: 5px;" title="Sửa">
												<i class="fa fa-edit"></i>
											</a>
										</c:if>
										<c:if test="${hoanThanhHopDong eq true }">
											<c:if test="${item.trangThai ne '3' }">
												<a href="javascript:void(0)" onclick="alertRC('${contextPath}/danh-sach-khach-hang/thay-doi-trang-thai?id=${item.id}', 'Bạn có chắc muốn hoàn thành hợp đồng?')" class="text-info" style="margin-left: 5px;" title="Hoàn thành hợp đồng">
													<i class="fa fa-check" aria-hidden="true"></i>
												</a>
											</c:if>
										</c:if>
										<c:if test="${empty item.token }">
											<a href="javascript:void(0)" onclick="deleteRC('${contextPath}/danh-sach-khach-hang/xoa?id=${item.id}')" class="text-danger" style="margin-left: 5px;" title="Xóa">
												<i class="fa fa-trash"></i>
											</a>
										</c:if>
										<c:if test="${xemTaiKhoanSeabank eq true }">
											 <c:if test="${item.trangThaiEkyc eq 'thanhcong' }">
												<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang/xem-thong-tin-tktt-sb?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Thông tin TKTT SeABank">
													<i class="fa fa-id-card-o"></i>
												</a>
											</c:if>
										 </c:if>  
										 <c:if test="${thongTinGiaiNgan eq true }">
											<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang/xem-thong-tin-giai-ngan?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Thông tin giải ngân">
												<i class="fa fa-usd"></i>
											</a>
										 </c:if> 
										 <c:if test="${item.moTKTTSB eq 'Thành công' }">
										 	<c:if test="${dongTaiKhoan eq true }">
												 <a href="javascript:void(0)" onclick="alertRC('${contextPath}/danh-sach-khach-hang/dong-tai-khoan?id=${item.id}', 'Bạn có chắc muốn đóng tài khoản?\nCMT: ${item.soCmt }\nSTK: ${item.accountId }\nCoCode: ${item.coCode }')"  class="text-danger" style="margin-left: 5px;" title="Đóng tài khoản">
													<i class="fa fa-window-close"></i>
												</a>
											</c:if>
										</c:if>
									</td>
									<td>
										<a href="javascript:void(0)" onclick="loadEdit('${contextPath}/danh-sach-khach-hang/ky-so/ghi-chu?id=${item.id}')" data-toggle="modal" data-target="#largeModal" class="text-info" style="margin-left: 5px;" title="Ghi chú">
											<i class="fa  fa-edit"></i>
										</a>
									</td>
									<td><c:out value="${item.soTaiKhoan }"/> </td>
									<td><c:out value="${item.soCmt }"/></td>
									<td><c:out value="${item.hoVaTen }"/></td>
									<td><c:out value="${item.soDienThoai }"/></td>
									<td><c:out value="${item.email }"/></td>
									<td><c:out value="${item.token }"/></td>
									<td>
										<c:if test="${item.trangThai eq '3' }"><span style="color: #3c8dbc;">Hoàn thành</span></c:if>
										<c:if test="${item.trangThai eq '2' }"><span style="color: green;">Bảo hiểm ký</span></c:if>
										<c:if test="${item.trangThai eq '1' }"><span style="color: blue">Khách hàng ký</span></c:if>
										<c:if test="${item.trangThai eq '4' }"><span style="color: orange;">Chờ ký</span></c:if>
										<c:if test="${item.trangThai eq '0' or empty item.trangThai }">
											<span style="color: red;">Chưa ký</span>
										</c:if>
									</td>
									<td>
										<c:if test="${item.trangThaiEkyc eq 'thatbai' }">
											<span style="color: red;">Thất bại</span>
										</c:if>
										<c:if test="${item.trangThaiEkyc eq 'thanhcong' }">
											<span style="color: blue;">Thành công</span>
										</c:if>
										<c:if test="${item.trangThaiEkyc eq 'truyenthong' }">
											<span style="color: orange;">Truyền thống</span>
										</c:if>
										<c:if test="${empty item.trangThaiEkyc}">
											<span style="color: green;">Chưa xác thực</span>
										</c:if>
									</td>
									<td>
										<c:forTokens items="${item.thongBao }" delims="|" var="mySplit" varStatus="st">
											<c:if test="${st.index <= 1 }">
										   		- <c:out value="${mySplit}"/><br/>
										   	</c:if>
										</c:forTokens>
									</td>
									<td>
										<c:if test="${ empty item.chiDinh}">
											<span style="color: red;">Không có</span>
										</c:if>
										<c:if test="${not empty item.chiDinh }">
											<span style="color: blue;">Có</span>
										</c:if>
									</td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.ngayTao }" /></td>
									<td><c:out value="${item.khuVuc}"/></td>
									<td><c:out value="${item.nguoiTao}"/></td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.khachHangKy }" /></td>
									<td><fmt:formatDate pattern = "dd/MM/yyyy HH:mm:ss" value = "${item.baoHiemKy }" /></td>
									<td>
										<c:if test="${item.trangThai eq '3'}">
											<a href="javascript:void(0)" onclick="alertRC('${contextPath}/danh-sach-khach-hang/ky-so/gui-mail-hop-dong?id=${item.id}','Bạn có chắc muốn gửi hợp đồng cho khách hàng?')" class="text-info" style="margin-left: 5px;" title="Gửi email">
												Gửi
											</a>
										</c:if>
									</td>
									<td><c:out value="${item.ghiChu}"/></td>
									<td><c:out value="${item.ngayYCMoTKTT}"/></td>
									<td><c:out value="${item.moTKTTSB}"/></td>
									<td><c:out value="${item.ttTKTT}"/></td>
									<td><c:out value="${item.ttGuiChungTu}"/></td>
									<td><c:out value="${item.spHanMuc}"/></td>
									<td><c:out value="${item.yeuCauGiaiNgan}"/></td>
									<fmt:setLocale value = "en_US" scope="session"/>
									<td><fmt:formatNumber pattern="#,###" value = "${item.soTienGiaiNgan }" /></td>
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
<script src="${contextPath }/js/select2.full.min.js"></script>
<script type="text/javascript">
	
	$("#khuVuc").select2();
	
	$(document).ready(function(){
		$("#download").click(function(){
			$("#submitForm").attr("action", "${contextPath}/danh-sach-khach-hang/export");
			$("#submitForm").submit();
		});
		$("#search").click(function(){
			$("#submitForm").attr("action", "${contextPath}/danh-sach-khach-hang");
			$("#submitForm").submit();
		});
		$('.datepicker').datepicker({
		     autoclose: true,
		     format: 'dd/mm/yyyy'
		})
	});
	
</script>
<style type="text/css">
.select2 {
    width: 150px !important;
    border-radius: 3px;
    margin-top: 10px;
}
.select2-container .select2-selection--single {
height: 30px;
}
table td{
word-break: break-all;
}
</style>
<%@include file="../../layout/footer2.jsp"%>