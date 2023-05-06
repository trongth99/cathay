package fis.com.vn.cron;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import fis.com.vn.callapi.CallSeabank;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.table.EkycKyso;

@Component
public class CloseAccount {
	private final Logger LOGGER = LoggerFactory.getLogger(CloseAccount.class);
	
	@Autowired EkycKysoRepository ekycKysoRepository;
	@Autowired CallSeabank callSeabank;
	
	@Async
	public void start() {
		LOGGER.info("Start CloseAccount");
		long timeEnd = System.currentTimeMillis() - 60*60*1000L;
		List<EkycKyso> ekycKysos = ekycKysoRepository.selectCloseAcc(new Date(timeEnd));
		LOGGER.info("Start CloseAccount size: {}", ekycKysos.size());
		for (EkycKyso ekycKyso : ekycKysos) {
			try {
				callSeabank.closeAcccCron(ekycKyso.getAccountId(), ekycKyso.getCoCode(), ekycKyso, "cron");
				ekycKyso.setCronDongTk(1);
				ekycKysoRepository.save(ekycKyso);
			} catch (Exception e) {
			}
		}
	}
}
