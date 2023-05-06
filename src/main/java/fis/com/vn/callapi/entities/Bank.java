package fis.com.vn.callapi.entities;

import lombok.Data;

@Data
public class Bank {
	String type;
	String name;
	String value;
	String parentValue;
	InforBranch inforBranch;
	

}
