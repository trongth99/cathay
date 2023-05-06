package fis.com.vn.callapi.entities;

import lombok.Data;

@Data
public class ThongTinNguoiDungTuLOSBody {
	String response_code;
	String status;
	ThongTinNguoiDungTuLOS dataRes;
}
