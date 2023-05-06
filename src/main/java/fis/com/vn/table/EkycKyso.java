package fis.com.vn.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "ekyc_kyso", indexes = { 
		@Index(name = "IDX_SODIENTHOAI", columnList = "soDienThoai"),
		@Index(name = "IDX_HOVATEN", columnList = "hoVaTen"),
		@Index(name = "IDX_ID_CONTRACT", columnList = "soTaiKhoan"),
		@Index(name = "IDX_KHUVUC", columnList = "khuVuc"),
		@Index(name = "IDX_NGAYTAO", columnList = "ngayTao"),
		@Index(name = "IDX_khachHangKy", columnList = "khachHangKy"),
		@Index(name = "IDX_TRANGTHAI", columnList = "trangThai"),
		@Index(name = "IDX_ghiChu", columnList = "ghiChu"),
		@Index(name = "IDX_loaiKiemTra", columnList = "loaiKiemTra"),
		@Index(name = "IDX_SOCMT", columnList = "soCmt") 
		})
@Data
public class EkycKyso {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@Column(name = "soCmt")
	@Nationalized
	String soCmt;
	
	@Nationalized
	String sohd;
	
	@Column(name = "anhCaNhan")
	@Nationalized
	String anhCaNhan;
	
	@Column(name = "anhMatTruoc")
	@Nationalized
	String anhMatTruoc;
	
	@Column(name = "anhMatSau")
	@Nationalized
	String anhMatSau;
	
	@Column(name = "maGiayTo")
	@Nationalized
	String maGiayTo;
	
	@Column(name = "ocr", columnDefinition = "LONGTEXT")
	@Nationalized
	String ocr;
	
	@Column(name = "noiDungForm", columnDefinition = "LONGTEXT")
	@Nationalized
	String noiDungForm;
	
	@Column(name = "duongDanFileKy")
	@Nationalized
	String duongDanFileKy;
	
	@Column(name = "duongDanFileKySeaBank")
	@Nationalized
	String duongDanFileKySeaBank;
	
	@Column(name = "kiemTraThongTin", columnDefinition = "LONGTEXT")
	@Nationalized
	String kiemTraThongTin;
	
	@Column(name = "soDienThoai")
	@Nationalized
	String soDienThoai;
	
	@Column(name = "loai")
	@Nationalized
	String loai;
	
	@Column(name = "hoVaTen")
	@Nationalized
	String hoVaTen;
	
	@Column(name = "danhSachFile", columnDefinition = "TEXT")
	@Nationalized
	String danhSachFile;
	
	@Column(name = "anhTinNhan")
	@Nationalized
	String anhTinNhan;
	
	@Column(name = "danhSachFilePaySlip", columnDefinition = "TEXT")
	@Nationalized
	String danhSachFilePaySlip;
	
	@Column(name = "trangThai")
	@Nationalized
	String trangThai;
	
	@Column(name = "tenFile")
	@Nationalized
	String tenFile;
	
	@Column(name = "maKhachHang")
	@Nationalized
	String maKhachHang;
	
	@Column(name = "buocThucHien")
	@Nationalized
	String buocThucHien;
	
	@Column(name = "thoiGianThayDoi")
	Long thoiGianThayDoi;
	
	@Column(name = "thongBao", columnDefinition = "LONGTEXT")
	@Nationalized
	String thongBao;
	
	@Column(name = "email")
	@Nationalized
	String email;
	
	@Nationalized
	String trangThaiGui;
	
	@Column(name = "activeEmail")
	@Nationalized
	String activeEmail;
	
	@Column(name = "ngayTao")
	Date ngayTao;
	
	@Column(name = "trangThaiEkyc")
	@Nationalized
	String trangThaiEkyc;
	
	@Column(name = "tinhTrangCapMa")
	@Nationalized
	String tinhTrangCapMa;
	
	@Column(name = "ngaySinh")
	@Nationalized
	String ngaySinh;
	
	@Column(name = "thoiGianCacBuoc", columnDefinition = "TEXT")
	@Nationalized
	String thoiGianCacBuoc;
	
	@Column(name = "token")
	@Nationalized
	String token;
	
	@Column(name = "ngayCap")
	@Nationalized
	String ngayCap;
	
	@Column(name = "ngayHetHan")
	@Nationalized
	String ngayHetHan;
	
	@Column(name = "gioiTinh")
	@Nationalized
	String gioiTinh;
	
	@Column(name = "namSinh")
	@Nationalized
	String namSinh;
	
	@Column(name = "noiTru")
	@Nationalized
	String noiTru;
	
	@Column(name = "quocGia")
	@Nationalized
	String quocGia;
	
	@Column(name = "noiCap")
	@Nationalized
	String noiCap;
	
	@Column(name = "diaChi")
	@Nationalized
	String diaChi;
	
	@Column(name = "thanhPho")
	@Nationalized
	String thanhPho;
	
	@Column(name = "bang")
	@Nationalized
	String bang;
	
	@Column(name = "quocGia2")
	@Nationalized
	String quocGia2;
	
	@Column(name = "diaChi2")
	@Nationalized
	String diaChi2;
	
	@Column(name = "thanhPho2")
	@Nationalized
	String thanhPho2;
	
	@Column(name = "bang2")
	@Nationalized
	String bang2;
	
	@Column(name = "trinhDo")
	@Nationalized
	String trinhDo;
	
	@Column(name = "tuoi")
	@Nationalized
	String tuoi;
	
	@Column(name = "khachHang")
	@Nationalized
	String khachHang;
	
	@Column(name = "phanKhucKhachHang")
	@Nationalized
	String phanKhucKhachHang;

	@Column(name = "congTy")
	@Nationalized
	String congTy;
	
