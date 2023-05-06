package fis.com.vn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.UserModule;

public interface UserModuleRepository extends CrudRepository<UserModule, Long>{

	@Query(value = "SELECT * FROM user_module WHERE status=?1 ORDER BY place", nativeQuery = true)
	Iterable<UserModule> selectAll(Integer ttModuleHoatdong);

	@Query(value = "SELECT * FROM user_module WHERE parent_id is null or parent_id=0 UNION ALL "
			+ "SELECT DISTINCT tbdm.* FROM user_group_permission tbpq INNER JOIN user_module tbdm ON tbpq.module_id=tbdm.id WHERE tbpq.group_id in ?1 order by place", nativeQuery = true)
	List<UserModule> selectGroupId(String[] groupIds);

	@Query(value = "SELECT * FROM user_module WHERE (?1 is null or url like %?1%)", nativeQuery = true)
	Page<UserModule> selectParams(String stringParams, Pageable pageable);

	List<UserModule> findByParentId(long l);

}
