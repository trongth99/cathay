package fis.com.vn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fis.com.vn.table.EkycKyso;

@Repository
public interface EkycKysoRepository extends CrudRepository<EkycKyso, Long> {

	@Query(value = "select * from ekyc_kyso where " + "(:sten is null or hoVaTen like %:sten%) "
			+ "and (:ssoDienThoai is null or soDienThoai = :ssoDienThoai) "
			+ "and (:ssoCmt is null or soCmt = :ssoCmt) "
			+ "and (:smaKhachHang is null or maKhachHang = :smaKhachHang) "
			+ "and (:sstatus is null or trangThai = :sstatus) "
			+ "and (:sstatusekyc is null or trangThaiEkyc = :sstatusekyc) "
			+ "and (:tinhTrangCapMa is null or tinhTrangCapMa = :tinhTrangCapMa) "
			+ "and (:sngaySinh is null or ngaySinh = :sngaySinh) " + "and (:fromDate is null or ngayTao >= :fromDate)  "
			+ "and (:toDate is null or ngayTao < :toDate)  ", countQuery = "select count(1) from ekyc_kyso where "
					+ "(:sten is null or hoVaTen like %:sten%) "
					+ "and (:ssoDienThoai is null or soDienThoai = :ssoDienThoai) "
					+ "and (:ssoCmt is null or soCmt = :ssoCmt) "
					+ "and (:smaKhachHang is null or maKhachHang = :smaKhachHang) "
					+ "and (:sstatus is null or trangThai = :sstatus) "
					+ "and (:sstatusekyc is null or trangThaiEkyc = :sstatusekyc) "
					+ "and (:tinhTrangCapMa is null or tinhTrangCapMa = :tinhTrangCapMa) "
					+ "and (:sngaySinh is null or ngaySinh = :sngaySinh) "
					+ "and (:fromDate is null or ngayTao >= :fromDate)  "
					+ "and (:toDate is null or ngayTao < :toDate)  ", nativeQuery = true)
	Page<EkycKyso> selectParams(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("sten") String sten, @Param("ssoDienThoai") String ssoDienThoai, @Param("ssoCmt") String ssoCmt,
			@Param("smaKhachHang") String smaKhachHang, @Param("sstatus") String sstatus,
			@Param("tinhTrangCapMa") String tinhTrangCapMa, @Param("sstatusekyc") String sstatusekyc,
			@Param("sngaySinh") String sngaySinh, Pageable pageable);

	@Query(value = "select * from ekyc_kyso where " + "(:sten is null or hoVaTen like %:sten%) "
			+ "and (:ssoDienThoai is null or soDienThoai = :ssoDienThoai) "
			+ "and (:ssoCmt is null or soCmt = :ssoCmt) "
			+ "and (:smaKhachHang is null or maKhachHang = :smaKhachHang) "
			+ "and (:sstatus is null or trangThai = :sstatus) "
			+ "and (:sstatusekyc is null or trangThaiEkyc = :sstatusekyc) "
			+ "and (:tinhTrangCapMa is null or tinhTrangCapMa = :tinhTrangCapMa) "
			+ "and (:sngaySinh is null or ngaySinh = :sngaySinh) " + "and (:fromDate is null or ngayTao >= :fromDate)  "
			+ "and (:toDate is null or ngayTao < :toDate)  ", countQuery = "select count(1) from ekyc_kyso where "
					+ "(:sten is null or hoVaTen like %:sten%) "
					+ "and (:ssoDienThoai is null or soDienThoai = :ssoDienThoai) "
					+ "and (:ssoCmt is null or soCmt = :ssoCmt) "
					+ "and (:smaKhachHang is null or maKhachHang = :smaKhachHang) "
					+ "and (:sstatus is null or trangThai = :sstatus) "
					+ "and (:sstatusekyc is null or trangThaiEkyc = :sstatusekyc) "
					+ "and (:tinhTrangCapMa is null or tinhTrangCapMa = :tinhTrangCapMa) "
					+ "and (:sngaySinh is null or ngaySinh = :sngaySinh) "
					+ "and (:ngayTao is null or ngayTao >= :fromDate)  "
					+ "and (:toDate is null or ngayTao < :toDate)  ", nativeQuery = true)
	List<EkycKyso> selectParamsAll(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("sten") String sten, @Param("ssoDienThoai") String ssoDienThoai, @Param("ssoCmt") String ssoCmt,
			@Param("smaKhachHang") String smaKhachHang, @Param("sstatus") String sstatus,
			@Param("tinhTrangCapMa") String tinhTrangCapMa, @Param("sstatusekyc") String sstatusekyc,
			@Param("sngaySinh") String sngaySinh);

