package fis.com.vn.api.entities;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import fis.com.vn.common.FileHandling;
import fis.com.vn.common.StringUtils;
import fis.com.vn.entities.NoiDungFile;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class Params {
	private static final Logger LOGGER = LoggerFactory.getLogger(Params.class);
	
	@ToString.Exclude private String anhMatTruoc;
	@ToString.Exclude private String anhMatSau;
	@ToString.Exclude private String anhKhachHang;
	@ToString.Exclude private ArrayList<String> anhNhanDang;
	@ToString.Exclude private ArrayList<NoiDungAnh> anhVideo;
	@ToString.Exclude private ArrayList<String> danhSachAnh;
	@ToString.Exclude private String anhChanDung;
	@ToString.Exclude private String anhChuKy;
	@ToString.Exclude private String noiDungFile;
	@ToString.Exclude private String noiDungFileMaHoa;
	@ToString.Exclude private String anhTinNhan;
	@ToString.Exclude private String anhHoaDon;
	@ToString.Exclude private ArrayList<String> danhSachBangKe;
	
	@Expose private String maTest;
	@Expose private String loaiGplx;
	@Expose private String maGiayTo;
	@Expose private String maSoThue;
	@Expose private String hoVaTen;
	@Expose private String soCmt;
	@Expose private String namSinh;
	@Expose private String gioiTinh;
	@Expose private String danToc;
	@Expose private String queQuan;
	@Expose private String noiTru;
	@Expose private String tonGiao;
	@Expose private String ngayCap;
	@Expose private String noiCap;
	@Expose private String email;
	@Expose private String dienThoai;
	@Expose private String diaChi;
	@Expose private String soHoChieu;
	@Expose private String soGiayPhepLaiXe;
	@Expose private String mstCaNhan;
	@Expose private String maSoBHXH;
	@Expose private String quocTich;
	@Expose private String ngayHetHan;
	@Expose private String dangKyMoi;
	@Expose private String agreementUUID;
	@Expose private String challengeNonce;
	@Expose private String billCode;
	@Expose private String otp;
	@Expose private String code;
	@Expose private String thongTinBoXung;
	@Expose private String token;
	@Expose private String maCaptcha;
	@Expose private String soTien;
	@Expose private String anhVaSdt;
	@Expose private String tenFile;
	@Expose private String latitude;
	@Expose private String longitude;
	@Expose private String diaChiMobile;
	@Expose private String fileOcr;
	@Expose private String loaiGiayTo;
	@Expose private String noiDungTinNhan;
	@Expose private String thoiGianGui;
	@Expose private String thoiGianNhan;
	@Expose private String videoCall;
	@Expose private String nguongSoSanh;
	@Expose private String nguongTimKiem;
	@Expose private String loaiAnhMatTruoc;
	@Expose private String loaiAnhKhachHang;
	@Expose private String loaiAnhNhanDang;
	@Expose private String soDienThoai;
	@Expose private String danhDauTrang;
	@Expose private String loai;
	@Expose private String template;
	
	@Expose private String trinhDoHocVan;
	@Expose private String chucVu;
	@Expose private String loaiHinhLamViec;
	@Expose private String tinhTrangHonNhan;
	@Expose private String noiCuTru;
	@Expose private String maTinh;
	@Expose private String maQuanHuyen;
	@Expose private String maPhuongXa;
	@Expose private String tinh;
	@Expose private String quanHuyen;
	@Expose private String phuongXa;
	@Expose private String tinhNoiTru;
	@Expose private String quanHuyenNoiTru;
	@Expose private String kiemTraThongTin;
	@Expose private String maLanGoi;
	
	//sakura
	@Expose private String soHopDong;
	@Expose private String matKhau;
	@Expose private String matKhauCu;
	
	@Expose private String idBenGoi;
	@Expose private String idBenNghe;
	@Expose private String thoiGianBatDau;
	@Expose private String thoiGianKetThuc;
	@Expose private String thoiGian;
	@Expose private String doTuongDong;
	@Expose private String linkCuocGoi;
	@Expose private String maSoBhyt;
	@Expose private String tuThang;
	@Expose private String denThang;
	@Expose private String params;
	@Expose private String response;
	@Expose private String images;
	@Expose private String logVideo;
	@Expose private String thanhPho;
	@Expose private String quocGia;
	@Expose private String maKy;
	@Expose private String duongDanFileKy;
	
	//kbank
	@Expose private String bang;
	@Expose private String quocGia2;
	@Expose private String diaChi2;
	@Expose private String thanhPho2;
	@Expose private String bang2;
	@Expose private String trinhDo;
	@Expose private String tuoi;
	@Expose private String khachHang;
	@Expose private String phanKhucKhachHang;
	@Expose private String congTy;
	@Expose private String chiDinh;
	@Expose private String nhomNganh;
	@Expose private String maNganh;
	@Expose private String maNganhPhu;
	@Expose private String maCuTru;
	
	@Expose private String mucDichVayVon;
	@Expose private String kyHan;
	@Expose private String tenNganHang;
	@Expose private String soTaiKhoan;
	@Expose private String maGioiThieu;
	@ToString.Exclude private ArrayList<NoiDungFile> saoKe;
	@ToString.Exclude private ArrayList<NoiDungFile> payslip;
	@Expose private String namKinhDoanh;
	@Expose private String thangKinhDoanh;
	@Expose private String doanhThu;
	@Expose private String tenCuaHang;
	@Expose private String sanPhamKinhDoanh;
	@Expose private String nenTangKinhDoanh;
	@Expose private String ngheNghiep;
	@Expose private String linhVucKinhDoanh;
	@Expose private String tenVanPhong;
	@Expose private String soDienThoaiVanPhong;
	@Expose private String chucDanh;
	@Expose private String nguonThuNhap;
	@Expose private String quocGiaNguonThuNhap;
	@Expose private String luong;
	@Expose private String tocDoOcr;
	@Expose private String soGiayTo;
	@Expose private String trangThai;
	@Expose private String maCongTy;
	@Expose private String tenCongTy;
	
	@Expose private String maSoDoanhNghiep;
	
	@Expose private String codeTransaction;
	
	public void logImage(String logImage, String logFolder, String logResize, Params params) {
		try {
			ParamPathImage paramPathImage = new ParamPathImage();
			if(logImage.equals("true")) {
				FileHandling fileHandling = new FileHandling();

				if(anhNhanDang != null && anhNhanDang.size() >= 0) {
					ArrayList<String> pathAnhNhanDang = new ArrayList<String>();
					for (String anh : anhNhanDang) {
						if(!StringUtils.isEmpty(anh)) {
							String pathFileLog = logResize.equals("true")?fileHandling.saveResize(anh, logFolder): fileHandling.save(anh, logFolder);
							pathAnhNhanDang.add(pathFileLog);
							LOGGER.info("anhNhanDang: "+pathFileLog);
						}
					}
					paramPathImage.setAnhNhanDang(pathAnhNhanDang);
				}
				if(!StringUtils.isEmpty(anhMatTruoc)) {
					String pathFileLog = logResize.equals("true")?fileHandling.saveResize(anhMatTruoc, logFolder):fileHandling.save(anhMatTruoc, logFolder);
					LOGGER.info("anhMatTruoc: "+pathFileLog);
					paramPathImage.setAnhMatTruoc(pathFileLog);
				}
				if(!StringUtils.isEmpty(anhKhachHang)) {
					String pathFileLog = logResize.equals("true")?fileHandling.saveResize(anhKhachHang, logFolder):fileHandling.save(anhKhachHang, logFolder);
					LOGGER.info("anhKhachHang: "+pathFileLog);
					paramPathImage.setAnhKhachHang(pathFileLog);
				}
				if(!StringUtils.isEmpty(anhMatSau)) {
					String pathFileLog = logResize.equals("true")?fileHandling.saveResize(anhMatSau, logFolder):fileHandling.save(anhMatSau, logFolder);
					LOGGER.info("anhMatSau: "+pathFileLog);
					paramPathImage.setAnhMatSau(pathFileLog);
				}
				if(!StringUtils.isEmpty(anhTinNhan)) {
					String pathFileLog = logResize.equals("true")?fileHandling.saveResize(anhTinNhan, logFolder):fileHandling.save(anhTinNhan, logFolder);
					LOGGER.info("anhTinNhan: "+pathFileLog);
					paramPathImage.setAnhTinNhan(pathFileLog);
				}
				
				if(params.getLogVideo() != null && params.getLogVideo().equals("true")) {
					if(anhVideo != null && anhVideo.size() >= 0) {
						ArrayList<String> pathAnhVideo = new ArrayList<String>();
						for (NoiDungAnh anh : anhVideo) {
							String pathFileLog = fileHandling.save(anh.getAnh(), logFolder);
							pathAnhVideo.add(pathFileLog);
							LOGGER.info("anhVideo: "+pathFileLog);
						}
						paramPathImage.setAnhVideo(pathAnhVideo);
					}
				}
				
			}
			params.setImages(new Gson().toJson(paramPathImage));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
