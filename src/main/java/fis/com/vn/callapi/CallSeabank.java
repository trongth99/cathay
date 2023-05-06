package fis.com.vn.callapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.google.gson.Gson;

import fis.com.vn.callapi.entities.Bank;
import fis.com.vn.callapi.entities.DanhSachNganHang;
import fis.com.vn.callapi.entities.DanhSachTaiKhoan;
import fis.com.vn.callapi.entities.InforBranch;
import fis.com.vn.callapi.entities.PramMoTaiKhoan;
import fis.com.vn.callapi.entities.TaiKhoan;
import fis.com.vn.callapi.entities.ThongTinNguoiDungTuLOS;
import fis.com.vn.callapi.entities.ThongTinNguoiDungTuLOSResp;
import fis.com.vn.callapi.entities.ThongTinTaiKhoanDangKy;
import fis.com.vn.common.StringUtils;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.repository.LogApiSeaBankRepository;
import fis.com.vn.table.EkycKyso;
import fis.com.vn.table.LogApiSeaBank;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class CallSeabank {
	@Value("${MOI_TRUONG}")
	String MOI_TRUONG;
	@Autowired
	LogApiSeaBankRepository logApiSeaBankRepository;
	@Autowired
	EkycKysoRepository ekycKysoRepository;
	private final Logger LOGGER = LoggerFactory.getLogger(CallSeabank.class);
	private final String PTF = "PTF";
	private final String Signature = "c7bad2a16adec5174910bd31f66684dd8151e10f7281f984e41ae08cd3239e2f58e904da777334e1b42716edadec0214d6ee27143f7ec880ff883572ff8f867d";
	private final String X_CLIENT_ID = "745b1e84-c0f0-494d-baea-169faf579340";
	private final String X_SECRET_API = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVSUQwMDc0NTkiLCJpYXQiOjE2NjU3NDM1Mjl9.HY79-_9vvIWCnyGOInN_LUTbO08AYwFBuLr7lWklHZo";
	
	public String moTaiKhoan(PramMoTaiKhoan pramMoTaiKhoan, HttpServletRequest req) {
		long time1 = System.currentTimeMillis();
		String urlMoTaiKhoan = "https://seapartnertst.seabank.com.vn/services/sibcoaccountinfo/api/signature/agent/v1/current-account";
		try {
			String permanentAddress = cutString(pramMoTaiKhoan.getPermanentAddress(), 34);
			String birtPlace = cutString(pramMoTaiKhoan.getBirtPlace(), 34);
			pramMoTaiKhoan.setPermanentAddress(permanentAddress);
			
			LOGGER.info("moTaiKhoan params 2: {}", new Gson().toJson(pramMoTaiKhoan));
			
			OkHttpClient client = getOkHttpClient();
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("frontPortraitImage", "frontPortraitImage.jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(pramMoTaiKhoan.getFrontPortraitImage())))
					.addFormDataPart("otherPortraitImages", "otherPortraitImages.jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(pramMoTaiKhoan.getOtherPortraitImages())))
					.addFormDataPart("otherPortraitImages", "otherPortraitImages.jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(pramMoTaiKhoan.getOtherPortraitImages1())))
					.addFormDataPart("frontIdentityImage", "frontIdentityImage.jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(pramMoTaiKhoan.getFrontIdentityImage())))
					.addFormDataPart("backIdentityImages", "backIdentityImages.jpg", RequestBody.create(MediaType.parse("image/jpg"), new File(pramMoTaiKhoan.getBackIdentityImages())))
					.addFormDataPart("eKycRate", pramMoTaiKhoan.getEKycRate())
					.addFormDataPart("fullName", pramMoTaiKhoan.getFullName())
					.addFormDataPart("dob", pramMoTaiKhoan.getDob()).addFormDataPart("birtPlace", birtPlace)
					.addFormDataPart("gender", pramMoTaiKhoan.getGender())
					.addFormDataPart("identityType", pramMoTaiKhoan.getIdentityType())
					.addFormDataPart("identityNumber", pramMoTaiKhoan.getIdentityNumber())
					.addFormDataPart("issuedDate", pramMoTaiKhoan.getIssuedDate())
					.addFormDataPart("issuedBy", pramMoTaiKhoan.getIssuedBy())
					.addFormDataPart("countryCode", pramMoTaiKhoan.getCountryCode())
					.addFormDataPart("phoneNumber", pramMoTaiKhoan.getPhoneNumber())
					.addFormDataPart("email", pramMoTaiKhoan.getEmail())
					.addFormDataPart("income", pramMoTaiKhoan.getIncome())
					.addFormDataPart("taxCode", pramMoTaiKhoan.getTaxCode())
					.addFormDataPart("industryGroup", pramMoTaiKhoan.getIndustryGroup())
					.addFormDataPart("job", pramMoTaiKhoan.getJob())
					.addFormDataPart("position", pramMoTaiKhoan.getPosition())
					.addFormDataPart("maritalStatus", pramMoTaiKhoan.getMaritalStatus())
					.addFormDataPart("permanentAddress", permanentAddress)
					.addFormDataPart("address", pramMoTaiKhoan.getAddress())
					.addFormDataPart("district", pramMoTaiKhoan.getDistrict())
					.addFormDataPart("province", pramMoTaiKhoan.getProvince())
					.addFormDataPart("resident", pramMoTaiKhoan.getResident())
					.addFormDataPart("otherOwner", pramMoTaiKhoan.getOtherOwner())
					.addFormDataPart("purpose", pramMoTaiKhoan.getPurpose())
					.addFormDataPart("legalAgreement", pramMoTaiKhoan.getLegalAgreement())
					.addFormDataPart("fatcaInformation", pramMoTaiKhoan.getFatcaInformation())
					.addFormDataPart("preferLocation", pramMoTaiKhoan.getPreferLocation())
					.addFormDataPart("productCode", pramMoTaiKhoan.getProductCode())
					.addFormDataPart("accuracyMethod", pramMoTaiKhoan.getAccuracyMethod())
					.addFormDataPart("referral", pramMoTaiKhoan.getReferral())
					.addFormDataPart("confirmTermSea", pramMoTaiKhoan.getConfirmTermSea())
					.addFormDataPart("agent", pramMoTaiKhoan.getAgent())
					.addFormDataPart("expiryDate", pramMoTaiKhoan.getExpiryDate())
					.addFormDataPart("registerEbankUsername", pramMoTaiKhoan.getRegisterEbankUsername()).build();
			Request request = new Request.Builder().url(urlMoTaiKhoan)
					.method("POST", body)
					.addHeader("Partner", "PTF")
					.addHeader("Signature", Signature)
					.build();
			Response response = client.newCall(request).execute();

			JSONObject jsonParam = new JSONObject(pramMoTaiKhoan);
			String text = response.body().string();
			
			LOGGER.info ("moTaiKhoan resp: {}", text);
			
			if (text == null) text = "{}";
			
			JSONObject jsonObject = new JSONObject(text);
			try {
				luuLogApiSeaBank(time1, Integer.valueOf(jsonObject.get("code").toString()), urlMoTaiKhoan, "POST", text, req, jsonParam.toString(), "api mở tktt seabank");
			} catch (Exception e) {
				luuLogApiSeaBank(time1, 500, urlMoTaiKhoan, "POST", text, req, jsonParam.toString(), "api mở tktt seabank");
			}
			
			if (!jsonObject.getString("code").equals("0") && !jsonObject.getString("code").equals("201"))
				return null;

			return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBank(time1, 501, urlMoTaiKhoan, "POST", msg, req, new Gson().toJson(pramMoTaiKhoan), "api mở tktt seabank");
			LOGGER.error("moTaiKhoan error: ", pramMoTaiKhoan.getIdentityNumber());
			LOGGER.error("moTaiKhoan error: {}", e);
		}

		return null;
	}
	
	private String cutString(String str, int sub) {
		try {
			if(str.length() > sub) return str.substring(0, sub);
		} catch (Exception e) {
		}
		return str;
	}
	
	public String listBank() {
		try {
			OkHttpClient client = getOkHttpClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"getMetaData\":true}");
			Request request = new Request.Builder().url("https://gwdev.ptf.com.vn/masterdatas/list")
					.method("POST", body).addHeader("X-CLIENT-ID", X_CLIENT_ID).addHeader("X-SECRET-API", X_SECRET_API)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();
			response.body().close();
			response.close();
			return text;
		} catch (Exception e) {
			LOGGER.error("listbank error: {}", e);
		}

		return null;
	}

	

	public String listCity() {
		try {
			OkHttpClient client = getOkHttpClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"getMetaData\":true}");
			Request request = new Request.Builder().url("https://gwdev.ptf.com.vn/masterdatas/list")
					.method("POST", body).addHeader("X-CLIENT-ID", X_CLIENT_ID).addHeader("X-SECRET-API", X_SECRET_API)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();
			response.body().close();
			response.close();
			return text;
		} catch (Exception e) {
			LOGGER.error("listCity error: {}", e);
		}

		return null;
	}

	public String closeAccc(String accountId, String coCode, HttpServletRequest req) {
		long time1 = System.currentTimeMillis();
		String urlClose = "https://seapartnertst.seabank.com.vn/services/sibcoaccountinfo/api/signature/agent/v1/close-account";
		JSONObject jsonObject = new JSONObject().put("accountId", accountId).put("coCode", coCode);
		try {
			LOGGER.info ("closeAccc params: {}", jsonObject.toString());
			
			OkHttpClient client = getOkHttpClient();
		    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
		      .addFormDataPart("accountId", accountId)
		      .addFormDataPart("settlementAccount","VND1890100990009")
		      .addFormDataPart("coCode", coCode)
		      .build();
		    Request request = new Request.Builder()
		      .url(urlClose)
		      .method("POST", body)
		      .addHeader("Partner",  PTF)
		      .addHeader("Signature", Signature)
		      .build();
		    Response response = client.newCall(request).execute();
		    
		    String text = response.body().string();
		    
		    LOGGER.info ("closeAccc resp: {}", text);
		    
		    if(text == null) text = "{}";
		    try {
		    	JSONObject jsonObject2 = new JSONObject(text);
				luuLogApiSeaBank(time1, jsonObject2.getInt("code"), urlClose, "POST", text, req, jsonObject.toString(), "api close acc");
			} catch (Exception e) {
				luuLogApiSeaBank(time1, 500, urlClose, "POST", text, req, jsonObject.toString(), "api close acc");
			}
		    
		    return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBank(time1, 501, urlClose, "POST", msg, req, jsonObject.toString(), "api close acc");
			LOGGER.error("listCity error: {}", e);
		}

		return null;
	}
	public String closeAcccCron(String accountId, String coCode, EkycKyso ekycKyso, String username) {
		long time1 = System.currentTimeMillis();
		String urlClose = "https://seapartnertst.seabank.com.vn/services/sibcoaccountinfo/api/signature/agent/v1/close-account";
		JSONObject jsonObject = new JSONObject().put("accountId", accountId).put("coCode", coCode);
		try {
			LOGGER.info ("closeAccc params: {}", jsonObject.toString());
			
			OkHttpClient client = getOkHttpClient();
		    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
		      .addFormDataPart("accountId", accountId)
		      .addFormDataPart("settlementAccount","VND1890100990009")
		      .addFormDataPart("coCode", coCode)
		      .build();
		    Request request = new Request.Builder()
		      .url(urlClose)
		      .method("POST", body)
		      .addHeader("Partner",  PTF)
		      .addHeader("Signature", Signature)
		      .build();
		    Response response = client.newCall(request).execute();
		    
		    String text = response.body().string();
		    
		    LOGGER.info ("closeAccc resp: {}", text);
		    
		    if(text == null) text = "{}";
		    try {
		    	JSONObject jsonObject2 = new JSONObject(text);
				luuLogApiSeaBankCron(time1, jsonObject2.getInt("code"), urlClose, "POST", text, null, jsonObject.toString(), "api close acc", ekycKyso, username);
			} catch (Exception e) {
				luuLogApiSeaBankCron(time1, 500, urlClose, "POST", text, null, jsonObject.toString(), "api close acc", ekycKyso, username);
			}
		    
		    return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBankCron(time1, 501, urlClose, "POST", msg, null, jsonObject.toString(), "api close acc", ekycKyso, username);
			LOGGER.error("closeAcccCron error: {}", e);
		}

		return null;
	}
	public String listBranch() {
		try {
			OkHttpClient client = getOkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"getMetaData\":true}");
			Request request = new Request.Builder().url("https://gwdev.ptf.com.vn/masterdatas/list")
					.method("POST", body).addHeader("X-CLIENT-ID", X_CLIENT_ID).addHeader("X-SECRET-API", X_SECRET_API)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();
			response.body().close();
			response.close();
			return text;
		} catch (Exception e) {
			LOGGER.error("listBranch error: {}", e);
		}

		return null;
	}

	public String getAccountInfor(String bankAccount, String bankCode, HttpServletRequest req) {
		long time1 = System.currentTimeMillis();
		String urlGetAccInfo = "https://gwdev.ptf.com.vn/fetchsvc/bankacc";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("bankAccount", bankAccount);
		jsonObject.put("bankCode", bankCode);
		
		try {
			OkHttpClient client = getOkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "{\"bankAccount\": \"" + bankAccount+ "\",\"bankCode\": \"" + bankCode + "\"}");
			Request request = new Request.Builder().url(urlGetAccInfo)
					.method("POST", body).addHeader("X-CLIENT-ID", "745b1e84-c0f0-494d-baea-169faf579340")
					.addHeader("X-SECRET-API", X_SECRET_API)
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();

			String text = response.body().string();
			
			LOGGER.info ("getAccountInfor resp: {}", text);
			
			if(text == null) text = "{}";
			
			JSONObject jsonObject3 = new JSONObject(text);
			long code = 500;
			try {
				if (jsonObject3.get("success").equals(true)) code = 200;
				else code = jsonObject3.getJSONObject("error").getInt("code");
			} catch (Exception e) {
			}
			
			luuLogApiSeaBank(time1, code, urlGetAccInfo, "POST", text, req, jsonObject.toString(), "api check Bankaccount");

			response.body().close();
			response.close();

			return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBank(time1, 501, urlGetAccInfo, "POST", msg, req, jsonObject.toString(), "api check Bankaccount");
			LOGGER.error("getAccountInfor error: {}", e);
		}

		return null;
	}

	public String updateLos(String benAccountName, String bankName, String bankCity, String bankBranch, String bankCode,
			String benAccountNumber, String userID, String caseid, HttpServletRequest req, String amount) {

		int bank_name = Integer.parseInt(bankName);
		int bank_city = Integer.parseInt(bankCity);
		
		if(StringUtils.isEmpty(amount)) amount = "";
		
		String urlUpdateLos = "https://gwextdev.seabank.com.vn/seabank/seabank-external/api/v1/seapartner/ptf-los/process";
		long time1 = System.currentTimeMillis();
		
		JSONObject jsonObject = new JSONObject("{\r\n" + "    \"header\": {\r\n"
				+ "        \"reqType\": \"REQUEST\",\r\n" + "        \"api\": \"PTF_LOS_API\",\r\n"
				+ "        \"apiKey\": \"qmklfoni1ezxlf2ckpygpfx122020\",\r\n" + "        \"priority\": \"1\",\r\n"
				+ "        \"channel\": \"PTF_LOS\",\r\n" + "        \"subChannel\": \"PTF\",\r\n"
				+ "        \"location\": \"10.9.12.90\",\r\n" + "        \"context\": \"PC\",\r\n"
				+ "        \"trusted\": \"false\",\r\n" + "        \"requestAPI\": \"t24Server\",\r\n"
				+ "        \"requestNode\": \"10.9.10.14\",\r\n" + "        \"userID\": \"1365778600\",\r\n"
				+ "        \"sync\": \"true\"\r\n" + "    },\r\n" + "    \"body\": {\r\n"
				+ "        \"command\": \"GET_TRANSACTION\",\r\n" + "        \"transaction\": {\r\n"
				+ "        \"authenType\": \"savePreDisbursementInfo\",\r\n"
				+ "        \"session_id\": \"-714650147\",\r\n" + "        \"case_id\": \"" + caseid + "\",\r\n"
				+ "        \"queue_name\": \"PTF_PreDisbursement\",\r\n" + "        \"action\": \"U\",\r\n"
				+ "        \"disbursement_method\": \"2\",\r\n" + "        \"account_number\": \""
				+ benAccountNumber + "\",\r\n" + "        \"bank_name\": " + bank_name + ",\r\n"
				+ "        \"bank_city\": " + bank_city + ",\r\n" + "        \"bank_branch\": \"" + bankBranch
				+ "\",\r\n" + "        \"bank_code\": \"" + bankCode + "\",\r\n"
				+ "        \"partner_name\": \"\",\r\n" + "        \"partner_branch\": \"\",\r\n"
				+ "        \"ben_account_name\": \"" + benAccountName + "\",\r\n"
				+ "        \"amount\": \""+amount+"\"\r\n" + "        }\r\n" + "    }\r\n" + "}");
		
		LOGGER.info ("updateLos params: {}", jsonObject.toString());
		
		try {
			OkHttpClient client = getOkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonObject.toString());

			Request request = new Request.Builder()
					.url(urlUpdateLos)
					.method("POST", body).addHeader("X-IBM-Client-Id", "06d0f204ca09575fe57828b8d9525421")
					.addHeader("X-IBM-Client-Secret", "d7652fb681ac3a19e3851c1fff1d7b3d")
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();

			LOGGER.info ("updateLos resp: {}", text);
			if (text == null) text = "{}";
			long code = 500;
			try {
				JSONObject jsonObject3 = new JSONObject(text);
				if (jsonObject3.getJSONObject("body").get("status").equals("OK")) code = Long.parseLong(jsonObject3.getJSONObject("body").getString("response_code"));
				if (jsonObject3.getJSONObject("body").get("status").equals("FAILE")) code = Long.parseLong(jsonObject3.getJSONObject("error").getString("code"));
			} catch (Exception e) {
			}
			
			luuLogApiSeaBank(time1, code, urlUpdateLos, "POST", text, req, jsonObject.toString(), "api update thông tin los");

			response.body().close();
			response.close();
			return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBank(time1, 501, urlUpdateLos, "POST", msg, req, jsonObject.toString(), "api update thông tin los");
			LOGGER.error("updateLos error: {}", e);
		}

		return null;
	}

	public String getInforLos(String userID, HttpServletRequest req, EkycKyso ekycKyso) throws ParseException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String ngay = formatter.format(date);
		String urlInfoLos = "https://gwextdev.seabank.com.vn/seabank/seabank-external/api/v1/seapartner/ptf-los/process";
		
		long time1 = System.currentTimeMillis();
		JSONObject jsonObject = new JSONObject("{\r\n" + "    \"header\": {\r\n"
				+ "        \"reqType\": \"REQUEST\",\r\n" + "        \"api\": \"PTF_LOS_API\",\r\n"
				+ "        \"apiKey\": \"qmklfoni1ezxlf2ckpygpfx122020\",\r\n"
				+ "        \"channel\": \"PTF_LOS\",\r\n" + "        \"subChannel\": \"PTF\",\r\n"
				+ "        \"location\": \"10.9.12.90\",\r\n" + "        \"context\": \"PC\",\r\n"
				+ "        \"trusted\": \"false\",\r\n" + "        \"requestAPI\": \"t24Server\",\r\n"
				+ "        \"requestNode\": \"10.9.10.14\",\r\n" + "        \"priority\": 1,\r\n"
				+ "        \"userID\": \"1365778600\",\r\n" + "        \"sync\": true\r\n" + "    },\r\n"
				+ "    \"body\": {\r\n" + "        \"command\": \"GET_ENQUIRY\",\r\n" + "        \"enquiry\": {\r\n"
				+ "            \"authenType\": \"getCustInfo\",\r\n" + "            \"caseId\": \"" + userID
				+ "\",\r\n" + "            \"requestDate\": \"" + ngay + "\"\r\n" + "        }\r\n" + "    }\r\n"
				+ "}");
		
		LOGGER.info ("getInforLos params: {}", jsonObject.toString());
		try {
			OkHttpClient client = getOkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,jsonObject.toString());
			Request request = new Request.Builder()
					.url(urlInfoLos)
					.method("POST", body).addHeader("X-IBM-Client-Id", "06d0f204ca09575fe57828b8d9525421")
					.addHeader("X-IBM-Client-Secret", "d7652fb681ac3a19e3851c1fff1d7b3d")
					.addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();

			LOGGER.info ("getInforLos resp: {}", text);
			
			if (text == null) text = "{}";
			long code = 500;
			try {
				JSONObject jsonObject3 = new JSONObject(text);
				if (jsonObject3.getJSONObject("body").get("status").equals("OK")) code = jsonObject3.getJSONObject("body").getInt("response_code");
				else code = jsonObject3.getJSONObject("error").getInt("code");
			} catch (Exception e) {
			}
			
			if(ekycKyso == null)
				luuLogApiSeaBank(time1, code, urlInfoLos, "POST", text, req, jsonObject.toString(), "api lấy thông tin từ los");
			
			response.body().close();
			response.close();

			return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			if(ekycKyso == null)
				luuLogApiSeaBank(time1, 501, urlInfoLos, "POST", msg, req, jsonObject.toString(), "api lấy thông tin từ los");
			
			
			LOGGER.error("getInforLos error: {}", e);
		}

		return null;
	}

	public String getSendContracts(String identityNumber, String base64Sb, HttpServletRequest req) throws ParseException {
		long time1 = System.currentTimeMillis();
		String urlSendContract = "https://seapartnertst.seabank.com.vn/services/sibcoaccountinfo/api/signature/agent/v1/send-contracts";
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("identityNumber", identityNumber);
		jsonObject.put("identityType", "CMND");
		jsonObject.put("format", "PDF");
		jsonObject.put("openAccountContract", "");
		try {
			LOGGER.info("getSendContracts params: {}", jsonObject.toString());
			
			OkHttpClient client = getOkHttpClient();
			RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					.addFormDataPart("identityNumber", identityNumber).addFormDataPart("identityType", "CMND")
					.addFormDataPart("format", "PDF").addFormDataPart("openAccountContract", base64Sb).build();
			Request request = new Request.Builder().url(urlSendContract)
					.method("POST", body)
					.addHeader("Partner", PTF)
					.addHeader("Signature", Signature)
					.build();
			Response response = client.newCall(request).execute();
			String text = response.body().string();
			
			LOGGER.info("getSendContracts resp: {}", text);
			
			if(text == null) text = "{}";
			try {
				JSONObject jsonObject3 = new JSONObject(text);
				if(jsonObject3.has("code") && jsonObject3.getString("code").equals("0")) jsonObject3.getJSONObject("data").remove("contract");
				luuLogApiSeaBank(time1, Integer.parseInt(jsonObject3.getString("code")), urlSendContract, "POST", jsonObject3.toString(), req, jsonObject.toString(), "api send-contracts");
			} catch (Exception e) {
				luuLogApiSeaBank(time1, 500, urlSendContract, "POST", text, req, jsonObject.toString(), "api send-contracts");
			}
			
			response.body().close();
			response.close();
			return text;
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBank(time1, 501, urlSendContract, "POST", msg, req, jsonObject.toString(), "api send-contracts");
			
			LOGGER.error("getSendContracts error: {}", e);
		}

		return null;
	}

	private OkHttpClient getOkHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };

		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

		OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
		newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
		newBuilder.hostnameVerifier((hostname, session) -> true);

		OkHttpClient client = newBuilder
			    .connectTimeout(100, TimeUnit.SECONDS)
			    .writeTimeout(100, TimeUnit.SECONDS)
			    .readTimeout(100, TimeUnit.SECONDS)
			    .build();

		return client;
	}
	private OkHttpClient getOkHttpClient2() throws KeyManagementException, NoSuchAlgorithmException {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };

		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

		OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
		newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
		newBuilder.hostnameVerifier((hostname, session) -> true);

		OkHttpClient client = newBuilder
			    .readTimeout(1, TimeUnit.MILLISECONDS)
			    .build();

		return client;
	}
	public DanhSachTaiKhoan taoDanhSachTaiKhoan(JSONObject jsonObject) {
		DanhSachTaiKhoan danhSachTaiKhoan = new DanhSachTaiKhoan();
		if (jsonObject.getString("code").equals("0")) {
			TaiKhoan taiKhoan = new TaiKhoan();
			taiKhoan.setHoVaTen(jsonObject.getJSONObject("data").getString("customerName"));
			taiKhoan.setSoTaiKhoan(jsonObject.getJSONObject("data").getString("accountNo"));
			taiKhoan.setIdentityNumber(jsonObject.getJSONObject("data").getString("identityNumber"));
			taiKhoan.setCreateDate(jsonObject.getJSONObject("data").getString("createDate"));
			taiKhoan.setCoCode(jsonObject.getJSONObject("data").getString("coCode"));
			taiKhoan.setCoCodeFull(jsonObject.getJSONObject("data").getString("coCode"));			
			danhSachTaiKhoan.add(taiKhoan);
			danhSachTaiKhoan.setTaiKhoanMoi("true");
		}
		if (jsonObject.getString("code").equals("201")) {
			JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("listAccount");
			for (int i = 0; i < jsonArray.length(); i++) {
				String[] accountIds = jsonArray.getJSONObject(i).getString("accountId").split(" ");
				for (String str : accountIds) {
					if(str.trim().equals("")) continue;
					TaiKhoan taiKhoan = new TaiKhoan();
					taiKhoan.setHoVaTen(jsonArray.getJSONObject(i).getString("custName"));
					taiKhoan.setSoTaiKhoan(str.trim());
					taiKhoan.setCoCode(jsonArray.getJSONObject(i).getString("coCode"));
					taiKhoan.setCoName(jsonArray.getJSONObject(i).getString("coName"));
					taiKhoan.setCoCodeFull(jsonArray.getJSONObject(i).getString("coCode")+" - "+jsonArray.getJSONObject(i).getString("coName"));
					taiKhoan.setLegalIssAuth(jsonArray.getJSONObject(i).getString("legalIssAuth"));

					danhSachTaiKhoan.add(taiKhoan);
				}
			}
			danhSachTaiKhoan.setTaiKhoanMoi("false");
		}
		return danhSachTaiKhoan;
	}

	public DanhSachNganHang createListBank(String text, JSONObject jsonObject) {
		DanhSachNganHang danhSachNganHang = new DanhSachNganHang();

		try {
			JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");
			for (int i = 0; i < jsonArray.length(); i++) {
				Bank bank = new Bank();

				InforBranch inforBranch = new InforBranch();
				inforBranch.setCode(jsonArray.getJSONObject(i).getJSONObject("metaData").get("code").toString());
				inforBranch.setId(jsonArray.getJSONObject(i).getJSONObject("metaData").get("id").toString());
				bank.setType(jsonArray.getJSONObject(i).getString("type"));
				bank.setName(jsonArray.getJSONObject(i).getString("name"));
				bank.setValue(jsonArray.getJSONObject(i).getString("value"));
				bank.setParentValue(jsonArray.getJSONObject(i).getString("parentValue"));
				bank.setInforBranch(inforBranch);
				danhSachNganHang.add(bank);
			}
		} catch (Exception e) {
		}

		return danhSachNganHang;
	}

	public DanhSachNganHang createListBranch(String text, JSONObject jsonObject) {
		DanhSachNganHang danhSachNganHang = new DanhSachNganHang();

		JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");
		for (int i = 0; i < jsonArray.length(); i++) {
			Bank bank = new Bank();

			InforBranch inforBranch = new InforBranch();
			inforBranch.setCode(jsonArray.getJSONObject(i).getJSONObject("metaData").get("code").toString());
			inforBranch.setId(jsonArray.getJSONObject(i).getJSONObject("metaData").get("id").toString());
			inforBranch.setBankNameId(jsonArray.getJSONObject(i).getJSONObject("metaData").getInt("bank_name_id"));
			inforBranch.setBankCityId(jsonArray.getJSONObject(i).getJSONObject("metaData").getInt("bank_city_id"));

			// inforBranch.setBank_name_id(jsonArray.getJSONObject(i).getJSONObject("metaData").get("bank_name_id").toString());

			bank.setType(jsonArray.getJSONObject(i).getString("type"));
			bank.setName(jsonArray.getJSONObject(i).getString("name"));
			bank.setValue(jsonArray.getJSONObject(i).getString("value"));
			bank.setParentValue(jsonArray.getJSONObject(i).getString("parentValue"));
			bank.setInforBranch(inforBranch);
			danhSachNganHang.add(bank);
		}

		return danhSachNganHang;
	}

	public ThongTinTaiKhoanDangKy getAccountInfor(String text, JSONObject jsonObject, Model model) {
		ThongTinTaiKhoanDangKy thongTinTaiKhoanDangKy = new ThongTinTaiKhoanDangKy();

		try {
			if (jsonObject.getBoolean("success") == true) {
				if (!jsonObject.getJSONObject("data").isEmpty()) {
					thongTinTaiKhoanDangKy.setAccountName(jsonObject.getJSONObject("data").getString("accountName"));
					thongTinTaiKhoanDangKy.setError("false");
					thongTinTaiKhoanDangKy.setMessage("");
				} else {
					thongTinTaiKhoanDangKy.setError("true");
					thongTinTaiKhoanDangKy.setMessage(jsonObject.getJSONObject("error").getString("message"));
				}

			} else {
				thongTinTaiKhoanDangKy.setError("true");
				thongTinTaiKhoanDangKy.setMessage(jsonObject.getJSONObject("error").getString("message"));
			}
		} catch (Exception e) {
			thongTinTaiKhoanDangKy.setError("true");
			thongTinTaiKhoanDangKy.setMessage("Lỗi kiểm tra tài khoản");
		}

		return thongTinTaiKhoanDangKy;
	}

	public String convertDate(String time) throws ParseException {
		try {
			Date date = new Date(time);
			int ngay = date.getDate();
			int month = date.getMonth() + 1;
			int year = date.getYear() + 1900;
			String dob = "" + ngay + "/" + month + "/" + year + "";

			return dob;
		} catch (Exception e) {
		}
		return "";
	}

	public ThongTinNguoiDungTuLOS getInfor(String text, JSONObject jsonObject, Model model) throws ParseException {

		ThongTinNguoiDungTuLOS thongTinNguoiDungTuLOS = new ThongTinNguoiDungTuLOS();

		if (jsonObject.has("body") && jsonObject.getJSONObject("body").has("status") && jsonObject.getJSONObject("body").get("status").equals("OK")) {
			String ngaysing = "";
			String ngaycap = "";
			try {
				ngaysing = convertDate(jsonObject.getJSONObject("body").getJSONObject("dataRes").getString("dob").substring(0, 8));
				ngaycap = convertDate(jsonObject.getJSONObject("body").getJSONObject("dataRes").getString("issueDate").substring(0, 9));
			} catch (Exception e) {
			}

			thongTinNguoiDungTuLOS.setFullName(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("fullName").toString());
			thongTinNguoiDungTuLOS.setGenderName(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("genderName").toString());
			thongTinNguoiDungTuLOS.setCity(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("city").toString());
			thongTinNguoiDungTuLOS.setPrimaryPhone(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("primaryPhone").toString());
			thongTinNguoiDungTuLOS.setCurrentAddress(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("currentAddress").toString());
			thongTinNguoiDungTuLOS.setPermanentAddress(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("permanentAddress").toString());
			thongTinNguoiDungTuLOS.setDob(ngaysing);
			thongTinNguoiDungTuLOS.setMaritalStatus(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("maritalStatus").toString());
			thongTinNguoiDungTuLOS.setPrimaryPhone(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("primaryPhone").toString());
			thongTinNguoiDungTuLOS.setEmail(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("email").toString());
			thongTinNguoiDungTuLOS.setIncome(convertIncome(jsonObject.getJSONObject("body").getJSONObject("dataRes").get("income").toString()));
			thongTinNguoiDungTuLOS.setIssueDate(ngaycap);
		}

		return thongTinNguoiDungTuLOS;
	}

	private String convertIncome(String string) {
		try {
			return string.replaceAll("[^0-9]", "");
		} catch (Exception e) {
		}
		return "";
	}

	public ThongTinNguoiDungTuLOS layThongTinnguoiDungTuLOS(String caseId, HttpServletRequest req) {
		long time1 = System.currentTimeMillis();
		JSONObject jsonObjectReq = taoThongTinReq(caseId);
		LOGGER.info ("layThongTinnguoiDungTuLOS req: {}", jsonObjectReq.toString());
		String url = "https://gwextdev.seabank.com.vn/seabank/seabank-external/api/v1/seapartner/ptf-los/process";
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(jsonObjectReq.toString(), "UTF-8");
			request.addHeader("Content-Type", "application/json");
			request.addHeader("X-IBM-Client-Id", "06d0f204ca09575fe57828b8d9525421");
			request.addHeader("X-IBM-Client-Secret", "d7652fb681ac3a19e3851c1fff1d7b3d");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			
			LOGGER.info("layThongTinnguoiDungTuLOS resp: {}", responseString);
			
			if (responseString == null) responseString = "{}";
			long code = 500;
			try {
				JSONObject jsonObject3 = new JSONObject(responseString);
				if (jsonObject3.getJSONObject("body").get("status").equals("OK")) code = Long.parseLong(jsonObject3.getJSONObject("body").getString("response_code"));
				else if (jsonObject3.getJSONObject("body").get("status").equals("FAILE")) code = Long.parseLong(jsonObject3.getJSONObject("error").getString("code"));
			} catch (Exception e) {
			}
			
			luuLogApiSeaBank(time1, code, url, "POST", responseString, req, jsonObjectReq.toString(), "api lấy thông tin từ los");
			
			ThongTinNguoiDungTuLOSResp thongTinNguoiDungTuLOSResp = new Gson().fromJson(responseString, ThongTinNguoiDungTuLOSResp.class);

			if (StringUtils.isEmpty(thongTinNguoiDungTuLOSResp.getBody().getResponse_code())) {
				return null;
			}

			if (!thongTinNguoiDungTuLOSResp.getBody().getResponse_code().equals("00")) {
				return null;
			}

			if (StringUtils.isEmpty(thongTinNguoiDungTuLOSResp.getBody().getDataRes().getFullName()))
				return null;

			return thongTinNguoiDungTuLOSResp.getBody().getDataRes();
		} catch (Exception e) {
			String msg = "{\"errorMsg\":\""+e.getMessage()+"\"}";
			luuLogApiSeaBank(time1, 501, url, "POST", msg, req, jsonObjectReq.toString(), "api lấy thông tin từ los");
			
			LOGGER.error("layThongTinnguoiDungTuLOS error: {}", e);
		}

		return null;
	}

	private JSONObject taoThongTinReq(String caseId) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("reqType", "REQUEST");
		jsonObject.put("api", "PTF_LOS_API");
		jsonObject.put("apiKey", "qmklfoni1ezxlf2ckpygpfx122020");
		jsonObject.put("priority", "3");
		jsonObject.put("channel", "PTF_LOS");
		jsonObject.put("subChannel", "PTF");
		jsonObject.put("location", "10.9.12.90");
		jsonObject.put("context", "PC");
		jsonObject.put("trusted", "false");
		jsonObject.put("userID", "hieu.lt5");
		jsonObject.put("requestAPI", "t24Server");
		jsonObject.put("requestNode", "10.9.10.14");
		jsonObject.put("sync", "true");

		JSONObject jsonObjectBody = new JSONObject();
		jsonObjectBody.put("command", "GET_ENQUIRY");

		JSONObject jsonObjectEnquiry = new JSONObject();
		jsonObjectEnquiry.put("authenType", "getCustInfo");
		jsonObjectEnquiry.put("caseId", caseId);
		jsonObjectEnquiry.put("requestDate", simpleDateFormat.format(new Date()));

		jsonObjectBody.put("enquiry", jsonObjectEnquiry);

		JSONObject jsonObjectReq = new JSONObject();
		jsonObjectReq.put("header", jsonObject);
		jsonObjectReq.put("body", jsonObjectBody);

		return jsonObjectReq;
	}

	public String luuLogApiSeaBank(long time1, long status, String uri, String method, String resp,
			HttpServletRequest req, String params, String describe) {
		try {
			long time2 = System.currentTimeMillis();
			long timeHandling = time2 - time1;

			LogApiSeaBank logApiSeaBank = new LogApiSeaBank();
			logApiSeaBank.setLogId(UUID.randomUUID().toString());
			logApiSeaBank.setTimeHandling(timeHandling);
			logApiSeaBank.setDate(new Date());
			logApiSeaBank.setStatus(status);
			try {
				ParamsKbank paramsKbank = getParams(req);
				EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(paramsKbank.getSoCmt(), paramsKbank.getMatKhau());
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
	
	public String luuLogApiSeaBankCron(long time1, long status, String uri, String method, String resp,
			HttpServletRequest req, String params, String describe, EkycKyso ekycKyso, String username) {
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
			logApiSeaBank.setUsername(username);
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
	
	public ParamsKbank getParams(HttpServletRequest req) {
		return (ParamsKbank) req.getSession().getAttribute("params");
	}
}
