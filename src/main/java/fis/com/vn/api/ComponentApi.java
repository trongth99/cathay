package fis.com.vn.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.api.entities.Params;
import fis.com.vn.api.face.FaceIdValid;
import fis.com.vn.api.face.ParamsImg;
import fis.com.vn.api.face.ThongTinAnh;
import fis.com.vn.common.StringUtils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.Language;
import fis.com.vn.entities.ReturnRest;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;
import fis.com.vn.ocr.OCRParser;
import fis.com.vn.ocr.Ocr;
import fis.com.vn.ocr.ParamOcr;
import fis.com.vn.repository.QuanLyKhachHangRepository;
import lombok.Data;

@Component
public class ComponentApi {
	@Autowired ConfigProperties configProperties;
	@Autowired FaceIdValid faceIdValid;
	@Autowired ThongTinAnh layThongTinTuAnh;
	@Autowired OCRParser parser;
	@Autowired KhachHang khachHang;
	@Autowired QuanLyKhachHangRepository quanLyKhachHangRepository;
	@Autowired Language language;
	@Autowired LogService logService;
	
	public String soSanhAnh(Params params, String maKhachHang) throws ValidException , Exception, CheckException{
		if(StringUtils.isEmpty(params.getAnhMatTruoc())) throw new ValidException(language.getMessage("Truyền lên ảnh mặt trước giấy tờ"));
		if(StringUtils.isEmpty(params.getAnhKhachHang())) throw new ValidException(language.getMessage("Truyền lên ảnh khách hàng"));
		if(!StringUtils.isEmpty(params.getDoTuongDong()) && params.getDoTuongDong().equals("true")) {
			ParamsImg paramsImg = new ParamsImg(
					params.getAnhMatTruoc(), 
					params.getAnhKhachHang(), 
					language.getMessage("Ảnh không khớp"), 
					configProperties.getConfig().getDoubleNguong_chinh_xac(), 
					khachHang.getKiemTraKhuonMat(maKhachHang),
					maKhachHang
					); 
			Double doChinhXac = faceIdValid.validImageScore(paramsImg);
			
			return ReturnRest.success(doChinhXac);
		} else {
			ParamsImg paramsImg = new ParamsImg(
					params.getAnhMatTruoc(), 
					params.getAnhKhachHang(), 
					language.getMessage("Ảnh không khớp"), 
					configProperties.getConfig().getDoubleNguong_chinh_xac(), 
					khachHang.getKiemTraKhuonMat(maKhachHang),
					maKhachHang
					);
			Double doChinhXac = faceIdValid.validImageScore(paramsImg);
			
			if(doChinhXac < configProperties.getConfig().getDoubleNguong_chinh_xac()) throw new CheckException(language.getMessage("Ảnh không khớp"));
			
			return ReturnRest.success(doChinhXac); 
		}
	} 
	
	@Data
	class ScoreCompare{
		Double score;
	}

	public String docNoiDungOCr(Params params, String code) throws ValidException , Exception, CheckException {
		boolean kiemTraLoaiGiayToMatTruocVaMatSau = false;
		boolean kiemTraThongTinDb = false;
		return docNoiDungOCr(params, kiemTraThongTinDb, true, code, kiemTraLoaiGiayToMatTruocVaMatSau);
	}
	public String docNoiDungOCr(Params params, String code, boolean kiemTraLoaiGiayToMatTruocVaMatSau) throws ValidException , Exception, CheckException {
		return docNoiDungOCr(params, false, true, code, kiemTraLoaiGiayToMatTruocVaMatSau);
	}
	public String docNoiDungOCr(Params params, boolean kiemTraThongTinDb, boolean kiemTraThongTinAnh, String maKhachHang, boolean kiemTraLoaiGiayToMatTruocVaMatSau) throws ValidException , Exception, CheckException {
		Ocr ocr = null;
		
		ocr = parser.parsing(new ParamOcr(params.getAnhMatTruoc(), params.getAnhMatSau(), params.getMaGiayTo(), kiemTraThongTinAnh, kiemTraLoaiGiayToMatTruocVaMatSau, "", maKhachHang));
    	
    	return ReturnRest.success(ocr, null);
	}
	public String xacThucYeuCauHanhDong(Params params, String maKhachHang) throws CheckException, ValidException {
		boolean checkFakeFalse = layThongTinTuAnh.checkFake(params, maKhachHang);
		
		if(!checkFakeFalse) throw new CheckException(language.getMessage("Bạn vui lòng cử động khuôn mặt không quá nhanh"));
		
		Double nguongSoSanh = configProperties.getConfig().getDoubleNguong_chinh_xac();
		Double similar = 0.0;
		if(!StringUtils.isEmpty(params.getAnhMatTruoc())) {
			similar = compareImage(params.getAnhMatTruoc(), params.getAnhVideo().get(0).getAnh(), maKhachHang, language.getMessage("Ảnh mặt trước không khớp với ảnh trong video"));
			if(similar < nguongSoSanh) throw new CheckException(language.getMessage("Ảnh mặt trước không khớp với ảnh trong video"));
		}
		if(!StringUtils.isEmpty(params.getAnhKhachHang())) {
			Double similar1 = compareImage(params.getAnhKhachHang(), params.getAnhVideo().get(0).getAnh(), maKhachHang, language.getMessage("Ảnh khách hàng không khớp với ảnh trong video"));
			if(similar1 < nguongSoSanh) throw new CheckException(language.getMessage("Ảnh khách hàng không khớp với ảnh trong video"));
		}
		
		return ReturnRest.success(similar);
	}
	
	public String xacThucYeuCauHanhDongVideo(Params params, String maKhachHang) throws CheckException {
		boolean checkFakeFalse = layThongTinTuAnh.checkFake(params, maKhachHang);
		
		if(!checkFakeFalse) throw new CheckException(language.getMessage("Bạn vui lòng cử động khuôn mặt không quá nhanh"));
		
		return ReturnRest.success(null);
	}
	
	/**
	 * @param params
	 * @param maKhachHang
	 * @throws CheckException 
	 */
	public Double compareImage(String base64Img1, String base64Img2, String maKhachHang, String message) throws CheckException {
		Double nguongSoSanh = configProperties.getConfig().getDoubleNguong_chinh_xac();
		ParamsImg paramsImg = new ParamsImg(base64Img1, base64Img2, language.getMessage(message), nguongSoSanh, khachHang.getKiemTraKhuonMat(maKhachHang), maKhachHang);
		return faceIdValid.validImageScore(paramsImg);
	}

}
