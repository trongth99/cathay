package fis.com.vn.ocr;

import lombok.Data;

@Data
public class ParseOcr {
	String type;
	FieldOcr id;
	FieldOcr name;
	FieldOcr dob;
	FieldOcr home_town;
	FieldOcr address;
	FieldOcr expr_date;
	FieldOcr features;
	FieldOcr reg_city;
	FieldOcr reg_date;
}
