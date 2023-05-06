package fis.com.vn.table;

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
@Table(name = "log_api_detail", indexes = { 
		@Index(name = "IDX_LOG_ID", columnList = "log_id")
		})
@Data
public class LogApiDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@Column(name = "log_id", length = 150)
	String logId;
	
	@Column(name = "params", columnDefinition = "LONGTEXT")
	@Nationalized
	String params;
	
	@Column(name = "response", columnDefinition = "LONGTEXT")
	@Nationalized
	String response;
	
	@Column(name = "images", columnDefinition = "LONGTEXT")
	@Nationalized
	String images;
}
