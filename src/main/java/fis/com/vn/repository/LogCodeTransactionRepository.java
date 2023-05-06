/**
 * 
 */
package fis.com.vn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fis.com.vn.table.LogCodeTransaction;

/**
 * @author ChinhVD4
 *
 */
public interface LogCodeTransactionRepository extends CrudRepository<LogCodeTransaction, Long>{

	/**
	 * @param codeTransaction
	 * @return
	 */
	LogCodeTransaction findByCodeTransaction(String codeTransaction);

	
	@Query(nativeQuery = true, value = "SELECT count(*) FROM log_code_transaction WHERE date >= :ngayBatDau AND (:code is null or code = :code)")
	int countTransactionByDate(@Param("ngayBatDau") Date ngayBatDau, @Param("code")  String code);

	@Query(value = "select * from log_code_transaction where "+ 
			"(:khachHang is null or code = :khachHang) " +
            "and (:maGiayTo is null or code_transaction = :maGiayTo) " +
            "and (:soDienThoai is null or phone = :soDienThoai) " +
            "and (:soCmt is null or id_card = :soCmt) " +
            "and (:soHopDong is null or id_contract = :soHopDong) " +
            "and (:hoVaTen is null or full_name = :hoVaTen) " +
            "and date >= :fromDate AND date < :toDate order by date desc",
            countQuery = "select count(1) from log_code_transaction where  " +
            		"(:khachHang is null or code = :khachHang) " +
                    "and (:maGiayTo is null or code_transaction = :maGiayTo) " +
                    "and (:soDienThoai is null or phone = :soDienThoai) " +
                    "and (:soCmt is null or id_card = :soCmt) " +
                    "and (:soHopDong is null or id_contract = :soHopDong) " +
                    "and (:hoVaTen is null or full_name = :hoVaTen) " +
                    "and date >= :fromDate AND date < :toDate"
                    , nativeQuery = true)
	Page<LogCodeTransaction> selectParams(@Param("khachHang") String code
			, @Param("maGiayTo") String maGiayTo
			, @Param("fromDate") Date fromDate
			, @Param("toDate") Date toDate
			, @Param("soDienThoai") String soDienThoai
			, @Param("soCmt") String soCmt
			, @Param("soHopDong") String soHopDong
			, @Param("hoVaTen") String hoVaTen
			, Pageable pageable);
	
	@Query(value = "select * from log_code_transaction where "+ 
			"(:khachHang is null or code = :khachHang) " +
            "and (:maGiayTo is null or code_transaction = :maGiayTo) " +
            "and (:soDienThoai is null or phone = :soDienThoai) " +
            "and (:soCmt is null or id_card = :soCmt) " +
            "and (:soHopDong is null or id_contract = :soHopDong) " +
            "and (:hoVaTen is null or full_name = :hoVaTen) " +
            "and date >= :fromDate AND date < :toDate order by date desc",
            countQuery = "select count(1) from log_code_transaction where  " +
            		"(:khachHang is null or code = :khachHang) " +
                    "and (:maGiayTo is null or code_transaction = :maGiayTo) " +
                    "and (:soDienThoai is null or phone = :soDienThoai) " +
                    "and (:soCmt is null or id_card = :soCmt) " +
                    "and (:soHopDong is null or id_contract = :soHopDong) " +
                    "and (:hoVaTen is null or full_name = :hoVaTen) " +
                    "and date >= :fromDate AND date < :toDate"
                    , nativeQuery = true)
	List<LogCodeTransaction> selectParamsAll(@Param("khachHang") String code
			, @Param("maGiayTo") String maGiayTo
			, @Param("fromDate") Date fromDate
			, @Param("toDate") Date toDate
			, @Param("soDienThoai") String soDienThoai
			, @Param("soCmt") String soCmt
			, @Param("soHopDong") String soHopDong
			, @Param("hoVaTen") String hoVaTen
			);
}
