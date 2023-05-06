package fis.com.vn.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ValidException;
import fis.com.vn.ocr.Ocr;

public class Utils {
	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
	
	public static String randomPw() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 16;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();

		return generatedString;
	}
	
	public static String formatNumberDot(String number) {
		try {
			if(StringUtils.isEmpty(number)) return "";
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
	        decimalFormat.setGroupingUsed(true);
	        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.JAPAN));
	        decimalFormat.setGroupingSize(3);
	        
	       return decimalFormat.format(Long.valueOf(number)) + " VNĐ";
		} catch (Exception e) {
		}
		
		return number+" VNĐ";
	}
	
	public static String formatNumberDotVND(String number) {
		try {
			if(StringUtils.isEmpty(number)) return "";
			DecimalFormat decimalFormat = new DecimalFormat("#.###");
	        decimalFormat.setGroupingUsed(true);
	        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.JAPAN));
	        decimalFormat.setGroupingSize(3);
	        
	       return decimalFormat.format(Long.valueOf(number)) + " VNĐ";
		} catch (Exception e) {
		}
		
		return number+" VNĐ";
	}
	
	public static boolean isValidDate(String dateStr, String dateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
	
	public static String capitalizeFirstLetter(String str) {
	    if(str == null || str.isEmpty()) {
	        return str;
	    }
	    String words[]=str.toLowerCase().split("\\s");
        String capitalizeStr="";
 
        for(String word:words){
            // Capitalize first letter
            String firstLetter=word.substring(0,1);
            // Get remaining letter
            String remainingLetters=word.substring(1);
            capitalizeStr+=firstLetter.toUpperCase()+remainingLetters+" ";
        }
        System.out.println(capitalizeStr);
	    return capitalizeStr;
	}
	public static String formatNumber(String numberStr) {
		return numberStr.replaceAll("[.]+([0-9]{1,2})$", ",$1");
	}
	
	public static LinkedHashMap<String, JSONObject> sortkeys(JSONObject jsonObject) {
		Iterator<String> it= jsonObject.keys();
		ArrayList<Integer> keys = new ArrayList<Integer>();
		LinkedHashMap<String, JSONObject> map = new LinkedHashMap<String, JSONObject>();
		while (it.hasNext()) {
		    keys.add(Integer.valueOf(it.next()));
		}
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
		    String key = String.valueOf(keys.get(i));
		    map.put(key, jsonObject.getJSONObject(key));
		}
		return map;
	}
	public static Date convertStringToDate(String strDate) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			return dateFormat.parse(strDate);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static Long convertPhone(String phone) {
		try {
			phone = phone.substring(1);
			
			return Long.valueOf("84"+phone);
		} catch (Exception e) {
		}
		return 0L;
	}
	
	/**
	 * @param ocr
	 * @throws ValidException 
	 */
	public static void kiemTraHanSuDungGiayTo(Ocr ocr) throws CheckException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if(!StringUtils.isEmpty(ocr.getNgayHetHan())) {
			long tghetHanms = 0;
			try {
				tghetHanms = dateFormat.parse(ocr.getNgayHetHan()).getTime();
			} catch (Exception e) {
			}
			if(tghetHanms !=0 && tghetHanms < System.currentTimeMillis()) {
				throw new CheckException("Giấy tờ đã hết hạn sử dụng");
			}
		} else {
			if(!StringUtils.isEmpty(ocr.getNgayCap())) {
				long tghetHanms = 0;
				try {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dateFormat.parse(ocr.getNgayCap()));
					calendar.add(Calendar.YEAR, 15);
					tghetHanms = calendar.getTimeInMillis();
				} catch (Exception e) {
				}
				
				if(tghetHanms !=0 && tghetHanms < System.currentTimeMillis()) {
					throw new CheckException("Giấy tờ đã hết hạn  sử dụng");
				}
			}
		}
		
	}
	public static void updateHanSuDungGiayTo(Ocr ocr) {
		try {
			try {
				kiemTraHanSuDungGiayTo(ocr);
				ocr.getKiemTraMatTruoc().setNgayHetHan("0");
			} catch (Exception e) {
				ocr.getKiemTraMatTruoc().setNgayHetHan("1");
			}
		} catch (Exception e) {
		}
	}
	public static String alias(String str) {
		try {
			if(!StringUtils.isEmpty(str)) {
				String alias = org.apache.commons.lang3.StringUtils.stripAccents(str).trim().toLowerCase().replace("đ", "d").replace("Đ", "D");;
				
				return alias;
			}
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		}
		return str;
	}
	public static String alias2(String str) {
		try {
			if(!StringUtils.isEmpty(str)) {
				String alias = str.trim().toLowerCase();
				
				return alias.replaceAll("['\\\"-=+`]+", " ").replaceAll("[ ]+", " ").trim();
			}
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		}
		return str;
	}
	private static String convertNamSinh(ArrayList<String> arr) {
		for (String string : arr) {
			try {
				string = convertStrToDateStr(string.trim());
				if(Utils.checkPatterinStr(string, "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}") || string.length() == 4)
					return string;
			} catch (Exception e) {
			}
		}
		if(arr.size() > 0) return arr.get(0).trim();
		return "";
	}

	public static String convertStrToDateStr(String str) {
		if(!StringUtils.isEmpty(str)) {
			str = str.trim().replaceAll("[ ]+", "").replaceAll("[^0-9]+", "/").replaceAll("^[/]+", "").replaceAll("[/]+$", "");
			try {
				String strDate = Utils.getStrPatterFirst(str, "([0-9]{1,2}/[0-9]{1,2}/[0-9]{4})");
				if(!StringUtils.isEmpty(strDate))
					return strDate.trim();
			} catch (Exception e) {
			}
		}
		return str;
	}
	public static String convertName(String str) {
		if(!StringUtils.isEmpty(str)) {
			str = str.replaceAll("^[^a-zA-Z ]+", "").replaceAll("[^a-zA-Z ]+$", "").replaceAll("[ ]+", " ").trim();
			str = StringEscapeUtils.unescapeJava(str);
		}
		return str;
	}
	public static String xoaBoKyTuDacBiet(String str) {
		if(!StringUtils.isEmpty(str)) {
			str = str.replaceAll("[^\\p{L}\\p{Z}]", "").trim();
		}
		return str;
	}
	public static String convertListPhone(String str) {
		if(!StringUtils.isEmpty(str)) {
			str = str.replaceAll("[^0-9.,; ]+", "").replaceAll("[.,;]+", " ").trim().replaceAll("[ ]+", ",");
		}
		return str;
	}
	public static String convertIdCardNumber(ArrayList<String> arr) {
		for (String string : arr) {
			try {
				string = string.replaceAll("[^0-9]+", "").trim();
				if(string.length() == 9 || string.length() == 12) {
					return string;
				}
			} catch (Exception e) {
			}
		}
		if(arr.size() > 0) return arr.get(0).replaceAll("[^0-9]+", "").trim();
		return "";
	}
	private static String convertStringSms(String strMsg) {
		strMsg = strMsg.toLowerCase().replace("\\n", " ").
		replace("\n", " ")
		.replaceAll("[\\s]+", " ")
		.replaceAll("[\\|]+", " ")
		.replaceAll("[ ]+", " ")
		.replaceAll("loai[ 0-9]*giay[ 0-9]*to", ",maCungCap")
		.replaceAll("so giay to[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*giay to[ :.;,]*", "cmt:")
		.replaceAll("cmnd[ :.;,]*", "cmt:")
		.replaceAll("cccd[ :.;,]*", "cmt:")
		.replaceAll("so giay t0[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*giay[ 0-9]*to[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*giay[ :.;,]*", "cmt:")
		.replaceAll("be[ 0-9]*giay[ 0-9]*to[ :.;,]*", "cmt:")
		.replaceAll("the[ 0-9]*can[ 0-9]*cuoc[ :.;,]*", "cmt:")
		.replaceAll("the[ 0-9]*can[ 0-9]*cuce[ :.;,]*", "cmt:")
		.replaceAll("the[ 0-9]*can[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*gtay[ 0-9]*io[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*glay[ 0-9]*to[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*gisy[ 0-9]*to[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*qiay[ 0-9]*to[ :.;,]*", "cmt:")
		.replaceAll("so[ 0-9]*g ay[ 0-9]*to[ :.;,]*", "cmt:")
		.replaceAll("mt[ :.;,]*", "cmt:")
		.replaceAll("ten[ :.;,]*", "ho ten:")
		.replaceAll("ngay[ 0-9]*sans[ :.;,]*", "ngay sinh:")
		.replaceAll("ngay[ 0-9]*sing[ :.;,]*", "ngay sinh:")
		.replaceAll("hgay[ 0-9]*sinh[ :.;,]*", "ngay sinh:")
		.replaceAll("ngay[ 0-9]*sinh[ :.;,]*", "ngay sinh:")
		.replaceAll("ngay[ 0-9]*cap[ :.;,]*", "ngay cap:")
		.replaceAll("ngay[^:]+sinh:", "ngay sinh:")
		.replaceAll("dung co thue bao[ :.;,]*", "dung cac thue bao:")
		.replaceAll("ho[ 0-9]*nem[ :.;,]*", "ho ten:")
		.replaceAll("ho[ 0-9]*ton[ :.;,]*", "ho ten:")
		;
		
		strMsg = strMsg.replace("ho ten", ",ho ten")
				.replace("ngay sinh", ",ngay sinh")
				.replace("cmt", ",cmt")
				.replace("noi cap", ",noi cap")
				.replace("ngay cap", ",ngay cap")
				.replace("dung cac thue bao", ".dung cac thue bao")
				.replace("dich vu thue bao", ".dich vu thue bao")
				.replace("neu thong tin khong dung", ".neu thong tin khong dung")
				.replace("tin khong dung", ".tin khong dung")
				.replace("quy khach", ".quy khach")
				;
		strMsg = strMsg.replaceAll("[ ]+", " ");
		return strMsg;
	}
	public static BufferedImage decodeToImage(String imageString) {
	    BufferedImage image = null;
	    byte[] imageByte;
	    try {
	        Base64.Decoder decoder = Base64.getDecoder();
	        imageByte = decoder.decode(imageString);
	        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	        image = ImageIO.read(bis);
	        bis.close();
	    } catch (Exception e) {
	    	LOGGER.error("ERROR: {}", e.getMessage());
	    }
	    return image;
	}
	
	public static String getBase64Img(BufferedImage image) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "JPG", os); 
		return Base64.getEncoder().encodeToString(os.toByteArray()); 
	}
	
	public static byte[] convertBase64ImgToJpgByte(String base64Img) throws Exception {
//		ByteArrayOutputStream os = new ByteArrayOutputStream();
//		ImageIO.write(decodeToImage(base64Img), "JPG", os); 
//		return os.toByteArray(); 
		Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64Img);
	}
	
	private static String rotate(BufferedImage bimg, double angle) throws Exception {

		int w = bimg.getWidth();
		int h = bimg.getHeight();

		BufferedImage rotated = new BufferedImage(w, h, bimg.getType());
		Graphics2D graphic = rotated.createGraphics();
		graphic.rotate(Math.toRadians(angle), w / 2, h / 2);
		graphic.drawImage(bimg, null, 0, 0);
		graphic.dispose();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(rotated, "JPG", os);

		return Base64.getEncoder().encodeToString(os.toByteArray());
	}
	
	public static Boolean equals(String str1, String str2) {
		if(str1 == null || str2 == null) return false;
		str1 = strim(str1);
		str2 = strim(str2);
		Collator c = Collator.getInstance(Locale.US);
		c.setStrength(Collator.IDENTICAL);
		c.setDecomposition(Collator.FULL_DECOMPOSITION);
		Boolean check = c.equals(str1, str2);
		
		return check;
	}
	
	public static int equalsNotEmpty(String str1, String str2) {
		if(StringUtils.isEmpty(str1) || StringUtils.isEmpty(str2)) return -1;
		str1 = strim(str1);
		str2 = strim(str2);
		Collator c = Collator.getInstance(Locale.US);
		c.setStrength(Collator.IDENTICAL);
		c.setDecomposition(Collator.FULL_DECOMPOSITION);
		if (c.equals(str1, str2)) return 1;
		
		return 0;
	}
	
	public static int equalsEmpty(String str1, String str2) {
		int check = equalsNotEmpty(str1, str2);
		if(check == 1 || check == -1) return 1;
		
		return 0;
	}
	
	public static int equalsGioiTinh(Integer gt1, Integer gt2) {
		if(gt1 == null || gt2 == null) return -1;
		if(gt1 != 0 && gt1 != 1) return -1;
		if(gt2 != 0 && gt2 != 1) return -1;
		
		if (gt1 == gt2) return 1;
		
		return 0;
	}
	
	public static int equalsGioiTinhNull(Integer gt1, Integer gt2) {
		int check = equalsGioiTinh(gt1, gt2);
		if(check == 1 || check == -1) return 1;
		
		return 0;
	}
	
	public static String getStrPatter(String str, String pattern) {
		try {
			Pattern r = Pattern.compile(pattern);

		    Matcher m = r.matcher(str);
			String strFind = null;
		    while  (m.find()) {
		    	strFind = m.group(1).replaceAll("[\\s]+", " ").trim();
		    }
		    return strFind;
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		} 
	    return null;
	}
	
	public static boolean checkPatterinStr (String str, String pattern) {
		try {
			Pattern r = Pattern.compile(pattern);

		    Matcher m = r.matcher(str);
		    boolean b = m.matches();  
		    
		    return b;
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		} 
	    return false;
	}
	
	public static String getStrPatterFirst(String str, String pattern) {
		try {
			Pattern r = Pattern.compile(pattern);

		    Matcher m = r.matcher(str);
		    while  (m.find()) {
		    	return  m.group(1).replaceAll("[\\s]+", " ").trim();
		    }
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		} 
	    return null;
	}
	
	public static ArrayList<String> getStrPatterArray(String str, String pattern) {
		try {
			ArrayList<String> arr = new ArrayList<String>();
			Pattern r = Pattern.compile(pattern);

		    Matcher m = r.matcher(str);
		    while  (m.find()) {
		    	arr.add( m.group(1).replaceAll("[\\s]+", " ").trim());
		    }
		    return arr;
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		} 
	    return null;
	}
	
	public static String convertGioiTinh(String gioiTinh) {
		return gioiTinh.toLowerCase().trim().equals("nam")?"1":gioiTinh.toLowerCase().trim().equals("nữ")?"0":"";
	}
	public static Integer convertGioiTinhInt(String gioiTinh) {
		String strGt = convertGioiTinh(gioiTinh);
		return StringUtils.isEmpty(strGt)?null:Integer.valueOf(strGt);
	}
	public static String strim(String str) {
		return str.toLowerCase().trim().replaceAll("[ ]+", " ");
	}
	
	public static String getBase64EncodeFromUrl(String urlImage) {
		try {
			java.net.URL url = new java.net.URL(urlImage);
			InputStream is = url.openStream();
			byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String formatStringDate(String strDate) {
		try {
			if(strDate != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				return dateFormat.format(dateFormat.parse(strDate));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return strDate;
	}
	public static String convertDateToString(Date date) {
		try {
			if(date != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				return dateFormat.format(date);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public static String removeBreak(String str) {
		if(StringUtils.isEmpty(str)) return str;
		return str.replaceAll("(\r\n|\n)", "").replaceAll("[ ]+", "+");
	}
	@SuppressWarnings("resource")
	public static String encodeFileToBase64Binary(File file){
        try {
        	FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            return new String(Base64.getEncoder().encode(bytes));
		} catch (Exception e) {
			// TODO: handle exception
		}
        return null;
    }
	public static String formatMoney(String money) {
		try {
			long amount = Long.valueOf(money);    
			DecimalFormat formatter = new DecimalFormat("###,###,###");
			return formatter.format(amount);  
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	public static Boolean checkCaptcha(String token, String maCaptcha) {
		if(StringUtils.isEmpty(token) || StringUtils.isEmpty(maCaptcha)) return false;
		if(token.equals(getMd5(maCaptcha))) return true;
		
		return false;
	}
	public static int randomNumber(int min, int max) {
		Random generator = new Random();
        return generator.nextInt((max - min) + 1) + min;
    }
	
	public static String getMD5PW(String password) {
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
	
	public static String getMd5(String input) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhh");
			String s = "covid19sa123"+dateFormat.format(new Date() );
			input = input+s;
			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
