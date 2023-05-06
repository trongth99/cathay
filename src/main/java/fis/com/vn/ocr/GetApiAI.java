package fis.com.vn.ocr;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.component.ConfigProperties;
import fis.com.vn.cron.CheckServiceAi;

@Component
public class GetApiAI {
	@Autowired ConfigProperties configProperties;
	@Autowired CheckServiceAi checkServiceAi;
	
	int soSanh = 0;
	int liveness = 0;
	int ocr = 0;
	int ocrKbank = 0;
	int ocrFull = 0;
	
	public synchronized String getUrlOcr() {
		try {
			ArrayList<String> listUrlOcr = checkServiceAi.urlOcr(null, null);
			
			if(ocr >= listUrlOcr.size()) ocr = 0;
			
			String url = listUrlOcr.get(ocr);
			ocr ++;
			return url;
		} catch (Exception e) {
		}
		return null;
	}
	
	public synchronized String getUrlLiveness() {
		try {
			ArrayList<String> listUrlLiveness = checkServiceAi.urlLiveness(null, null);
			
			if(liveness >= listUrlLiveness.size()) liveness = 0;
			
			String url = listUrlLiveness.get(liveness);
			liveness ++;
			return url;
		} catch (Exception e) {
		}
		return null;
	}
	
	public synchronized String getUrlSoSanhAnh() {
		try {
			ArrayList<String> listUrlCompare = checkServiceAi.urlCompare(null, null);
			
			if(soSanh >= listUrlCompare.size()) soSanh = 0;
			
			String url = listUrlCompare.get(soSanh);
			soSanh ++;
			return url;
		} catch (Exception e) {
		}
		return null;
	}
}
