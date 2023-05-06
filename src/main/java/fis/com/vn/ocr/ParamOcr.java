/**
 * 
 */
package fis.com.vn.ocr;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Data
public class ParamOcr {
	String base64AnhMatTruoc;
	String base64AnhMatSau;
	String loaiGiayTo;
	/**
	 * kiểm tra chụp lại từ màn hình, dấu nổi, sửa đổi, dấu vân tay
	 */
	Boolean checkInfoCard;
	
	/**
	 * kiểm tra mặt trước và mặt sau cmt là cùng loại
	 */
	Boolean checkCard;
	String apiKey;
	String maToChuc;
	
	/**
	 * 
	 * @param base64AnhMatTruoc
	 * @param base64AnhMatSau
	 * @param loaiGiayTo
	 * @param checkInfoCard kiểm tra chụp lại từ màn hình, dấu quốc huy, dấu vân tay
	 * @param checkCard kiểm tra mặt trước và mặt sau cmt là cùng loại
	 * @param apiKey
	 */
	public ParamOcr(String base64AnhMatTruoc, String base64AnhMatSau, String loaiGiayTo, Boolean checkInfoCard, Boolean checkCard, String apiKey, String maToChuc) {
		this.base64AnhMatTruoc = base64AnhMatTruoc;
		this.base64AnhMatSau = base64AnhMatSau;
		this.loaiGiayTo = loaiGiayTo;
		this.checkInfoCard = checkInfoCard;
		this.checkCard = checkCard;
		this.apiKey = apiKey;
		this.maToChuc = maToChuc;
	}
}
