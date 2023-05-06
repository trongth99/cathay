package fis.com.vn.entities;

import lombok.Data;

@Data
public class ParamsKbank {
	String id;
	String matKhau;
	String thayDoiFileBaoHiem;
	String thayDoiFileKy;
	String khuVuc;
	String viTriKyDn;
	String trangKydn;
	String viTriKyCaNhan;
	String trangKyCaNhan;
	String viTriKyBaoHiem;
	
	String soDienThoai;
	String soHopDong;
	String soCmt;
	String hoVaTen;
	String email;
	String tenFile;
	String noiDungFile;
	String noiDungFileBaoHiem;
	String noiDungFileSeaBank;
	String trangThai;
	
	String anhCaNhan;
	String anhMatTruoc;
	String anhMatSau;
	String anhChuKy;
	String anhTinNhan;
	String maGiayTo;
	String maXacMinh;
	String respGiayTo;
	FormInfo formInfo;
	
	String maKy;
	String maOtpKy;
	String agreementUUID;
	String duongDanFileKy;
	String danhSachFile;
	String danhSachFilePayShip;
	String html;
	String codeTransaction;
	String code;
	
	String uuid;
	String bilCode;
	
	Boolean checkOcr;
	Boolean checkLiveness;
	Boolean checkOtp;
	String checkAccountSB;
	
	String caseId;
	
	String spHanMuc;
	String responseMoTaiKhoan;
	String thongTinNguoiDungTuLOS;
	int soLanKy;
	
	Boolean thongBaoCloseAcc;
}
