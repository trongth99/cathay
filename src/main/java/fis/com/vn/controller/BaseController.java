package fis.com.vn.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.component.Language;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.entities.RespApi;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.table.UserModule;

@Controller
public class BaseController {
	@Value("${TOKEN}")
	protected String token;
	
	@Value("${CODE}")
	protected String code;
	
	@Value("${API_SERVICE}")
	protected
	String API_SERVICE;
	
	@Value("${KY_SO_FOLDER}")
	protected
	String KY_SO_FOLDER;
	
	@Autowired Language language;

	@SuppressWarnings("unchecked")
	public boolean isAllowUrlContains(HttpServletRequest req, String uri) {
		List<UserModule> danhMucQuyens = (List<UserModule>) req.getSession().getAttribute("userModuleMenus");
		
		boolean checkPermission = false;
        for (UserModule tbDanhMucQuyen : danhMucQuyens) {
            if (tbDanhMucQuyen.getUrl() != null && uri.equals(tbDanhMucQuyen.getUrl()) && tbDanhMucQuyen.getParentId() != 0) {
                checkPermission = true;
            }
        }
        return checkPermission;
	}
	
	public void setSession (Object object, String name, HttpServletRequest req) {
		 req.getSession().setAttribute(name, object);
	}
	public Object getSession (String name, HttpServletRequest req) {
		return req.getSession().getAttribute(name);
	}
	
	public Boolean isSuperAdmin(HttpServletRequest req) {
		String userName = getUserName(req);
		if(userName.equals("supper_admin")) return true;
		
		return false;
	}
	
