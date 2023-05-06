/**
 * 
 */
package fis.com.vn.entities;

import lombok.Data;

/**
 * @author ChinhVD4
 *
 */
@Data
public class ConfigParams {
	private String  toa_do_them_thong_tin_file_hop_dong ;
	private String  token_get_list_contract ;
	
	private String  link_liveness_face;
	private String  link_liveness_face1;
	
	private String  link_compare_face;
	private String  link_compare_face1;
	
	private String  score_check;
	private String  kiem_tra_liveness;
	private String  link_dieu_khoan_dieu_kien;
	private String  kiem_tra;
	private String  link_ocr_cmt_cccd_fis2;
	private String  link_ocr_cmt_cccd_fis1;
	private String  link_ocr_cmt_cccd_fis;
	private String version;
	private String image_folder_log;
	private String toa_do_check_co_uy_quyen;
	private String toa_do_check_khong_co_uy_quyen;
	private String trang_danh_dau_uy_quyen;
	private String nguong_chinh_xac;
	private String toa_do_ngay_thang_nam;
	private String username_sms;
	private String password_sms;
	
	public Double getDoubleScore_check () {
		try {
			return Double.valueOf(score_check);
		} catch (Exception e) {
		}
		return 0.85;
	}
	
	public Double getDoubleNguong_chinh_xac () {
		try {
			return Double.valueOf(nguong_chinh_xac);
		} catch (Exception e) {
		}
		return 0.85;
	}
}
