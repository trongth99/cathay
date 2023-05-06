/**
 * 
 */
package fis.com.vn.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fis.com.vn.contains.Contains;
import fis.com.vn.entities.ConfigParams;
import fis.com.vn.repository.ConfigRepository;
import fis.com.vn.table.Config;

/**
 * @author ChinhVD4
 *
 */
@Component
public class ConfigProperties {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigProperties.class);
	@Autowired ConfigRepository configRepository;
	ConfigParams configParams = new ConfigParams();
	@PostConstruct
    private void postConstruct() {
		khoiTaoConfigDb();
    }
	/**
	 * 
	 */
	public ConfigParams getConfig() {
		if(configParams.getVersion() == null) {
			khoiTaoConfigDb();
		}
		return configParams;
	}
	public synchronized void khoiTaoConfigDb() {
		List<Config> configs = configRepository.findByTrangThai(Contains.TT_CONFIG_HOATDONG);
		Map<String, String> mapConfig = configs.stream().collect(Collectors.toMap(o->o.getMa(), o->o.getGiaTri()));
		configParams = new ConfigParams();
		updateMapToObject(mapConfig, configParams, ConfigParams.class);
	}
	/**
	 * 
	 */
	public void resetConfig() {
		khoiTaoConfigDb();
		LOGGER.info("RESET LIST CONFIG SUCCESS: "+new Date());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	<T> void updateMapToObject(Map<String, String> params, T source, Class cls) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Object overrideObj = mapper.convertValue(params, cls);
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			mapper.updateValue(source, overrideObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
