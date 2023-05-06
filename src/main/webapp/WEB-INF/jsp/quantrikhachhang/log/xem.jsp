<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="../../layout/js.jsp"%>
<style type="text/css">
.help-block{
color: #dc3545;
}
</style>

<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
	<div class="modal-header">
		<h2><spring:message code="thong_tin_khach_hang" /></h2>
	</div>
	<div class="modal-body">
		<div class="row clearfix">
			<h3 style="clear: both;">Step 1</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="so_dien_thoai" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.soDienThoai }"/>" />
				</div>
			</div>
			
			<h3 style="clear: both;">Step 2</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="anh_mat_truoc" /></label>
					<img src="${anhMatTruoc }" style="width: 100%;"/>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="anh_mat_sau" /></label>
					<img src="${anhMatSau }" style="width: 100%;"/>
				</div>
			</div>
			
			<h3 style="clear: both;">Step 3</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="anh_ca_nhan" /></label>
					<img src="${anhCaNhan }" style="width: 100%;"/>
				</div>
			</div>
			
			<h3 style="clear: both;">Step 4</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ho_va_ten" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.hoVaTen }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="e_so_giay_to" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.soCmt }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ngay_cap" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.ngayCap }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ngay_het_han" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.ngayHetHan }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="gioi_tinh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.gioiTinh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="nam_sinh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.namSinh }"/>" />
				</div>
			</div>
			<div class="col-sm-6 clearfix">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="e_quoc_tich" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.noiTru }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="quoc_gia" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.quocGia }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="e_noi_cap" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.noiCap }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="e_dia_chi" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.diaChi }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="e_thanh_pho" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.thanhPho }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="e_quan_huyen" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.bang }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="quoc_gia_hien_tai" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.quocGia2 }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="dia_chi_hien_tai" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.diaChi2 }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="thanh_pho_hien_tai" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.thanhPho2 }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="tinh_hien_tai" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.bang2 }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="trinh_do" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.trinhDo }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="tuoi" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.tuoi }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ma_giay_to" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.maGiayTo }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="loai_khach_hang" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.khachHang }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="phan_khuc_khach_hang" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.phanKhucKhachHang }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ma_cong_ty" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.congTy }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="chi_dinh_hop_phap" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.chiDinh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="nhom_nganh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.nhomNganh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ma_nganh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.maNganh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ma_nganh_phu" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.maNganhPhu }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ma_cu_tru" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.maCuTru }"/>" />
				</div>
			</div>
			
			<h3 style="clear: both;">Step 5</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="muc_dich_vay_von" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.mucDichVayVon }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="so_tien" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.soTien }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ky_han" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.kyHan }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ten_ngan_hang" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.tenNganHang }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="so_tai_khoan" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.soTaiKhoan }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ma_gioi_thieu" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.maGioiThieu }"/>" />
				</div>
			</div>
			
			<h3 style="clear: both;">Step 6</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			
			<h3 style="clear: both;">Step 7</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-12">
				<iframe id="base64File"
					style="width: 100%; height: 400px; border: 0;"></iframe>
			</div>
			
			<h3 style="clear: both;">Step 8</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="email" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.email }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="nam_kinh_doanh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.namKinhDoanh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="thang_kinh_doanh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.thangKinhDoanh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="doanh_thu" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.doanhThu }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ten_cua_hang" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.tenCuaHang }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="san_pham_kinh_doanh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.sanPhamKinhDoanh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="nen_tang_kinh_foanh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.nenTangKinhDoanh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="nghe_nghiep" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.ngheNghiep }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="linh_vuc_kinh_doanh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.linhVucKinhDoanh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="ten_van_phong" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.tenVanPhong }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="so_dien_thoai_van_phong" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.soDienThoaiVanPhong }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="chuc_danh" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.chucDanh }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="nguon_thu_nhap" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.nguonThuNhap }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="quoc_gia_co_nguon_thu_nhap" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.quocGiaNguonThuNhap }"/>" />
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group  has-feedback">
					<label class="control-label"><spring:message code="luong" /></label>
					<input type="text" class="form-control form-control-sm" readonly="readonly" value="<c:out value="${formInfo.luong }"/>" />
				</div>
			</div>
			
			<h3 style="clear: both;">Step 9</h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Bank Statement</label>
					<ul>
					<c:forEach items="${danhSachFile.saoKe}" var="item" varStatus="status">
						<li><c:out value="${item.ten }"/></li>
					</c:forEach>
					</ul>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="form-group  has-feedback">
					<label class="control-label">Pay Slip</label>
					<ul>
					<c:forEach items="${danhSachFilePaySlip.payslip}" var="item" varStatus="status">
						<li><c:out value="${item.ten }"/></li>
					</c:forEach>
					</ul>
				</div>
			</div>
			
			<h3 style="clear: both;"><spring:message code="kiem_tra" /></h3>
			<hr style="color: #CCC;border-top: 1px solid #CCC;"/>
			<div class="col-sm-12">
				<table class="table table-bordered table-sm table-striped">
					<tr>
						<th>Checking step</th>
						<th>Status</th>
					</tr>
					<tbody id="showstep">
					
					</tbody>
				</table>
			</div>
			
			<div style="clear: both;margin-top: 20px;"></div>
		</div>
		
		<div class="col-md-12 mb-0 text-right">
			<button class="btn btn-danger btn-sm" data-dismiss="modal">
				<i class="fa fa-times"></i>
				<span><spring:message code="thoat" /></span>
			</button>
		</div>
		<div style="clear: both;margin-top: 20px;"></div>
	</div>
</div>
<script type="text/javascript">
var b64toBlob = (b64Data, contentType='', sliceSize=512) => {
	var byteCharacters = atob(b64Data);
	var byteArrays = [];
	
	for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
		var slice = byteCharacters.slice(offset, offset + sliceSize);
		
		var byteNumbers = new Array(slice.length);
		for (let i = 0; i < slice.length; i++) {
			byteNumbers[i] = slice.charCodeAt(i);
		}
		
		var byteArray = new Uint8Array(byteNumbers);
		byteArrays.push(byteArray);
	}
	
	var blob = new Blob(byteArrays, {type: contentType});
	return blob;
}

var contentType = 'application/pdf';
var b64Data = '${file }';

var blob = b64toBlob(b64Data, contentType);
var blobUrl = URL.createObjectURL(blob);

var checks = JSON.parse('${check}');
$(document).ready(function(){
	$("#base64File").attr("src", blobUrl);	
	
	var steps = "";
	var trangThai = "<i style='font-size:16px;color:blue;' class='fa fa-check'></i>";
	for(x in checks) {
		console.log(checks[x])
		if(checks[x] != 'true') {
			trangThai = "<i style='font-size:16px;color:red;' class='fa fa-times'></i>";
		} else {
			trangThai = "<i style='font-size:16px;color:blue;' class='fa fa-check'></i>";
		}
		if(x != 'data')
			steps += "<tr><td>"+x+"</td><td>"+trangThai+"</td></tr>";
	}
	$("#showstep").html(steps);
});
</script>
<%@include file="../../layout/footerAjax.jsp"%>
