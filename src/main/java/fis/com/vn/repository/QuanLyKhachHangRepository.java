package fis.com.vn.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.QuanLyKhachHang;

public interface QuanLyKhachHangRepository extends CrudRepository<QuanLyKhachHang, Long>{

	List<QuanLyKhachHang> findByStatus(Integer trangThaiKhachHangHoatDong);

	/**
	 * @param layMaKhachHang
	 * @return
	 */
	QuanLyKhachHang findByCode(String layMaKhachHang);

}
