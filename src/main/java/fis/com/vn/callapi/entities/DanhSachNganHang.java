package fis.com.vn.callapi.entities;

import java.util.ArrayList;

import lombok.Data;

@Data
public class DanhSachNganHang {
	ArrayList<Bank> banks;
	
	public void add(Bank bank) {
		if(banks == null) banks = new ArrayList<>();
		banks.add(bank);
	}
}
