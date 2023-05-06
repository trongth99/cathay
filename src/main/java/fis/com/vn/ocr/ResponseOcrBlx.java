package fis.com.vn.ocr;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseOcrBlx {
	int errorCode;
	String errorMessage;
	
	ArrayList<OcrBlx> data;
}
