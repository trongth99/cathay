package fis.com.vn.ocr;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseOcrHoChieu {
	int errorCode;
	String errorMessage;
	
	ArrayList<OcrHoChieu> data;
}
