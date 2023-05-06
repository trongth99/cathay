package fis.com.vn.ocr;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fis.com.vn.component.ConfigProperties;

@Component
public class CallApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CallApi.class);
	int timeOutRequest = 2*60*1000;
	@Autowired ConfigProperties configProperties;
	@Autowired GetApiAI getApiAI;
	
	public String sendRequest(String base64Image, String url) {
		try {
			byte[] byteImage = Base64.getDecoder().decode(base64Image);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpPost uploadFile = new HttpPost(url);
			uploadFile.setConfig(timeout().build());

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			builder.addBinaryBody("file", byteImage, ContentType.MULTIPART_FORM_DATA, "abc.jpg" );
			builder.addTextBody("check", "{ \"check_photocopied\": true, \"check_corner_cut\": true, \"check_emblem\": true, \"check_embossed_stamp\": true, \"check_avatar\": true, \"check_replacement_avatar\": true, \"check_recaptured\": true, \"check_exprity_date\": true, \"check_red_stamp\": true, \"check_embossed_stamp\": true, \"check_rfp\": true, \"check_lfp\": true, \"check_glare\": true, \"check_frame\":true }", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			return text;
		} catch (Exception e) {
			LOGGER.error("RQ: {}", e.getMessage());
		}
		return null;
	}
	
	public String requestCmtCccd(String base64Image) {
		String url = getApiAI.getUrlOcr();
		return sendRequest(base64Image, url);
	}
	
	
	public String postRequest(String data, String url) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(data, "UTF-8");
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			
			LOGGER.info(responseString);
			
			return responseString;
		} catch (Exception e) {
			LOGGER.error("RQ: {}", e.getMessage());
		}
		return null;
	}
	public String postRequestOcrAsync(MultipartFile file, String url, String ocrType, String ocrKey) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			HttpPost uploadFile = new HttpPost(url);
			uploadFile.setConfig(timeout().build());

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			builder.addBinaryBody("file", file.getBytes(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename() );
			builder.addTextBody("ocr_type", ocrType, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addTextBody("ocr_key", ocrKey, ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			builder.addTextBody("file_type", file.getOriginalFilename().toLowerCase().endsWith("pdf")?"PDF":"IMAGE", ContentType.TEXT_PLAIN.withCharset(Charset.forName("utf-8")));
			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			JSONObject jsonObject = new JSONObject(text);
			if(jsonObject.getInt("error") == 0) return jsonObject.getString("result");
		} catch (Exception e) {
			LOGGER.error("RQ: {}", e.getMessage());
		}
		return null;
	}
	private RequestConfig.Builder timeout() {
		RequestConfig.Builder requestConfig = RequestConfig.custom();
		requestConfig.setConnectTimeout(timeOutRequest);
		requestConfig.setConnectionRequestTimeout(timeOutRequest);
		requestConfig.setSocketTimeout(timeOutRequest);
		
		return requestConfig;
	}
}
