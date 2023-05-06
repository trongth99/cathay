package fis.com.vn.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fis.com.vn.api.entities.Params;
import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.Language;
import fis.com.vn.contains.Contains;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;

@Controller
public class BaseApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseApi.class);
	@Autowired ConfigProperties configProperties;
	
	@Autowired Language language;
	@Autowired KhachHang khachHangBase;
	@Autowired LogService logService;
	
	public String getMessage(String keyMessage) {
		return language.getMessage(keyMessage);
	}
	
	public String layMaKhachHang(HttpServletRequest req) {
		return req.getHeader(Contains.MA_TO_CHUC_HEADER);
	}
	
	public String layMaGiaoDich(HttpServletRequest req) {
		return logService.layMaGiaoDichHd(req);
	}
	
	public Params getParamsFromInputstream(HttpServletRequest req) throws ValidException , Exception, CheckException {
		long time1 = System.currentTimeMillis();
		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		long time2 = System.currentTimeMillis();
		
		System.out.println("Time IO: "+ (time2-time1));
		
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		Params params = null;
		try {
			params = gson.fromJson(text.trim(), Params.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		
		if(params == null) throw new ValidException("Định dạng nội dung truyền lên không đúng");
		
		Gson gsonLog = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		LOGGER.info("Params: "+gsonLog.toJson(params));
		params.setParams(gsonLog.toJson(params));
		
		String maKhachHang = layMaKhachHang(req);
		String imgFolderLog = configProperties.getConfig().getImage_folder_log()+maKhachHang+"/";
		params.setLogVideo("true");
		if(khachHangBase.getCoLuuLogAnh(maKhachHang))
			params.logImage("true", imgFolderLog, "false", params);
		
		req.setAttribute("params", params.getParams());
		req.setAttribute("images", params.getImages());
		
		return params;
	}
	public String readInputStream(InputStream inputStream) {
		try {
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer, "UTF-8");
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public String getStrParamsFromInputstream(HttpServletRequest req) throws IOException {
		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		return text.trim();
	}
	
	public String getStringParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return allParams.get(nameParam);
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
	public String getSort(Map<String, String> allParams) {
		if (allParams.get("sort") == null) {
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
}
