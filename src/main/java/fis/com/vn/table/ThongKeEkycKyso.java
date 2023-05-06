package fis.com.vn.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ekyc_kyso_thong_ke")
@Data
public class ThongKeEkycKyso {
	@Id
	long id;

	@Column(name = "trangThaiKy")
	// @Nationalized
	String trangThaiKy;

	@Column(name = "tongSoKy")
	int tongSoKy;

	@Column(name = "trangThaiGui")
	// @Nationalized
	String trangThaiGui;

	@Column(name = "tongSoGui")
	int tongSoGui;
}
