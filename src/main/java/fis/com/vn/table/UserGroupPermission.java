package fis.com.vn.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "user_group_permission", indexes = { 
		@Index(name = "IDX_GROUP_ID", columnList = "group_id"),
		@Index(name = "IDX_MODULE_ID", columnList = "module_id") 
		})
@Data
public class UserGroupPermission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "group_id")
	long groupId;
	
	@Column(name = "module_id")
	long moduleId;
}
