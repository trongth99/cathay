package fis.com.vn.cron;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import fis.com.vn.component.ConfigProperties;
import fis.com.vn.repository.ConfigRepository;
import fis.com.vn.table.Config;

@Component
public class CheckServiceAi {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckServiceAi.class);
	@Autowired ConfigProperties configProperties;
	@Autowired ConfigRepository configRepository;
	
	ArrayList<String> listUrlOcr = new ArrayList<>();
	ArrayList<String> listUrlLiveness = new ArrayList<>();
	ArrayList<String> listUrlCompare = new ArrayList<>();
	
	@PostConstruct
    private void postConstruct() {
		start();
    }
	
	public synchronized ArrayList<String> urlOcr(String type, ArrayList<String> ocrs) {
		if(type != null && type.equals("add")) {
			listUrlOcr = ocrs;
		}
		return listUrlOcr;
	}
	
	public synchronized ArrayList<String> urlLiveness(String type, ArrayList<String> liveness) {
		if(type != null && type.equals("add")) {
			listUrlLiveness = liveness;
		}
		return listUrlLiveness;
	}
	
	public synchronized ArrayList<String> urlCompare(String type, ArrayList<String> compares) {
		if(type != null && type.equals("add")) {
			listUrlCompare = compares;
		}
		return listUrlCompare;
	}
	
	public void start() {
		Config configOcr = configRepository.findByMa("link_ocr_cmt_cccd_fis");
		
		if(configOcr != null) {
			String[] arrOcr = configOcr.getGiaTri().split(",");
			ArrayList<String> ocrs = new ArrayList<>();
			for (String url : arrOcr) {
				url = url.trim();
				boolean checkOcr = checkService(url, "Nice to meet you!");
				if(checkOcr)
					ocrs.add(url);
			}
			urlOcr("add", ocrs);
			
			LOGGER.info("List ocr: {}", new Gson().toJson(ocrs));
		}
		
		Config configComapre = configRepository.findByMa("link_compare_face");

		if(configComapre != null) {
			String[] arrCompare = configComapre.getGiaTri().split(",");
			ArrayList<String> compares = new ArrayList<>();
			for (String url : arrCompare) {
				url = url.trim();
				boolean check = checkService(url, "Face services ! Nice to meet you!");
				if(check) {
					compares.add(url);
				}
			}
			urlCompare("add", compares);
			
			LOGGER.info("List compares: {}", new Gson().toJson(compares));
		}
		
		Config configLiveness = configRepository.findByMa("link_liveness_face");
		
		if(configLiveness != null) {
			String[] arrLiveness = configLiveness.getGiaTri().split(",");
			ArrayList<String> liveness = new ArrayList<>();
			for (String url : arrLiveness) {
				url = url.trim();
				boolean check = checkService(url, "Face services ! Nice to meet you!");
				if(check) {
					liveness.add(url);
				}
			}
			
			urlLiveness("add", liveness);
			
			LOGGER.info("List liveness: {}", new Gson().toJson(liveness));
		}
	}
	
	int  timeOutRequest = 8*1000;
	private  RequestConfig.Builder timeout() {
		RequestConfig.Builder requestConfig = RequestConfig.custom();
		requestConfig.setConnectTimeout(timeOutRequest);
		requestConfig.setConnectionRequestTimeout(timeOutRequest);
		requestConfig.setSocketTimeout(timeOutRequest);
		
		return requestConfig;
	}
	private boolean checkService(String url, String textCompare) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet uploadFile = new HttpGet(url.replaceAll("/api/[\\w\\W]*", ""));
			uploadFile.setConfig(timeout().build());
	
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			String text = IOUtils.toString(responseEntity.getContent(), StandardCharsets.UTF_8.name());
			System.out.println(text);
			if(text.equals(textCompare)) return true;
		} catch (Exception e) {
		}
		return false;
	}
}
