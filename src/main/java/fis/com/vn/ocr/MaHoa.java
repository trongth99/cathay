package fis.com.vn.ocr;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MaHoa {
	@SerializedName(value = "noiDung", alternate = "raw")
	String noiDung;
	
	@SerializedName(value = "soCmt", alternate = "id")
	String soCmt;
	
	@SerializedName(value = "hoVaTen", alternate = "name")
	String hoVaTen;
	
	@SerializedName(value = "namSinh", alternate = "dob")
	String namSinh;
	
	@SerializedName(value = "ngayHetHan", alternate = "doe")
	String ngayHetHan;
	
	@SerializedName(value = "gioiTinh", alternate = "sex")
	String gioiTinh;
	
}
