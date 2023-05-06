package fis.com.vn.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fis.com.vn.common.Common;
import fis.com.vn.common.StringUtils;
import fis.com.vn.repository.LogApiDetailRepository;
import fis.com.vn.repository.LogApiRepository;
import fis.com.vn.repository.LogCodeTransactionRepository;
import fis.com.vn.table.LogApi;
import fis.com.vn.table.LogApiDetail;
import fis.com.vn.table.LogCodeTransaction;

@Component
public class LogDatabase {
	@Autowired LogApiRepository logApiRepository;
	@Autowired LogApiDetailRepository logApiDetailRepository;
	@Autowired KhachHang khachHang;
	@Autowired LogCodeTransactionRepository logCodeTransactionRepository;
	@Value("${logging.file.name}")
	protected String loggingFile;
	
	@Async
	public void start(String requestId, long timeHandling, String token, String code, String uri, String method, int status, String codeTransaction, String responseBody, Object params, Object images,
			Object soDienThoai, Object hoVaTen, Object soCmt, Object soHopDong) {
		luuLichSuApi(requestId, timeHandling, token, code, uri, method, status, codeTransaction, responseBody, soDienThoai, hoVaTen, soCmt, soHopDong);
		luuChiTietLichSuApi(requestId, responseBody, params, images, code);

		if(status != 500) {
			luuTruGiaoDich(code, codeTransaction, uri, soDienThoai, hoVaTen, soCmt, soHopDong);
		}
	}
	private void luuChiTietLichSuApi(String requestId, String responseBody, Object params, Object images, String code) {
		try {
			LogApiDetail logApi = new LogApiDetail();
			logApi.setLogId(requestId);
			logApi.setResponse(responseBody);
			logApi.setParams(params.toString());
			logApi.setImages(images.toString());
			logApiDetailRepository.save(logApi);
			
		} catch (Exception e) {
		}
		
	}
	private String codeTransactionConvert(String code, String codeTransaction, String uri) {
		if(uri.endsWith("/doc-noi-dung-ocr")) {   
			if(StringUtils.isEmpty(codeTransaction)) {
				return Common.layMaGiaoDich(1);
			} else {
				return codeTransaction+"_"+System.currentTimeMillis();
			}
		} else { 
			return null;
		}
	}
	/**
	 * @param responseBody 
	 * @param soHopDong 
	 * @param soCmt 
	 * @param hoVaTen 
	 * @param soDienThoai 
	 * 
	 */
	private void luuLichSuApi(String requestId, long timeHandling, String token, String code, String uri, String method, int status, String codeTransaction, String responseBody
			, Object soDienThoai, Object hoVaTen, Object soCmt, Object soHopDong) {
		try {
			String resp = responseBody;
			if(responseBody.startsWith("{")) {
				try {
					JSONObject jsonObject = new JSONObject(responseBody);
					jsonObject.remove("data");
					jsonObject.remove("included");
					resp = jsonObject.toString();
				} catch (Exception e) {
				}
			}
			
			LogApi logApi = new LogApi();
			logApi.setLogId(requestId);
			logApi.setTimeHandling(timeHandling);
			logApi.setDate(new Date());
			logApi.setStatus(status);
			logApi.setToken(token);
			logApi.setCode(code);
			logApi.setUri(uri);
			logApi.setMethod(method);
			logApi.setCodeTransaction(codeTransaction);
			logApi.setResponse(resp);
			logApi.setPhone(soDienThoai!=null?soDienThoai.toString():"");
			logApi.setFullName(hoVaTen!=null?hoVaTen.toString():"");
			logApi.setIdCard(soCmt!=null?soCmt.toString():"");
			logApi.setIdContract(soHopDong!=null?soHopDong.toString():"");;
			logApi.setFileLog(loggingFile.split("\\/")[loggingFile.split("\\/").length - 1]);
			logApiRepository.save(logApi);
		} catch (Exception e) {
		}
	}
	/**
	 * @param code
	 * @param codeTransaction
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat dateFormatHour = new SimpleDateFormat("yyyy/MM/dd HH");
	private void luuTruGiaoDich(String code, String codeTransaction, String uri, Object soDienThoai, Object hoVaTen, Object soCmt, Object soHopDong) {
		try {
			codeTransaction = codeTransactionConvert(code, codeTransaction, uri);
			if(!StringUtils.isEmpty(codeTransaction)) {
				LogCodeTransaction logApi = new LogCodeTransaction();
				logApi.setDate(new Date());
				logApi.setCode(code);
				logApi.setCodeTransaction(codeTransaction);
				logApi.setDateString(dateFormat.format(new Date()));
				logApi.setDateStringHour(dateFormatHour.format(new Date()));
				logApi.setPhone(soDienThoai!=null?soDienThoai.toString():"");
				logApi.setFullName(hoVaTen!=null?hoVaTen.toString():"");
				logApi.setIdCard(soCmt!=null?soCmt.toString():"");
				logApi.setIdContract(soHopDong!=null?soHopDong.toString():"");;
				logCodeTransactionRepository.save(logApi);
			}
			
		} catch (Exception e) {
		}
	}
	
}
