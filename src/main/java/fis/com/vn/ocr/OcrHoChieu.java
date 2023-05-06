package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OcrHoChieu {
	//Số Hộ Chiếu
	@SerializedName(value = "soHoChieu", alternate = {"passport_number", "id"})
	String soHoChieu;
	
	//Tên
	@SerializedName(value = "hoVaTen", alternate = "name")
	String hoVaTen;
	
	//Ngày sinh
	@SerializedName(value = "namSinh", alternate = "dob")
	String namSinh;
	
	//Nơi sinh
	@SerializedName(value = "queQuan", alternate = "pob")
	String queQuan;
	
	//Giới tính
	@SerializedName(value = "gioiTinh", alternate = {"sex","gender"})
	String gioiTinh;
	
	//Sô ID
	@SerializedName(value = "soCmt", alternate = {"id_number", "idcard"})
	String soCmt;
	
	//Ngày cấp
	@SerializedName(value = "ngayCap", alternate = "doi")
	String ngayCap;
	
	//Ngày hết hạn
	@SerializedName(value = "ngayHetHan", alternate = "doe")
	String ngayHetHan;
	
	@SerializedName(value = "quocTich", alternate = {"nationality", "national"})
	String quocTich;
	
	@SerializedName(value = "diaChi", alternate = "address")
	String diaChi;
	
	@SerializedName(value = "noiCap", alternate = "iloc")
	String noiCap;
	
	@SerializedName(value = "checkingResult", alternate = "checking_result")
	CheckingResult checkingResult;
}
