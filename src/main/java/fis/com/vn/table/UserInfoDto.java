package fis.com.vn.table;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.Date;

@Data
@Entity
public class UserInfoDto {
    @Id
    private Long id;

    String username;

    @Column(name = "full_name")
    String fullName;

    Integer status;

    @Column(name = "group_id")
    String groupId;

    @Column(name = "create_time")
    Date createTime;

    @Column(name = "create_by")
    String createBy;

    String email;
    
    @Column(name = "dien_thoai")
    String dienThoai;
    
    @Column(name = "khu_vuc")
    String khuVuc;
    
    @Transient
    String nhomNguoiDung;
}
