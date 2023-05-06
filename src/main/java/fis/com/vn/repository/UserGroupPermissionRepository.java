package fis.com.vn.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.UserGroupPermission;

public interface UserGroupPermissionRepository extends CrudRepository<UserGroupPermission, Long>{

	@Transactional
	void deleteByGroupId(Long id);

	List<UserGroupPermission> findByGroupId(Long id);

}
