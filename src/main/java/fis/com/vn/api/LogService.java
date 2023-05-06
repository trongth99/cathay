package fis.com.vn.api;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import fis.com.vn.common.StringUtils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.RespApi;

@Service
public class LogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);
	@Autowired LogDatabase logDatabase;
	@Autowired ConfigProperties configProperties;
	
	public void logRequest(HttpServletRequest req, String requestId) {
		try {
			req.setAttribute("requestId", requestId);
			req.setAttribute("timeStart", System.currentTimeMillis());
			req.setAttribute("code", req.getHeader(Contains.MA_TO_CHUC_HEADER));
			req.setAttribute("code_transaction", req.getAttribute("codeTransaction"));
			LOGGER.info("Url: " +req.getHeader(Contains.MA_TO_CHUC_HEADER)+"|"+ req.getRequestURI()+"?"+req.getQueryString()+"|"+req.getMethod()+"|"+req.getHeader("token").substring(0,20));
			LOGGER.info("MGD: " +req.getAttribute("codeTransaction"));
		} catch (Exception e) {
		}
	}
	public String layMaGiaoDichHd(HttpServletRequest req) {
		String code_transaction = req.getHeader(Contains.MA_GIAO_DICH_HEADER);
		if(StringUtils.isEmpty(code_transaction)) code_transaction  = req.getHeader(Contains.MA_GIAO_DICH_HEADER2);
		return code_transaction;
	}
	public void logResponse(Object body, ServerHttpRequest request, ServerHttpResponse response) {
		try {
			HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
			long endTime = System.currentTimeMillis();
			long timeStart = req.getAttribute("timeStart")  != null ?(long)req.getAttribute("timeStart"):0;
			long timeHandling = endTime - timeStart;
			String uri = getUriFromRequest(req);
			String requestId = (String) req.getAttribute("requestId");
			String codeTransaction = (String) req.getAttribute("codeTransaction");
			
			int status;
			String message = "";
			String responseBody = "";
			
			if(body instanceof RespApi) {
				if(uriPrintLog(uri))
					LOGGER.info("Response: "+timeHandling+"|"+new Gson().toJson(body));
				RespApi respApi = (RespApi) body;
				status = respApi.getStatus();
				message = respApi.getMessage();
				
				responseBody += new Gson().toJson(body);
			} else {
				if(uriPrintLog(uri))
					LOGGER.info("Response: "+timeHandling+"|"+body.toString());
				RespApi respApi = new Gson().fromJson(body.toString(), RespApi.class);
				status = respApi.getStatus();
				if(status == 0) {
					try {
						JSONObject jsonObject = new JSONObject(body.toString());
						status = jsonObject.getInt("code");
					} catch (Exception e) {
					}
				}
				
				if(uri.endsWith("/kiem-tra-thong-tin")) {
					responseBody += convertResponse(body.toString());
				} else {
					responseBody += body.toString();
				}
			}
			if(!message.equals(Contains.NOT_CHECK_MESSAGE))
				logSaveDb(requestId, 
						timeHandling, 
						req.getHeader("token"), 
						req.getHeader(Contains.MA_TO_CHUC_HEADER), 
						uri, 
						req.getMethod(), 
						status, 
						codeTransaction, 
						responseBody, 
						req.getAttribute("params"), 
						req.getAttribute("images"),
						req.getAttribute("soDienThoai"),
						req.getAttribute("hoVaTen"), 
						req.getAttribute("soCmt"), 
						req.getAttribute("soHopDong")
						);
		} catch (Exception e) {
//			LOGGER.error("Error: {}", e.getMessage());
		}
	}
	/**
	 * @param string
	 * @return
	 */
	private String convertResponse(String respStr) {
		return respStr;
	}
	private boolean uriPrintLog(String uri) {
		if(uri.endsWith("kbank/ky-so")) return false;
		if(uri.endsWith("/danh-sach-hop-dong")) return false;
		if(uri.endsWith("/kbank/danh-sach")) return false;
		if(uri.endsWith("/public/kiem-tra-thong-tin")) return true;
		if(uri.endsWith("/kiem-tra-thong-tin")) return false;
		if(uri.endsWith("/kiem-tra-mang-xa-hoi")) return false;
		if(uri.endsWith("/doc-noi-dung-ocr-valid-cmt")) return false;
		if(uri.endsWith("/public/captcha")) return false;
		if(uri.endsWith("/public/config")) return false;
		if(uri.endsWith("/public/all/quay-anh")) return false;
		if(uri.indexOf("/download/") != -1) return false;
		if(uri.indexOf("/midpoint/") != -1) return false;
		return true;
	}
	private String getUriFromRequest(HttpServletRequest req) {
		String uri = req.getRequestURI();
		return uri;
	}
	private void logSaveDb(String requestId, long timeHandling, String token, String code, String uri, String method, int status, String codeTransaction, String responseBody, Object params, Object images, 
			Object soDienThoai, Object hoVaTen, Object soCmt, Object soHopDong) {
		if(uriSaveLogDb(uri) && !StringUtils.isEmpty(token)) logDatabase.start(requestId, timeHandling, token, code, uri, method, status, codeTransaction, responseBody, params, images,
				soDienThoai, hoVaTen, soCmt, soHopDong);
	}
	
	private boolean uriSaveLogDb(String uri) {
		if(uri.endsWith("/doc-noi-dung-ocr")) return true;
		if(uri.endsWith("/xac-thuc-khuon-mat")) return true;
		if(uri.endsWith("/so-sanh-anh")) return true;
		
		return false;
	}
}


