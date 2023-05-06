package fis.com.vn.entities;

import lombok.Data;

@Data
public class RespApi {
	Object data;
	int status;
	String message;
	Object included;
}
