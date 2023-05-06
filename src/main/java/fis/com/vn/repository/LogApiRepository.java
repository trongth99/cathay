package fis.com.vn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fis.com.vn.table.LogApi;

@Repository
public interface LogApiRepository extends CrudRepository<LogApi, Long>{

	/**
	 * @param parse
	 * @param parse2
	 * @param stringParams
	 * @param pageable
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT * FROM log_api WHERE "
			+ "date >= :fromDate "
			+ "AND date < :toDate "
			+ "AND (:code is null or code = :code)",
			countQuery = "SELECT count(*) FROM log_api WHERE "
					+ "date >= :fromDate "
					+ "AND date < :toDate "
					+ "AND (:code is null or code = :code)"
			)
	Page<LogApi> danhSachUri(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("code") String code, Pageable pageable);

	LogApi findByLogId(String orDefault);
	
	@Query(value = "select * from log_api where  "+ 
			"(:khachHang is null or code = :khachHang) " +
            "and (:status is null or status = :status) " +
            "and (:uri is null or uri = :uri) " +
            "and (:maGiaoDich is null or code_transaction  = :maGiaoDich) " +
            "and (:soDienThoai is null or phone = :soDienThoai) " +
            "and (:soCmt is null or id_card = :soCmt) " +
            "and (:soHopDong is null or id_contract = :soHopDong) " +
            "and (:hoVaTen is null or full_name = :hoVaTen) " +
            "and create_date >= :fromDate AND create_date < :toDate order by create_date desc",
            countQuery = "select count(1) from log_api where  " +
            		"(:khachHang is null or code = :khachHang) " +
                    "and (:status is null or status = :status) " +
                    "and (:uri is null or uri = :uri) " +
                    "and (:maGiaoDich is null or code_transaction  = :maGiaoDich) " +
                    "and (:soDienThoai is null or phone = :soDienThoai) " +
                    "and (:soCmt is null or id_card = :soCmt) " +
                    "and (:soHopDong is null or id_contract = :soHopDong) " +
                    "and (:hoVaTen is null or full_name = :hoVaTen) " +
                    "and create_date >= :fromDate AND create_date < :toDate"
                    , nativeQuery = true)
	
	Page<LogApi> selectParams(@Param("khachHang") String khachHang, 
			@Param("uri") String uri,
			@Param("maGiaoDich") String maGiaoDich, 
			@Param("status") Integer string, 
			@Param("fromDate") Date fromDate, 
			@Param("toDate") Date toDate,
			@Param("soDienThoai") String soDienThoai
			, @Param("soCmt") String soCmt
			, @Param("soHopDong") String soHopDong
			, @Param("hoVaTen") String hoVaTen
			, Pageable pageable);
	
	
	@Query(value = "select * from log_api where "+ 
			"(:khachHang is null or code = :khachHang) " +
            "and (:status is null or status = :status) " +
            "and (:uri is null or uri = :uri) " +
            "and (:maGiaoDich is null or code_transaction  = :maGiaoDich) " +
            "and (:soDienThoai is null or phone = :soDienThoai) " +
            "and (:soCmt is null or id_card = :soCmt) " +
            "and (:soHopDong is null or id_contract = :soHopDong) " +
            "and (:hoVaTen is null or full_name = :hoVaTen) " +
            "and create_date >= :fromDate AND create_date < :toDate order by create_date desc",
            countQuery = "select count(1) from log_api where  " +
            		"(:khachHang is null or code = :khachHang) " +
                    "and (:status is null or status = :status) " +
                    "and (:uri is null or uri = :uri) " +
                    "and (:maGiaoDich is null or code_transaction  = :maGiaoDich) " +
                    "and (:soDienThoai is null or phone = :soDienThoai) " +
                    "and (:soCmt is null or id_card = :soCmt) " +
                    "and (:soHopDong is null or id_contract = :soHopDong) " +
                    "and (:hoVaTen is null or full_name = :hoVaTen) " +
                    "and create_date >= :fromDate AND create_date < :toDate"
                    , nativeQuery = true)
	
	List<LogApi> selectParamsAll(@Param("khachHang") String khachHang, 
			@Param("uri") String uri,
			@Param("maGiaoDich") String maGiaoDich, 
			@Param("status") Integer status, 
			@Param("fromDate") Date fromDate, 
			@Param("toDate") Date toDate,
			@Param("soDienThoai") String soDienThoai
			, @Param("soCmt") String soCmt
			, @Param("soHopDong") String soHopDong
			, @Param("hoVaTen") String hoVaTen);

	/**
	 * @param string
	 * @return
	 */
	List<LogApi> findByCode(String string);

	/**
	 * @param string
	 * @return
	 */
	List<LogApi> findByUri(String string);

	List<LogApi> findByCodeTransaction(String string);

}
