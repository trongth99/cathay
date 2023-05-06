package fis.com.vn.callapi.entities;

import lombok.Data;

@Data
public class ThongTinNguoiDungTuLOSResp {
	ThongTinNguoiDungTuLOSBody body;
	ThongTinNguoiDungTuLOSHeader header;
	ThongTinNguoiDungTuLOSError error;
}
