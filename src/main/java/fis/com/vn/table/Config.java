/**
 * 
 */
package fis.com.vn.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Entity
@Table(name = "config", indexes = { @Index(name = "IDX_MA", columnList = "ma"),
		@Index(name = "IDX_STATUS", columnList = "trang_thai") })
@Data
public class Config {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;

	@Column(name = "ma", length = 150)
	@Size(max = 150, message = "Mã cấu hình không quá 150 kí tự", min = 1)
	@NotBlank(message = "Mã cấu hình không được để trống")
	String ma;

	@Column(name = "mo_ta", length = 250)
	@Size(max = 250, message = "Mô tả độ dài không quá 250 kí tự")
	@Nationalized
	String moTa;

	@Column(name = "gia_tri")
	@NotBlank(message = "Giá trị không được để trống")
	@Nationalized
	String giaTri;

	@Column(name = "trang_thai", length = 2)
	int trangThai;

	/*
	 * @Column(name = "hien_thi", length = 2) int hienThi;
	 */

}
