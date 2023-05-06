package fis.com.vn.init;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.common.CommonUtils;
import fis.com.vn.contains.Contains;
import fis.com.vn.repository.ConfigRepository;
import fis.com.vn.repository.UserGroupPermissionRepository;
import fis.com.vn.repository.UserGroupRepository;
import fis.com.vn.repository.UserInfoRepository;
import fis.com.vn.repository.UserModuleRepository;
import fis.com.vn.table.Config;
import fis.com.vn.table.UserGroup;
import fis.com.vn.table.UserGroupPermission;
import fis.com.vn.table.UserInfo;
import fis.com.vn.table.UserModule;

@Component
public class CreateData {
	@Autowired UserModuleRepository userModuleRepository;
	@Autowired UserGroupRepository userGroupRepository;
	@Autowired UserInfoRepository userInfoRepository;
	@Autowired UserGroupPermissionRepository userGroupPermissionRepository;
	@Autowired ConfigRepository configRepository;
	
	@PostConstruct
	void started() { 
		UserInfo userInfo = userInfoRepository.findByUsername("supper_admin");
		if(userInfo == null) {
			UserGroup userGroup = new UserGroup();
			userGroup.setGroupName("supper_admin");
			userGroup.setStatus(Contains.TT_NHOM_HOATDONG);
			userGroup.setCreateTime(new Date());
			userGroupRepository.save(userGroup);
			
			long idGroup = userGroup.getId();
			
			UserModule userModule = new UserModule();
			userModule.setIcon("nav-icon fa fa-dashboard");
			userModule.setName("quan_tri");
			userModule.setParentId(0L);
			userModule.setPlace(1);
			userModule.setStatus(Contains.TT_MODULE_HOATDONG);
			userModule.setUrl("");
			userModuleRepository.save(userModule);
			
			long id = userModule.getId();
			
			userModule = new UserModule();
			userModule.setIcon("");
			userModule.setName("nguoi_dung");
			userModule.setParentId(id);
			userModule.setPlace(1);
			userModule.setStatus(Contains.TT_MODULE_HOATDONG);
			userModule.setUrl("/nguoi-dung");
			userModuleRepository.save(userModule);
			
			long idModule = userModule.getId();
			UserGroupPermission userGroupPermission = new UserGroupPermission();
			userGroupPermission.setGroupId(idGroup);
			userGroupPermission.setModuleId(idModule);
			userGroupPermissionRepository.save(userGroupPermission);
			
			userModule = new UserModule();
			userModule.setIcon("");
			userModule.setName("nhom_nguoi_dung");
			userModule.setParentId(id);
			userModule.setPlace(1);
			userModule.setStatus(Contains.TT_MODULE_HOATDONG);
			userModule.setUrl("/nhom-nguoi-dung");
			userModuleRepository.save(userModule);
			
			idModule = userModule.getId();
			userGroupPermission = new UserGroupPermission();
			userGroupPermission.setGroupId(idGroup);
			userGroupPermission.setModuleId(idModule);
			userGroupPermissionRepository.save(userGroupPermission);
			
			userModule = new UserModule();
			userModule.setIcon("");
			userModule.setName("chuc_nang");
			userModule.setParentId(id);
			userModule.setPlace(1);
			userModule.setStatus(Contains.TT_MODULE_HOATDONG);
			userModule.setUrl("/chuc-nang");
			userModuleRepository.save(userModule);
			
			idModule = userModule.getId();
			userGroupPermission = new UserGroupPermission();
			userGroupPermission.setGroupId(idGroup);
			userGroupPermission.setModuleId(idModule);
			userGroupPermissionRepository.save(userGroupPermission);
			
			userModule = new UserModule();
			userModule.setIcon("");
			userModule.setName("config");
			userModule.setParentId(id);
			userModule.setPlace(1);
			userModule.setStatus(Contains.TT_MODULE_HOATDONG);
			userModule.setUrl("/config");
			userModuleRepository.save(userModule);
			
			idModule = userModule.getId();
			userGroupPermission = new UserGroupPermission();
			userGroupPermission.setGroupId(idGroup);
			userGroupPermission.setModuleId(idModule);
			userGroupPermissionRepository.save(userGroupPermission);
			
			UserInfo info = new UserInfo();
			info.setFullName("supper_admin");
			info.setGroupId(idGroup+"");
			info.setPassword(CommonUtils.getMD5("abcD1234"));
			info.setUsername("supper_admin");
			info.setStatus(Contains.TT_NGUOI_DUNG_HOATDONG);
			info.setCreateTime(new Date());
			userInfoRepository.save(info);
			
			Config config = new Config();
			config.setMa("version");
			config.setTrangThai(Contains.TT_CONFIG_HOATDONG);
			config.setGiaTri("1.0.0");
			configRepository.save(config);
		}
	}
}
