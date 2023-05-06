package fis.com.vn.callapi.entities;

import lombok.Data;

@Data
public class ThongTinNguoiDungTuLOSHeader {
	String reqType;
	String api;
	String apiKey;
	String channel;
	String subChannel;
	String location;
	String context;
	String trusted;
	String requestAPI;
	String requestNode;
	int priority;
	String userID;
	Boolean sync;
}