	EkycKyso findBySoDienThoai(String soDienThoai);

	@Query(value = "select * from ekyc_kyso where " + " (:fromDate is null or ngayTao >= :fromDate)  "
			+ "and (:toDate is null or ngayTao < :toDate)  "
			+ "and (:fromDateSign is null or khachHangKy >= :fromDateSign)  "
			+ "and (:toDateSign is null or khachHangKy < :toDateSign)  "
			+ "and (:trangThai is null or trangThai = :trangThai) "
			+ "and (:soDienThoai is null or soDienThoai =:soDienThoai) "
			+ "and (:hoVaTen is null or hoVaTen like %:hoVaTen%) " + "and (:soCmt is null or soCmt =:soCmt) "
			+ "and (:soTaiKhoan is null or soTaiKhoan =:soTaiKhoan) " + "and (:khuVuc is null or khuVuc=:khuVuc) "
			+ "and (:ghiChu is null or ghiChu=:ghiChu) " + "and (:nguoiTao is null or nguoiTao=:nguoiTao) "
			+ "and (:moTKTTSB is null or moTKTTSB=:moTKTTSB) " + "and (:spHanMuc is null or spHanMuc=:spHanMuc) "
			+ "and (:ttTKTT is null or ttTKTT=:ttTKTT) "
			+ "and (:yeuCauGiaiNgan is null or yeuCauGiaiNgan=:yeuCauGiaiNgan) "
			+ "and (:fromNgayYC is null or ngayYCMoTKTT >= :fromNgayYC)  "
			+ "and (:trangThaiGuiChungTu is null or ttGuiChungTu=:trangThaiGuiChungTu)  "
			+ "and (:toDateNgayYC is null or ngayYCMoTKTT < :toDateNgayYC)  ", countQuery = "select count(1) from ekyc_kyso where "
					+ " (:fromDate is null or ngayTao >= :fromDate)  " + "and (:toDate is null or ngayTao < :toDate)  "
					+ "and (:fromDateSign is null or khachHangKy >= :fromDateSign)  "
					+ "and (:toDateSign is null or khachHangKy < :toDateSign)  "
					+ "and (:trangThai is null or trangThai = :trangThai) "
					+ "and (:soDienThoai is null or soDienThoai =:soDienThoai) "
					+ "and (:hoVaTen is null or hoVaTen like %:hoVaTen%) " + "and (:soCmt is null or soCmt =:soCmt) "
					+ "and (:soTaiKhoan is null or soTaiKhoan =:soTaiKhoan) "
					+ "and (:khuVuc is null or khuVuc=:khuVuc) " + "and (:ghiChu is null or ghiChu=:ghiChu) "
					+ "and (:nguoiTao is null or nguoiTao=:nguoiTao) "
					+ "and (:moTKTTSB is null or moTKTTSB=:moTKTTSB) "
					+ "and (:spHanMuc is null or spHanMuc=:spHanMuc) " + "and (:ttTKTT is null or ttTKTT=:ttTKTT) "
					+ "and (:yeuCauGiaiNgan is null or yeuCauGiaiNgan=:yeuCauGiaiNgan) "
					+ "and (:fromNgayYC is null or ngayYCMoTKTT >= :fromNgayYC)  "
					+ "and (:trangThaiGuiChungTu is null or ttGuiChungTu=:trangThaiGuiChungTu)  "
					+ "and (:toDateNgayYC is null or ngayYCMoTKTT < :toDateNgayYC)  ", nativeQuery = true)
	Page<EkycKyso> selectParams2(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("trangThai") String trangThai, @Param("soDienThoai") String soDienThoai,
			@Param("hoVaTen") String hoVaTen, @Param("soCmt") String soCmt, @Param("soTaiKhoan") String soTaiKhoan,
			@Param("khuVuc") String khuVuc, @Param("fromDateSign") Date fromDateSign,
			@Param("toDateSign") Date toDateSign, @Param("ghiChu") String ghiChu, @Param("nguoiTao") String nguoiTao,
			@Param("moTKTTSB") String moTKTTSB, @Param("spHanMuc") String spHanMuc, @Param("ttTKTT") String ttTKTT,
			@Param("yeuCauGiaiNgan") String yeuCauGiaiNgan, @Param("fromNgayYC") Date fromNgayYC,
			@Param("toDateNgayYC") Date toDateNgayYC, @Param("trangThaiGuiChungTu") String trangThaiGuiChungTu,
			Pageable pageable);