	@Column(name = "chiDinh")
	@Nationalized
	String chiDinh;
	
	@Column(name = "nhomNganh")
	@Nationalized
	String nhomNganh;
	
	@Column(name = "maNganh")
	@Nationalized
	String maNganh;
	
	@Column(name = "maNganhPhu")
	@Nationalized
	String maNganhPhu;
	
	@Column(name = "maCuTru")
	@Nationalized
	String maCuTru;
	
	
	//step5
	@Column(name = "mucDichVayVon")
	@Nationalized
	String mucDichVayVon;
	
	@Column(name = "soTien")
	@Nationalized
	String soTien;
	
	@Column(name = "kyHan")
	@Nationalized
	String kyHan;
	
	@Column(name = "tenNganHang")
	@Nationalized
	String tenNganHang;
	
	@Column(name = "soTaiKhoan")
	@Nationalized
	String soTaiKhoan;
	
	@Column(name = "maGioiThieu")
	@Nationalized
	String maGioiThieu;
	
	//step8
	@Column(name = "namKinhDoanh")
	@Nationalized
	String namKinhDoanh;
	
	@Column(name = "thangKinhDoanh")
	@Nationalized
	String thangKinhDoanh;
	
	@Column(name = "doanhThu")
	@Nationalized
	String doanhThu;
	
	@Column(name = "tenCuaHang")
	@Nationalized
	String tenCuaHang;
	
	@Column(name = "sanPhamKinhDoanh")
	@Nationalized
	String sanPhamKinhDoanh;
	
	@Column(name = "nenTangKinhDoanh")
	@Nationalized
	String nenTangKinhDoanh;
	
	@Column(name = "ngheNghiep")
	@Nationalized
	String ngheNghiep;
	
	@Column(name = "linhVucKinhDoanh")
	@Nationalized
	String linhVucKinhDoanh;
	
	@Column(name = "tenVanPhong")
	@Nationalized
	String tenVanPhong;
	
	@Column(name = "soDienThoaiVanPhong")
	@Nationalized
	String soDienThoaiVanPhong;
	
	@Column(name = "chucDanh")
	@Nationalized
	String chucDanh;
	
	@Column(name = "nguonThuNhap")
	@Nationalized
	String nguonThuNhap;
	
	@Column(name = "quocGiaNguonThuNhap")
	@Nationalized
	String quocGiaNguonThuNhap;
	
	@Column(name = "luong")
	@Nationalized
	String luong;
	
	@Column(name = "quocTich")
	@Nationalized
	String quocTich;
	
	@Column(name = "soDienThoaiKiemTra")
	@Nationalized
	String soDienThoaiKiemTra;
	
	@Column(name = "thoiGianHetHanToken")
	Long thoiGianHetHanToken;
	
	@Column(name = "anhVideo", columnDefinition = "TEXT")
	@Nationalized
	String anhVideo;
	
	@Column(name = "khachHangKy")
	Date khachHangKy;
	
	@Column(name = "baoHiemKy")
	Date baoHiemKy;
	
	@Column(name = "uuidKySo")
	@Nationalized
	String uuidKySo;
	
	@Column(name = "billCodeKySo")
	@Nationalized
	String billCodeKySo;
	
	@Column(name = "khuVuc", length = 100)
	@Nationalized
	String khuVuc;
	
	@Column(name = "diemEkyc", length = 100)
	@Nationalized
	String diemEkyc;
	
	@Column(name = "ghiChu", length = 200)
	@Nationalized
	String ghiChu;
	
	@Column(name = "loaiKiemTra", length = 10)
	@Nationalized
	String loaiKiemTra;
	
	@Column(name = "nguoiTao", length = 150)
	@Nationalized
	String nguoiTao;
	
	@Column(name = "caseId", length = 150)
	@Nationalized
	String caseId;
	
	@Column(name = "noiDungGiaiNgan", columnDefinition = "LONGTEXT")
	@Nationalized
	String noiDungGiaiNgan;
	
	@Column(name = "yeuCauGiaiNgan", length = 150)
	@Nationalized
	String yeuCauGiaiNgan;
	
	@Column(name = "soTienGiaiNgan", length = 150)
	@Nationalized
	String soTienGiaiNgan;
	
	@Column(name = "ttTKTT", length = 150)
	@Nationalized
	String ttTKTT;
	
	@Column(name = "ngayYCMoTKTT")
	@Nationalized
	Date ngayYCMoTKTT;
	
	@Column(name = "spHanMuc", length = 150)
	@Nationalized
	String spHanMuc;
	
	@Column(name = "ttGuiChungTu", length = 150)
	@Nationalized
	String ttGuiChungTu;
	@Column(name = "moTKTTSB", length = 150)
	@Nationalized
	String moTKTTSB;
	
	@Column(name = "coCode", length = 50)
	@Nationalized
	String coCode;
	
	@Column(name = "accountId", length = 50)
	@Nationalized
	String accountId;
	
	@Column(name = "closeAcc", length = 50)
	@Nationalized
	String closeAcc;
	
	@Column(name = "duongDanFileGoc", columnDefinition = "LONGTEXT")
	@Nationalized
	String duongDanFileGoc;
	
	@Column(name = "thoiGianHetHanKySo")
	Long thoiGianHetHanKySo;
	
	@Column(name = "cronDongTk")
	Integer cronDongTk;
	
	@Column(name = "soLanKy")
	Integer soLanKy;
	
	@Column(name = "thongTinTaiKhoanSeabank", columnDefinition = "LONGTEXT")
	@Nationalized
	String thongTinTaiKhoanSeabank;
	
	@Column(name = "thongTinNguoiDungTuLos", columnDefinition = "LONGTEXT")
	@Nationalized
	String thongTinNguoiDungTuLos;
}
