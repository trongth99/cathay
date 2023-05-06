/**
 * 
 */
package fis.com.vn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.Config;

/**
 * @author ChinhVD4
 *
 */
public interface ConfigRepository extends CrudRepository<Config, Long>{

	/**
	 * @param stringParams
	 * @param pageable
	 * @return
	 */
	@Query(nativeQuery = true, 
			value = "SELECT * FROM config WHERE (?1 is null or ma like %?1%) AND hien_thi=1", 
			countQuery = "SELECT count(*) FROM config WHERE (?1 is null or ma like %?1%)  AND hien_thi=1")
	Page<Config> selectParams(String ma, Pageable pageable);

	/**
	 * @param ma
	 * @return
	 */
	Config findByMa(String ma);

	List<Config> findByTrangThai(Integer ttConfigHoatdong);

}
