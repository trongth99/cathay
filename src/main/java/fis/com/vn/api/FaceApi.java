package fis.com.vn.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import fis.com.vn.api.entities.Params;
import fis.com.vn.contains.Contains;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;

@RestController
public class FaceApi  extends BaseApi{
	@Autowired ComponentApi componentApi;
	@PostMapping(value = {"/api/public/all/xac-thuc-khuon-mat"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public String xacThucKhuonMat(HttpServletRequest req) throws ValidException , Exception, CheckException{
		Params params = getParamsFromInputstream(req); 
		
		req.setAttribute("soDienThoai",  params.getSoDienThoai());
		req.setAttribute("hoVaTen",  params.getHoVaTen());
		req.setAttribute("soCmt",  params.getSoCmt());
		req.setAttribute("soHopDong",  params.getSoHopDong());
		req.setAttribute("codeTransaction",  params.getCodeTransaction());
		
		return componentApi.xacThucYeuCauHanhDong(params, layMaKhachHang(req));
	} 
	
	@PostMapping(value = {"/api/public/all/so-sanh-anh"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public String soSanhAnh(HttpServletRequest req) throws ValidException , Exception, CheckException{
		Params params = getParamsFromInputstream(req);
		
		req.setAttribute("soDienThoai",  params.getSoDienThoai());
		req.setAttribute("hoVaTen",  params.getHoVaTen());
		req.setAttribute("soCmt",  params.getSoCmt());
		req.setAttribute("soHopDong",  params.getSoHopDong());
		req.setAttribute("codeTransaction",  params.getCodeTransaction());
		
		return componentApi.soSanhAnh(params, req.getHeader(Contains.MA_TO_CHUC_HEADER));
	}
}
