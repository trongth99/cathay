package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OcrCmtQd {
	//Số cmt
	@SerializedName(value = "soCmt", alternate = "id_number")
	String soCmt;
	@SerializedName(value = "soCmtScore", alternate = "id_number_prob")
	String soCmtScore;
	
	//Tên
	@SerializedName(value = "hoVaTen", alternate = "name")
	String hoVaTen;
	@SerializedName(value = "hoVaTenScore", alternate = "name_prob")
	String hoVaTenScore;
	
	//Ngày sinh
	@SerializedName(value = "namSinh", alternate = "birth_date")
	String namSinh;
	@SerializedName(value = "namSinhScore", alternate = "birth_date_prob")
	String namSinhScore;
	
	@SerializedName(value = "ngayHetHan", alternate = "expiry_date")
	String ngayHetHan;
	@SerializedName(value = "ngayHetHanScore", alternate = "expiry_date_prob")
	String ngayHetHanScore;
	
	//Nguyên quán
	@SerializedName(value = "queQuan", alternate = "address")
	String queQuan;
	@SerializedName(value = "queQuanScore", alternate = "address_prob")
	String queQuanScore;
	
	//Địa chỉ
	@SerializedName(value = "noiTru", alternate = "residence")
	String noiTru;
	@SerializedName(value = "noiTruScore", alternate = "residence_prob")
	String noiTruScore;
	
	//Dấu vết riêng và dị hình
	@SerializedName(value = "dacDiemNhanDang", alternate = "blood_type")
	String dacDiemNhanDang;
	
	//Ngày cấp
	@SerializedName(value = "ngayCap", alternate = "issue_date")
	String ngayCap;
	@SerializedName(value = "ngayCapScore", alternate = "issue_date_prob")
	String ngayCapScore;
	
	//Nơi cấp
	@SerializedName(value = "noiCap", alternate = "location")
	String noiCap;
	@SerializedName(value = "noiCapScore", alternate = "location_prob")
	String noiCapScore;
	
	//Loại cmt
	@SerializedName(value = "loaiCmt", alternate = "type")
	String loaiCmt;
	
	@SerializedName(value = "gioiTinh", alternate = "gender")
	String gioiTinh;
	@SerializedName(value = "gioiTinhScore", alternate = "gender_prob")
	String gioiTinhScore;
	
	@SerializedName(value = "score", alternate = "overall_score")
	String score;
	
	@SerializedName(value = "chucVu", alternate = "position")
	String chucVu;
}
