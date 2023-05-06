package fis.com.vn.api.entities;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ParamPathImage {
	ArrayList<String> anhNhanDang;
	String anhMatTruoc;
	String anhKhachHang;
	String anhMatSau;
	String anhTinNhan;
	ArrayList<String> anhVideo;
}
