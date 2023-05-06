package fis.com.vn.ocr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.api.KhachHang;
import fis.com.vn.exception.CheckException;

@Component
public class OCRParser {
	@Autowired KhachHang khachHang;
	@Autowired FISOCRParser fisocrParser;
	
	public Ocr parsing (ParamOcr paramOcr) throws CheckException {
		return fisocrParser.parsing(paramOcr);
	}
}
