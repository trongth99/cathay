package fis.com.vn;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import fis.com.vn.api.LogService;
import fis.com.vn.component.Language;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.HttpStatusApi;
import fis.com.vn.entities.RespApi;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.NotCheckException;
import fis.com.vn.exception.OcrException;
import fis.com.vn.exception.ValidException;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler  implements ResponseBodyAdvice<Object>{
	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	@Autowired LogService logService;
	@Autowired Language language;
	
	@ExceptionHandler(value = { ValidException.class })
	protected ResponseEntity<RespApi> validException(ValidException ex, WebRequest request, HttpServletRequest req, HandlerMethod handlerMethod) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THAT_BAI);
		respApi.setMessage(language.getMessage(ex.getMessage()));
		return new ResponseEntity<>(respApi, HttpStatus.OK);
	}
	
	@ExceptionHandler(value = { CheckException.class })
	protected ResponseEntity<RespApi> checkException(CheckException ex, WebRequest request, HttpServletRequest req, HandlerMethod handlerMethod) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.LOI_NHAN_DANG);
		respApi.setMessage(ex.getMessage());
		return new ResponseEntity<>(respApi, HttpStatus.OK);
	}
	
	@ExceptionHandler(value = { NotCheckException.class })
	protected ResponseEntity<RespApi> notCheckException(NotCheckException ex, WebRequest request, HttpServletRequest req, HandlerMethod handlerMethod) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THANH_CONG);
		respApi.setMessage(Contains.NOT_CHECK_MESSAGE);
		return new ResponseEntity<>(respApi, HttpStatus.OK);
	}
	@ExceptionHandler(value = { OcrException.class })
	protected ResponseEntity<RespApi> ocrException(OcrException ex, WebRequest request, HttpServletRequest req, HandlerMethod handlerMethod) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.KHONG_CO_DU_LIEU);
		respApi.setMessage(ex.getMessage());
		return new ResponseEntity<>(respApi, HttpStatus.OK);
	}
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<RespApi> exception(Exception ex, WebRequest request, HttpServletRequest req, HttpServletResponse resp, HandlerMethod handlerMethod) {
		Class controllerClass = handlerMethod.getMethod().getDeclaringClass();
		RespApi respApi = new RespApi();
		ex.printStackTrace();
		LOGGER.error(controllerClass.getSimpleName()+": "+ex.getMessage());
		respApi.setStatus(HttpStatusApi.LOI_HE_THONG);
		respApi.setMessage(language.getMessage("Lỗi hệ thống"));
		return new ResponseEntity<>(respApi, HttpStatus.OK);
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		logService.logResponse(body, request, response);
		return body;
	}
}
