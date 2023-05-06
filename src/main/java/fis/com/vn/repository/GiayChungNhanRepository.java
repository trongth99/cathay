package fis.com.vn.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fis.com.vn.table.GiayChungNhan;


@Repository
public interface GiayChungNhanRepository extends CrudRepository<GiayChungNhan,Long> {
	
	@Query(value = "SELECT * FROM giay_chung_nhan  WHERE tenFile = :tenFile ", nativeQuery = true)
	     GiayChungNhan   findByTenFile(String tenFile);

}
