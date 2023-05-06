package fis.com.vn.ocr;

import lombok.Data;

@Data
public class Ocr {
	//Số cmt
	String soCmt;
	
	String soGiayTo;
	
	//Tên
	String hoVaTen;
	
	String hoVaTenLaTinh;
	
	//Ngày sinh
	String namSinh;
	
	//Nguyên quán
	String queQuan;
	
	//Địa chỉ
	String noiTru;
	
	//Dân tộc
	String danToc;
	
	//Tôn giáo
	String tonGiao;
	
	//Dấu vết riêng và dị hình
	String dacDiemNhanDang;
	
	//Ngày cấp
	String ngayCap;
	
	//Nơi cấp
	String noiCap;
	
	//Loại cmt
	String loaiCmt;
	
	String loaiCmtKhac;
	
	String loaiCmtMatTruoc;
	
	String loaiCmtKhacMatTruoc;
	
	//anh chân dung
	String anhChanDung;
	
	//Số GPLX
	String soGiayPhepLaiXe;
	
	//Quốc tịch
	String quocTich;
	
	//Ngày hết hạn
	String ngayHetHan;
	
	//Mã số
	String maSo;
	
	//hang
	String hangGiayPhepLaiXe;
	
	//loại
	String loaiGiayPhepLaiXe;
	
	//Số Hộ Chiếu
	String soHoChieu;
	
	//Giới tính
	String gioiTinh;
	
	String maLoaiGiayTo;
	
	String maGiayTo;
	
	String chucVu;
	
	AddressEntities chiTietNoiTru;
	
	CheckingResult kiemTra;
	
	CheckingResult kiemTraMatTruoc;
	
	CheckingResult kiemTraMatSau;
	
	String score;
	
	String soCmtScore;
	
	String hoVaTenScore;
	
	String namSinhScore;
	
	String ngayHetHanScore;
	
	String queQuanScore;
	
	String noiTruScore;
	
	String danTocScore;
	
	String tonGiaoScore;
	
	String ngayCapScore;
	
	String noiCapScore;
	
	String gioiTinhScore;
	
	String quocTichScore;
	
	//dang ky xe
	String soDangKy;
	String diaChi;
	String soMay;
	String soKhung;
	String nhanHieu;
	String soLoai;
	String loaiXe;
	String mauSon;
	String dungTich;
	String soNguoi;
	String nguonGoc;
	String bienSo;
	String ngayDangKyLanDau;
	String taiTrong;
	String soChungNhan;
	
	MaHoa maHoa;
	
	String maHoaScore;
	
	String maTinhQueQuan;
	
	String maTinhDiaChi;
	
	String maTinhNoiCap;
}