	public String getStringParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return allParams.get(nameParam);
	}
	public String getParamsQuery(Map<String, String> allParams) {
		if (StringUtils.isEmpty(allParams.get("paramsQuery"))) {
			return "";
		}
		return allParams.get("paramsQuery");
	}
	public Long getLongParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return Long.valueOf(allParams.get(nameParam));
	}

	public Integer getIntParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return Integer.valueOf(allParams.get(nameParam));
	}

	private static final List<String> EXTENSIONS = Arrays.asList(".doc", ".docx", ".xls", ".xlsx");
	
	public void forwartParams(Map<String, String> allParams, Model model) {
		for (Entry<String, String> entry : allParams.entrySet()) {
			model.addAttribute(entry.getKey(), entry.getValue());
		}
	}
	
	public void forwartParams(Map<String, String> allParams, Model model, HttpServletRequest req) {
		for (Entry<String, String> entry : allParams.entrySet()) {
			model.addAttribute(entry.getKey(), entry.getValue());
		}
		model.addAttribute("uri", "/"+req.getRequestURI().split("/")[1]);
	}

	public <T> String checkErrorMessage(T obj) throws ErrorException {
		String errorStr = "";
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(obj);
		if (violations.size() > 0) {
			for (ConstraintViolation<T> constraintViolation : violations) {
				errorStr += constraintViolation.getMessage() + "<br/>";
			}
			throw new ErrorException(errorStr);
		}

		return errorStr;
	}

	public long getUserId(HttpServletRequest req) {
		return (Long) req.getSession().getAttribute("userid");
	}
	public long getKhachHangId(HttpServletRequest req) {
		return (Long) req.getSession().getAttribute("khachHangId");
	}
	public String getMaKhachHang(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("maKhachHang");
	}
	public String getUserName(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("username");
	}
	public String getKhuVuc(HttpServletRequest req) {
		return (String) req.getSession().getAttribute("khuVuc");
	}
	public String queryStringBuilder(Map<String, String> allParams) {
		return getParamsQuery(allParams);
	}

	public String queryStringBuilderAll(Map<String, String> allParams) {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : allParams.entrySet()) {
			builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}

		return builder.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void updateMapToObject(Map<String, String> params, T source, Class cls) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Object overrideObj = mapper.convertValue(params, cls);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, overrideObj);
	}

	public <T> void updateObjectToObject(T source, T objectEdit) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, objectEdit);
	}
	
	public <T> void updateObjectToObjectNull(T source, T objectEdit) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.updateValue(source, objectEdit);
	}

	public Date convertStringToDate(String format, String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(strDate);
		} catch (ParseException e) {
		}
		return null;
	}

	public Long convertStringToLong(String format, String strDate) {
		if (strDate == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(strDate).getTime();
		} catch (ParseException e) {
		}
		return null;
	}

	public static boolean isValidFormat(String format, String value, Locale locale) {
		LocalDateTime ldt = null;
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

		try {
			ldt = LocalDateTime.parse(value, fomatter);
			String result = ldt.format(fomatter);
			return result.equals(value);
		} catch (DateTimeParseException e) {
			try {
				LocalDate ld = LocalDate.parse(value, fomatter);
				String result = ld.format(fomatter);
				return result.equals(value);
			} catch (DateTimeParseException exp) {
				try {
					LocalTime lt = LocalTime.parse(value, fomatter);
					String result = lt.format(fomatter);
					return result.equals(value);
				} catch (DateTimeParseException e2) {
					// Debugging purposes
					// e2.printStackTrace();
				}
			}
		}

		return false;
	}

	public String getSort(Map<String, String> allParams) {
		if (StringUtils.isEmpty(allParams.get("sort"))) {
			return "desc";
		}
		return allParams.get("sort");
	}

	public Pageable getPageable(Map<String, String> allParams, Paginate paginate) {

		Pageable pageable;
		String order = "id";
		if (!StringUtils.isEmpty(allParams.get("order"))) {
			order = allParams.get("order");
		}
		Sort sort;
		if (getSort(allParams).equals("desc")) {
			sort = Sort.by(order).descending();
		} else {
			sort = Sort.by(order).ascending();
		}
		pageable = PageRequest.of(paginate.getPage() - 1, paginate.getLimit(), sort);
		return pageable;
	}

	boolean allowExtension(String fileName) {
		for (String ext : EXTENSIONS) {
			if (fileName.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
	public String postRequest(String data, String url, ParamsKbank params) {
		return postRequest(data, url, token, code, params);
	}
	
	public String postRequest(String data, String url, String tokenApi, String codeApi, ParamsKbank paramsKbank) {
		RespApi respApi = new RespApi();
		try {
			JSONObject jsonObject = new JSONObject(data);
			jsonObject.put("soDienThoai",  paramsKbank.getSoDienThoai());
			jsonObject.put("hoVaTen",  paramsKbank.getHoVaTen());
			jsonObject.put("soCmt",  paramsKbank.getSoCmt());
			jsonObject.put("soHopDong",  paramsKbank.getSoHopDong());
			jsonObject.put("codeTransaction",  paramsKbank.getCodeTransaction());
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(API_SERVICE+url);
			StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
			request.addHeader("content-type", "application/json");
			request.addHeader("token", tokenApi);
			request.addHeader("code", codeApi);
			request.addHeader("code_transaction", paramsKbank.getCodeTransaction());
			request.addHeader("code-transaction", paramsKbank.getCodeTransaction());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
			respApi.setStatus(400);
			respApi.setMessage("Lỗi hệ thống");
		}
		return new Gson().toJson(respApi);
	}
	public String getRequest(String url) {
		return getRequest(url, token, code);
	}
	public String getRequest(String url, String tokenApi, String codeApi) {
		RespApi respApi = new RespApi();
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(API_SERVICE+url);
			request.addHeader("content-type", "application/json");
			request.addHeader("token", tokenApi);
			request.addHeader("code", codeApi);
			HttpResponse response = httpClient.execute(request);
			
			String responseString = new BasicResponseHandler().handleResponse(response);
			
			return responseString;
		} catch (Exception e) {
			respApi.setStatus(400);
			respApi.setMessage("Lỗi hệ thống");
		}
		return new Gson().toJson(respApi);
	}
}
