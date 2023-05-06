package fis.com.vn.ocr;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseOcrCmtCccd {
	int errorCode;
	String errorMessage;
	
	ArrayList<OcrCmtCccd> data;
}
