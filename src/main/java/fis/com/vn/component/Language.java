package fis.com.vn.component;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import fis.com.vn.common.StringUtils;

@Component
public class Language {
	@Autowired private HttpServletRequest request;
	@Autowired private MessageSource messageSource;
	
	public String getMessage(String keyMessage, String language) {
		String keyMessageAlias = keyMessage.trim().replaceAll("[^a-z _]+", "").replaceAll("[ ]+", "_");
		String message = keyMessage;
		try {
			if(StringUtils.isEmpty(language)) {
				message = messageSource.getMessage(keyMessageAlias, null, new Locale("vi"));
			} else {
				message = messageSource.getMessage(keyMessageAlias, null, new Locale(language));
			}
		} catch (Exception e) {
		}
		return message;
	}
	
	public String getMessage(String keyMessage) {
		return getMessage(keyMessage, getLanguage());
	}
	public String getLanguage() {
		try {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			 if (localeResolver instanceof CookieLocaleResolver) {
			    CookieLocaleResolver cookieLocaleResolver = (CookieLocaleResolver) localeResolver;
			    Cookie cookie = WebUtils.getCookie(request, cookieLocaleResolver.getCookieName());
			    return cookie.getValue();
			}
		} catch (Exception e) {
		}
	    return "vi";
	}
}
