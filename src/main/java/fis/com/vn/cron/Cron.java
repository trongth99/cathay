package fis.com.vn.cron;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fis.com.vn.api.KhachHang;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.KySoComponent;
import fis.com.vn.contains.ContainsKySo;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.repository.GiayChungNhanRepository;
import fis.com.vn.repository.SoHDRepository;
import fis.com.vn.table.EkycKyso;
import fis.com.vn.table.SoHD;

@Component
public class Cron {
	private static final Logger LOGGER = LoggerFactory.getLogger(Cron.class);

	@Autowired
	KhachHang khachHang;
	@Autowired
	ConfigProperties configProperties;
	@Autowired
	CheckServiceAi checkServiceAi;
	@Autowired
	CloseAccount closeAccount;

	@Autowired
	GiayChungNhanRepository giayChungNhanRepository;
	@Autowired
	EkycKysoRepository ekycKysoRepository;
	@Autowired
	SoHDRepository hdRepository;
	@Autowired
	KySoComponent kySoComponent;
	@Value("${KY_SO_FOLDER}")
	String KY_SO_FOLDER;

	@Scheduled(cron = "0 * * * * *")
	public void closeAccount() {
		closeAccount.start();
	}

	@Scheduled(cron = "0 * * * * *")
	public void layToKenKhachHang() {
		khachHang.resetListTokenKhachHang();
	}

	@Scheduled(cron = "0 * * * * *")
	public void layCauHinh() {
		configProperties.resetConfig();
	}

	@Scheduled(cron = "0 * * * * *")
	public void checkServiceAi() {
		LOGGER.info("Check URL AI");
		checkServiceAi.start();
	}

	@Scheduled(cron = "30 * * * * *")
	public void kyHopDongAuto() throws Exception {

		kyFile();
		deleteFile();

	}

	public void kyFile() throws Exception {
		File folder = new File(KY_SO_FOLDER + "cathay/");
		File[] listOfFiles = folder.listFiles();
		System.err.println("6666666666000877:");
		if (listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				if (file.isFile()) {

					////////////////

					String sohd = file.getName().replaceAll(".pdf", "");
					System.err.println("6666666666:" + file.getName().replaceAll(".pdf", ""));
					System.err.println("6666666666:" + sohd);
					String pathFileSign = kySoComponent.signFile(
							Utils.encodeFileToBase64Binary(new File(KY_SO_FOLDER + "cathay/" + file.getName())),
							"AUTHORIZED SIGNATURE", ContainsKySo.UUID_KY_BAO_HIEM_DEMO,
							ContainsKySo.PASS_CODE_KY_BAO_HIEM_DEMO, file.getName().replaceAll(".pdf", ""), sohd);
					EkycKyso ekycKyso = new EkycKyso();
					ekycKyso.setDuongDanFileKySeaBank(pathFileSign);
					ekycKyso.setTenFile(file.getName().replaceAll(".pdf", ""));
					ekycKyso.setTrangThai("2");
					ekycKyso.setBaoHiemKy(new Date());
					ekycKyso.setNgayTao(new Date());

					SoHD hd = hdRepository.findBySoHd(sohd);
					hd.setDuongDanFileSeaBank(pathFileSign);
					hdRepository.save(hd);
					ekycKyso.setHoVaTen(hd.getHovaten());
					ekycKyso.setSoCmt(hd.getSocmt());
					ekycKyso.setEmail(hd.getEmail());
					ekycKyso.setSohd(hd.getSoHD());
					ekycKyso.setSoDienThoai(hd.getDienthoai());
					ekycKyso.setTrangThaiGui("1");

					ekycKysoRepository.save(ekycKyso);

					System.err.println("6666666666");

				}

			}
		}

		// deleteFile();
	}

	public void deleteFile() throws IOException {

		File folder = new File(KY_SO_FOLDER + "cathay/");
		File[] listOfFiles = folder.listFiles();
		System.err.println("delete roi" + listOfFiles.length);
		ArrayList<EkycKyso> list = (ArrayList<EkycKyso>) ekycKysoRepository.findAll();
		if (listOfFiles.length > 0) {

			for (File file : listOfFiles) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getTenFile().equals(file.getName().replaceAll(".pdf", ""))
							&& list.get(i).getTrangThai().equals("2")) {
						FileOutputStream fout = new FileOutputStream(file);
						fout.close();
						Thread ty = new Thread() {
							public void run() {
								file.delete();
								System.err.println("hdsjbfhsj");
							}

							@Override
							protected void finalize() throws Throwable {

							}
						};
						ty.start();
					}

				}

				// }
			}

		}

	}
}
