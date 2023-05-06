package fis.com.vn.component;

import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fis.com.vn.common.FileHandling;
import fis.com.vn.common.StringUtils;
import fis.com.vn.esigncloud.ESignCloudConstant;
import fis.com.vn.esigncloud.eSignCall2;
import fis.com.vn.esigncloud.datatypes.SignCloudMetaData;
import fis.com.vn.esigncloud.datatypes.SignCloudResp;
import fis.com.vn.repository.QuanLyChuKySoRepository;
import fis.com.vn.table.QuanLyChuKySo;

@Component
public class KySoComponent {
	@Value("${KY_SO_FOLDER}")
	String KY_SO_FOLDER;

	@Value("${MOI_TRUONG}")
	String MOI_TRUONG;

	@Autowired
	QuanLyChuKySoRepository quanLyChuKySoRepository;

	public QuanLyChuKySo layThongTinChuKy() {
		try {
			QuanLyChuKySo quanLyChuKySo = null;

			Calendar calendar = Calendar.getInstance();
			Iterable<QuanLyChuKySo> quanLyChuKySos = quanLyChuKySoRepository.findAll();
			for (QuanLyChuKySo quanLyChuKySo2 : quanLyChuKySos) {
				calendar.setTime(quanLyChuKySo2.getNgayKetThuc());
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				long ngayKetThuc = calendar.getTime().getTime();
				long ngayBatDau = quanLyChuKySo2.getNgayBatDau().getTime();
				if (ngayBatDau <= System.currentTimeMillis() && System.currentTimeMillis() < ngayKetThuc) {
					quanLyChuKySo = quanLyChuKySo2;
					break;
				}
			}
			if (quanLyChuKySo == null) {
				for (QuanLyChuKySo quanLyChuKySo2 : quanLyChuKySos) {
					if (!StringUtils.isEmpty(quanLyChuKySo2.getMacDinh()) && quanLyChuKySo2.getMacDinh().equals("1"))
						quanLyChuKySo = quanLyChuKySo2;
				}
			}
			return quanLyChuKySo;
		} catch (Exception e) {
		}
		return null;
	}

	public String signFile(String base64, String textKy, String uid, String passCode, String tenfile, String sohopdong)
			throws Exception {

		byte[] pdfData = Base64.getDecoder().decode(base64);
		System.err.println("aaaaa111");
		String ORGANIZATION_SIGNATURE_UUID = uid;
		String agreementUUid = ORGANIZATION_SIGNATURE_UUID;
		System.err.println("aaaaa2222");
		System.err.println("textky:" + textKy);
		HashMap<String, String> singletonSigningForItem01 = new HashMap<>();
		singletonSigningForItem01.put("PAGENO", "1");
		singletonSigningForItem01.put("POSITIONIDENTIFIER", textKy);
		singletonSigningForItem01.put("RECTANGLEOFFSET", "-30,-70");
		singletonSigningForItem01.put("RECTANGLESIZE", "170,70");
		singletonSigningForItem01.put("VISIBLESIGNATURE", "True");
		singletonSigningForItem01.put("VISUALSTATUS", "False");
		singletonSigningForItem01.put("SHOWSIGNERINFO", "True");
		singletonSigningForItem01.put("SIGNERINFOPREFIX", "Ký bởi:");
		singletonSigningForItem01.put("SHOWDATETIME", "True");
		singletonSigningForItem01.put("DATETIMEPREFIX", "Ký ngày:");
		singletonSigningForItem01.put("SHADOWSIGNATUREPROPERTIES", "all");

		SignCloudMetaData signCloudMetaData = new SignCloudMetaData();
		System.err.println("aaaaa333333");
		signCloudMetaData.setSingletonSigning(singletonSigningForItem01);
		String jsonResponse = "";
//        if(MOI_TRUONG.equals("dev")) {
//        	eSignCall2 service = new eSignCall2();
//			jsonResponse = service.prepareFileForSignCloudForOrganization(
//					agreementUUid, 
//					ESignCloudConstant.AUTHORISATION_METHOD_PASSCODE, 
//					passCode,
//					ESignCloudConstant.SYNCHRONOUS,
//					null, 
//					null, 
//					pdfData, 
//					"pdf_file_name.pdf", 
//					ESignCloudConstant.MIMETYPE_PDF, 
//					signCloudMetaData);
//			System.err.println("aaaaa4444441");
//        } else if(MOI_TRUONG.equals("prod")) {
		// eSignCall service = new eSignCall();
		eSignCall2 service = new eSignCall2();
		jsonResponse = service.prepareFileForSignCloudForOrganization(agreementUUid,
				ESignCloudConstant.AUTHORISATION_METHOD_PASSCODE, passCode, ESignCloudConstant.SYNCHRONOUS, null, null,
				pdfData, "pdf_file_name.pdf", ESignCloudConstant.MIMETYPE_PDF, signCloudMetaData);
		System.err.println("aaaaa4444442");
		// }

		ObjectMapper objectMapper = new ObjectMapper();
		SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);
		System.err.println("aaaaa55555");
		if (signCloudResp.getResponseCode() == 0 || signCloudResp.getResponseCode() == 1018) {

			if (signCloudResp.getSignedFileData() != null) {

				FileHandling fileHandling = new FileHandling();

				// String imgFolderLog = fileHandling.getFolder(KY_SO_FOLDER+"30_3_2023/") ;
				String imgFolderLog = KY_SO_FOLDER + sohopdong + "/";

				String file = imgFolderLog + tenfile + ".pdf";
				System.err.println("signCloudResp.getSignedFileData()4: " + file);

				FileOutputStream fos = new FileOutputStream(file);

				IOUtils.write(signCloudResp.getSignedFileData(), fos);

				fos.close();
				System.err.println("aaaaa66666");
				return file;

			} else {
				throw new Exception("Error signing: SignedData is NULL");
			}
		} else {
			throw new Exception("Error signing: " + signCloudResp.getResponseCode());
		}

	}
}