	@Query(value = "select * from ekyc_kyso where " + " (:fromDate is null or ngayTao >= :fromDate)  "
			+ "and (:toDate is null or ngayTao < :toDate)  "
			+ "and (:fromDateSign is null or khachHangKy >= :fromDateSign)  "
			+ "and (:toDateSign is null or khachHangKy < :toDateSign)  "
			+ "and (:trangThai is null or trangThai = :trangThai) "
			+ "and (:soDienThoai is null or soDienThoai =:soDienThoai) "
			+ "and (:hoVaTen is null or hoVaTen like %:hoVaTen%) " + "and (:soCmt is null or soCmt =:soCmt) "
			+ "and (:soTaiKhoan is null or soTaiKhoan =:soTaiKhoan) " + "and (:khuVuc is null or khuVuc=:khuVuc) "
			+ "and (:ghiChu is null or ghiChu=:ghiChu) " + "and (:nguoiTao is null or nguoiTao=:nguoiTao) "
			+ "and (:moTKTTSB is null or moTKTTSB=:moTKTTSB) " + "and (:spHanMuc is null or spHanMuc=:spHanMuc) "
			+ "and (:ttTKTT is null or ttTKTT=:ttTKTT) "
			+ "and (:yeuCauGiaiNgan is null or yeuCauGiaiNgan=:yeuCauGiaiNgan) "
			+ "and (:fromNgayYC is null or ngayYCMoTKTT >= :fromNgayYC)  "
			+ "and (:trangThaiGuiChungTu is null or ttGuiChungTu=:trangThaiGuiChungTu)  "
			+ "and (:toDateNgayYC is null or ngayYCMoTKTT < :toDateNgayYC)  ", countQuery = "select count(1) from ekyc_kyso where "
					+ " (:fromDate is null or ngayTao >= :fromDate)  " + "and (:toDate is null or ngayTao < :toDate)  "
					+ "and (:fromDateSign is null or khachHangKy >= :fromDateSign)  "
					+ "and (:toDateSign is null or khachHangKy < :toDateSign)  "
					+ "and (:trangThai is null or trangThai = :trangThai) "
					+ "and (:soDienThoai is null or soDienThoai =:soDienThoai) "
					+ "and (:hoVaTen is null or hoVaTen like %:hoVaTen%) " + "and (:soCmt is null or soCmt =:soCmt) "
					+ "and (:soTaiKhoan is null or soTaiKhoan =:soTaiKhoan) "
					+ "and (:khuVuc is null or khuVuc=:khuVuc) " + "and (:ghiChu is null or ghiChu=:ghiChu) "
					+ "and (:nguoiTao is null or nguoiTao=:nguoiTao) "
					+ "and (:moTKTTSB is null or moTKTTSB=:moTKTTSB) "
					+ "and (:spHanMuc is null or spHanMuc=:spHanMuc) " + "and (:ttTKTT is null or ttTKTT=:ttTKTT) "
					+ "and (:yeuCauGiaiNgan is null or yeuCauGiaiNgan=:yeuCauGiaiNgan) "
					+ "and (:fromNgayYC is null or ngayYCMoTKTT >= :fromNgayYC)  "
					+ "and (:trangThaiGuiChungTu is null or ttGuiChungTu=:trangThaiGuiChungTu)  "
					+ "and (:toDateNgayYC is null or ngayYCMoTKTT < :toDateNgayYC)  ", nativeQuery = true)
	List<EkycKyso> selectParams2All(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("trangThai") String trangThai, @Param("soDienThoai") String soDienThoai,
			@Param("hoVaTen") String hoVaTen, @Param("soCmt") String soCmt, @Param("soTaiKhoan") String soTaiKhoan,
			@Param("khuVuc") String khuVuc, @Param("fromDateSign") Date fromDateSign,
			@Param("toDateSign") Date toDateSign, @Param("ghiChu") String ghiChu, @Param("nguoiTao") String nguoiTao,
			@Param("moTKTTSB") String moTKTTSB, @Param("spHanMuc") String spHanMuc, @Param("ttTKTT") String ttTKTT,
			@Param("yeuCauGiaiNgan") String yeuCauGiaiNgan, @Param("fromNgayYC") Date fromNgayYC,
			@Param("toDateNgayYC") Date toDateNgayYC, @Param("trangThaiGuiChungTu") String trangThaiGuiChungTu);

