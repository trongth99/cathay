/**
 * 
 */
package fis.com.vn.table;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Entity
@Table(name = "quan_ly_chu_ky_so")
@Data
public class QuanLyChuKySo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@Column(name = "ma")
	@Nationalized
	String ma ;
	
	@Column(name = "mat_khau")
	@Nationalized
	String matKhau ;
	
	@Column(name = "mac_dinh")
	@Nationalized
	String macDinh ;
	
	@Column(name = "nguoi_dai_dien")
	@Nationalized
	String nguoiDaiDien ;
	
	@Column(name = "ngay_bat_dau")
	Date ngayBatDau ;
	
	@Column(name = "ngay_ket_thuc")
	Date ngayKetThuc ;
}
