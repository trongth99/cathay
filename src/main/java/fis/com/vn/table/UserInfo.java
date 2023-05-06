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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import lombok.Data;

@Entity
@Table(name = "user_info", indexes = { 
		@Index(name = "IDX_USERNAME", columnList = "username"),
		@Index(name = "IDX_FULLNAME", columnList = "full_name"),
		@Index(name = "IDX_EMAIL", columnList = "email"), 
		@Index(name = "IDX_STATUS", columnList = "status") 
		})
@Data
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nationalized
	@Size(max = 255, message = "Tên đăng nhập dài không quá 255 ký tự")
	@Column(name = "username", length = 255)
	@NotBlank(message = "Tên đăng nhập không được để trống")
	String username;
	
	@Nationalized
	@Size(max = 255, message = "Mật khẩu không dài quá 255 ký tự")
	@Column(name = "password", length = 255)
	String password;
	
	@Nationalized
	@Size(max = 255, message = "Họ và tên tối đa 255 ký tự")
	@Column(name = "full_name", length = 255)
	@NotBlank(message = "Họ và tên không được để trống")
	String fullName;
	
	@Column(name = "status")
	Integer status;
	
	@Column(name = "group_id")
	String groupId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	Date createTime;
	
	@Nationalized
	@Size(max = 255, message = "Độ dài không quá 255 kí tự")
	@Column(name = "create_by", length = 255)
	String createBy;
	
	@Nationalized
	@Size(max = 255, message = "Độ dài không quá 255 kí tự")
	@Column(name = "email", length = 255)
	@Email(message = "Email không đúng định dạng")
	String email;
	
	@Nationalized
	@Size(max = 15, message = "Độ dài không quá 15 kí tự")
	@Column(name = "dien_thoai", length = 15) 
	String dienThoai;
	
	@Column(name = "khu_vuc", columnDefinition = "TEXT")
	@Nationalized
	String khuVuc;
}
