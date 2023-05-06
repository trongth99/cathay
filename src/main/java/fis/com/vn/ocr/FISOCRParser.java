package fis.com.vn.ocr;

import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import fis.com.vn.api.KhachHang;
import fis.com.vn.common.Common;
import fis.com.vn.common.StringUtils;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.Language;
import fis.com.vn.contains.ContainsVision;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;

@Component
public class FISOCRParser {
	@Autowired ConfigProperties configProperties;
	@Autowired Language language;
	@Autowired FisOcrThread fisOcrThread;
	@Autowired KhachHang khachHang;
	@Autowired CallApi callApi;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FISOCRParser.class);
	public Ocr parsing (ParamOcr paramOcr) throws CheckException {
		Ocr ocr = null;
		if(StringUtils.isEmpty(paramOcr.getLoaiGiayTo())) paramOcr.setLoaiGiayTo(ContainsVision.CHUNG_MINH_THU);
		if(paramOcr.getLoaiGiayTo().equals(ContainsVision.CHUNG_MINH_THU) || paramOcr.getLoaiGiayTo().equals(ContainsVision.CAN_CUOC_CONG_DAN) || paramOcr.getLoaiGiayTo().equals(ContainsVision.CAN_CUOC_CONG_DAN_CHIP)) {
			ocr = callCmtCccd(paramOcr);
		}
		return handlingOcr(ocr);
	}

	public Ocr handlingOcr(Ocr ocr) {
		if(ocr != null) {
			if(!StringUtils.isEmpty(ocr.getNamSinh())) {
				ocr.setNamSinh(Utils.formatStringDate(ocr.getNamSinh()));
			}
			if(!StringUtils.isEmpty(ocr.getNgayCap())) {
				ocr.setNgayCap(Utils.formatStringDate(ocr.getNgayCap()));
			}
			
			if(!StringUtils.isEmpty(ocr.getGioiTinh()) && ocr.getGioiTinh().equals("N/A")) {
				ocr.setGioiTinh("");
			}
			if(!StringUtils.isEmpty(ocr.getDanToc()) && ocr.getDanToc().equals("N/A")) {
				ocr.setDanToc("");
			}
			if(!StringUtils.isEmpty(ocr.getTonGiao()) && ocr.getTonGiao().equals("N/A")) {
				ocr.setTonGiao("");
			}
			if(!StringUtils.isEmpty(ocr.getNgayHetHan()) && ocr.getNgayHetHan().equals("N/A")) {
				ocr.setNgayHetHan("");
			}
			if(!StringUtils.isEmpty(ocr.getQuocTich()) && ocr.getQuocTich().equals("N/A")) {
				ocr.setQuocTich("");
			}
			if(!StringUtils.isEmpty(ocr.getNgayHetHan())) {
				ocr.setNgayHetHan(Utils.formatStringDate(ocr.getNgayHetHan()));
			}
			if(!StringUtils.isEmpty(ocr.getGioiTinh())) {
				ocr.setGioiTinh(ocr.getGioiTinh().replaceAll("/[\\w\\W]+$", ""));
			}
			
		}
		
		LOGGER.info("Ocr: "+new Gson().toJson(ocr));
		
		return ocr;
	}
	
	private Ocr callCmtCccd(ParamOcr paramOcr) throws CheckException  {
		Ocr ocr = new Ocr();
		String loaiGiayToMt = "";
		CompletableFuture<String> responeApiMt = null;
		CompletableFuture<String> responeApiMs = null;
		if(!StringUtils.isEmpty(paramOcr.getBase64AnhMatTruoc())) {
			responeApiMt = fisOcrThread.request(paramOcr.getBase64AnhMatTruoc());
		}
		if(!StringUtils.isEmpty(paramOcr.getBase64AnhMatSau())) {
			responeApiMs = fisOcrThread.request(paramOcr.getBase64AnhMatSau());
		}
		if(!StringUtils.isEmpty(paramOcr.getBase64AnhMatTruoc())) {
			try {
				String jsonMatTruoc = responeApiMt.get();
				
				LOGGER.info("Mat Truoc ocr: {}", jsonMatTruoc.replaceAll("[\\n\\r]+", "")); 
				
				ResponseOcrCmtCccd responseOcrCmtCccd = new Gson().fromJson(jsonMatTruoc, ResponseOcrCmtCccd.class);
				
				if(responseOcrCmtCccd.getErrorCode() != 0) throw new CheckException(language.getMessage("Ảnh không đủ chất lượng"));
				
				OcrCmtCccd ocrCmtCccd = responseOcrCmtCccd.getData().get(0);
				
				if(StringUtils.isEmpty(ocrCmtCccd.getSoCmt())) throw new CheckException(language.getMessage("Không đọc được số cmt hoặc cccd"));
				if(StringUtils.isEmpty(ocrCmtCccd.getSoCmt().replaceAll("[^0-9]+", ""))) throw new CheckException(language.getMessage("Không đọc được số cmt hoặc cccd")); 
				
				ocrCmtCccd.setSoCmt(convertSoCmt(ocrCmtCccd.getSoCmt()));
				
				if(ocrCmtCccd.getSoCmt().length() != 9 && ocrCmtCccd.getSoCmt().length() != 12) throw new CheckException(language.getMessage("Ảnh không đủ chất lượng"));
				
				loaiGiayToMt = ocrCmtCccd.getLoaiCmt();
				Common.updateObjectToObject(ocr, ocrCmtCccd);
				
				kiemTraSoCccd(ocr);
				
				ocr.setLoaiCmtKhacMatTruoc(ocr.getLoaiCmt());
				ocr.setLoaiCmtMatTruoc(ocr.getLoaiCmt());
				ocr.setKiemTraMatTruoc(ocr.getKiemTra());
				ocr.setKiemTra(null);
				UtilsVision.convertKiemTraMatTruoc(ocr, paramOcr.getMaToChuc());
			} catch (CheckException e) {
				throw new CheckException(e.getMessage()); 
			} catch (Exception e) {
				LOGGER.error("CMT-MT: {}", e.getMessage());
				throw new CheckException(language.getMessage("Ảnh mặt trước cmt hoặc cccd không đúng")); 
			}
		}
		if(!StringUtils.isEmpty(paramOcr.getBase64AnhMatSau())) { 
			try {
				String jsonMatSau = responeApiMs.get();
				
				LOGGER.info("Mat Sau ocr: {}", jsonMatSau.replaceAll("[\\n\\r]+", ""));
				
				ResponseOcrCmtCccd responseOcrCmtCccd = new Gson().fromJson(jsonMatSau, ResponseOcrCmtCccd.class);
				
				if(responseOcrCmtCccd.getErrorCode() != 0) throw new CheckException(language.getMessage("Ảnh không đủ chất lượng"));
				
				OcrCmtCccd ocrCmtCccd = responseOcrCmtCccd.getData().get(0);
				
				Common.updateObjectToObject(ocr, ocrCmtCccd);
				if(StringUtils.isEmpty(ocr.getNgayCap())) throw new CheckException(language.getMessage("Không đọc được ngày cấp cmt hoặc cccd"));
				
				validCmtAndCccdMs(ocr, paramOcr.getLoaiGiayTo(), loaiGiayToMt);
				ocr.setKiemTraMatSau(ocr.getKiemTra());
				ocr.setKiemTra(null);
				UtilsVision.convertKiemTraMatSau(ocr);
			} catch (CheckException e) {
				throw new CheckException(e.getMessage());
			} catch (Exception e) {
				LOGGER.error("CMT-MS: {}", e.getMessage());
				throw new CheckException(language.getMessage("Ảnh mặt sau cmt hoặc cccd không đúng"));
			}
		}
		
		Utils.updateHanSuDungGiayTo(ocr);
		
//		validThongTinMtVaMsCccdchip(ocr);
		
		try {
			if(ocr.getSoCmt().equals("381589033")) {
				ocr.setHoVaTen("CAO THỊ BÉ");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		validFake(ocr, paramOcr.getMaToChuc()); 
		
		return ocr;
	}
	
	private void kiemTraMaTinhVoiCmt9So(Ocr ocr) throws CheckException {
		try {
			if(ocr.getLoaiCmtMatTruoc().equals("cmt_9_mt")) {
				if(!StringUtils.isEmpty(ocr.getMaTinhDiaChi()) && !StringUtils.isEmpty(ocr.getMaTinhNoiCap())) {
					if(!ocr.getMaTinhDiaChi().equals(ocr.getMaTinhNoiCap())) throw new CheckException(language.getMessage("Giấy tờ mặt sau chứng minh thư không khớp với mặt trước"));
				}
			}
		} catch (CheckException e) {
			 throw new CheckException(e.getMessage());
		} catch (Exception e) {
		}
		
	}
	
	private void kiemTraDuThongTinOcr(Ocr ocr) throws CheckException {
		if(isEmpty(ocr.getNamSinh())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(isEmpty(ocr.getSoCmt())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(isEmpty(ocr.getNgayCap())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(isEmpty(ocr.getQueQuan())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(isEmpty(ocr.getNoiTru())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(isEmpty(ocr.getHoVaTen())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(isEmpty(ocr.getNoiCap())) throw new CheckException(language.getMessage("Thông tin không đủ"));
		if(!ocr.getLoaiCmtKhacMatTruoc().equals("cmt_9_mt") && isEmpty(ocr.getGioiTinh())) throw new CheckException(language.getMessage("Thông tin không đủ"));
	}
	private boolean isEmpty(String str) {
		try {
			if(StringUtils.isEmpty(str)) return true;
			if(str.equals("N/A")) return true;
		} catch (Exception e) {
		}
		return false;
	}
	private void validThongTinMtVaMsCccdchip(Ocr ocr) throws CheckException {
		try {
			if(ocr.getLoaiCmtMatTruoc().equals("cccd_chip_mt")) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyMMdd");
				
				if(isEmpty(ocr.getHoVaTen())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getSoCmt())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getGioiTinh())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getNamSinh())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getMaHoa().getHoVaTen())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getMaHoa().getSoCmt())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getMaHoa().getGioiTinh())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getMaHoa().getNamSinh())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getNgayHetHan())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(isEmpty(ocr.getMaHoa().getNgayHetHan())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				
				if(!Utils.alias(ocr.getHoVaTen().toLowerCase()).equals(ocr.getMaHoa().getHoVaTen().toLowerCase())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(!ocr.getSoCmt().equals(ocr.getMaHoa().getSoCmt())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(convertGioiTinh (ocr.getGioiTinh()).toLowerCase().equals("female") && !ocr.getMaHoa().getGioiTinh().toLowerCase().equals("f")) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(convertGioiTinh(ocr.getGioiTinh()).toLowerCase().equals("male") && !ocr.getMaHoa().getGioiTinh().toLowerCase().equals("m")) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				if(!StringUtils.isEmpty(ocr.getNamSinh())) {
					String namSinh = dateFormat2.format(dateFormat.parse(ocr.getNamSinh()));
					if(!namSinh.equals(ocr.getMaHoa().getNamSinh())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				}
				if(!StringUtils.isEmpty(ocr.getNgayHetHan())) {
					String ngayHetHan = dateFormat2.format(dateFormat.parse(ocr.getNgayHetHan()));
					if(!ngayHetHan.equals(ocr.getMaHoa().getNgayHetHan())) throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
				}
			}
		} catch (CheckException e) {
			throw new CheckException(e.getMessage());
		} catch (Exception e) {
		}
	}
	private String convertGioiTinh(String gioiTinh) {
    	try {
			if(gioiTinh.toLowerCase().equals("nam")) return "Male";
			if(gioiTinh.toLowerCase().equals("nữ")) return "Female";
		} catch (Exception e) {
		}
    	return "";
    }
    private void updateScore(String maToChuc, Ocr ocr) {
		if(maToChuc.equals("vcsc")) {
			ocr.setSoCmtScore(convert(ocr.getSoCmtScore()));
			ocr.setHoVaTenScore(convert(ocr.getHoVaTenScore()));
			ocr.setNamSinhScore(convert(ocr.getNamSinhScore()));
			ocr.setNgayHetHanScore(convert(ocr.getNgayHetHanScore()));
			ocr.setQueQuanScore(convert(ocr.getQueQuanScore()));
			ocr.setNoiTruScore(convert(ocr.getNoiTruScore()));
			ocr.setDanTocScore(convert(ocr.getDanTocScore()));
			ocr.setTonGiaoScore(convert(ocr.getTonGiaoScore()));
			ocr.setNgayCapScore(convert(ocr.getNgayCapScore()));
			ocr.setNoiCapScore(convert(ocr.getNoiCapScore()));
			ocr.setGioiTinhScore(convert(ocr.getGioiTinhScore()));
			ocr.setQuocTichScore(convert(ocr.getQuocTichScore()));
			ocr.setScore(tinhScoreTong(ocr));
		}
		
	}
    private String tinhScoreTong(Ocr ocr) {
    	Double score = 0.0;
    	int count = 0;
    	
    	if(convertDouble(ocr.getSoCmtScore()) != -1.0) {
    		score += convertDouble(ocr.getSoCmtScore());
    		count++;
    	}
    	if(convertDouble(ocr.getHoVaTenScore()) != -1.0) {
	    	score += convertDouble(ocr.getHoVaTenScore());
	    	count++;
    	}
    	if(convertDouble(ocr.getNamSinhScore()) != -1.0) {
    		score += convertDouble(ocr.getNamSinhScore());
    		count++;
    	}	
    	if(convertDouble(ocr.getNgayHetHanScore()) != -1.0) {
    		score += convertDouble(ocr.getNgayHetHanScore());
    		count++;
    	}
    	if(convertDouble(ocr.getQueQuanScore()) != -1.0) {
    		score += convertDouble(ocr.getQueQuanScore());
    		count++;
    	}
    	if(convertDouble(ocr.getNoiTruScore()) != -1.0) {
    		score += convertDouble(ocr.getNoiTruScore());
    		count++;
    	}
    	if(convertDouble(ocr.getDanTocScore()) != -1.0) {
    		score += convertDouble(ocr.getDanTocScore());
    		count++;
    	}
    	if(convertDouble(ocr.getTonGiaoScore()) != -1.0) {
    		score += convertDouble(ocr.getTonGiaoScore());
    		count++;
    	}
    	if(convertDouble(ocr.getNgayCapScore()) != -1.0) {
    		score += convertDouble(ocr.getNgayCapScore());
    		count++;
    	}
    	if(convertDouble(ocr.getNoiCapScore()) != -1.0) {
    		score += convertDouble(ocr.getNoiCapScore());
    		count++;
    	}
    	if(convertDouble(ocr.getGioiTinhScore()) != -1.0) {
    		score += convertDouble(ocr.getGioiTinhScore());
    		count++;
    	}
    	if(convertDouble(ocr.getQuocTichScore()) != -1.0) {
    		score += convertDouble(ocr.getQuocTichScore());
    		count++;
    	}
		return String.valueOf(score/count);
	}
    private Double convertDouble(String douString) {
    	try {
			return Double.valueOf(douString);
		} catch (Exception e) {
		}
    	return -1.0;
    }
	private String convert(String douString) {
    	try {
			return String.valueOf(Double.valueOf(douString)*100);
		} catch (Exception e) {
		}
    	return "N/A";
    }
	private String convertSoCmt(String soCmt) {
		try {
			if(soCmt.length() == 13) {
				soCmt = soCmt.substring(0,12);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return soCmt;
	}

	private boolean validFake(Ocr ocrCmtCccd, String maToChuc) throws CheckException {
		if(ocrCmtCccd.getKiemTraMatTruoc() == null) return false;
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getQuyLuatSo(), kiemTra(",qls,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Số cmt hoặc cccd không đúng định dạng"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getDenTrang(), kiemTra(",dt,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Bạn vui lòng chụp bản gốc cmt hoặc cccd"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getChupLaiTuManHinh(), kiemTra(",mh,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Bạn vui lòng chụp bản gốc cmt hoặc cccd"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getCatGoc(), kiemTra(",cg,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Chứng minh thư hoặc căn cước công dân bị cắt góc"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getKiemTraAnh(), kiemTra(",anh,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Ảnh trên chứng minh thư hoặc căn cước công dân bị sửa đổi"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getNgayHetHan(), kiemTra(",nhh,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Chứng minh thư hoặc căn cước công dân hết hạn sử dụng"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getDauNoi(), kiemTra(",dn,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Dấu nổi trên ảnh bị sửa đổi"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getThayTheAnh(), kiemTra(",tta,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Ảnh trên giấy tờ bị thay thế"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getSuaDoi(), kiemTra(",sd,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Chứng minh thư hoặc căn cước bị sửa đổi"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getAnhBiLoa(), kiemTra(",loa,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Ảnh bị lóa"));
		if(isFake(ocrCmtCccd.getKiemTraMatTruoc().getKhungHinh(), kiemTra(",kh,", configProperties.getConfig().getKiem_tra()))) 
			throw new CheckException(language.getMessage("Ảnh chụp phải nằm trong khung hình"));
		
		if(kiemTra(",9so,", configProperties.getConfig().getKiem_tra())) {
			kiemTraMaTinhVoiCmt9So(ocrCmtCccd);
		}
		if(kiemTra(",dtt,", configProperties.getConfig().getKiem_tra())) {
			kiemTraDuThongTinOcr(ocrCmtCccd);
		}
		
		if(ocrCmtCccd.getKiemTraMatSau() == null) return false;
		if(isFake(ocrCmtCccd.getKiemTraMatSau().getDauDo(), khachHang.getKiemTraDauXacNhanMs(maToChuc))) 
			throw new CheckException(language.getMessage("Không thấy dấu xác nhận"));
		if(isFake(ocrCmtCccd.getKiemTraMatSau().getAnhBiLoa(), khachHang.getKiemTraAnhBiLoa(maToChuc))) 
			throw new CheckException(language.getMessage("Ảnh bị lóa"));
		if(isFake(ocrCmtCccd.getKiemTraMatSau().getVanTayPhai(), khachHang.getKiemTraVanTay(maToChuc))) 
			throw new CheckException(language.getMessage("Thiếu vân tay"));
		if(isFake(ocrCmtCccd.getKiemTraMatSau().getVanTayTrai(), khachHang.getKiemTraVanTay(maToChuc))) 
			throw new CheckException(language.getMessage("Thiếu vân tay"));
		
		return true;
	}
	private boolean kiemTra(String string, String kiem_tra) {
		try {
			if(kiem_tra.indexOf(string) != -1) return true;
		} catch (Exception e) {
		}
		return false;
	}

	private boolean isFake(String str, boolean kiemTra) {
		try {
			if(str != null && str.equals("1") && kiemTra) return true;
			if(str != null && str.equals("failed") && kiemTra) return true;
		} catch (Exception e) {
		}
		
		return false;
	}
	
	private void kiemTraSoCccd(Ocr ocr) throws CheckException {
		System.out.println(new Gson().toJson(ocr));
		if(ocr.getLoaiCmt().equals("cccd/cmt_12_mt") || ocr.getLoaiCmt().equals("cccd_chip_mt")) {
			String quyLuatSo = "0";
			try {
				String nameSinhFromSoCmt = ocr.getSoCmt().substring(4, 6);
				String namSinh = "";
				if(ocr.getNamSinh().indexOf("/") != -1) {
					namSinh = ocr.getNamSinh().split("/")[2].substring(2,4);
				} else {
					namSinh = ocr.getNamSinh().substring(2,4);
				}
				if(!nameSinhFromSoCmt.equals(namSinh)) throw new CheckException(language.getMessage("Mã năm sinh không khớp"));
				
				String maGioiTinh = ocr.getSoCmt().substring(3, 4);
				String ocrGioiTinh = Utils.convertGioiTinh(ocr.getGioiTinh());
				if(maGioiTinh.equals("0") || maGioiTinh.equals("2") || maGioiTinh.equals("4") || maGioiTinh.equals("6") || maGioiTinh.equals("8")) {
					if(ocrGioiTinh.equals("0")) throw new CheckException(language.getMessage("Mã giới tính không khớp"));
				}
				if(maGioiTinh.equals("1") || maGioiTinh.equals("3") || maGioiTinh.equals("5") || maGioiTinh.equals("7") || maGioiTinh.equals("9")) {
					if(ocrGioiTinh.equals("1")) throw new CheckException(language.getMessage("Mã giới tính không khớp"));
				}
			} catch (CheckException e) {
				quyLuatSo = "1";
				throw new CheckException(e.getMessage());
			} catch (Exception e) {
				
			}
			CheckingResult checkingResult = ocr.getKiemTra();
			if(checkingResult != null) {
				checkingResult.setDauNoi("0");
				checkingResult.setQuyLuatSo(quyLuatSo);
			}
			ocr.setKiemTra(checkingResult);
		}
	}
	
	/**
	 * kiem tra mat sau giay to có phu hop voi mat truoc va loai giay to
	 * @param ocrCmtCccd
	 * @param loaiGiayTo
	 * @param loaiGiayToMt 
	 * @throws ValidException 
	 */
	private void validCmtAndCccdMs(Ocr ocrCmtCccd, String loaiGiayTo, String loaiGiayToMt) throws CheckException {
		if(ocrCmtCccd.getLoaiCmt().equals("cmt_9_ms") && !loaiGiayToMt.equals("cmt_9_mt")) {
			throw new CheckException(language.getMessage("Giấy tờ mặt sau chứng minh thư không khớp với mặt trước"));
		}
		if(ocrCmtCccd.getLoaiCmt().equals("cccd/cmt_12_ms") && !loaiGiayToMt.equals("cccd/cmt_12_mt")) {
			throw new CheckException(language.getMessage("Giấy tờ mặt sau chứng minh thư/căn cước công dân không khớp với mặt trước"));
		}
		if(ocrCmtCccd.getLoaiCmt().equals("cccd_chip_ms") && !loaiGiayToMt.equals("cccd_chip_mt")) {
			throw new CheckException(language.getMessage("Giấy tờ mặt sau căn cước công dân chip không khớp với mặt trước"));
		}
	}
}
