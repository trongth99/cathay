package fis.com.vn.api.face;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParamsImg {
	String base64Img1;
	String base64Img2;
	String errorMsg;
	Double compareValue;
	Boolean kiemTraKhuonMat;
	String maToChuc;
}
