package fis.com.vn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fis.com.vn.table.LogApiSeaBank;

@Repository
public interface LogApiSeaBankRepository extends CrudRepository<LogApiSeaBank, Long> {

	/**
	 * @param parse
	 * @param parse2
	 * @param stringParams
	 * @param pageable
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT * FROM log_api_seabank WHERE " + "date >= :fromDate "
			+ "AND date < :toDate "
			+ "AND (:code is null or code = :code)", countQuery = "SELECT count(*) FROM log_api WHERE "
					+ "date >= :fromDate " + "AND date < :toDate " + "AND (:code is null or code = :code)")
	Page<LogApiSeaBank> danhSachUri(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("code") String code, Pageable pageable);

	LogApiSeaBank findByLogId(String orDefault);

	@Query(value = "select * from log_api_seabank where " 
			+ " (:status is null or status = :status) "
			+ "and (:uri is null or uri = :uri) " 
			+ "and (:soCmt is null or id_card = :soCmt) "
			+ "and (:soHopDong is null or id_contract = :soHopDong) " 
			+ "and (:id_los is null or id_los = :id_los) "
			+ "and (:loaiApi is null or mota = :loaiApi) "
			+ "and create_date >= :fromDate AND create_date < :toDate order by create_date desc "
			, countQuery = "select count(1) from log_api_seabank where "
					+ " (:status is null or status = :status) " 
					+ " and (:uri is null or uri = :uri) "
					+ "and (:soCmt is null or id_card = :soCmt) "
					+ "and (:soHopDong is null or id_contract = :soHopDong) "
					+ "and (:id_los is null or id_los = :id_los) "
					+ "and (:loaiApi is null or mota = :loaiApi) "
					+ " and create_date >= :fromDate AND create_date < :toDate ", nativeQuery = true)

	Page<LogApiSeaBank> selectParams(
			@Param("uri") String uri, 
			@Param("soCmt") String soCmt, 
			@Param("soHopDong") String soHopDong,
			@Param("id_los") String id_los, 
			@Param("status") Integer string, 
			@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate,
			@Param("loaiApi") String loaiApi,
			Pageable pageable);
	
	@Query(value = "select * from log_api_seabank where " 
			+ " (:status is null or status = :status) "
			+ "and (:uri is null or uri = :uri) " 
			+ "and (:soCmt is null or id_card = :soCmt) "
			+ "and (:soHopDong is null or id_contract = :soHopDong) " 
			+ "and (:id_los is null or id_los = :id_los) "
			+ "and (:loaiApi is null or mota = :loaiApi) "
			+ "and create_date >= :fromDate AND create_date < :toDate order by create_date desc ", 
			countQuery = "select count(1) from log_api_seabank where "
					+ " (:status is null or status = :status) " 
					+ " and (:uri is null or uri = :uri) "
					+ "and (:soCmt is null or id_card = :soCmt) "
					+ "and (:soHopDong is null or id_contract = :soHopDong) "
					+ "and (:id_los is null or id_los = :id_los) "
					+ "and (:loaiApi is null or mota = :loaiApi) "
					+ " and create_date >= :fromDate AND create_date < :toDate ", nativeQuery = true)

	List<LogApiSeaBank> selectParamsAll(
			@Param("uri") String uri, 
			@Param("soCmt") String soCmt, 
			@Param("soHopDong") String soHopDong,
			@Param("id_los") String id_los, 
			@Param("status") Integer string, 
			@Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate,
			@Param("loaiApi") String loaiApi
			);

	@Query(value = "select * from log_api_seabank where " + "(:khachHang is null or code = :khachHang) "
			+ "and (:status is null or status = :status) " + "and (:uri is null or uri = :uri) "
			+ "and (:maGiaoDich is null or code_transaction  = :maGiaoDich) "
			+ "and (:soDienThoai is null or phone = :soDienThoai) " + "and (:soCmt is null or id_card = :soCmt) "
			+ "and (:soHopDong is null or id_contract = :soHopDong) "
			+ "and (:hoVaTen is null or full_name = :hoVaTen) "
			+ "and create_date >= :fromDate AND create_date < :toDate order by create_date desc", countQuery = "select count(1) from log_api_seabank where  "
					+ "(:khachHang is null or code = :khachHang) " + "and (:status is null or status = :status) "
					+ "and (:uri is null or uri = :uri) "
					+ "and (:maGiaoDich is null or code_transaction  = :maGiaoDich) "
					+ "and (:soDienThoai is null or phone = :soDienThoai) "
					+ "and (:soCmt is null or id_card = :soCmt) "
					+ "and (:soHopDong is null or id_contract = :soHopDong) "
					+ "and (:hoVaTen is null or full_name = :hoVaTen) "
					+ "and create_date >= :fromDate AND create_date < :toDate", nativeQuery = true)

	List<LogApiSeaBank> selectParamsAll(@Param("khachHang") String khachHang, @Param("uri") String uri,
			@Param("maGiaoDich") String maGiaoDich, @Param("status") Integer status, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate
	/*
	 * @Param("soDienThoai") String soDienThoai ,
	 * 
	 * @Param("soCmt") String soCmt , @Param("soHopDong") String soHopDong
	 * , @Param("hoVaTen") String hoVaTen
	 */);

	/**
	 * @param string
	 * @return
	 */
	List<LogApiSeaBank> findByCode(String string);

	/**
	 * @param string
	 * @return
	 */
	List<LogApiSeaBank> findByUri(String string);

	List<LogApiSeaBank> findByCodeTransaction(String string);

}
