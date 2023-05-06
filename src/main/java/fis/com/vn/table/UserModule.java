package fis.com.vn.table;

import java.io.Serializable;

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

@SuppressWarnings("serial")
@Entity
@Table(name = "user_module", indexes = { @Index(name = "IDX_URL", columnList = "url"), @Index(name = "IDX_STATUS", columnList = "status") })
@Data
public class UserModule implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nationalized
	@Size(max = 255, message = "Tên chức năng độ dài không quá 255 kí tự")
	@Column(name = "name", length = 255)
	@NotBlank(message = "Tên chức năng không được để trống")
	String name;
	
	@Column(name = "status")
	Integer status;
	
	@Column(name = "parent_id")
	Long parentId;
	
	@Nationalized
	@Size(max = 255, message = "Url độ dài không quá 255 kí tự")
	@Column(name = "url", length = 255)
	String url;
	
	@Column(name = "place")
	long place;
	
	@Nationalized
	@Size(max = 255, message = "Icon độ dài không quá 255 kí tự")
	@Column(name = "icon", length = 255)
	String icon;
}
