package fis.com.vn.ocr;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseOcrCmtQd {
	int errorCode;
	String errorMessage;
	
	ArrayList<OcrCmtQd> data;
}
