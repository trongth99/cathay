package fis.com.vn.api.entities;

import java.util.ArrayList;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoiDungHopDong {
	String soHopDong;
	String email;
	String trangThai;
	String hoVaTen;
	String soDienThoai;
	String soCmt;
	String gioiTinh;
	String namSinh;
	String queQuan;
	String noiCap;
	String noiTru;
	String diemSoSanhKhuonMat;
	String uuidkySo;
	String billcodeKySo;
	String anhMatTruoc;
	String anhMatSau;
	String anhCaNhan;
	ArrayList<String> anhVideo;
	String hopDongVay;
	String hopDongBaoHiem;
	String dangKyChungThuSo;
	String trangThaiEkyc;
	Date ngayTao;
	String khuVuc;
	Date ngayKhachHangKy;
	Date ngayBaoHiemKy;
}
