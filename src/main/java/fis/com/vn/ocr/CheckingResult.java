package fis.com.vn.ocr;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class CheckingResult {
	@SerializedName(value = "chupLaiTuManHinh", alternate = "recaptured_result")
	String chupLaiTuManHinh;
	
	@SerializedName(value = "chupLaiTuManHinhScore", alternate = "recaptured_prob")
	String chupLaiTuManHinhScore;
	
	@SerializedName(value = "denTrang", alternate = "check_photocopied_result")
	String denTrang;
	
	@SerializedName(value = "denTrangScore", alternate = "check_photocopied_prob")
	String denTrangScore;
	
	@SerializedName(value = "catGoc", alternate = "corner_cut_result")
	String catGoc;
	
	@SerializedName(value = "catGocScore", alternate = "corner_cut_prob")
	ArrayList<String> catGocScore;
	
	@SerializedName(value = "suaDoi", alternate = "edited_result")
	String suaDoi;
	
	@SerializedName(value = "suaDoiDcx", alternate = "edited_prob")
	String suaDoiDcx;
	
	@SerializedName(value = "dauNoi", alternate = "check_embossed_stamp_result")
	String dauNoi;
	
	@SerializedName(value = "dauNoiScore", alternate = "check_embossed_stamp_prob")
	String dauNoiScore;
	
	@SerializedName(value = "dauQuocHuy", alternate = "check_national_emblem_result")
	String dauQuocHuy;
	
	@SerializedName(value = "dauQuocHuyScore", alternate = "check_national_emblem_prob")
	String dauQuocHuyScore;
	
	@SerializedName(value = "dauDo", alternate = "check_red_stamp_result")
	String dauDo;
	
	@SerializedName(value = "dauDoScore", alternate = "check_red_stamp_prob")
	String dauDoScore;
	
	@SerializedName(value = "anhBiLoa", alternate = "check_glare_result")
	String anhBiLoa;
	
	@SerializedName(value = "anhBiLoaScore", alternate = "check_glare_prob")
	String anhBiLoaScore;
	
	@SerializedName(value = "vanTayPhai", alternate = "check_rfp_result")
	String vanTayPhai;
	
	@SerializedName(value = "vanTayPhaiScore", alternate = "check_rfp_prob")
	String vanTayPhaiScore;
	
	@SerializedName(value = "vanTayTrai", alternate = "check_lfp_result")
	String vanTayTrai;
	
	@SerializedName(value = "vanTayTraiScore", alternate = "check_lfp_prob")
	String vanTayTraiScore;
	
	@SerializedName(value = "kiemTraAnh", alternate = "check_avatar_result")
	String kiemTraAnh;
	
	@SerializedName(value = "kiemTraAnhScore", alternate = "check_avatar_prob")
	String kiemTraAnhScore;
	
	@SerializedName(value = "thayTheAnh", alternate = "check_replacement_avatar_result")
	String thayTheAnh;
	
	@SerializedName(value = "thayTheAnhScore", alternate = "check_replacement_avatar_prob")
	String thayTheAnhScore;
	
	@SerializedName(value = "khungHinh", alternate = "on_frame_result")
	String khungHinh;
	
	@SerializedName(value = "khungHinhScore", alternate = "on_frame_prob")
	String khungHinhScore;
	
	String ngayHetHan;
	
	String quyLuatSo;
}
