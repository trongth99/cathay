package fis.com.vn.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "SoHD")
@Data
public class SoHD {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@Nationalized
	String soHD;
	@Nationalized
	String email;
	@Nationalized
	String hovaten;
	@Nationalized
	String dienthoai;
	@Nationalized
	String socmt;
	
	@Nationalized
	String anhCaNhan;
	@Nationalized
	String duongDanHd;
	
	@Nationalized
	String duongDanFileSeaBank;
}
