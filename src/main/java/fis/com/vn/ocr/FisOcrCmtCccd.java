package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FisOcrCmtCccd {
	//Số cmt
	@SerializedName(value = "soCmt", alternate = "id")
	String soCmt;
	@SerializedName(value = "soCmtScore", alternate = "id_prob")
	String soCmtScore;
	
	//Tên
	@SerializedName(value = "hoVaTen", alternate = "name")
	String hoVaTen;
	@SerializedName(value = "hoVaTenScore", alternate = "name_prob")
	String hoVaTenScore;
	
	//Ngày sinh
	@SerializedName(value = "namSinh", alternate = "dob")
	String namSinh;
	@SerializedName(value = "namSinhScore", alternate = "dob_prob")
	String namSinhScore;
	
	@SerializedName(value = "ngayHetHan", alternate = "doe")
	String ngayHetHan;
	@SerializedName(value = "ngayHetHanScore", alternate = "doe_prob")
	String ngayHetHanScore;
	
	//Nguyên quán
	@SerializedName(value = "queQuan", alternate = "home")
	String queQuan;
	@SerializedName(value = "queQuanScore", alternate = "home_prob")
	String queQuanScore;
	
	//Địa chỉ
	@SerializedName(value = "noiTru", alternate = "address")
	String noiTru;
	@SerializedName(value = "noiTruScore", alternate = "address_prob")
	String noiTruScore;
	
	//Dân tộc
	@SerializedName(value = "danToc", alternate = "ethnicity")
	String danToc;
	@SerializedName(value = "danTocScore", alternate = "ethnicity_prob")
	String danTocScore;
	
	//Tôn giáo
	@SerializedName(value = "tonGiao", alternate = "religion")
	String tonGiao;
	@SerializedName(value = "tonGiaoScore", alternate = "religion_prob")
	String tonGiaoScore;
	
	//Dấu vết riêng và dị hình
	@SerializedName(value = "dacDiemNhanDang", alternate = "features")
	String dacDiemNhanDang;
	
	//Ngày cấp
	@SerializedName(value = "ngayCap", alternate = "issue_date")
	String ngayCap;
	@SerializedName(value = "ngayCapScore", alternate = "issue_date_prob")
	String ngayCapScore;
	
	//Nơi cấp
	@SerializedName(value = "noiCap", alternate = "issue_loc")
	String noiCap;
	@SerializedName(value = "noiCapScore", alternate = "issue_loc_prob")
	String noiCapScore;
	
	//Loại cmt
	@SerializedName(value = "loaiCmt", alternate = "type")
	String loaiCmt;
	
	@SerializedName(value = "loaiCmtKhac", alternate = "type_new")
	String loaiCmtKhac;
	
	//anh chân dung
	@SerializedName(value = "anhChanDung", alternate = "face")
	String anhChanDung;
	
	@SerializedName(value = "gioiTinh", alternate = "sex")
	String gioiTinh;
	@SerializedName(value = "gioiTinhScore", alternate = "sex_prob")
	String gioiTinhScore;
	
	@SerializedName(value = "quocTich", alternate = "nationality")
	String quocTich;
	@SerializedName(value = "quocTichScore", alternate = "nationality_prob")
	String quocTichScore;
	
	@SerializedName(value = "score", alternate = "overall_score")
	String score;
	
	@SerializedName(value = "chiTietNoiTru", alternate = "address_entities")
	AddressEntities chiTietNoiTru;
	
	@SerializedName(value = "checkingResult", alternate = "checking_result")
	CheckingResult checkingResult;
}
