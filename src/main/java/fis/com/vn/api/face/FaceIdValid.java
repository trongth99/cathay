package fis.com.vn.api.face;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.api.KhachHang;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.exception.CheckException;

@Component
public class FaceIdValid {
	@Autowired ThongTinAnh layThongTinTuAnh;
	@Autowired KhachHang khachHang;
	@Autowired ConfigProperties configProperties;
	
	public void validImage(ParamsImg paramsImg) throws CheckException{
		ResponseFaceMatch responseFaceMatch = layThongTinTuAnh.compare(paramsImg.getBase64Img1(), paramsImg.getBase64Img2(), paramsImg.getMaToChuc());
		
		if(responseFaceMatch == null) throw new CheckException("Không lấy được thông tin so sánh ảnh");
		if(responseFaceMatch.getError() == 1) throw new CheckException("Không tìm thấy gương mặt trong ảnh mặt trước");
		if(responseFaceMatch.getError() == 2) throw new CheckException("Ảnh 1 có nhiều hơn 1 gương mặt");
		if(responseFaceMatch.getError() == 3) throw new CheckException("Không tìm thấy gương mặt trong ảnh khách hàng");
		if(responseFaceMatch.getError() == 4) throw new CheckException("Ảnh 2 có nhiều hơn 1 gương mặt");
		if(responseFaceMatch.getError() == 5) throw new CheckException("Bạn đang đeo kính, vui lòng bỏ kính");
		if(responseFaceMatch.getError() == 6) throw new CheckException("Bạn đang đeo khẩu trang, vui lòng bỏ khẩu trang");
		
		if(responseFaceMatch.getSimilarity() <= paramsImg.getCompareValue()) {
			throw new CheckException(paramsImg.getErrorMsg());
		}
	}
	public Double validImageScore(ParamsImg paramsImg) throws CheckException{
		ResponseFaceMatch responseFaceMatch = layThongTinTuAnh.compare(paramsImg.getBase64Img1(), paramsImg.getBase64Img2(), paramsImg.getMaToChuc());
		
		if(responseFaceMatch == null) throw new CheckException("Không lấy được thông tin so sánh ảnh");
		if(responseFaceMatch.getError() == 1) throw new CheckException("Không tìm thấy gương mặt trong ảnh mặt trước");
		if(responseFaceMatch.getError() == 2) throw new CheckException("Ảnh mặt trước có nhiều hơn 1 gương mặt");
		if(responseFaceMatch.getError() == 3) throw new CheckException("Không tìm thấy gương mặt trong ảnh khách hàng");
		if(responseFaceMatch.getError() == 4) throw new CheckException("Ảnh khách hàng có nhiều hơn 1 gương mặt");
		if(responseFaceMatch.getError() == 5) throw new CheckException("Bạn đang đeo kính, vui lòng bỏ kính");
		if(responseFaceMatch.getError() == 6) throw new CheckException("Bạn đang đeo khẩu trang, vui lòng bỏ khẩu trang");
		
		return responseFaceMatch.getSimilarity();
	}
}
