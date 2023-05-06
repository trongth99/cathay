package fis.com.vn.ocr;

import org.springframework.util.StringUtils;

public class UtilsVision {
	public static void convertKiemTraMatTruoc(Ocr ocr, String maToChuc) {
		try {
			if(ocr.getKiemTraMatTruoc() != null) {
				ocr.getKiemTraMatTruoc().setDenTrang(convertValue(ocr.getKiemTraMatTruoc().getDenTrang()));
				if(!ocr.getLoaiCmtKhacMatTruoc().equals("cmt_9_mt") && !ocr.getLoaiCmtKhacMatTruoc().equals("cmnd_09_front") && !maToChuc.equals("test")) {
					ocr.getKiemTraMatTruoc().setCatGoc("0");
					ocr.getKiemTraMatTruoc().setDauNoi(null);
					ocr.getKiemTraMatTruoc().setDauNoiScore(null);
				}
				ocr.getKiemTraMatTruoc().setDauNoi(convertValue(ocr.getKiemTraMatTruoc().getDauNoi()));
				ocr.getKiemTraMatTruoc().setKiemTraAnh(convertValue(ocr.getKiemTraMatTruoc().getKiemTraAnh()));
				ocr.getKiemTraMatTruoc().setDauQuocHuy(convertValue(ocr.getKiemTraMatTruoc().getDauQuocHuy()));
				ocr.getKiemTraMatTruoc().setNgayHetHan(null);
				ocr.getKiemTraMatTruoc().setVanTayPhai(null);
				ocr.getKiemTraMatTruoc().setVanTayPhaiScore(null);
				ocr.getKiemTraMatTruoc().setVanTayTrai(null);
				ocr.getKiemTraMatTruoc().setVanTayTraiScore(null);
				ocr.getKiemTraMatTruoc().setDauDo(null);
				ocr.getKiemTraMatTruoc().setDauDoScore(null);
				
				if(ocr.getQuocTich() != null && !ocr.getQuocTich().equals("VIỆT NAM")) {
					ocr.setDanToc(ocr.getQuocTich());
					ocr.setDanTocScore(ocr.getQuocTichScore());
					ocr.setQuocTich(null);
					ocr.setQuocTichScore(null);
				}
			}
		} catch (Exception e) {
		}
	}
	public static void convertKiemTraMatSau(Ocr ocr) {
		try {
			if(ocr.getKiemTraMatSau() != null) {
				ocr.getKiemTraMatSau().setDenTrang(null);
				ocr.getKiemTraMatSau().setCatGoc(null);
				ocr.getKiemTraMatSau().setDauNoi(null);
				ocr.getKiemTraMatSau().setKiemTraAnh(null);
				ocr.getKiemTraMatSau().setDenTrangScore(null);
				ocr.getKiemTraMatSau().setCatGocScore(null);
				ocr.getKiemTraMatSau().setDauNoiScore(null);
				ocr.getKiemTraMatSau().setKiemTraAnhScore(null);
				ocr.getKiemTraMatSau().setDauQuocHuy(null);
				ocr.getKiemTraMatSau().setDauQuocHuyScore(null);
				ocr.getKiemTraMatSau().setNgayHetHan(null);
			}
		} catch (Exception e) {
		}
	}
	private static String convertValue(String str) {
		if((!StringUtils.isEmpty(str) && str.equals("failed")) || (!StringUtils.isEmpty(str) && str.equals("1"))) {
			return "1";
		} 
		if((!StringUtils.isEmpty(str) && str.equals("passed")) || (!StringUtils.isEmpty(str) && str.equals("0"))) {
			return "0";
		}
		return "N/A";
	}
}
