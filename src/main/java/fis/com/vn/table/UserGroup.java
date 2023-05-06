package fis.com.vn.table;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "user_group", indexes = { 
		@Index(name = "IDX_GROUPNAME", columnList = "group_name"),
		@Index(name = "IDX_DESCRIPTION", columnList = "description"),
		@Index(name = "IDX_STATUS", columnList = "status") 
		})
@Data
public class UserGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nationalized
	@Size(max = 255, message = "Độ dài 1 - 255 kí tự", min = 1)
	@Column(name = "group_name", length = 255)
	String groupName;
	
	@Nationalized
	@Size(max = 255, message = "Độ dài không quá 255 kí tự")
	@Column(name = "description", length = 255)
	String description;
	
	@Column(name = "status")
	Integer status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	Date createTime;
	
	@Nationalized
	@Size(max = 255, message = "Độ dài không quá 255 kí tự")
	@Column(name = "create_by", length = 255)
	String createBy;
}
