package fis.com.vn.captcha;

import org.springframework.stereotype.Component;

import fis.com.vn.common.CommonUtils;

@Component
public class ValidCaptcha {
	
	public Boolean isValid(String token, String valueCaptcha) {
		return checkCaptcha(token, valueCaptcha);
	}
	
	public static Boolean checkCaptcha(String token, String maCaptcha) {
		if(token.equals(CommonUtils.getMD5(maCaptcha))) return true;
		
		return false;
	}
}
