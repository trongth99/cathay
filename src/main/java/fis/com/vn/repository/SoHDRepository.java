package fis.com.vn.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import fis.com.vn.table.SoHD;

@Repository
public interface SoHDRepository extends CrudRepository<SoHD,Long>{

	
	@Query(value = "SELECT * FROM SoHD  WHERE soHD = :soHD ", nativeQuery = true)
	SoHD   findBySoHd(String soHD);
}
