package fis.com.vn.common;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.component.ConfigProperties;
import fis.com.vn.entities.RespApi;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.LogApiSeaBankRepository;
import fis.com.vn.table.EkycKyso;
import fis.com.vn.table.LogApiSeaBank;

@Component
public class SendSMS {
	private static final Logger LOGGER = LoggerFactory.getLogger(SendSMS.class);

	@Autowired ConfigProperties configProperties;
	@Autowired LogApiSeaBankRepository logApiSeaBankRepository;
	
	public String postRequestSMS(String phone, String message, EkycKyso ekycKyso) throws ErrorException {
		RespApi respApi = new RespApi();
		long time1 = System.currentTimeMillis();
		JSONObject jsonReq = new JSONObject();
		jsonReq.put("message", message);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Username", configProperties.getConfig().getUsername_sms());
			jsonObject.put("Password", configProperties.getConfig().getPassword_sms());
			jsonObject.put("PhoneNumber", phone);
			jsonObject.put("PrefixId", "PTFTAICHINH");
			jsonObject.put("CommandCode", "PTFTAICHINH");
			jsonObject.put("RequestId", "0");
			jsonObject.put("MsgContent", message);
			jsonObject.put("MsgContentTypeId", 0);
			jsonObject.put("FeeTypeId", 0);
			
			LOGGER.info("SMS send phone: "+ phone);
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost("http://apiv2.incomsms.vn/MtService/SendSms");
			StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			
			luuLogApiSeaBankCron(time1, 1, "http://apiv2.incomsms.vn/MtService/SendSms", "POST", responseString, null, jsonReq.toString(), "send sms", ekycKyso);
			
			LOGGER.info("SMS response str: "+ responseString);
			JSONObject object = new JSONObject(responseString);
			LOGGER.info("SMS response: "+ object.toString());
			if(!object.getString("StatusCode").equals("1")) throw new Exception("Không gửi được tin nhắn");
			
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBankCron(time1, 501, "http://apiv2.incomsms.vn/MtService/SendSms", "POST", msg, null, jsonReq.toString(), "send sms", ekycKyso);
			LOGGER.error("Error send SMS: "+ e);
			respApi.setStatus(400);
			respApi.setMessage("Lỗi hệ thống");
			throw new ErrorException("Không gửi được tin nhắn");
		}
	}
	
	public String luuLogApiSeaBankCron(long time1, long status, String uri, String method, String resp,
			HttpServletRequest req, String params, String describe, EkycKyso ekycKyso) {
		try {
			long time2 = System.currentTimeMillis();
			long timeHandling = time2 - time1;

			LogApiSeaBank logApiSeaBank = new LogApiSeaBank();
			logApiSeaBank.setLogId(UUID.randomUUID().toString());
			logApiSeaBank.setTimeHandling(timeHandling);
			logApiSeaBank.setDate(new Date());
			logApiSeaBank.setStatus(status);
			try {
				if (ekycKyso != null) {
					logApiSeaBank.setFullName(ekycKyso.getHoVaTen());
					logApiSeaBank.setIdCard(ekycKyso.getSoCmt());
					logApiSeaBank.setPhone(ekycKyso.getSoDienThoai());
					logApiSeaBank.setIdContract(ekycKyso.getSoTaiKhoan());
					logApiSeaBank.setIdLos(ekycKyso.getCaseId());

				}
			} catch (Exception e) {
			}
			
			logApiSeaBank.setMota(describe);
			logApiSeaBank.setParams(params);
			logApiSeaBank.setUri(uri);
			logApiSeaBank.setMethod(method);
			logApiSeaBank.setResponse(resp);
			logApiSeaBankRepository.save(logApiSeaBank);
			return logApiSeaBank.getLogId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
