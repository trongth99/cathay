package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OcrBlx {
	//Số GPLX
	@SerializedName(value = "soGiayPhepLaiXe", alternate = "id")
	String soGiayPhepLaiXe;
	
	@SerializedName(value = "soGiayPhepLaiXeDcx", alternate = "id_prob")
	String soGiayPhepLaiXeDcx;
	
	//Tên
	@SerializedName(value = "hoVaTen", alternate = "name")
	String hoVaTen;
	
	@SerializedName(value = "hoVaTenDcx", alternate = "name_prob")
	String hoVaTenDcx;
	
	//Ngày sinh
	@SerializedName(value = "namSinh", alternate = "dob")
	String namSinh;
	
	@SerializedName(value = "namSinhDcx", alternate = "dob_prob")
	String namSinhDcx;
	
	//Quốc tịch
	@SerializedName(value = "quocTich", alternate = {"nation", "national"})
	String quocTich;
	
	@SerializedName(value = "quocTichDcx", alternate = {"nation_prob", "national_prob"})
	String quocTichDcx;
	
	//Nơi cư trú
	@SerializedName(value = "noiTru", alternate = "address")
	String noiTru;
	
	@SerializedName(value = "noiTruDcx", alternate = "address_prob")
	String noiTruDcx;
	
	//Nơi cấp
	@SerializedName(value = "noiCap", alternate = "place_issue")
	String noiCap;
	
	@SerializedName(value = "noiCapDcx", alternate = "place_issue_prob")
	String noiCapDcx;
	
	//Ngày cấp
	@SerializedName(value = "ngayCap", alternate = {"date", "reg_date"})
	String ngayCap;
	
	@SerializedName(value = "ngayCapDcx", alternate = {"date_prob", "reg_date_prob"})
	String ngayCapDcx;
	
	//Ngày hết hạn
	@SerializedName(value = "ngayHetHan", alternate = {"doe", "expr_date"})
	String ngayHetHan;
	
	@SerializedName(value = "ngayHetHanDcx", alternate = {"doe_prob", "expr_date_prob"})
	String ngayHetHanDcx;
	
	//Mã số
	@SerializedName(value = "maSo", alternate = "code")
	String maSo;
	
	@SerializedName(value = "maSoDcx", alternate = "code_prob")
	String maSoDcx;
	
	//hang
	@SerializedName(value = "hangGiayPhepLaiXe", alternate = "class")
	String hangGiayPhepLaiXe;
	
	@SerializedName(value = "hangGiayPhepLaiXeDcx", alternate = "class_prob")
	String hangGiayPhepLaiXeDcx;
	
	//loại
	@SerializedName(value = "loaiGiayPhepLaiXe", alternate = "type")
	String loaiGiayPhepLaiXe;
	
	@SerializedName(value = "dcx", alternate = "overall_score")
	String dcx;
	
	@SerializedName(value = "chiTietNoiTru", alternate = "address_entities")
	AddressEntities chiTietNoiTru;
}
