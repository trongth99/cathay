package fis.com.vn.common;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Common {
	public static Boolean dungMaGiaoDich(String maGiaDich) {
		try {
			if(maGiaDich.length() != 58) return false;
			String[] arr = maGiaDich.split("-");
			String timeStr = arr[arr.length-2] + arr[0] + arr[arr.length-1];
			long time = Long.valueOf(timeStr.replaceAll("[^0-9]+", ""));
			
			long rangeTime = time - System.currentTimeMillis();
			
			if(rangeTime >= 24*60*60*1000) return false;
			
			if(time > System.currentTimeMillis()) return true;
		} catch (Exception e) {
		}
		return false;
	}
	public static String layMaGiaoDich() {
		return layMaGiaoDich(1);
	}
	public static String layMaGiaoDich(int day) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, day);
			String timeStr = String.valueOf(calendar.getTimeInMillis());
			String timeStrOne = timeStr.substring(0, 4);
			String timeStrTwo = timeStr.substring(4, 8);
			String timeStrThree = timeStr.substring(8, timeStr.length());
			String maGiaDich = ranDomChar(timeStrTwo)+"-"+UUID.randomUUID().toString()+"-"+ranDomChar(timeStrOne)+"-"+ranDomChar(timeStrThree);
			
			return maGiaDich;
		} catch (Exception e) {
		}
		return null;
	}
	private static String ranDomChar(String timeStr) {
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'a');
		char c1 = (char)(r.nextInt(26) + 'a');
		return String.valueOf(c1)+timeStr+String.valueOf(c);
	}
	public static String getMD5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

			return myHash;
		} catch (Exception e) {
		}
		return null;
	}
	public static <T> void updateObjectToObject(T source, T objectEdit) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, objectEdit);
	}
	@SuppressWarnings("resource")
	public static String encodeFileToBase64Binary(File file) throws Exception{
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.getEncoder().encode(bytes));
    }
}
