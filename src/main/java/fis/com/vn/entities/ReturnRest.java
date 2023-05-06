/**
 * 
 */
package fis.com.vn.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author ChinhVD4
 *
 */

public class ReturnRest {
	public static String convertJson(RespApi respApi) {
		String str = new Gson().toJson(respApi); 
		return str;
	}
	public static String success() {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THANH_CONG);
		respApi.setMessage("Thành công");
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String success(Object data) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THANH_CONG);
		respApi.setMessage("Thành công");
		respApi.setData(data);
		String str = getGson().toJson(respApi); 
		return str;
	}
	
	public static String successFullDate(Object data) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THANH_CONG);
		respApi.setMessage("Thành công");
		respApi.setData(data);
		String str = getGsonFullDate().toJson(respApi); 
		return str;
	}
	
	public static String successNull(Object data) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THANH_CONG);
		respApi.setMessage("Thành công");
		respApi.setData(data);
		String str = getGsonNull().toJson(respApi); 
		return str;
	}
	public static String success205(Object data, Object included) {
		RespApi respApi = new RespApi();
		respApi.setStatus(205);
		respApi.setMessage("Thành công");
		respApi.setData(data);
		respApi.setIncluded(included);
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String success(Object data, Object included) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THANH_CONG);
		respApi.setMessage("Thành công");
		respApi.setData(data);
		respApi.setIncluded(included);
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String error(Object data, String message) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THAT_BAI);
		respApi.setMessage(message);
		respApi.setData(data);
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String error500(Object data, String message) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.LOI_HE_THONG);
		respApi.setMessage(message);
		respApi.setData(data);
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String error(Object data, String message, Object included ) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.THAT_BAI);
		respApi.setMessage(message);
		respApi.setData(data);
		respApi.setIncluded(included);
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String errorValidate(Object data, String message) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.LOI_NHAN_DANG);
		respApi.setMessage(message);
		respApi.setData(data);
		
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String errorValidate(Object data, String message, Object included) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.LOI_NHAN_DANG);
		respApi.setMessage(message);
		respApi.setData(data);
		respApi.setIncluded(included);
		
		String str = getGson().toJson(respApi); 
		return str;
	}
	public static String notData(Object data, String message) {
		RespApi respApi = new RespApi();
		respApi.setStatus(HttpStatusApi.KHONG_CO_DU_LIEU);
		respApi.setMessage(message);
		respApi.setData(data);
		
		String str = getGson().toJson(respApi); 
		return str;
	}
	private static Gson getGson() {
		return new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
	}
	private static Gson getGsonFullDate() {
		return new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
	}
	private static Gson getGsonNull() {
		Gson gson = new GsonBuilder()
		        .serializeNulls()
		        .setDateFormat("dd/MM/yyyy")
		        .create();
		return gson;
	}
	public static String success206() {
		RespApi respApi = new RespApi();
		respApi.setStatus(206);
		respApi.setMessage("Thành công");
		String str = getGson().toJson(respApi); 
		return str;
	}
}
