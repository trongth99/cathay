package fis.com.vn.entities;

import lombok.Data;

@Data
public class ThongTin {
	Integer trang;
	ToaDo chuTaiKhoan;
	ToaDo soTaiKhoan;
	ToaDo nganHang;
	ToaDo chiNhanh;
	ToaDo soTien;
}
