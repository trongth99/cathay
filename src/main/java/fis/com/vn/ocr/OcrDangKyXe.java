package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OcrDangKyXe {
	@SerializedName(value = "soDangKy", alternate = "id")
	String soDangKy;
	
	@SerializedName(value = "hoVaTen", alternate = "owner_name")
	String hoVaTen;
	
	@SerializedName(value = "diaChi", alternate = "address")
	String diaChi;
	
	@SerializedName(value = "soMay", alternate = "engine_number")
	String soMay;
	
	@SerializedName(value = "soKhung", alternate = "chassis_number")
	String soKhung;
	
	@SerializedName(value = "nhanHieu", alternate = "brand")
	String nhanHieu;
	
	@SerializedName(value = "soLoai", alternate = "modole_code")
	String soLoai;
	
	@SerializedName(value = "loaiXe", alternate = "type")
	String loaiXe;
	
	@SerializedName(value = "mauSon", alternate = "color")
	String mauSon;
	
	@SerializedName(value = "dungTich", alternate = "capacity")
	String dungTich;
	
	@SerializedName(value = "soNguoi", alternate = "sit")
	String soNguoi;
	
//	@SerializedName(value = "nguonGoc", alternate = "nguonGoc")
//	String nguonGoc;
	
	@SerializedName(value = "bienSo", alternate = "number_plate")
	String bienSo;
	
	@SerializedName(value = "ngayCap", alternate = "doe")
	String ngayCap;
	
	@SerializedName(value = "dangKyLanDau", alternate = "dofr")
	String ngayDangKyLanDau;
	
	@SerializedName(value = "taiTrong", alternate = "weight_load")
	String taiTrong;
	
	@SerializedName(value = "soChungNhan", alternate = "number")
	String soChungNhan;
}