	EkycKyso findBySoCmt(String soCmt);

	@Query(value = "select * from ekyc_kyso where " + " (:fromDate is null or ngayTao >= :fromDate)  "
			+ "and (:toDate is null or ngayTao < :toDate)  " + "and trangThai  =:trangThais "

			+ "and (:soDienThoai is null or soDienThoai like %:soDienThoai%) "
			+ "and (:hoVaTen is null or hoVaTen like %:hoVaTen%) "
			+ "and (:soCmt is null or soCmt like %:soCmt%) ", countQuery = "select count(1) from ekyc_kyso where "
					+ " (:fromDate is null or ngayTao >= :fromDate)  " + "and (:toDate is null or ngayTao < :toDate)  "
					+ "and trangThai  =:trangThais "
					+ "and (:tinhTrangCapMa is null or tinhTrangCapMa = :tinhTrangCapMa) "
					+ "and (:soDienThoai is null or soDienThoai like %:soDienThoai%) "
					+ "and (:hoVaTen is null or hoVaTen like %:hoVaTen%) "
					+ "and (:soCmt is null or soCmt like %:soCmt%)", nativeQuery = true)
	Page<EkycKyso> selectParamsDemo(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate,
			@Param("trangThais") String trangThais, @Param("soDienThoai") String soDienThoai,
			@Param("hoVaTen") String hoVaTen, @Param("soCmt") String soCmt, Pageable pageable);

	EkycKyso findBySoCmtAndToken(String string, String token);

	EkycKyso findByToken(String token);

	List<EkycKyso> findBySoTaiKhoan(String soHopDong);

	@Query(value = "select * from ekyc_kyso where " + " (:dateEnd is null or ngayYCMoTKTT <= :dateEnd)  "
			+ "and moTKTTSB ='Thành công' and cronDongTk is null", countQuery = "select count(1) from ekyc_kyso where "
					+ " (:dateEnd is null or ngayYCMoTKTT <= :dateEnd)  "
					+ "and moTKTTSB ='Thành công' and cronDongTk is null", nativeQuery = true)
	List<EkycKyso> selectCloseAcc(@Param("dateEnd") Date dateEnd);

	@Query(value = "SELECT * FROM ekyc_kyso  WHERE tenFile = :tenFile ", nativeQuery = true)
	EkycKyso findByTenFile(String tenFile);

	@Query(value = "SELECT COUNT(*) FROM ekyc_kyso  WHERE Month(baoHiemKy) =:baoHiemKy and trangThai =:trangThai ", nativeQuery = true)
	Integer countKySo(String baoHiemKy, String trangThai);

	@Query(value = "SELECT COUNT(*) FROM ekyc_kyso  WHERE Month(baoHiemKy) =:baoHiemKy and trangThaiGui =:trangThaiGui ", nativeQuery = true)
	Integer countDaGui(String baoHiemKy, String trangThaiGui);

	@Query(value = "SELECT COUNT(*) FROM ekyc_kyso  WHERE Day(baoHiemKy) =:Day and Month(baoHiemKy) =:Month and trangThai =2 ", nativeQuery = true)
	Integer countKySoDay(String Day, String Month);

	@Query(value = "SELECT COUNT(*) FROM ekyc_kyso  WHERE Day(baoHiemKy) =:Day and Month(baoHiemKy) =:Month and trangThaiGui =2 ", nativeQuery = true)
	Integer countDaGuiDay(String Day, String Month);

	@Query(value = "SELECT COUNT(*) FROM ekyc_kyso  WHERE  trangThai =2 ", nativeQuery = true)
	Integer sumDaKy();

	@Query(value = "SELECT COUNT(*) FROM ekyc_kyso  WHERE  trangThaiGui =2 ", nativeQuery = true)
	Integer sumDaGui();

}
