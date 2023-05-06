package fis.com.vn.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "quan_ly_khach_hang")
@Data
public class QuanLyKhachHang {
	@Id
	long id;
	
	@Column(name = "name")
	@Nationalized
	String name;
	
	@Column(name = "token")
	String token;
	
	@Column(name = "code")
	String code;
	
	@Column(name = "username")
	String username;
	
	@Column(name = "password")
	String password;
	
	@Column(name = "status")
	Integer status;
	
	@Column(name = "create_date")
	Date date;
	
	@Column(name = "date_update")
	Date dateUpdate;
	
	@Column(name = "number_request")
	Integer numberRequest;
	
	@Column(name = "nguong_so_sanh_img")
	String nguongSoSanhImg;
	
	@Column(name = "kiem_tra_giay_to")
	String kiemTraGiayTo;
	
	@Column(name = "tong_so_giao_dich")
	Integer tongSoGiaoDich;
	
	@Column(name = "giao_dich_su_dung")
	Integer giaoDichSuDung;
	
	@Column(name = "loai_goi")
	String loaiGoi;
	
	@Column(name = "ngay_bat_dau")
	Date ngayBatDau;
	
	@Column(name = "token_vision")
	String tokenVision;
	
	@Column(name = "hanh_dong")
	String hanhDong;
	
	@Column(name = "su_dung_ocr")
	String suDungOcr;
	
	@Column(name = "kiem_tra_khuon_mat")
	String kiemTraKhuonMat;
	
	@Column(name = "ti_le_gia_mao_liveness")
	String tiLeGiaMaoLiveness;
	
	@Column(name = "luu_log_anh")
	String luuLogAnh;
	
	@Column(name = "telco")
	int telco;
	
	@Column(name = "video_call")
	String videoCall;
	
	@Column(name = "luu_giao_dich")
	String luuGiaoDich;
	
	@Column(name = "uri_su_dung")
	String uriSuDung;
	
	@Column(name = "nguong_nhan_dien_khuon_mat")
	String nguongNhanDienKhuonMat;
}
