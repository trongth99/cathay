package fis.com.vn.table;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "giay_chung_nhan")
@Data
public class GiayChungNhan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	

	@Nationalized
	String tenFile;
	
	
	@Nationalized
	String duongDanFile;

}
