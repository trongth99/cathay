package fis.com.vn.callapi.entities;

import java.util.ArrayList;

import lombok.Data;

@Data
public class DanhSachTaiKhoan {
	String taiKhoanMoi;
	ArrayList<TaiKhoan> taiKhoans;
	
	public void add(TaiKhoan taiKhoan) {
		if(taiKhoans == null) taiKhoans = new ArrayList<>();
		taiKhoans.add(taiKhoan);
	}
}
