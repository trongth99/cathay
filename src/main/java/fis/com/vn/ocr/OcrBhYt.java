/**
 * 
 */
package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Data
public class OcrBhYt {
	@SerializedName(value = "maSo", alternate = "code")
	String maSo;
	
	@SerializedName(value = "maSoDcx", alternate = "code_prob")
	String maSoDcx;
	
	@SerializedName(value = "hoVaTen", alternate = "full_name")
	String hoVaTen;
	
	@SerializedName(value = "hoVaTenDcx", alternate = "full_name_prob")
	String hoVaTenDcx;
	
	@SerializedName(value = "namSinh", alternate = "dob")
	String namSinh;
	
	@SerializedName(value = "namSinhDcx", alternate = "dob_prob")
	String namSinhDcx;
	
	@SerializedName(value = "gioiTinh", alternate = "gender")
	String gioiTinh;
	
	@SerializedName(value = "gioiTinhDcx", alternate = "gender_prob")
	String gioiTinhDcx;
	
	@SerializedName(value = "diaChi", alternate = "address")
	String diaChi;
	
	@SerializedName(value = "diaChiDcx", alternate = "address_prob")
	String diaChiDcx;
	
	@SerializedName(value = "maBenhVien", alternate = "hospital_reg_code")
	String maBenhVien;
	
	@SerializedName(value = "maBenhVienDcx", alternate = "hospital_reg_code_prob")
	String maBenhVienDcx;
	
	@SerializedName(value = "noiDkKcb", alternate = "hospital_reg")
	String noiDkKcb;
	
	@SerializedName(value = "noiDkKcbDcx", alternate = "hospital_reg_prob")
	String noiDkKcbDcx;
	
	@SerializedName(value = "ngaySuDung", alternate = "usr_start")
	String ngaySuDung;
	
	@SerializedName(value = "ngaySuDungDcx", alternate = "usr_start_prob")
	String ngaySuDungDcx;
	
	@SerializedName(value = "ngayHetHan", alternate = "exp_date")
	String ngayHetHan;
	
	@SerializedName(value = "ngayHetHanDcx", alternate = "exp_date_prob")
	String ngayHetHanDcx;
	
	@SerializedName(value = "ngayDangKy", alternate = "reg_date")
	String ngayDangKy;
	
	@SerializedName(value = "ngayDangKyDcx", alternate = "reg_date_prob")
	String ngayDangKyDcx;
	
	String dcx;
}
