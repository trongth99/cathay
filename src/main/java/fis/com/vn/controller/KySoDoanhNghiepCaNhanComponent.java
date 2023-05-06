package fis.com.vn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fis.com.vn.callapi.CallSeabank;
import fis.com.vn.callapi.entities.Bank;
import fis.com.vn.callapi.entities.DanhSachNganHang;
import fis.com.vn.callapi.entities.DanhSachTaiKhoan;
import fis.com.vn.callapi.entities.PramMoTaiKhoan;
import fis.com.vn.callapi.entities.TaiKhoan;
import fis.com.vn.callapi.entities.ThongTinNguoiDungTuLOS;
import fis.com.vn.callapi.entities.ThongTinTaiKhoanDangKy;
import fis.com.vn.common.Common;
import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.Email;
import fis.com.vn.common.EnDeCryption;
import fis.com.vn.common.FileHandling;
import fis.com.vn.common.PdfHandling;
import fis.com.vn.common.SendSMS;
import fis.com.vn.common.StringUtils;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.KySoComponent;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.FileObject;
import fis.com.vn.entities.FormInfo;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.entities.RespApi;
import fis.com.vn.entities.Thongtingiaingan;
import fis.com.vn.esigncloud.ESignCloudConstant;
import fis.com.vn.esigncloud.eSignCall;
import fis.com.vn.esigncloud.eSignCall2;
import fis.com.vn.esigncloud.datatypes.MultipleSignedFileData;
import fis.com.vn.esigncloud.datatypes.MultipleSigningFileData;
import fis.com.vn.esigncloud.datatypes.SignCloudMetaData;
import fis.com.vn.esigncloud.datatypes.SignCloudResp;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.ocr.Ocr;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.repository.LogApiRepository;
import fis.com.vn.repository.LogApiSeaBankRepository;
import fis.com.vn.table.EkycKyso;

@Component
public class KySoDoanhNghiepCaNhanComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(KySoDoanhNghiepCaNhanController.class);

	@Value("${TOKEN}")
	String token;

	@Value("${KY_SO_FOLDER}")
	String KY_SO_FOLDER;

	@Value("${LINK_ADMIN}")
	String LINK_ADMIN;

	@Value("${CODE}")
	String code;

	@Value("${API_SERVICE}")
	String API_SERVICE;

	@Value("${MOI_TRUONG}")
	String MOI_TRUONG;

	@Autowired
	EkycKysoRepository ekycKysoRepository;
	@Autowired
	ConfigProperties configProperties;
	@Autowired
	PdfHandling pdfHandling;
	@Autowired
	EnDeCryption enDeCryption;
	@Autowired
	Email email;
	@Autowired
	KySoComponent kySoComponent;
	@Autowired
	SendSMS sendSMS;
	@Autowired
	CallSeabank callSeabank;

	@Autowired
	LogApiRepository logApiRepository;

	@Autowired
	LogApiSeaBankRepository logApiSeaBankRepository;

	public String notificationTemplate = "[FPT-CA] Ma xac thuc (OTP) cua Quy khach la {AuthorizeCode}. Vui long dien ma so nay de ky Hop dong Dien Tu va khong cung cap OTP cho bat ky ai";
	public String notificationSubject = "[FPT-CA] Ma xac thuc (OTP)";

	public String step1(HttpServletRequest req) {
		try {
			ParamsKbank params = new ParamsKbank();
			params.setCodeTransaction(Common.layMaGiaoDich(1));
			setParams(params, req);

			return "demo/kySoDoanhNgiepCaNhan/step/step1";
		} catch (Exception e) {
			LOGGER.error("Error step1: {}", e);
		}
		return "demo/kySoDoanhNgiepCaNhan/error";
	}

	public void setParams(ParamsKbank params, HttpServletRequest req) {
		req.getSession().setAttribute("params", params);
	}

	public ParamsKbank getParams(HttpServletRequest req) {
		return (ParamsKbank) req.getSession().getAttribute("params");
	}

	public String poststep1(HttpServletRequest req, Map<String, String> allParams, Model model) {
		try {
			EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(allParams.get("soCmt"), allParams.get("matKhau"));

			if (ekycKyso == null) {
				model.addAttribute("error", "Số chứng minh thư hoặc mật khẩu không đúng");
				return "demo/kySoDoanhNgiepCaNhan/step/step1";
			}

			if (trangThaiHopDongDaKy(ekycKyso)) {
				model.addAttribute("error", "Hợp đồng đã được ký");

				return "demo/kySoDoanhNgiepCaNhan/step/step1";
			}

			if (System.currentTimeMillis() > ekycKyso.getThoiGianHetHanToken()) {
				model.addAttribute("error", "Mật khẩu hết hạn");

				return "demo/kySoDoanhNgiepCaNhan/step/step1";
			}

			ParamsKbank params = new ParamsKbank();
			params.setSoCmt(allParams.get("soCmt"));
			params.setMatKhau(allParams.get("matKhau"));
			params.setCodeTransaction(Common.layMaGiaoDich(1));
			params.setCheckOtp(true);
			setParams(params, req);

			LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

			return "demo/kySoDoanhNgiepCaNhan/step/step3";
		} catch (Exception e) {
			LOGGER.error("Error step1 post: {}", e);
		}
		return "demo/kySoDoanhNgiepCaNhan/error";
	}

	private boolean trangThaiHopDongDaKy(EkycKyso ekycKyso) {
		if (StringUtils.isEmpty(ekycKyso.getTrangThai()))
			return false;
		if (ekycKyso.getTrangThai().equals("1"))
			return true;
		if (ekycKyso.getTrangThai().equals("2"))
			return true;
		if (ekycKyso.getTrangThai().equals("3"))
			return true;

		return false;
	}

	public String step2(HttpServletRequest req, Map<String, String> allParams, Model model) {
		try {
			forwartParams(allParams, model);

			ParamsKbank params = getParams(req);
			if (params == null)
				return redirect();
			LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

			if (!validOtp(params))
				return redirect();
			resetOcrLiveness(params, req);

			String anhMatSauBase64 = allParams.get("anhMatSauBase64");
			String anhMatTruocBase64 = allParams.get("anhMatTruocBase64");

			EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());

			params.setSoDienThoai(ekycKyso.getSoDienThoai());
			params.setSoCmt(ekycKyso.getSoCmt());
			params.setSoHopDong(ekycKyso.getSoTaiKhoan());
			params.setHoVaTen(ekycKyso.getHoVaTen());

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("anhMatTruoc", anhMatTruocBase64);
			jsonObject.put("anhMatSau", anhMatSauBase64);

			String respone = "";
			if (MOI_TRUONG.equals("dev")) {
				respone = "{ \"data\": { \"soCmt\": \"030084000333\", \"hoVaTen\": \"VŨ ĐỨC CHÍNH\", \"namSinh\": \"05/05/1988\", \"queQuan\": \"QUANG HÁN, TRÙNG KHÁNH, CAO BẰNG\", \"noiTru\": \"TỔ 3 SÔNG BẰNG, THÀNH PHỐ CAO BẰNG, CAO BẰNG\", \"dacDiemNhanDang\": \"SẸO CHẤM C 3CM TRÊN SAU ĐẦU LÔNG MÀY TRÁI\", \"ngayCap\": \"27/02/2011\", \"noiCap\": \"CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI\", \"loaiCmtMatTruoc\": \"cccd_chip_mt\", \"loaiCmtMatSau\": \"cccd_chip_ms\", \"loaiCmtKhacMatTruoc\": \"anhMatTruoc\", \"quocTich\": \"VIỆT NAM\", \"ngayHetHan\": \"05/05/2028\", \"gioiTinh\": \"NAM\", \"chiTietNoiTru\": { \"province\": \"Cao Bằng\", \"district\": \"Cao Bằng\", \"ward\": \"Sông Bằng\", \"street\": \"TỔ 3\", \"country\": \"\" }, \"kiemTraMatTruoc\": { \"chupLaiTuManHinh\": \"0\", \"chupLaiTuManHinhScore\": \"3.5762786865234375e-06\", \"denTrang\": \"0\", \"denTrangScore\": \"2.0265579223632812e-06\", \"catGoc\": \"0\", \"catGocScore\": [ \"0.003006458282470703\", \"0.004161238670349121\", \"0.007349550724029541\", \"0.0025257468223571777\" ], \"dauNoi\": \"N/A\", \"dauQuocHuy\": \"0\", \"dauQuocHuyScore\": \"0.0017094016075134277\", \"anhBiLoa\": \"0\", \"anhBiLoaScore\": \"0.0\", \"kiemTraAnh\": \"0\", \"kiemTraAnhScore\": \"0.0005601048469543457\", \"thayTheAnh\": \"0\", \"thayTheAnhScore\": \"0.15914003157246734\", \"khungHinh\": \"0\", \"khungHinhScore\": \"3.8504600524902344e-05\", \"ngayHetHan\": \"0\", \"quyLuatSo\": \"0\" }, \"kiemTraMatSau\": { \"dauDo\": \"0\", \"dauDoScore\": \"0.0010663866996765137\", \"anhBiLoa\": \"0\", \"anhBiLoaScore\": \"0.0\", \"vanTayPhai\": \"0\", \"vanTayPhaiScore\": \"0.0013790130615234375\", \"vanTayTrai\": \"0\", \"vanTayTraiScore\": \"0.0010455846786499023\", \"khungHinh\": \"0\", \"khungHinhScore\": \"4.2557716369628906e-05\" }, \"score\": \"95.51362159391967\", \"soCmtScore\": \"96.07729407034527\", \"hoVaTenScore\": \"89.36560841707083\", \"namSinhScore\": \"96.14297035637324\", \"ngayHetHanScore\": \"96.01513475801936\", \"queQuanScore\": \"95.08003536491653\", \"noiTruScore\": \"96.46951775618805\", \"danTocScore\": \"N/A\", \"tonGiaoScore\": \"N/A\", \"ngayCapScore\": \"95.92182549755955\", \"noiCapScore\": \"98.13244922040579\", \"gioiTinhScore\": \"95.82308486321452\", \"quocTichScore\": \"96.10829563510352\", \"maHoa\": { \"noiDung\": \"IDVNM0880003405004088000340<<9 8805050M2805058VNM<<<<<<<<<<<6 MONG<<VAN<LUAN<<<<<<<<<<<<<<<<\", \"soCmt\": \"004088000340\", \"hoVaTen\": \"MONG VAN LUAN\", \"namSinh\": \"880505\", \"ngayHetHan\": \"280505\", \"gioiTinh\": \"M\" }, \"maTinhQueQuan\": \"004\", \"maTinhDiaChi\": \"004\", \"maTinhNoiCap\": \"\" }, \"status\": 200, \"message\": \"Thành công\", \"included\": {} }";

			} else {
				respone = postRequest(jsonObject.toString(), "/public/all/doc-noi-dung-ocr", params);
			}

			System.out.println(respone);
			JSONObject object = new JSONObject(respone);
			if (object.getInt("status") != 200) {
				model.addAttribute("error", object.getString("message"));
				String json = ekycKyso.getThongBao();
				ekycKyso.setThongBao(themLoi(json, object.getString("message")));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			params.setRespGiayTo(respone);

			setParams(params, req);

			JSONObject objectRespGiayTo = new JSONObject(respone);
			Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);
			req.getSession().setAttribute("soCMT", ocr.getSoCmt());

			if (!ekycKyso.getSoCmt().equals(ocr.getSoCmt())) {
				model.addAttribute("error", "Số chứng minh thư không khớp với hồ sơ");
				String json = ekycKyso.getThongBao();
				ekycKyso.setThongBao(themLoi(json, "Số chứng minh thư không khớp với hồ sơ"));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			if (!Utils.equals(Utils.alias2(ekycKyso.getHoVaTen()), Utils.alias2(ocr.getHoVaTen()))) {
				model.addAttribute("error", "Họ tên trên giấy tờ không khớp với hồ sơ");
				String json = ekycKyso.getThongBao();
				ekycKyso.setThongBao(themLoi(json, "Họ tên trên giấy tờ không khớp với hồ sơ"));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			// 5-6-2022
			if (kiemTraAnhKhongDuChatLuong(ocr)) {
				model.addAttribute("error", "Ảnh không đủ chất lượng");
				String json = ekycKyso.getThongBao();
				ekycKyso.setThongBao(themLoi(json, "Ảnh không đủ chất lượng"));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			// 5-6-2022
			if (kiemTraNgayCapKhongDung(ocr, ekycKyso)) {
				model.addAttribute("error", "Thông tin ngày cấp giấy tờ không đúng");
				String json = ekycKyso.getThongBao();
				ekycKyso.setThongBao(themLoi(json, "Thông tin ngày cấp giấy tờ không đúng"));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			if (!StringUtils.isEmpty(ekycKyso.getTrangThai()) && ekycKyso.getTrangThai().equals("1")) {
				model.addAttribute("error", "Hợp đồng đã được ký");

				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			JSONObject jsonObjectSS = new JSONObject();
			jsonObjectSS.put("anhMatTruoc", anhMatTruocBase64);
			jsonObjectSS.put("anhKhachHang", CommonUtils.encodeFileToBase64Binary(new File(ekycKyso.getAnhCaNhan())));

			String respone1 = "{}";

			if (MOI_TRUONG.equals("dev")) {
				respone1 = "{\"status\": 200, \"message\": \"Thành công\" }";
			} else {
				respone1 = postRequest(jsonObjectSS.toString(), "/public/all/so-sanh-anh", params);
			}

			object = new JSONObject(respone1);
			if (object.getInt("status") != 200) {
				model.addAttribute("error", object.getString("message"));
				String json = ekycKyso.getThongBao();
				ekycKyso.setThongBao(themLoi(json, object.getString("message")));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/step3";
			}

			String str = ekycKyso.getDuongDanFileKy();
			File file2 = new File(str);
			String base64Img2 = CommonUtils.encodeFileToBase64Binary(file2);
			model.addAttribute("file", base64Img2);

			model.addAttribute("ocr", ocr);

			String imgFolderLog = configProperties.getConfig().getImage_folder_log() + code + "/";
			FileHandling fileHandling = new FileHandling();
			String pathAnhMatTruoc = fileHandling.save(anhMatTruocBase64, imgFolderLog);
			String pathAnhMatSau = fileHandling.save(anhMatSauBase64, imgFolderLog);

			ekycKyso.setOcr(respone);
			ekycKyso.setAnhMatTruoc(pathAnhMatTruoc);
			ekycKyso.setAnhMatSau(pathAnhMatSau);

			ekycKysoRepository.save(ekycKyso);

			params.setCheckOcr(true);
			setParams(params, req);

			if (!StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc())
					&& ekycKyso.getTrangThaiEkyc().equals(Contains.TT_EKYC_TRUYEN_THONG)) {
				model.addAttribute("anhVideo", Utils.encodeFileToBase64Binary(new File(ekycKyso.getAnhVideo())));
			}

			return "demo/kySoDoanhNgiepCaNhan/step/step30";
		} catch (Exception e) {
			LOGGER.error("Error step2 post: {}", e);
		}
		return "demo/kySoDoanhNgiepCaNhan/error";
	}

	public String step20(HttpServletRequest req, Map<String, String> allParams, Model model) {
		try {
			forwartParams(allParams, model);

			ParamsKbank params = getParams(req);
			if (params == null)
				return "redirect:/khach-hang/ky-so/step1";
			LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

			if (!validOtpOCr(params))
				return redirect();
			resetLiveness(params, req);

			if (StringUtils.isEmpty(allParams.get("noiTru"))) {
				JSONObject objectRespGiayTo = new JSONObject(params.getRespGiayTo());
				Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);
				model.addAttribute("ocr", ocr);
				return "demo/kySoDoanhNgiepCaNhan/step/step30";
			}

			EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());
			ekycKyso.setNoiTru(allParams.get("noiTru"));
			ekycKysoRepository.save(ekycKyso);

			if (!StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc())
					&& ekycKyso.getTrangThaiEkyc().equals(Contains.TT_EKYC_TRUYEN_THONG)) {
				model.addAttribute("anhVideo", Utils.encodeFileToBase64Binary(new File(ekycKyso.getAnhVideo())));
			}
			
			return "demo/kySoDoanhNgiepCaNhan/step/step31";
		} catch (Exception e) {
			LOGGER.error("Error step2 post: {}", e);
		}
		return "demo/kySoDoanhNgiepCaNhan/error";
	}

	private boolean kiemTraNgayCapKhongDung(Ocr ocr, EkycKyso ekycKyso) {
		try {
			String str = pdfHandling
					.layThongTinNgayCap(Utils.encodeFileToBase64Binary(new File(ekycKyso.getDuongDanFileKy())));
			LOGGER.info("Ngay cap pdf " + ekycKyso.getSoCmt() + ": {}", str);
			LOGGER.info("Ngay cap ocr " + ekycKyso.getSoCmt() + ": {}", ocr.getNgayCap());

			if (ocr.getNgayCap().equals(str))
				return false;
		} catch (Exception e) {
		}
		return true;
	}

	private boolean kiemTraAnhKhongDuChatLuong(Ocr ocr) {
		Double scoreCheck = configProperties.getConfig().getDoubleScore_check();
		try {
			if (getScoreDouble(ocr.getSoCmtScore()) < scoreCheck)
				return true;
			if (getScoreDouble(ocr.getHoVaTenScore()) < scoreCheck)
				return true;
			if (getScoreDouble(ocr.getNamSinhScore()) < scoreCheck)
				return true;
			if (getScoreDouble(ocr.getQueQuanScore()) < scoreCheck)
				return true;
			if (getScoreDouble(ocr.getNoiTruScore()) < scoreCheck)
				return true;
			if (getScoreDouble(ocr.getNoiCapScore()) < scoreCheck)
				return true;
			if (getScoreDouble(ocr.getNgayCapScore()) < scoreCheck)
				return true;
			if (ocr.getLoaiCmtMatTruoc() != null && ocr.getLoaiCmtMatTruoc().equals("cccd_chip_mt")) {
				if (getScoreDouble(ocr.getNgayHetHanScore()) < scoreCheck)
					return true;
				if (getScoreDouble(ocr.getGioiTinhScore()) < scoreCheck)
					return true;
				if (getScoreDouble(ocr.getQuocTichScore()) < scoreCheck)
					return true;
			}
			if (ocr.getLoaiCmtMatTruoc() != null && ocr.getLoaiCmtMatTruoc().equals("cccd/cmt_12_mt")) {
				if (getScoreDouble(ocr.getNgayHetHanScore()) < scoreCheck)
					return true;
				if (getScoreDouble(ocr.getGioiTinhScore()) < scoreCheck)
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private Double getScoreDouble(String str) {
		try {
			String st = str.replaceAll("[^0-9.]+", "");
			return Double.valueOf(st);
		} catch (Exception e) {
		}
		return 0.0;
	}

	public String step31(HttpServletRequest req, Map<String, String> allParams, Model model) {
		try {
			forwartParams(allParams, model);
			String listImage = allParams.get("listImage");
			String[] arr = listImage.split(",");
			JSONArray jsonArray = new JSONArray();
			String anhVideo = "";
			FileHandling fileHandling = new FileHandling();
			String imgFolderLog = configProperties.getConfig().getImage_folder_log() + code + "/";
			for (int i = 0; i < arr.length; i++) {
				jsonArray.put(i, new JSONObject().put("anh", arr[i]).put("thoiGian", (i + 1)));
				String pathImg = fileHandling.save(arr[i], imgFolderLog);
				if (StringUtils.isEmpty(anhVideo)) {
					anhVideo = pathImg;
				} else {
					anhVideo += "," + pathImg;
				}
			}

			ParamsKbank params = getParams(req);
			if (params == null)
				return "redirect:/khach-hang/ky-so/step1";
			LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

			if (!validOtpOCr(params))
				return redirect();
			resetLiveness(params, req);

			EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("anhMatTruoc", CommonUtils.encodeFileToBase64Binary(new File(ekycKyso.getAnhMatTruoc())));
			jsonObject.put("anhVideo", jsonArray);

			Double similar = null;

			if (StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc()) || (!StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc())
					&& !ekycKyso.getTrangThaiEkyc().equals(Contains.TT_EKYC_TRUYEN_THONG))) {
				String respone = "{}";

				if (MOI_TRUONG.equals("dev")) {
					respone = "{\"status\": 200, \"data\": 0.9}";
				} else {
					respone = postRequest(jsonObject.toString(), "/public/all/xac-thuc-khuon-mat", params);
				}

				JSONObject object = new JSONObject(respone);

				ekycKyso.setAnhVideo(anhVideo);

				if (object.getInt("status") != 200) {
					model.addAttribute("error", object.getString("message"));
					String json = ekycKyso.getThongBao();
					ekycKyso.setTrangThaiEkyc(Contains.TT_EKYC_THAT_BAI);
					ekycKyso.setThongBao(themLoi(json, object.getString("message")));
					ekycKyso.setDiemEkyc("");
					ekycKysoRepository.save(ekycKyso);
					return "demo/kySoDoanhNgiepCaNhan/step/step31";
				}
				similar = object.getDouble("data");
			}
			setParams(params, req);

			JSONObject objectRespGiayTo = new JSONObject(ekycKyso.getOcr());
			Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);

			params.setHoVaTen(ekycKyso.getHoVaTen());
			params.setSoDienThoai(ekycKyso.getSoDienThoai());
			FormInfo formInfo = new FormInfo();
			formInfo.setNgayCap(ocr.getNgayCap());
			formInfo.setNoiCap(ocr.getNoiCap());
			formInfo.setDiaChi(ekycKyso.getNoiTru());
			formInfo.setNamSinh(ocr.getNamSinh());
			params.setFormInfo(formInfo);
			params.setCheckLiveness(true);
			setParams(params, req);

			if (StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc()) || (!StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc())
					&& !ekycKyso.getTrangThaiEkyc().equals(Contains.TT_EKYC_TRUYEN_THONG))) {
				ekycKyso.setTrangThaiEkyc(Contains.TT_EKYC_THANH_CONG);
			}

			try {
				if(!StringUtils.isEmpty(similar))
					ekycKyso.setDiemEkyc(String.valueOf(similar));
			} catch (Exception e) {
			}

			ekycKysoRepository.save(ekycKyso);

			
			if((ekycKyso.getCloseAcc() != null && ekycKyso.getCloseAcc().equals(Contains.TAI_KHOAN_BI_DONG)) || dongTaiKhoanThanhCong(ekycKyso, req)) {
				thongBaoDongTaiKhoan(model, req, params);
				return thongTinGiaiNgan(req, model);
			}
			
			if (!StringUtils.isEmpty(ekycKyso.getTrangThaiEkyc()) && ekycKyso.getTrangThaiEkyc().equals(Contains.TT_EKYC_TRUYEN_THONG)) {
				return "redirect:/khach-hang/ky-so/thong-tin-giai-ngan";
			} else {
				return "demo/kySoDoanhNgiepCaNhan/step/chonmotaikhoan";
			}
		} catch (Exception e) {
			LOGGER.error("Error step31 post: {}", e);
		}
		return "demo/kySoDoanhNgiepCaNhan/error";
	}

	private String thucHienKySo(EkycKyso ekycKyso, Model model, ParamsKbank params) {
		FileHandling fileHandling = new FileHandling();

		String outputFilePath = fileHandling.getFolder(configProperties.getConfig().getImage_folder_log() + code + "/")+ UUID.randomUUID().toString() + ".pdf";
		fileHandling.createFolder(configProperties.getConfig().getImage_folder_log() + code + "/");

		pdfHandling.editContentPdf(outputFilePath, params);
		model.addAttribute("pdfSign", enDeCryption.encrypt(outputFilePath));
		model.addAttribute("LINK_ADMIN", LINK_ADMIN);

		LOGGER.info("Complete step 31: " + params.getCodeTransaction() + "|" + outputFilePath);

		return "demo/kySoDoanhNgiepCaNhan/step/step32";
	}

	public String step32(HttpServletRequest req, Map<String, String> allParams, Model model) {
		try {
			ParamsKbank params = getParams(req);
			if (params == null)
				return "redirect:/khach-hang/ky-so/step1";
			LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

			if (!validOtpOCrLiveness(params))
				return redirect();

			if (StringUtils.isEmpty(allParams.get("chuKySo"))) {
				LOGGER.info("EMPTY SIGN: " + params.getSoCmt());
				return "redirect:/khach-hang/ky-so/step1";
			}

			params.setAnhChuKy(allParams.get("chuKySo"));

			// edit
			FileHandling fileHandling = new FileHandling();

			String outputFilePath = fileHandling.getFolder(configProperties.getConfig().getImage_folder_log() + code + "/")+ UUID.randomUUID().toString() + ".pdf";
			fileHandling.createFolder(configProperties.getConfig().getImage_folder_log() + code + "/");

			pdfHandling.editContentPdf(outputFilePath, params);
			model.addAttribute("pdfSign", enDeCryption.encrypt(outputFilePath));
			model.addAttribute("LINK_ADMIN", LINK_ADMIN);
			model.addAttribute("LINK_DKDK", configProperties.getConfig().getLink_dieu_khoan_dieu_kien());

			if (StringUtils.isEmpty(outputFilePath)) {
				LOGGER.info("EMPTY SIGN OUTPUTFILEPATH: " + params.getSoCmt());
				return "redirect:/khach-hang/ky-so/step1";
			}

			params.setAnhChuKy(outputFilePath);
			setParams(params, req);

			kysoPage(allParams, model, params);
			return "demo/kySoDoanhNgiepCaNhan/step/step4";
		} catch (Exception e) {
			LOGGER.error("Error step32 post: {}", e);
		}

		return "demo/kySoDoanhNgiepCaNhan/error";
	}

	private void kysoPage(Map<String, String> allParams, Model model, ParamsKbank params) {
		EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());

		model.addAttribute("file", enDeCryption.encrypt(layDuongDanFileKy(ekycKyso)));
		if (!StringUtils.isEmpty(ekycKyso.getDuongDanFileKySeaBank()) && ekycKyso.getMoTKTTSB() != null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
			String strSb = ekycKyso.getDuongDanFileKySeaBank();
			model.addAttribute("fileSeaBank", enDeCryption.encrypt(strSb));
		}

		if (!StringUtils.isEmpty(ekycKyso.getChiDinh())) {
			model.addAttribute("fileBaoHiem", enDeCryption.encrypt(ekycKyso.getChiDinh()));
		}

		JSONObject objectRespGiayTo = new JSONObject(params.getRespGiayTo());
		Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);

		model.addAttribute("params", params);
		model.addAttribute("ocr", ocr);
	}

	public String step3(HttpServletRequest req, Map<String, String> allParams, Model model) {
		ParamsKbank params = getParams(req);
		if (params == null)
			return "redirect:/khach-hang/ky-so/step1";
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

		if (!validOtpOCrLiveness(params))
			return redirect();

		forwartParams(allParams, model);

		if (StringUtils.isEmpty(params.getAnhChuKy())) {
			LOGGER.info("EMPTY SIGN SIGN: " + params.getSoCmt());
			return "redirect:/khach-hang/ky-so/step1";
		}

		try {
			EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(allParams.get("soCmt"), params.getMatKhau());
			String jsonResponse = "";
			if (MOI_TRUONG.equals("dev") || MOI_TRUONG.equals("test")) {
				eSignCall2 service = new eSignCall2();
				jsonResponse = service.authorizeCounterSigningForSignCloud(allParams.get("agreementUUID"),
						allParams.get("otpKySo"), allParams.get("maKy"));
			} else {
				eSignCall service = new eSignCall();
				jsonResponse = service.authorizeCounterSigningForSignCloud(allParams.get("agreementUUID"),
						allParams.get("otpKySo"), allParams.get("maKy"));
			}
			ObjectMapper objectMapper = new ObjectMapper();
			SignCloudResp signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);
			if ((signCloudResp.getResponseCode() == 0 && signCloudResp.getMultipleSignedFileData() != null)
					|| signCloudResp.getResponseCode() == 0 && signCloudResp.getSignedFileData() != null) {
				if (signCloudResp.getSignedFileData() != null) {
					String file = ekycKyso.getDuongDanFileKy();
					if (file.indexOf(signCloudResp.getSignedFileName()) == -1 && !StringUtils.isEmpty(ekycKyso.getChiDinh())) file = ekycKyso.getChiDinh();
					System.out.println("Saved in " + file);
					System.out.println("MimeType: " + signCloudResp.getMimeType());
					FileOutputStream fos = new FileOutputStream(file);
					IOUtils.write(signCloudResp.getSignedFileData(), fos);
					fos.close();
				} else {
					System.out.println(signCloudResp.getSignedFileUUID());
					System.out.println(signCloudResp.getDmsMetaData());
				}

				// for multiple files
				if (signCloudResp.getMultipleSignedFileData() != null) {
					if (!signCloudResp.getMultipleSignedFileData().isEmpty()) {
						for (int i = 0; i < signCloudResp.getMultipleSignedFileData().size(); i++) {
							MultipleSignedFileData multipleSignedFileData = signCloudResp.getMultipleSignedFileData().get(i);
							if (multipleSignedFileData.getSignedFileData() != null) {
								String file = ekycKyso.getDuongDanFileKy();
								if (file.indexOf(multipleSignedFileData.getSignedFileName()) == -1 && !StringUtils.isEmpty(ekycKyso.getChiDinh())) file = ekycKyso.getChiDinh();
								if (file.indexOf(multipleSignedFileData.getSignedFileName()) == -1 && !StringUtils.isEmpty(ekycKyso.getDuongDanFileKySeaBank())) file = ekycKyso.getDuongDanFileKySeaBank();
								System.out.println("Saved in " + file);
								System.out.println("MimeType: " + multipleSignedFileData.getMimeType());
								IOUtils.write(multipleSignedFileData.getSignedFileData(), new FileOutputStream(file));
							} else {
								System.out.println(multipleSignedFileData.getSignedFileUUID());
								System.out.println(multipleSignedFileData.getDmsMetaData());
							}
						}
					}
				}

				String str = ekycKyso.getDuongDanFileKy();

				model.addAttribute("file", enDeCryption.encrypt(str));

				ekycKyso.setTrangThai("1");
				ekycKyso.setAnhTinNhan(params.getAnhChuKy());
				ekycKyso.setKhachHangKy(new Date());
				ekycKyso.setSoLanKy(params.getSoLanKy());

				LOGGER.info("Ky hop dong thanh cong: {}", ekycKyso.getSoCmt() + "|" + ekycKyso.getSoDienThoai());

				String resp = guiThongTinSendContract(ekycKyso, req);
				
				if(loiGuiThongTinSendContract(resp)) {
					if(ekycKyso.getMoTKTTSB() !=null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
						LOGGER.info("Close Acc: " + ekycKyso.getSoDienThoai()+" - "+ekycKyso.getAccountId());
						
						callSeabank.closeAccc(ekycKyso.getAccountId(), ekycKyso.getCoCode(), req);
						
						ekycKyso.setTtTKTT(Contains.TRANG_THAI_MO_TAI_KHOAN_TT_THAT_BAI);
						ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_THAT_BAI);
						ekycKyso.setCloseAcc(Contains.TAI_KHOAN_BI_DONG);
						ekycKyso.setCronDongTk(2);
					}
					ekycKysoRepository.save(ekycKyso);
					model.addAttribute("thongBao", "true");
					
					resetOcrLivenessOtp(params, req);
					setParams(null, req);
					
					ArrayList<FileObject> fileObjects = taoFileGuiMail(ekycKyso, false);

					if (!StringUtils.isEmpty(ekycKyso.getEmail())) {
						email.sendMultipleFile(ekycKyso.getEmail(), "Hợp đồng điện tử pdf", "Xin chào " + ekycKyso.getHoVaTen() + ", \n\n" + "Bạn đã thực hiện thành công hợp đồng vay", fileObjects);
					}
					
					return "demo/kySoDoanhNgiepCaNhan/step/step5";
				}
				JSONObject jsonObject = new JSONObject(resp);
				if(jsonObject.has("code") && jsonObject.getInt("code") == 1) {
					if(ekycKyso.getMoTKTTSB() !=null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_DA_CO_TAI_KHOAN)) {
						LOGGER.info("Close Acc 2: " + ekycKyso.getSoDienThoai()+" - "+ekycKyso.getAccountId());
						
						callSeabank.closeAccc(ekycKyso.getAccountId(), ekycKyso.getCoCode(), req);
						
						ekycKyso.setTtTKTT(Contains.TRANG_THAI_MO_TAI_KHOAN_TT_THAT_BAI);
						ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_THAT_BAI);
						ekycKyso.setCloseAcc(Contains.TAI_KHOAN_BI_DONG);
						ekycKyso.setCronDongTk(2);
					}
				}
				
				luuHopDongSeabankNeuSendContractThanhCong(resp, ekycKyso, model);
				
				
				ArrayList<FileObject> fileObjects = taoFileGuiMail(ekycKyso, false);
				if (showHopDongSeabank(ekycKyso, resp)) {
					ekycKyso.setTtTKTT(Contains.TRANG_THAI_MO_TAI_KHOAN_TT_THANH_CONG);
					ekycKyso.setCronDongTk(2);
					String strSb = ekycKyso.getDuongDanFileKySeaBank();
					model.addAttribute("fileSeaBank", enDeCryption.encrypt(strSb));
					
					fileObjects = taoFileGuiMail(ekycKyso, true);
				}
				
				if(trangThaiHopDongSeabankla222(ekycKyso, resp)) {
					callSeabank.closeAccc(ekycKyso.getAccountId(), ekycKyso.getCoCode(), req);
					ekycKyso.setTtTKTT(Contains.TRANG_THAI_MO_TAI_KHOAN_TT_THAT_BAI);
					ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_THAT_BAI);
					ekycKyso.setCloseAcc(Contains.TAI_KHOAN_BI_DONG);
					ekycKyso.setCronDongTk(2);
				}

				if (!StringUtils.isEmpty(ekycKyso.getChiDinh())) {
					str = ekycKyso.getChiDinh();
					model.addAttribute("fileBaoHiem", enDeCryption.encrypt(str));
				}
				
				if(duocGuiThongTinSangLos(resp, ekycKyso))
					guiThongTinsangLos(ekycKyso, req);
				

				if (!StringUtils.isEmpty(ekycKyso.getEmail())) {
					email.sendMultipleFile(ekycKyso.getEmail(), "Hợp đồng điện tử pdf", "Xin chào " + ekycKyso.getHoVaTen() + ", \n\n" + "Bạn đã thực hiện thành công hợp đồng vay", fileObjects);
				}
				
				ekycKysoRepository.save(ekycKyso);
				
				resetOcrLivenessOtp(params, req);
				setParams(null, req);
				
			} else if (signCloudResp.getResponseCode() == 1004) {
				model.addAttribute("error", "Lỗi OTP");
				kysoPage(allParams, model, params);
				return "demo/kySoDoanhNgiepCaNhan/step/step4";
			} else {
				model.addAttribute("error", "Ký số thất bại");
				kysoPage(allParams, model, params);
				return "demo/kySoDoanhNgiepCaNhan/step/step4";
			}
		} catch (Exception e) {
			LOGGER.error("Error step3 post: {}", e);
			model.addAttribute("error", "Lỗi hệ thống");
			kysoPage(allParams, model, params);
			return "demo/kySoDoanhNgiepCaNhan/step/step4";
		}
		model.addAttribute("LINK_ADMIN", LINK_ADMIN);
		return "demo/kySoDoanhNgiepCaNhan/step/step5";
	}

	private boolean trangThaiHopDongSeabankla222(EkycKyso ekycKyso, String resp) {
		if(ekycKyso.getMoTKTTSB() != null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THAT_BAI)) return false;
		if(!StringUtils.isEmpty(ekycKyso.getDuongDanFileKySeaBank()) && ekycKyso.getMoTKTTSB() != null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
			JSONObject jsonObject = new JSONObject(resp);
			if(jsonObject.has("code") && jsonObject.get("code").toString().equals("222")) return true;
		}
		return false;
	}

	private void luuHopDongSeabankNeuSendContractThanhCong(String resp, EkycKyso ekycKyso, Model model) throws JSONException, FileNotFoundException, IOException {
		JSONObject jsonObject = new JSONObject(resp);
		if(jsonObject.has("code") && jsonObject.getString("code").equals("0")) {
			String file = ekycKyso.getDuongDanFileKySeaBank();
			byte[] democodeBytes = Base64.getDecoder().decode(jsonObject.getJSONObject("data").getString("contract"));
			IOUtils.write(democodeBytes, new FileOutputStream(file));
		}
		
	}

	private boolean showHopDongSeabank(EkycKyso ekycKyso, String resp) {
		if(ekycKyso.getMoTKTTSB() != null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THAT_BAI)) return false;
		if(!StringUtils.isEmpty(ekycKyso.getDuongDanFileKySeaBank()) && ekycKyso.getMoTKTTSB() != null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
			JSONObject jsonObject = new JSONObject(resp);
			if(jsonObject.has("code") && jsonObject.get("code").toString().equals("221")) return false;
			return true;
		}
		return false;
	}

	private boolean loiGuiThongTinSendContract(String resp) {
		if(resp == null) return true;
		JSONObject jsonObject = new JSONObject(resp);
		if(jsonObject.has("status") && jsonObject.getInt("status") == 500) return true;
		if(jsonObject.has("code") && jsonObject.getInt("code") == 101) return true;
		if(jsonObject.has("code") && jsonObject.getInt("code") == 998) return true;
		return false;
	}

	private boolean duocGuiThongTinSangLos(String resp, EkycKyso ekycKyso) {
		if(resp == null) return false;
		if(ekycKyso.getMoTKTTSB()!=null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
			JSONObject jsonObject = new JSONObject(resp);
			if (jsonObject.has("code") && jsonObject.get("code").equals("0")) return true;
			if(jsonObject.has("code") && jsonObject.get("code").toString().equals("222")) return true;
		} else {
			return true;
		}
		return false;
	}

	private String guiThongTinSendContract(EkycKyso ekycKyso, HttpServletRequest req) throws ParseException {
		String base64SeaBank = convertPdfToBase64(ekycKyso.getDuongDanFileKySeaBank());
		String sendContracts = callSeabank.getSendContracts(ekycKyso.getSoCmt(), base64SeaBank, req);
		if(loiSendContract(sendContracts)) sendContracts = callSeabank.getSendContracts(ekycKyso.getSoCmt(), base64SeaBank, req);
		if(loiSendContract(sendContracts)) sendContracts = callSeabank.getSendContracts(ekycKyso.getSoCmt(), base64SeaBank, req);
		if(loiSendContract(sendContracts)) sendContracts = callSeabank.getSendContracts(ekycKyso.getSoCmt(), base64SeaBank, req);
		if(loiSendContract(sendContracts)) sendContracts = callSeabank.getSendContracts(ekycKyso.getSoCmt(), base64SeaBank, req);
		
		if (sendContracts != null) {
			JSONObject jsonObject = new JSONObject(sendContracts);
			if (jsonObject.has("code") && jsonObject.getString("code").equals("0")) {
				ekycKyso.setTtGuiChungTu("Thành công");
			} else {
				ekycKyso.setTtGuiChungTu("Thất bại");
			}
		}
		
		return sendContracts;
	}

	private boolean loiSendContract(String sendContracts) {
		if(sendContracts == null) return true;
		try {
			JSONObject jsonObject = new JSONObject(sendContracts);
			if(jsonObject.has("code") && jsonObject.get("code").toString().equals("SYSTEM_UNAVAILABLE")) return true;
			if(jsonObject.has("status") && jsonObject.get("status").toString().equals("500")) return true;
			if(jsonObject.has("status") && jsonObject.get("status").toString().equals("998")) return true;
			if(jsonObject.has("code") && jsonObject.get("code").toString().equals("998")) return true;
		} catch (Exception e) {
		}
		return false;
	}

	private void guiThongTinsangLos(EkycKyso ekycKyso, HttpServletRequest req) {
		Thongtingiaingan thongtingiaingan = new Gson().fromJson(ekycKyso.getNoiDungGiaiNgan(), Thongtingiaingan.class);
		if (ekycKyso.getMoTKTTSB() == null || ekycKyso.getMoTKTTSB().equals("Không thành công") || ekycKyso.getMoTKTTSB().equals("")) {

			callSeabank.updateLos(ekycKyso.getHoVaTen(), thongtingiaingan.getBankName(),
					thongtingiaingan.getBankCity(), thongtingiaingan.getBankBranch(),
					thongtingiaingan.getBankCode(), thongtingiaingan.getStk(), thongtingiaingan.getTtk(),
					ekycKyso.getCaseId(), req, thongtingiaingan.getSoTienGiaiNgan());
		} else {

			callSeabank.updateLos(covertToString(thongtingiaingan.getTtk().toUpperCase()), "317", "4",
					"317-1317001", thongtingiaingan.getBankCode(), thongtingiaingan.getStk(), "",
					ekycKyso.getCaseId(), req, thongtingiaingan.getSoTienGiaiNgan());
		}
		
	}

	public String convertPdfToBase64(String fileName) {
		String b64 = "";
		try {
			File file = new File(fileName);
			byte[] bytes = Files.readAllBytes(file.toPath());

			b64 = Base64.getEncoder().encodeToString(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b64;
	}

	private ArrayList<FileObject> taoFileGuiMail(EkycKyso ekycKyso, boolean guiFileMoTKSeacbank) {
		ArrayList<FileObject> fileObjects = new ArrayList<FileObject>();
		FileObject fileObject = taoObject("hop_dong_vay.pdf", ekycKyso.getDuongDanFileKy(), ekycKyso.getTrangThai(), true);
		if (fileObject != null)
			fileObjects.add(fileObject);

		if(guiFileMoTKSeacbank) {
			fileObject = taoObject("don_dang_ky_mo_tktt_seabank.pdf", ekycKyso.getDuongDanFileKySeaBank(), ekycKyso.getTrangThai(), true);
			if (fileObject != null)
				fileObjects.add(fileObject);
		}

		fileObject = taoObject("hop_dong_bao_hiem.pdf", ekycKyso.getChiDinh(), ekycKyso.getTrangThai(), true);
		if (fileObject != null)
			fileObjects.add(fileObject);

		fileObject = taoObject("dang_ky_cap_chung_thu_so.pdf", ekycKyso.getAnhTinNhan(), ekycKyso.getTrangThai(), false);
		if (fileObject != null)
			fileObjects.add(fileObject);

		return fileObjects;
	}

	private FileObject taoObject(String tenFile, String pathFile, String trangThai, boolean checkSign) {
		if (StringUtils.isEmpty(pathFile))
			return null;
		FileObject fileObject = new FileObject();
		fileObject.setTen(tenFile);
		String str = pathFile;
//        String str1 = str.split("\\/")[str.split("\\/").length - 1];
//        if(!StringUtils.isEmpty(trangThai) && trangThai.equals("1") && checkSign) {
//        	str = str.replaceAll(str1, ".signed."+str1).replaceAll("/[0-9_]+/", "/");
//        }
		fileObject.setDuongDan(str);
		return fileObject;
	}

	public String postRequestFull(String data, String url, String codeTransaction) {
		return postRequestFull(data, url, token, code, codeTransaction);
	}

	public String postRequestFull(String data, String url, String tokenApi, String codeApi, String codeTransaction) {
		RespApi respApi = new RespApi();
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(data, "UTF-8");
			request.addHeader("content-type", "application/json");
			request.addHeader("token", tokenApi);
			request.addHeader("code", codeApi);
			request.addHeader("Accept-Language", "en");
			request.addHeader("code_transaction", codeTransaction);
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);

			String responseString = new BasicResponseHandler().handleResponse(response);

			return responseString;
		} catch (Exception e) {
			LOGGER.error("Error postRequestFull: {}", e);
			respApi.setStatus(400);
			respApi.setMessage("Lỗi hệ thống");
		}
		return new Gson().toJson(respApi);
	}

	public String dangKyKySo(HttpServletRequest req, Map<String, String> allParams, Model model,
			RedirectAttributes redirectAttributes) {
		JSONObject jsonResp = new JSONObject();
		forwartParams(allParams, model);
		ParamsKbank params = getParams(req);
		if (params == null) {
			jsonResp.put("status", 405);
			return jsonResp.toString();
		}

		if (!validOtpOCrLiveness(params))
			return redirect();

		if (StringUtils.isEmpty(params.getAnhChuKy())) {
			LOGGER.info("EMPTY SIGN REGISTER: " + params.getSoCmt());
			return "redirect:/khach-hang/ky-so/step1";
		}

		try {
			String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());

			JSONObject jsonObjectPr = new JSONObject(text);

			EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(jsonObjectPr.getString("soCmt"), params.getMatKhau());

			forwartParams(allParams, model);
			SignCloudResp signCloudResp;
			String agreementUUID = UUID.randomUUID().toString();
			String billCode = "";

			ObjectMapper objectMapper = new ObjectMapper();
			
			boolean demKySo = false;
			
			if (!StringUtils.isEmpty(ekycKyso.getUuidKySo()) 
					&& !StringUtils.isEmpty(ekycKyso.getBillCodeKySo()) 
					&& ekycKyso.getThoiGianHetHanKySo() != null 
					&& ekycKyso.getThoiGianHetHanKySo() > System.currentTimeMillis()) {
				agreementUUID = ekycKyso.getUuidKySo();
				
				LOGGER.info("Send again esign: " + agreementUUID);
				LOGGER.info("Send again esign phone: " + ekycKyso.getSoDienThoai());
				
			} else {
				ParamsKbank paramsSign = taoThongTinGuiDangKyChuKySo(ekycKyso, params);

				String jsonRegister = guiThongTinDangKyKySo(paramsSign, agreementUUID);
				SignCloudResp signCloudRespRegister = objectMapper.readValue(jsonRegister, SignCloudResp.class);

				LOGGER.info("Send new esign: " + agreementUUID);
				LOGGER.info("Send new esign phone: " + ekycKyso.getSoDienThoai());
				
				long thoiGianHetHanKySo = System.currentTimeMillis() + 24*60*60*1000L;
				ekycKyso.setThoiGianHetHanKySo(thoiGianHetHanKySo);
				
				if (signCloudRespRegister.getResponseCode() != 0) {
					jsonResp.put("message", "Không đăng ký được chữ ký ");
					jsonResp.put("status", 400);

					return jsonResp.toString();
				}
				demKySo = true;
			}

			String pathPdf = layDuongDanFileKy(ekycKyso);
			String nameFile = layTenFile(pathPdf);
			LOGGER.info("pathPdf: {}", pathPdf);
			LOGGER.info("nameFile: {}", nameFile);
			
			String pathPdfBaoHiem = layDuongDanFileBaoHiem(ekycKyso);
			String nameFileBaoHiem = layTenFile(pathPdfBaoHiem);
			LOGGER.info("pathPdfBaoHiem: {}", pathPdfBaoHiem);
			LOGGER.info("nameFileBaoHiem: {}", nameFileBaoHiem);
			
			String pathPdfSeaBank = layDuongDanFileSeabank(ekycKyso);
			String nameFileSeaBank = layTenFile(pathPdfSeaBank);
			LOGGER.info("pathPdfSeaBank: {}", pathPdfSeaBank);
			LOGGER.info("nameFileSeaBank: {}", nameFileSeaBank);
			
			String jsonResponse = guiThongTinKySo(req, pathPdf, nameFile, agreementUUID, ekycKyso, pathPdfBaoHiem, nameFileBaoHiem, pathPdfSeaBank, nameFileSeaBank);

			signCloudResp = objectMapper.readValue(jsonResponse, SignCloudResp.class);

			if (signCloudResp.getResponseCode() != 1007) {
				jsonResp.put("message", "Không gửi được chữ ký ");
				jsonResp.put("status", 400);

				LOGGER.error("Send new esign error: " + signCloudResp.getResponseCode());
				LOGGER.error("Send new esign phone error: " + ekycKyso.getSoDienThoai());

				return jsonResp.toString();
			}
			billCode = signCloudResp.getBillCode();

			if (!MOI_TRUONG.equals("dev")) {
				sendSMS.postRequestSMS(ekycKyso.getSoDienThoai(), "Quy Khach dang thuc hien giao dich xac thuc va ky Hop dong tin dung voi PTF. Mat khau OTP cua Quy Khach la: " + signCloudResp.getAuthorizeCredential(), ekycKyso);
			}

			ekycKyso.setUuidKySo(agreementUUID);
			ekycKyso.setBillCodeKySo(billCode);
			ekycKyso.setDuongDanFileKy(pathPdf);
			if(demKySo) {
				int soLanKy = ekycKyso.getSoLanKy() == null?1:ekycKyso.getSoLanKy()+1;
				params.setSoLanKy(soLanKy);
				setParams(params, req);
			}
			ekycKysoRepository.save(ekycKyso);

			jsonResp.put("otp", signCloudResp.getAuthorizeCredential());
			jsonResp.put("maKy", billCode);
			jsonResp.put("pathPdf", pathPdf);
			jsonResp.put("nameFile", nameFile);
			jsonResp.put("agreementUUID", agreementUUID);

			params.setUuid(agreementUUID);
			params.setBilCode(signCloudResp.getBillCode());
			setParams(params, req);
		} catch (ErrorException e) {
			LOGGER.error("Error register post: {}", e);
			jsonResp.put("message", e.getMessage());
			jsonResp.put("status", 400);

			return jsonResp.toString();
		} catch (Exception e) {
			LOGGER.error("Error register2 post: {}", e);
			jsonResp.put("message", "Lỗi hệ thống");
			jsonResp.put("status", 400);

			return jsonResp.toString();
		}

		jsonResp.put("status", 200);

		return jsonResp.toString();
	}

	private String layDuongDanFileSeabank(EkycKyso ekycKyso) {
		if (!StringUtils.isEmpty(ekycKyso.getDuongDanFileKySeaBank())) {
			return layDuongDanFile(ekycKyso, Contains.FILE_HOP_DONG_SEABANK, ekycKyso.getDuongDanFileKySeaBank());
		}

		if(ekycKyso.getMoTKTTSB() != null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
			
		} else {
			return "";
		}
		return "";
	}

	private String layTenFile(String pathPdf) {
		if(!StringUtils.isEmpty(pathPdf)) {
			String[] arr = pathPdf.split("\\/");
			return arr[arr.length - 1];
		}
		return "";
	}

	private String layDuongDanFileBaoHiem(EkycKyso ekycKyso) {
		String pathPdfBaoHiem = "";
		if (!StringUtils.isEmpty(ekycKyso.getChiDinh())) {
			pathPdfBaoHiem = layDuongDanFile(ekycKyso, Contains.FILE_HOP_DONG_BAO_HIEM, ekycKyso.getChiDinh());
		}
		
		return pathPdfBaoHiem;
	}

	private ParamsKbank taoThongTinGuiDangKyChuKySo(EkycKyso ekycKyso, ParamsKbank params) {
		ParamsKbank paramsSign = new ParamsKbank();
		FormInfo formInfo = new FormInfo();
		formInfo.setHoVaTen(ekycKyso.getHoVaTen());
		formInfo.setSoCmt(ekycKyso.getSoCmt());
		formInfo.setDiaChi(ekycKyso.getNoiTru());
		formInfo.setThanhPho(layThongTinThanhPho(ekycKyso));
		formInfo.setQuocGia("Việt Nam");
		paramsSign.setSoDienThoai(ekycKyso.getSoDienThoai());
		paramsSign.setFormInfo(formInfo);
		paramsSign.setAnhMatTruoc(CommonUtils.encodeFileToBase64Binary(new File(ekycKyso.getAnhMatTruoc())));
		paramsSign.setAnhMatSau(CommonUtils.encodeFileToBase64Binary(new File(ekycKyso.getAnhMatSau())));
		paramsSign.setAnhChuKy(CommonUtils.encodeFileToBase64Binary(new File(params.getAnhChuKy())));
		
		return paramsSign;
	}

	private String layDuongDanFileKy(EkycKyso ekycKyso) {
		String pathPdf = layDuongDanFile(ekycKyso, Contains.FILE_HOP_DONG_VAY,  ekycKyso.getDuongDanFileKy());

		FileHandling fileHandling = new FileHandling();
		String imgFolderLog = fileHandling.getFolder(KY_SO_FOLDER+"/") ;

		String outputFilePathPdf = imgFolderLog+ UUID.randomUUID().toString() + ".pdf";
		fileHandling.createFolder(configProperties.getConfig().getImage_folder_log() + code + "/");
		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		Thongtingiaingan thongtingiaingan = gson.fromJson(ekycKyso.getNoiDungGiaiNgan(), Thongtingiaingan.class);
		pdfHandling.themThongTin(pathPdf, outputFilePathPdf, thongtingiaingan.getTtk(), thongtingiaingan.getStk(), thongtingiaingan.getTenNganHang(), 
				thongtingiaingan.getChiNhanh(), thongtingiaingan.getSoTienGiaiNgan());
		
		return outputFilePathPdf;
	}

	private String layDuongDanFile(EkycKyso ekycKyso, String fileKey, String pathFile) {
		if(!StringUtils.isEmpty(ekycKyso.getDuongDanFileGoc()))  {
			JSONObject jsonObject = new JSONObject(ekycKyso.getDuongDanFileGoc());
			if(jsonObject.has(fileKey)) return jsonObject.getString(fileKey);
		}
		return pathFile;
	}

	private String layThongTinThanhPho(EkycKyso ekycKyso) {
		try {
			JSONObject objectRespGiayTo = new JSONObject(ekycKyso.getOcr());
			Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);

			return ocr.getChiTietNoiTru().getProvince();
		} catch (Exception e) {
		}
		return "Hà Nội";
	}

	private String guiThongTinDangKyKySo(ParamsKbank params, String agreementUUID) throws Exception {
		FormInfo formInfo = params.getFormInfo();
		System.out.println(new Gson().toJson(formInfo));

		byte[] frontSideOfIDDocument = Base64.getDecoder().decode(params.getAnhMatTruoc());
		byte[] backSideOfIDDocument = Base64.getDecoder().decode(params.getAnhMatSau());
		byte[] requestForm = Base64.getDecoder().decode(params.getAnhChuKy());

		String json = "";
		if (MOI_TRUONG.equals("dev") || MOI_TRUONG.equals("test")) {
			eSignCall2 service = new eSignCall2();

			json = service.prepareCertificateForSignCloud(agreementUUID, formInfo.getHoVaTen(), formInfo.getSoCmt(),
					formInfo.getSoCmt(), formInfo.getDiaChi(), formInfo.getThanhPho(), formInfo.getQuocGia(),
					frontSideOfIDDocument, backSideOfIDDocument, formInfo.getEmail(), params.getSoDienThoai(),
					requestForm);
		} else {
			eSignCall service = new eSignCall();

			json = service.prepareCertificateForSignCloud(agreementUUID, formInfo.getHoVaTen(), formInfo.getSoCmt(),
					formInfo.getSoCmt(), formInfo.getDiaChi(), formInfo.getThanhPho(), formInfo.getQuocGia(),
					frontSideOfIDDocument, backSideOfIDDocument, formInfo.getEmail(), params.getSoDienThoai(),
					requestForm);
		}
		return json;
	}

	private String guiThongTinKySo(HttpServletRequest req, String pathPdf, String nameFile, String agreementUUID,
			EkycKyso ekycKyso, String pathPdfBaoHiem, String nameFileBaoHiem, String pathPdfSeaBank,
			String nameFileSeaBank) throws Exception {
		byte[] fileData01, fileData02, fileData03;
		String mimeType01, mimeType02, mimeType03;
		SignCloudMetaData signCloudMetaDataForItem01;
		SignCloudMetaData signCloudMetaDataForItem02;
		SignCloudMetaData signCloudMetaDataForItem03;
		HashMap<String, String> singletonSigningForItem01;
		HashMap<String, String> singletonSigningForItem02;
		HashMap<String, String> singletonSigningForItem03;
		MultipleSigningFileData fileItem01;
		MultipleSigningFileData fileItem02;
		MultipleSigningFileData fileItem03;

		// file thứ nhất
		// =========================================================================
		fileData01 = IOUtils.toByteArray(new FileInputStream(pathPdf));
		mimeType01 = ESignCloudConstant.MIMETYPE_PDF;
		fileItem01 = new MultipleSigningFileData();

		fileItem01.setSigningFileData(fileData01);
		fileItem01.setMimeType(mimeType01);
		fileItem01.setSigningFileName(nameFile);

		FormInfo formInfo = new Gson().fromJson(ekycKyso.getNoiDungForm(), FormInfo.class);

		signCloudMetaDataForItem01 = new SignCloudMetaData();
		singletonSigningForItem01 = new HashMap<>();
		singletonSigningForItem01.put("COUNTERSIGNENABLED", "True");
		singletonSigningForItem01.put("PAGENO", "20");
		singletonSigningForItem01.put("POSITIONIDENTIFIER", formInfo.getViTriKyCaNhan());
		singletonSigningForItem01.put("RECTANGLEOFFSET", "-30,-70");
		singletonSigningForItem01.put("RECTANGLESIZE", "170,70");
		singletonSigningForItem01.put("VISIBLESIGNATURE", "True");
		singletonSigningForItem01.put("VISUALSTATUS", "False");
		singletonSigningForItem01.put("IMAGEANDTEXT", "False");
		singletonSigningForItem01.put("TEXTDIRECTION", "LEFTTORIGHT");
		singletonSigningForItem01.put("SHOWSIGNERINFO", "True");
		singletonSigningForItem01.put("SIGNERINFOPREFIX", "Ký bởi:");
		singletonSigningForItem01.put("SHOWDATETIME", "True");
		singletonSigningForItem01.put("DATETIMEPREFIX", "Ký ngày:");
		singletonSigningForItem01.put("SHOWREASON", "True");
		singletonSigningForItem01.put("SIGNREASONPREFIX", "Lý do:");
		singletonSigningForItem01.put("SIGNREASON", "Tôi đồng ý");
		singletonSigningForItem01.put("SHOWLOCATION", "True");
		singletonSigningForItem01.put("LOCATION", "Hà Nội");
		singletonSigningForItem01.put("LOCATIONPREFIX", "Nơi ký:");
		singletonSigningForItem01.put("TEXTCOLOR", "black");
		singletonSigningForItem01.put("SHADOWSIGNATUREPROPERTIES", "all");

		signCloudMetaDataForItem01.setSingletonSigning(singletonSigningForItem01);
		fileItem01.setSignCloudMetaData(signCloudMetaDataForItem01);

		List<MultipleSigningFileData> listOfSigningFileData = new ArrayList<>();
		listOfSigningFileData.add(fileItem01);

		// file thứ hai
		// =============================================================================
		if (!StringUtils.isEmpty(pathPdfBaoHiem)) {
			fileData02 = IOUtils.toByteArray(new FileInputStream(pathPdfBaoHiem));
			mimeType02 = ESignCloudConstant.MIMETYPE_PDF;
			fileItem02 = new MultipleSigningFileData();

			fileItem02.setSigningFileData(fileData02);
			fileItem02.setMimeType(mimeType02);
			fileItem02.setSigningFileName(nameFileBaoHiem);

			signCloudMetaDataForItem02 = new SignCloudMetaData();
			singletonSigningForItem02 = new HashMap<>();
			singletonSigningForItem02.put("COUNTERSIGNENABLED", "True");
			singletonSigningForItem02.put("PAGENO", "Last");
			singletonSigningForItem02.put("POSITIONIDENTIFIER", formInfo.getViTriKyCaNhan());
			singletonSigningForItem02.put("RECTANGLEOFFSET", "-30,-70");
			singletonSigningForItem02.put("RECTANGLESIZE", "170,70");
			singletonSigningForItem02.put("VISIBLESIGNATURE", "True");
			singletonSigningForItem02.put("VISUALSTATUS", "False");
			singletonSigningForItem02.put("IMAGEANDTEXT", "False");
			singletonSigningForItem02.put("TEXTDIRECTION", "LEFTTORIGHT");
			singletonSigningForItem02.put("SHOWSIGNERINFO", "True");
			singletonSigningForItem02.put("SIGNERINFOPREFIX", "Ký bởi:");
			singletonSigningForItem02.put("SHOWDATETIME", "True");
			singletonSigningForItem02.put("DATETIMEPREFIX", "Ký ngày:");
			singletonSigningForItem02.put("SHOWREASON", "True");
			singletonSigningForItem02.put("SIGNREASONPREFIX", "Lý do:");
			singletonSigningForItem02.put("SIGNREASON", "Tôi đồng ý");
			singletonSigningForItem02.put("SHOWLOCATION", "True");
			singletonSigningForItem02.put("LOCATION", "Hà Nội");
			singletonSigningForItem02.put("LOCATIONPREFIX", "Nơi ký:");
			singletonSigningForItem02.put("TEXTCOLOR", "black");
			singletonSigningForItem02.put("SHADOWSIGNATUREPROPERTIES", "all");
			signCloudMetaDataForItem02.setSingletonSigning(singletonSigningForItem02);

			fileItem02.setSignCloudMetaData(signCloudMetaDataForItem02);

			listOfSigningFileData.add(fileItem02);
		}

		// file thứ 3
		// =============================================================================
		if (!StringUtils.isEmpty(pathPdfSeaBank)) {
			fileData03 = IOUtils.toByteArray(new FileInputStream(pathPdfSeaBank));
			mimeType03 = ESignCloudConstant.MIMETYPE_PDF;
			fileItem03 = new MultipleSigningFileData();

			fileItem03.setSigningFileData(fileData03);
			fileItem03.setMimeType(mimeType03);
			fileItem03.setSigningFileName(nameFileSeaBank);

			signCloudMetaDataForItem03 = new SignCloudMetaData();
			singletonSigningForItem03 = new HashMap<>();
			singletonSigningForItem03.put("COUNTERSIGNENABLED", "True");
			singletonSigningForItem03.put("PAGENO", "4");
			singletonSigningForItem03.put("POSITIONIDENTIFIER", formInfo.getViTriKyCaNhan());
			singletonSigningForItem03.put("RECTANGLEOFFSET", "-30,-70");
			singletonSigningForItem03.put("RECTANGLESIZE", "170,70");
			singletonSigningForItem03.put("VISIBLESIGNATURE", "True");
			singletonSigningForItem03.put("VISUALSTATUS", "False");
			singletonSigningForItem03.put("IMAGEANDTEXT", "False");
			singletonSigningForItem03.put("TEXTDIRECTION", "LEFTTORIGHT");
			singletonSigningForItem03.put("SHOWSIGNERINFO", "True");
			singletonSigningForItem03.put("SIGNERINFOPREFIX", "Ký bởi:");
			singletonSigningForItem03.put("SHOWDATETIME", "True");
			singletonSigningForItem03.put("DATETIMEPREFIX", "Ký ngày:");
			singletonSigningForItem03.put("SHOWREASON", "True");
			singletonSigningForItem03.put("SIGNREASONPREFIX", "Lý do:");
			singletonSigningForItem03.put("SIGNREASON", "Tôi đồng ý");
			singletonSigningForItem03.put("SHOWLOCATION", "True");
			singletonSigningForItem03.put("LOCATION", "Hà Nội");
			singletonSigningForItem03.put("LOCATIONPREFIX", "Nơi ký:");
			singletonSigningForItem03.put("TEXTCOLOR", "black");
			singletonSigningForItem03.put("SHADOWSIGNATUREPROPERTIES", "all");
			signCloudMetaDataForItem03.setSingletonSigning(singletonSigningForItem03);

			fileItem03.setSignCloudMetaData(signCloudMetaDataForItem03);

			listOfSigningFileData.add(fileItem03);
		}

		String jsonResponse = "";
		if (MOI_TRUONG.equals("dev") || MOI_TRUONG.equals("test")) {
			eSignCall2 service = new eSignCall2();
			jsonResponse = service.prepareMultipleFilesForSignCloud(agreementUUID,
					ESignCloudConstant.AUTHORISATION_METHOD_SMS, null, notificationTemplate, notificationSubject,
					listOfSigningFileData);
		} else {
			eSignCall service = new eSignCall();
			jsonResponse = service.prepareMultipleFilesForSignCloud(agreementUUID,
					ESignCloudConstant.AUTHORISATION_METHOD_SMS, null, notificationTemplate, notificationSubject,
					listOfSigningFileData);
		}
		return jsonResponse;

	}

	public ResponseEntity<byte[]> getImage(HttpServletResponse resp, Map<String, String> allParams, String path) {
		try {
			String pathImg = enDeCryption.decrypt(path);
			File file = new File(pathImg);

			byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(file));
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResponseEntity<InputStreamResource> download(HttpServletResponse resp, Map<String, String> allParams,
			String path) {
		try {
			String pathImg = enDeCryption.decrypt(path);
			File file = new File(pathImg);

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
					.contentType(MediaType.APPLICATION_PDF).contentLength(file.length()).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String themLoi(String json, String check) {
		if (StringUtils.isEmpty(json))
			return check;
		json += "|" + check;
		String[] arr = json.split("\\|");

		if (arr.length >= 11)
			arr = Arrays.copyOfRange(arr, 1, arr.length);
		json = "";
		for (String string : arr) {
			if (StringUtils.isEmpty(json))
				json = string;
			else
				json += "|" + string;
		}

		return json;
	}

	private void resetLiveness(ParamsKbank params, HttpServletRequest req) {
		if (params != null) {
			params.setCheckLiveness(false);
			setParams(params, req);
		}
	}

	private void resetOcrLiveness(ParamsKbank params, HttpServletRequest req) {
		if (params != null) {
			params.setCheckLiveness(false);
			params.setCheckOcr(false);
			setParams(params, req);
		}
	}

	private void resetOcrLivenessOtp(ParamsKbank params, HttpServletRequest req) {
		if (params != null) {
			params.setCheckLiveness(false);
			params.setCheckOcr(false);
			params.setCheckOtp(false);
			setParams(params, req);
		}
	}

	private boolean validOtp(ParamsKbank params) {
		if (params == null)
			return false;
		if (!params.getCheckOtp())
			return false;
		return true;
	}

	private boolean validOtpOCr(ParamsKbank params) {
		if (params == null)
			return false;
		if (!params.getCheckOtp())
			return false;
		if (!params.getCheckOcr())
			return false;
		return true;
	}

	private boolean validOtpOCrLiveness(ParamsKbank params) {
		if (params == null)
			return false;
		if (!params.getCheckOtp())
			return false;
		if (!params.getCheckOcr())
			return false;
		if (!params.getCheckLiveness())
			return false;
		return true;
	}

	public void forwartParams(Map<String, String> allParams, Model model) {
		for (Entry<String, String> entry : allParams.entrySet()) {
			model.addAttribute(entry.getKey(), entry.getValue());
		}
	}

	public String postRequest(String data, String url, ParamsKbank params) {
		return postRequest(data, url, token, code, params);
	}

	public String postRequest(String data, String url, String tokenApi, String codeApi, ParamsKbank paramsKbank) {
		RespApi respApi = new RespApi();
		try {
			JSONObject jsonObject = new JSONObject(data);
			jsonObject.put("soDienThoai", paramsKbank.getSoDienThoai());
			jsonObject.put("hoVaTen", paramsKbank.getHoVaTen());
			jsonObject.put("soCmt", paramsKbank.getSoCmt());
			jsonObject.put("soHopDong", paramsKbank.getSoHopDong());
			jsonObject.put("codeTransaction", paramsKbank.getCodeTransaction());

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(API_SERVICE + url);
			StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
			request.addHeader("content-type", "application/json");
			request.addHeader("token", tokenApi);
			request.addHeader("code", codeApi);
			request.addHeader("code_transaction", paramsKbank.getCodeTransaction());
			request.addHeader("code-transaction", paramsKbank.getCodeTransaction());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);

			String responseString = new BasicResponseHandler().handleResponse(response);

			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
			respApi.setStatus(400);
			respApi.setMessage("Lỗi hệ thống");
		}
		return new Gson().toJson(respApi);
	}

	private String redirect() {
		return "redirect:/khach-hang/ky-so/step1";
	}

	public String getstep2(HttpServletRequest req, Map<String, String> allParams, Model model) {
		ParamsKbank params = getParams(req);
		if (params == null)
			return redirect();
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

		if (!validOtp(params))
			return redirect();
		resetOcrLiveness(params, req);

		return "demo/kySoDoanhNgiepCaNhan/step/step3";
	}

	public String moTaiKhoanStep1(HttpServletRequest req, Map<String, String> allParams, Model model) {
		ParamsKbank params = getParams(req);
		if (params == null) return "redirect:/khach-hang/ky-so/step1";
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());
		if (!validOtpOCrLiveness(params)) return redirect();

		forwartParams(allParams, model);

		return "demo/kySoDoanhNgiepCaNhan/step/dangKyTaiKhoan";
	}

	public String moTaiKhoanStep2(HttpServletRequest req, Map<String, String> allParams, Model model) throws ParseException, KeyManagementException, NoSuchAlgorithmException, IOException {
		ParamsKbank params = getParams(req);
		if (params == null) return redirect();
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());
		if (!validOtpOCrLiveness(params)) return redirect();

		forwartParams(allParams, model);

		EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());

		if(dongTaiKhoanThanhCong(ekycKyso, req)) {
			thongBaoDongTaiKhoan(model, req, params);
			return thongTinGiaiNgan(req, model);
		}
		
		if(ekycKyso.getCloseAcc() != null && ekycKyso.getCloseAcc().equals(Contains.TAI_KHOAN_BI_DONG)) {
			thongBaoDongTaiKhoan(model, req, params);
			return thongTinGiaiNgan(req, model);
		}
		
		ThongTinNguoiDungTuLOS thongTinNguoiDungTuLOS = callSeabank.layThongTinnguoiDungTuLOS(ekycKyso.getCaseId(), req);
		if (thongTinNguoiDungTuLOS == null) {
			model.addAttribute("error", "Không lấy được thông tin khách hàng từ LOS");

			return "demo/kySoDoanhNgiepCaNhan/step/tkttKhongThanhCong";

		} else {
			params.setThongTinNguoiDungTuLOS(new Gson().toJson(thongTinNguoiDungTuLOS));
			ekycKyso.setThongTinNguoiDungTuLos(new Gson().toJson(thongTinNguoiDungTuLOS));
			setParams(params, req);
			String response = callSeabank.moTaiKhoan(taoThongTinGuiMoTaiKhoan(params, thongTinNguoiDungTuLOS, ekycKyso), req);
			if (response == null) {
				model.addAttribute("errorPopup", "Tạo tài khoản không thành công");
				ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_THAT_BAI);
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/tkttKhongThanhCong";
			}

			return taoDanhSachTaiKhoan(response, req, model, ekycKyso, params);
		}

	}
	private void thongBaoDongTaiKhoan(Model model, HttpServletRequest req, ParamsKbank params) {
		if(params.getThongBaoCloseAcc() == null || (params.getThongBaoCloseAcc() != null && !params.getThongBaoCloseAcc())) {
			params.setThongBaoCloseAcc(true);
			setParams(params, req);
			model.addAttribute("dongTaiKhoan", "true");
		}
	}

	private String taoDanhSachTaiKhoan(String response, HttpServletRequest req, Model model, EkycKyso ekycKyso, ParamsKbank params) {
		JSONObject jsonObject = new JSONObject(response);
		DanhSachTaiKhoan danhSachTaiKhoan = callSeabank.taoDanhSachTaiKhoan(jsonObject);
		LOGGER.info("danhSachTaiKhoan: " + danhSachTaiKhoan.getTaiKhoans());
		model.addAttribute("danhSachTaiKhoan", danhSachTaiKhoan.getTaiKhoans());
		params.setResponseMoTaiKhoan(response);
		setParams(params, req);
		
//		if(StringUtils.isEmpty(ekycKyso.getMoTKTTSB())) {
			if (moTaiKhoanThanhCong(jsonObject)) {
				ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_THANH_CONG);
				ekycKyso.setNgayYCMoTKTT(new Date());
				ekycKyso.setCoCode(convertCoCode(danhSachTaiKhoan.getTaiKhoans().get(0).getCoCode()));
				ekycKyso.setAccountId(danhSachTaiKhoan.getTaiKhoans().get(0).getSoTaiKhoan());
				ekycKyso.setThongTinTaiKhoanSeabank(taoThongTinTaiKhoanSeabankNew(danhSachTaiKhoan.getTaiKhoans().get(0)));
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/thongTinTaiKhoan";
			} else {
				if(danhSachTaiKhoan.getTaiKhoans().size() == 1) {
					ekycKyso.setCoCode(convertCoCode(danhSachTaiKhoan.getTaiKhoans().get(0).getCoCode()));
					ekycKyso.setAccountId(danhSachTaiKhoan.getTaiKhoans().get(0).getSoTaiKhoan());
				}
				if(trangThaiMoTaiKhoanSeabankKhongThanhCong(ekycKyso))
					ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_DA_CO_TAI_KHOAN);
				ekycKysoRepository.save(ekycKyso);
				return "demo/kySoDoanhNgiepCaNhan/step/danhSachTaiKhoan";
			}
//		} else {
//			ekycKysoRepository.save(ekycKyso);
//			return "demo/kySoDoanhNgiepCaNhan/step/danhSachTaiKhoan";
//		}
	}

	private boolean trangThaiMoTaiKhoanSeabankKhongThanhCong(EkycKyso ekycKyso) {
		if(ekycKyso.getMoTKTTSB() == null) return true;
		if(ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) return false;
		return true;
	}

	private String taoThongTinTaiKhoanSeabankList(TaiKhoan taiKhoan) {
		try {
			return new Gson().toJson(taiKhoan);
		} catch (Exception e) {
		}
		return null;
	}

	private String taoThongTinTaiKhoanSeabankNew(TaiKhoan taiKhoan) {
		try {
			String[] arr = taiKhoan.getCoCode().split("-");
			taiKhoan.setCoCode(arr[0].trim());
			taiKhoan.setCoName(arr[1].trim());
			
			return new Gson().toJson(taiKhoan);
		} catch (Exception e) {
		}
		return null;
	}

	private Boolean dongTaiKhoanThanhCong(EkycKyso ekycKyso, HttpServletRequest req) {
		if(ekycKyso.getCloseAcc() != null && ekycKyso.getCloseAcc().equals(Contains.TAI_KHOAN_BI_DONG)) return false;
		if(ngayMoTaiKhoanThanhToanVuotThoiGianQuyDinh(ekycKyso)) {
			LOGGER.info("Close Acc: " + ekycKyso.getSoDienThoai()+" - "+ekycKyso.getAccountId());
			
			callSeabank.closeAccc(ekycKyso.getAccountId(), ekycKyso.getCoCode(), req);
			
			ekycKyso.setTtTKTT(Contains.TRANG_THAI_MO_TAI_KHOAN_TT_THAT_BAI);
			ekycKyso.setMoTKTTSB(Contains.MO_TKTTSB_THAT_BAI);
			ekycKyso.setCloseAcc(Contains.TAI_KHOAN_BI_DONG);
			ekycKyso.setCronDongTk(2);
			ekycKysoRepository.save(ekycKyso);
			return true;
		}
		return false;
	}
	private String convertCoCode(String coCode) {
		try {
			return coCode.split("-")[0].trim();
		} catch (Exception e) {
		}
		return coCode;
	}

	private Boolean ngayMoTaiKhoanThanhToanVuotThoiGianQuyDinh(EkycKyso ekycKyso) {
		if(ekycKyso.getMoTKTTSB() !=null && ekycKyso.getMoTKTTSB().equals(Contains.MO_TKTTSB_THANH_CONG)) {
			long thoiGianQuyDinh = 61 * 60 * 1000L;
			long thoiGianKiemTra = thoiGianQuyDinh + ekycKyso.getNgayYCMoTKTT().getTime();
			if(thoiGianKiemTra < System.currentTimeMillis()) return true;
		}
		return false;
	}
	private boolean moTaiKhoanThanhCong(JSONObject jsonObject) {
		if(jsonObject.getString("code").equals("0")) return true;
		return false;
	}

	public DanhSachNganHang listCity(Model model)
			throws ParseException, KeyManagementException, NoSuchAlgorithmException, IOException {
		String resposeCity = "";
		if (MOI_TRUONG.equals("dev")) {
			resposeCity = "{\"success\":true,\"error\":null,\"data\":{\"total\":63,\"items\":[{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HOA BINH\",\"value\":\"18\",\"parentType\":null,\"metaData\":{\"id\":18,\"name\":\"HOA BINH\",\"code\":null,\"name_vn\":\"HOA BINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HA GIANG\",\"value\":\"19\",\"parentType\":null,\"metaData\":{\"id\":19,\"name\":\"HA GIANG\",\"code\":null,\"name_vn\":\"HA GIANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"LAI CHAU\",\"value\":\"24\",\"parentType\":null,\"metaData\":{\"id\":24,\"name\":\"LAI CHAU\",\"code\":null,\"name_vn\":\"LAI CHAU\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"TUYEN QUANG\",\"value\":\"27\",\"parentType\":null,\"metaData\":{\"id\":27,\"name\":\"TUYEN QUANG\",\"code\":null,\"name_vn\":\"TUYEN QUANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"YEN BAI\",\"value\":\"29\",\"parentType\":null,\"metaData\":{\"id\":29,\"name\":\"YEN BAI\",\"code\":null,\"name_vn\":\"YEN BAI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"NGHE AN\",\"value\":\"38\",\"parentType\":null,\"metaData\":{\"id\":38,\"name\":\"NGHE AN\",\"code\":null,\"name_vn\":\"NGHE AN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"DAK LAK\",\"value\":\"50\",\"parentType\":null,\"metaData\":{\"id\":50,\"name\":\"DAK LAK\",\"code\":null,\"name_vn\":\"DAK LAK\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"QUANG BINH\",\"value\":\"52\",\"parentType\":null,\"metaData\":{\"id\":52,\"name\":\"QUANG BINH\",\"code\":null,\"name_vn\":\"QUANG BINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"QUANG TRI\",\"value\":\"53\",\"parentType\":null,\"metaData\":{\"id\":53,\"name\":\"QUANG TRI\",\"code\":null,\"name_vn\":\"QUANG TRI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"THUA THIEN-HUE\",\"value\":\"54\",\"parentType\":null,\"metaData\":{\"id\":54,\"name\":\"THUA THIEN-HUE\",\"code\":null,\"name_vn\":\"THUA THIEN-HUE\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BINH DINH\",\"value\":\"56\",\"parentType\":null,\"metaData\":{\"id\":56,\"name\":\"BINH DINH\",\"code\":null,\"name_vn\":\"BINH DINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"GIA LAI\",\"value\":\"59\",\"parentType\":null,\"metaData\":{\"id\":59,\"name\":\"GIA LAI\",\"code\":null,\"name_vn\":\"GIA LAI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"KON TUM\",\"value\":\"60\",\"parentType\":null,\"metaData\":{\"id\":60,\"name\":\"KON TUM\",\"code\":null,\"name_vn\":\"KON TUM\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BINH THUAN\",\"value\":\"62\",\"parentType\":null,\"metaData\":{\"id\":62,\"name\":\"BINH THUAN\",\"code\":null,\"name_vn\":\"BINH THUAN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"LAM DONG\",\"value\":\"63\",\"parentType\":null,\"metaData\":{\"id\":63,\"name\":\"LAM DONG\",\"code\":null,\"name_vn\":\"LAM DONG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"TINH BA RIA-VUNG TAU\",\"value\":\"64\",\"parentType\":null,\"metaData\":{\"id\":64,\"name\":\"TINH BA RIA-VUNG TAU\",\"code\":null,\"name_vn\":\"TINH BA RIA-VUNG TAU\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"CAN THO\",\"value\":\"71\",\"parentType\":null,\"metaData\":{\"id\":71,\"name\":\"CAN THO\",\"code\":null,\"name_vn\":\"CAN THO\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"LONG AN\",\"value\":\"72\",\"parentType\":null,\"metaData\":{\"id\":72,\"name\":\"LONG AN\",\"code\":null,\"name_vn\":\"LONG AN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"TRA VINH\",\"value\":\"74\",\"parentType\":null,\"metaData\":{\"id\":74,\"name\":\"TRA VINH\",\"code\":null,\"name_vn\":\"TRA VINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BEN TRE\",\"value\":\"75\",\"parentType\":null,\"metaData\":{\"id\":75,\"name\":\"BEN TRE\",\"code\":null,\"name_vn\":\"BEN TRE\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"AN GIANG\",\"value\":\"76\",\"parentType\":null,\"metaData\":{\"id\":76,\"name\":\"AN GIANG\",\"code\":null,\"name_vn\":\"AN GIANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"KIEN GIANG\",\"value\":\"77\",\"parentType\":null,\"metaData\":{\"id\":77,\"name\":\"KIEN GIANG\",\"code\":null,\"name_vn\":\"KIEN GIANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"THAI NGUYEN\",\"value\":\"280\",\"parentType\":null,\"metaData\":{\"id\":280,\"name\":\"THAI NGUYEN\",\"code\":null,\"name_vn\":\"THAI NGUYEN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BAC KAN\",\"value\":\"281\",\"parentType\":null,\"metaData\":{\"id\":281,\"name\":\"BAC KAN\",\"code\":null,\"name_vn\":\"BAC KAN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HAI DUONG\",\"value\":\"320\",\"parentType\":null,\"metaData\":{\"id\":320,\"name\":\"HAI DUONG\",\"code\":null,\"name_vn\":\"HAI DUONG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HUNG YEN\",\"value\":\"321\",\"parentType\":null,\"metaData\":{\"id\":321,\"name\":\"HUNG YEN\",\"code\":null,\"name_vn\":\"HUNG YEN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"QUANG NAM\",\"value\":\"510\",\"parentType\":null,\"metaData\":{\"id\":510,\"name\":\"QUANG NAM\",\"code\":null,\"name_vn\":\"QUANG NAM\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"DA NANG\",\"value\":\"511\",\"parentType\":null,\"metaData\":{\"id\":511,\"name\":\"DA NANG\",\"code\":null,\"name_vn\":\"DA NANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BINH PHUOC\",\"value\":\"651\",\"parentType\":null,\"metaData\":{\"id\":651,\"name\":\"BINH PHUOC\",\"code\":null,\"name_vn\":\"BINH PHUOC\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HAU GIANG\",\"value\":\"713\",\"parentType\":null,\"metaData\":{\"id\":713,\"name\":\"HAU GIANG\",\"code\":null,\"name_vn\":\"HAU GIANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"CA MAU\",\"value\":\"780\",\"parentType\":null,\"metaData\":{\"id\":780,\"name\":\"CA MAU\",\"code\":null,\"name_vn\":\"CA MAU\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BAC LIEU\",\"value\":\"781\",\"parentType\":null,\"metaData\":{\"id\":781,\"name\":\"BAC LIEU\",\"code\":null,\"name_vn\":\"BAC LIEU\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"THANH PHO HA NOI\",\"value\":\"4\",\"parentType\":null,\"metaData\":{\"id\":4,\"name\":\"THANH PHO HA NOI\",\"code\":null,\"name_vn\":\"THANH PHO HA NOI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HO CHI MINH\",\"value\":\"8\",\"parentType\":null,\"metaData\":{\"id\":8,\"name\":\"HO CHI MINH\",\"code\":null,\"name_vn\":\"HO CHI MINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"LAO CAI\",\"value\":\"20\",\"parentType\":null,\"metaData\":{\"id\":20,\"name\":\"LAO CAI\",\"code\":null,\"name_vn\":\"LAO CAI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"SON LA\",\"value\":\"22\",\"parentType\":null,\"metaData\":{\"id\":22,\"name\":\"SON LA\",\"code\":null,\"name_vn\":\"SON LA\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"DIEN BIEN\",\"value\":\"23\",\"parentType\":null,\"metaData\":{\"id\":23,\"name\":\"DIEN BIEN\",\"code\":null,\"name_vn\":\"DIEN BIEN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"LANG SON\",\"value\":\"25\",\"parentType\":null,\"metaData\":{\"id\":25,\"name\":\"LANG SON\",\"code\":null,\"name_vn\":\"LANG SON\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"CAO BANG\",\"value\":\"26\",\"parentType\":null,\"metaData\":{\"id\":26,\"name\":\"CAO BANG\",\"code\":null,\"name_vn\":\"CAO BANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"NINH BINH\",\"value\":\"30\",\"parentType\":null,\"metaData\":{\"id\":30,\"name\":\"NINH BINH\",\"code\":null,\"name_vn\":\"NINH BINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HAI PHONG\",\"value\":\"31\",\"parentType\":null,\"metaData\":{\"id\":31,\"name\":\"HAI PHONG\",\"code\":null,\"name_vn\":\"HAI PHONG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"QUANG NINH\",\"value\":\"33\",\"parentType\":null,\"metaData\":{\"id\":33,\"name\":\"QUANG NINH\",\"code\":null,\"name_vn\":\"QUANG NINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"THAI BINH\",\"value\":\"36\",\"parentType\":null,\"metaData\":{\"id\":36,\"name\":\"THAI BINH\",\"code\":null,\"name_vn\":\"THAI BINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"THANH HOA\",\"value\":\"37\",\"parentType\":null,\"metaData\":{\"id\":37,\"name\":\"THANH HOA\",\"code\":null,\"name_vn\":\"THANH HOA\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HA TINH\",\"value\":\"39\",\"parentType\":null,\"metaData\":{\"id\":39,\"name\":\"HA TINH\",\"code\":null,\"name_vn\":\"HA TINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"DAK NONG\",\"value\":\"51\",\"parentType\":null,\"metaData\":{\"id\":51,\"name\":\"DAK NONG\",\"code\":null,\"name_vn\":\"DAK NONG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"QUANG NGAI\",\"value\":\"55\",\"parentType\":null,\"metaData\":{\"id\":55,\"name\":\"QUANG NGAI\",\"code\":null,\"name_vn\":\"QUANG NGAI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"PHU YEN\",\"value\":\"57\",\"parentType\":null,\"metaData\":{\"id\":57,\"name\":\"PHU YEN\",\"code\":null,\"name_vn\":\"PHU YEN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"KHANH HOA\",\"value\":\"58\",\"parentType\":null,\"metaData\":{\"id\":58,\"name\":\"KHANH HOA\",\"code\":null,\"name_vn\":\"KHANH HOA\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"DONG NAI\",\"value\":\"61\",\"parentType\":null,\"metaData\":{\"id\":61,\"name\":\"DONG NAI\",\"code\":null,\"name_vn\":\"DONG NAI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"TAY NINH\",\"value\":\"66\",\"parentType\":null,\"metaData\":{\"id\":66,\"name\":\"TAY NINH\",\"code\":null,\"name_vn\":\"TAY NINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"DONG THAP\",\"value\":\"67\",\"parentType\":null,\"metaData\":{\"id\":67,\"name\":\"DONG THAP\",\"code\":null,\"name_vn\":\"DONG THAP\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"NINH THUAN\",\"value\":\"68\",\"parentType\":null,\"metaData\":{\"id\":68,\"name\":\"NINH THUAN\",\"code\":null,\"name_vn\":\"NINH THUAN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"VINH LONG\",\"value\":\"70\",\"parentType\":null,\"metaData\":{\"id\":70,\"name\":\"VINH LONG\",\"code\":null,\"name_vn\":\"VINH LONG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"TIEN GIANG\",\"value\":\"73\",\"parentType\":null,\"metaData\":{\"id\":73,\"name\":\"TIEN GIANG\",\"code\":null,\"name_vn\":\"TIEN GIANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"SOC TRANG\",\"value\":\"79\",\"parentType\":null,\"metaData\":{\"id\":79,\"name\":\"SOC TRANG\",\"code\":null,\"name_vn\":\"SOC TRANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"PHU THO\",\"value\":\"210\",\"parentType\":null,\"metaData\":{\"id\":210,\"name\":\"PHU THO\",\"code\":null,\"name_vn\":\"PHU THO\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"VINH PHUC\",\"value\":\"211\",\"parentType\":null,\"metaData\":{\"id\":211,\"name\":\"VINH PHUC\",\"code\":null,\"name_vn\":\"VINH PHUC\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BAC GIANG\",\"value\":\"240\",\"parentType\":null,\"metaData\":{\"id\":240,\"name\":\"BAC GIANG\",\"code\":null,\"name_vn\":\"BAC GIANG\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BAC NINH\",\"value\":\"241\",\"parentType\":null,\"metaData\":{\"id\":241,\"name\":\"BAC NINH\",\"code\":null,\"name_vn\":\"BAC NINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"NAM DINH\",\"value\":\"350\",\"parentType\":null,\"metaData\":{\"id\":350,\"name\":\"NAM DINH\",\"code\":null,\"name_vn\":\"NAM DINH\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"HA NAM\",\"value\":\"351\",\"parentType\":null,\"metaData\":{\"id\":351,\"name\":\"HA NAM\",\"code\":null,\"name_vn\":\"HA NAM\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_CITY\",\"name\":\"BINH DUONG\",\"value\":\"650\",\"parentType\":null,\"metaData\":{\"id\":650,\"name\":\"BINH DUONG\",\"code\":null,\"name_vn\":\"BINH DUONG\"},\"parentValue\":\"\"}]}}";
		} else {
			resposeCity = callSeabank.listCity();
		}

		JSONObject jsonObjectCity = new JSONObject(resposeCity);
		DanhSachNganHang danhSachThanhPho = callSeabank.createListBank(resposeCity, jsonObjectCity);
		return danhSachThanhPho;
	}

	public DanhSachNganHang listBank(Model model)
			throws ParseException, KeyManagementException, NoSuchAlgorithmException, IOException {
		String response = "";
		if (MOI_TRUONG.equals("dev")) {
			response = "{\"success\":true,\"error\":null,\"data\":{\"total\":103,\"items\":[{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"BAOVIETBANK-NH TMCP BAO VIET\",\"value\":\"359\",\"parentType\":null,\"metaData\":{\"id\":359,\"name\":\"BAOVIETBANK-NH TMCP BAO VIET\",\"code\":null,\"name_vn\":\"BAOVIETBANK-NH TMCP BAO VIET\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"VRB-NH LIEN DOANH VIET - NGA\",\"value\":\"505\",\"parentType\":null,\"metaData\":{\"id\":505,\"name\":\"VRB-NH LIEN DOANH VIET - NGA\",\"code\":null,\"name_vn\":\"VRB-NH LIEN DOANH VIET - NGA\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"ANZ-NH TNHH MTV ANZ VN\",\"value\":\"602\",\"parentType\":null,\"metaData\":{\"id\":602,\"name\":\"ANZ-NH TNHH MTV ANZ VN\",\"code\":null,\"name_vn\":\"ANZ-NH TNHH MTV ANZ VN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"HLBVN-NH HONG LEONG VN\",\"value\":\"603\",\"parentType\":null,\"metaData\":{\"id\":603,\"name\":\"HLBVN-NH HONG LEONG VN\",\"code\":null,\"name_vn\":\"HLBVN-NH HONG LEONG VN\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"SC-NH STANDARD CHARTERED BANK\",\"value\":\"604\",\"parentType\":null,\"metaData\":{\"id\":604,\"name\":\"SC-NH STANDARD CHARTERED BANK\",\"code\":null,\"name_vn\":\"SC-NH STANDARD CHARTERED BANK\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"CITIBANK-NH CITI BANK HA NOI\",\"value\":\"605\",\"parentType\":null,\"metaData\":{\"id\":605,\"name\":\"CITIBANK-NH CITI BANK HA NOI\",\"code\":null,\"name_vn\":\"CITIBANK-NH CITI BANK HA NOI\"},\"parentValue\":\"\"},{\"type\":\"PTF_LOS_MAS_BANK_NAME\",\"name\":\"SCSB-NH SHANGHAI COMMERCIAL SAVINGS\",\"value\":\"606\",\"parentType\":null,\"metaData\":{\"id\":606,\"name\":\"SCSB-NH SHANGHAI COMMERCIAL SAVINGS\",\"code\":null,\"name_vn\":\"SCSB-NH SHANGHAI COMMERCIAL SAVINGS\"},\"parentValue\":\"\"}]}}";
		} else {
			response = callSeabank.listBank();
		}

		JSONObject jsonObject = new JSONObject(response);
		DanhSachNganHang danhSachNganHang = callSeabank.createListBank(response, jsonObject);
		return danhSachNganHang;
	}

	public String moTaiKhoanStep3(HttpServletRequest req, Map<String, String> allParams, Model model, RedirectAttributes redirectAttributes) throws ParseException, KeyManagementException, NoSuchAlgorithmException, IOException {
		ParamsKbank params = getParams(req);
		
		if (params == null) return redirect();
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());
		if (!validOtpOCrLiveness(params)) return redirect();

		forwartParams(allParams, model);

		EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());		
		if(dongTaiKhoanThanhCong(ekycKyso, req)) {
			thongBaoDongTaiKhoan(model, req, params);
			return thongTinGiaiNgan(req, model);
		}
		
		JSONObject jsonObject = new JSONObject(params.getResponseMoTaiKhoan());
		DanhSachTaiKhoan danhSachTaiKhoan = callSeabank.taoDanhSachTaiKhoan(jsonObject);
		String phuongthucgn = "Tài khoản SeaBank";

		taoThongTinAppLoanAmount(params, model);
		
		if (moTaiKhoanThanhCong(jsonObject)) {
			TaiKhoan taiKhoan = danhSachTaiKhoan.getTaiKhoans().get(0);		
			model.addAttribute("tenNganHang", "SEABANK-NH TMCP DONG NAM A");
			model.addAttribute("khuVuc", "THANH PHO HA NOI");
			model.addAttribute("soTK", req.getParameter("soTK"));
			model.addAttribute("tenTK", req.getParameter("tenTK"));
			model.addAttribute("bankCode", "1317001");
			model.addAttribute("chiNhanh", "NH TMCP DONG NAM A HSC");
			model.addAttribute("spHanMuc", ekycKyso.getSpHanMuc());
			return "demo/kySoDoanhNgiepCaNhan/step/giaiNganTaiKhoanMoi";
		} else {
			TaiKhoan taiKhoan = layTaiKhoanChon(allParams, danhSachTaiKhoan);
			
			if(taiKhoan == null) {
				redirectAttributes.addFlashAttribute("error", "Vui lòng chọn lại tài khoản");
				return "redirect:/khach-hang/ky-so/mo-tai-khoan-1";
			}
			
			ekycKyso.setThongTinTaiKhoanSeabank(taoThongTinTaiKhoanSeabankList(taiKhoan));
			ekycKysoRepository.save(ekycKyso);
			
			model.addAttribute("phuongthucgn", phuongthucgn);
			model.addAttribute("tenNganHang", "SEABANK-NH TMCP DONG NAM A");

			model.addAttribute("soTK", taiKhoan.getSoTaiKhoan());
			model.addAttribute("tenTK", covertToString(taiKhoan.getHoVaTen().toUpperCase()));
			model.addAttribute("bankCode", "1317001");
			model.addAttribute("chiNhanh", "NH TMCP DONG NAM A HSC");
			model.addAttribute("khuVuc", "THANH PHO HA NOI");
			model.addAttribute("spHanMuc", ekycKyso.getSpHanMuc());

			return "demo/kySoDoanhNgiepCaNhan/step/giaiNganDaCoTaiKhoan";
		}
	}

	private void taoThongTinAppLoanAmount(ParamsKbank params, Model model) {
		if(params.getThongTinNguoiDungTuLOS() != null) {
			ThongTinNguoiDungTuLOS thongTinNguoiDungTuLOS = new Gson().fromJson(params.getThongTinNguoiDungTuLOS(), ThongTinNguoiDungTuLOS.class);
			if(thongTinNguoiDungTuLOS.getAppLoanAmount() != null)
				model.addAttribute("appLoanAmount", thongTinNguoiDungTuLOS.getAppLoanAmount());
		}
		
	}

	private TaiKhoan layTaiKhoanChon(Map<String, String> allParams, DanhSachTaiKhoan danhSachTaiKhoan) {
		try {
			for (int i = 0; i < danhSachTaiKhoan.getTaiKhoans().size(); i++) {
				if (danhSachTaiKhoan.getTaiKhoans().get(i).getSoTaiKhoan().equals(allParams.get("index"))) {
					return danhSachTaiKhoan.getTaiKhoans().get(i);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public String covertToString(String value) {
		try {
			String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
			Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
			return pattern.matcher(temp).replaceAll("");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public String moTaiKhoanStep4(HttpServletRequest req, Map<String, String> allParams, Model model)
			throws KeyManagementException, NoSuchAlgorithmException, ParseException, IOException {

		ParamsKbank params = getParams(req);
		if (params == null) return redirect();
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

		if (!validOtpOCrLiveness(params)) return redirect();

		forwartParams(allParams, model);

		EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());
		
		if(dongTaiKhoanThanhCong(ekycKyso, req)) {
			thongBaoDongTaiKhoan(model, req, params);
			return thongTinGiaiNgan(req, model);
		}

		Thongtingiaingan thongtingiaingan = new Thongtingiaingan();
		thongtingiaingan.setPhuongThucGiaiNgan(allParams.get("phuongThucGiaiNgan"));

		if (allParams.get("phuongThucGiaiNgan").equals("Tài khoản ngân hàng khác")) {
			DanhSachNganHang danhSachNganHang = listBank(model);
			for (int i = 0; i < danhSachNganHang.getBanks().size(); i++) {
				if (danhSachNganHang.getBanks().get(i).getValue().equals(allParams.get("tenNganHang"))) {
					thongtingiaingan.setTenNganHang(danhSachNganHang.getBanks().get(i).getName());
					thongtingiaingan.setBankName(danhSachNganHang.getBanks().get(i).getValue());
				}
			}
			DanhSachNganHang danhSachThanhPho = listCity(model);
			for (int i = 0; i < danhSachThanhPho.getBanks().size(); i++) {
				if (danhSachThanhPho.getBanks().get(i).getValue().equals(allParams.get("khuVuc"))) {
					thongtingiaingan.setBankCity(danhSachThanhPho.getBanks().get(i).getValue());
					thongtingiaingan.setTinh(danhSachThanhPho.getBanks().get(i).getName());

				}
			}
		} else {

			thongtingiaingan.setTenNganHang(allParams.get("tenNganHang"));
			thongtingiaingan.setTinh(allParams.get("khuVuc"));
		}

		thongtingiaingan.setBankBranch(allParams.get("id"));
		thongtingiaingan.setChiNhanh(allParams.get("chiNhanh"));
		thongtingiaingan.setBankCode(allParams.get("bankCode"));
		thongtingiaingan.setStk(allParams.get("stk"));
		thongtingiaingan.setTtk(allParams.get("ttk"));

		if (ekycKyso.getSpHanMuc().equals("Yes")) {
			if (req.getParameter("btnGiaiNgan").equals("Chưa giải ngân")) {
				thongtingiaingan.setSoTienGiaiNgan("0");
				ekycKyso.setSoTienGiaiNgan("0");
			} else {
				thongtingiaingan.setSoTienGiaiNgan(convertNumber(allParams.get("soTienGiaiNgan")));
				ekycKyso.setSoTienGiaiNgan(convertNumber(allParams.get("soTienGiaiNgan")));
			}
		}

		if (allParams.get("btnGiaiNgan").equals("Chưa giải ngân")) {
			thongtingiaingan.setTrangThaiGiaiNgan("Không");
			ekycKyso.setYeuCauGiaiNgan("Không");
		} else if (allParams.get("btnGiaiNgan").equals("Yêu cầu giải ngân")) {
			thongtingiaingan.setTrangThaiGiaiNgan("Có");
			ekycKyso.setYeuCauGiaiNgan("Có");
		}
		ekycKyso.setNoiDungGiaiNgan(new Gson().toJson(thongtingiaingan));
		ekycKysoRepository.save(ekycKyso);

		return thucHienKySo(ekycKyso, model, params);
	}

	private String convertNumber(String string) {
		try {
			return string.replaceAll("[^0-9]+", "");
		} catch (Exception e) {
		}
		return null;
	}

	private PramMoTaiKhoan taoThongTinGuiMoTaiKhoan(ParamsKbank params, ThongTinNguoiDungTuLOS thongTinNguoiDungTuLOS,
			EkycKyso ekycKyso) throws ParseException {
		
		String email = "";
		if(!StringUtils.isEmpty(ekycKyso.getEmail())) email = ekycKyso.getEmail();
		if(StringUtils.isEmpty(email)) email = thongTinNguoiDungTuLOS.getEmail();
		if(StringUtils.isEmpty(email)) email = "";
		
		JSONObject objectRespGiayTo = new JSONObject(ekycKyso.getOcr());
		Ocr ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);
		String[] arr = ekycKyso.getAnhVideo().split(",");
		PramMoTaiKhoan pramMoTaiKhoan = new PramMoTaiKhoan();
		pramMoTaiKhoan.setEKycRate(conVertScore(ekycKyso.getDiemEkyc()));
		pramMoTaiKhoan.setFullName(convertFullName(Utils.alias(thongTinNguoiDungTuLOS.getFullName())));
		pramMoTaiKhoan.setDob(convertDate(thongTinNguoiDungTuLOS.getDob(), "dd-MMM-yy"));
		pramMoTaiKhoan.setBirtPlace(convertQueQuan(ocr.getQueQuan()));
		pramMoTaiKhoan.setGender(convertGender(thongTinNguoiDungTuLOS.getGender()));
		pramMoTaiKhoan.setIdentityType(thongTinNguoiDungTuLOS.getIdCode());
		pramMoTaiKhoan.setIdentityNumber(ocr.getSoCmt());
		pramMoTaiKhoan.setIssuedDate(convertDate(ocr.getNgayCap(), "dd/MM/yyyy"));
		pramMoTaiKhoan.setIssuedBy(convertNoiCap(ocr.getNoiCap()));
		pramMoTaiKhoan.setCountryCode("VN");
		pramMoTaiKhoan.setPhoneNumber(ekycKyso.getSoDienThoai());
		pramMoTaiKhoan.setEmail(email);
		pramMoTaiKhoan.setIncome(thongTinNguoiDungTuLOS.getIncome());
		pramMoTaiKhoan.setTaxCode("");
		pramMoTaiKhoan.setIndustryGroup("");
		pramMoTaiKhoan.setJob("");
		pramMoTaiKhoan.setPosition("");
		
		String maritalStatus = thongTinNguoiDungTuLOS.getMaritalStatus().toUpperCase();
		if(maritalStatus != null && maritalStatus.equals("LIVINGWITHPARTNER")) maritalStatus = "SINGLE";
		
		pramMoTaiKhoan.setMaritalStatus(maritalStatus);
		pramMoTaiKhoan.setPermanentAddress(Utils.alias(thongTinNguoiDungTuLOS.getPermantWard() + "-"
				+ thongTinNguoiDungTuLOS.getPermentDistric() + "-" + thongTinNguoiDungTuLOS.getPernamentCity()));
		pramMoTaiKhoan.setAddress(convertCurrentAddress(thongTinNguoiDungTuLOS.getCurrentAddress()));
		pramMoTaiKhoan.setDistrict(thongTinNguoiDungTuLOS.getCurrentDistric());
		pramMoTaiKhoan.setProvince(thongTinNguoiDungTuLOS.getCurrentCity());
		pramMoTaiKhoan.setPreferLocation(thongTinNguoiDungTuLOS.getCurrentCity());
		pramMoTaiKhoan.setResident("Y");
		pramMoTaiKhoan.setOtherOwner("N");
		pramMoTaiKhoan.setPurpose("Daily banking");
		pramMoTaiKhoan.setLegalAgreement("N");
		pramMoTaiKhoan.setFatcaInformation("N");
		pramMoTaiKhoan.setProductCode("SUPPER");
		pramMoTaiKhoan.setAccuracyMethod("SMS");
		pramMoTaiKhoan.setReferral("PTF");
		pramMoTaiKhoan.setConfirmTermSea("Y");
		pramMoTaiKhoan.setAgent("PTF");
		pramMoTaiKhoan.setRegisterEbankUsername("");
		pramMoTaiKhoan.setExpiryDate(convertExp(ocr.getNgayHetHan()));
		
		LOGGER.info("moTaiKhoan params: {}", new Gson().toJson(pramMoTaiKhoan));

		pramMoTaiKhoan.setFrontPortraitImage(arr[0]);
		if(arr.length > 1)
			pramMoTaiKhoan.setOtherPortraitImages(arr[1]);
		else
			pramMoTaiKhoan.setOtherPortraitImages(arr[0]);
		
		if(arr.length > 2)
			pramMoTaiKhoan.setOtherPortraitImages1(arr[2]);
		else
			pramMoTaiKhoan.setOtherPortraitImages1(arr[0]);
		pramMoTaiKhoan.setFrontIdentityImage(ekycKyso.getAnhMatTruoc());
		pramMoTaiKhoan.setBackIdentityImages(ekycKyso.getAnhMatSau());

		return pramMoTaiKhoan;
	}
	public static void main(String[] args) {
		System.out.println(convertCurrentAddress("P 3208 FLC - Đại Mỗ - Nam Từ Liêm - Hà Nội"));
	}
	private static String convertCurrentAddress(String currentAddress) {
		if (currentAddress != null) {
			if (currentAddress.length() > 35)
				currentAddress = currentAddress.substring(0, 35);
			return Utils.alias(currentAddress).replaceAll("[^0-9a-z ]+", "").replaceAll("[ ]+", " ").trim();
		}
		return null;
	}

	private String convertFullName(String alias) {
		try {
			return alias.replace("'", "");
		} catch (Exception e) {
		}
		return alias;
	}

	private String convertExp(String ngayHetHan) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
			return dateFormat2.format(dateFormat.parse(ngayHetHan));
		} catch (Exception e) {
		}
		return "";
	}

	private String convertNoiCap(String noiCap) {
		if (noiCap != null) {
			if(noiCap.equals("CỤC TRƯỞNG CỤC CẢNH SÁT QUẢN LÝ HÀNH CHÍNH VỀ TRẬT TỰ XÃ HỘI")) return "CCS QLHC VTTXH";
			if(noiCap.equals("CỤC TRƯỞNG CỤC CẢNH SÁT ĐKQL CƯ TRÚ VÀ DLQG VỀ DÂN CƯ")) return "CCS DKQLCT V DLQGDC";
			if(noiCap.length() > 20)
				noiCap = noiCap.substring(0, 20);
			return Utils.alias(noiCap);
		}
		return null;
	}

	private String convertQueQuan(String queQuan) {
		if (queQuan != null) {
			if (queQuan.length() > 40)
				queQuan = queQuan.substring(0, 40);
			return Utils.alias(queQuan).replaceAll("[^0-9a-z ]+", "");
		}
		return null;
	}

	private String convertGender(String gioiTinh) {
		if (gioiTinh != null) {
			if (gioiTinh.equals("1"))
				return "MALE";
			else
				return "FEMALE";
		}
		return null;
	}

	private String convertDate(String strDate, String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");
		if (strDate != null) {
			return simpleDateFormat2.format(simpleDateFormat.parse(strDate));
		}
		return null;
	}

	private String conVertScore(String diemEkyc) {
		if (diemEkyc != null)
			return String.valueOf(Double.valueOf(diemEkyc) * 100).replaceAll(".[0-9]+$", "");
		return null;
	}

	public String listBranch(HttpServletRequest req, Map<String, String> allParams, Model model)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		String resposeBranch = "";
		if (MOI_TRUONG.equals("dev")) {
			resposeBranch = "{\"success\":true,\"error\":null,\"data\":{\"total\":4125,\"items\":[{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"name\":\"AGRIBANK CN HOA LU\",\"value\":\"204-64204018\",\"parentType\":\"PTF_LOS_MAS_BANK_NAME;PTF_LOS_MAS_BANK_CITY\",\"metaData\":{\"id\":\"204-64204018\",\"bank_name_id\":505,\"bank_city_id\":18,\"code\":\"64204018\",\"name\":\"AGRIBANK CN HOA LU\",\"name_vn\":\"AGRIBANK CN HOA LU\"},\"parentValue\":\"59\"},{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"name\":\"AGRIBANK CN DONG GIA LAI\",\"value\":\"204-64204020\",\"parentType\":\"PTF_LOS_MAS_BANK_NAME;PTF_LOS_MAS_BANK_CITY\",\"metaData\":{\"id\":\"204-64204020\",\"bank_name_id\":204,\"bank_city_id\":59,\"code\":\"64204020\",\"name\":\"AGRIBANK CN DONG GIA LAI\",\"name_vn\":\"AGRIBANK CN DONG GIA LAI\"},\"parentValue\":\"59\"},{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"name\":\"AGRIBANK CN H. DAK DOA\",\"value\":\"204-64204021\",\"parentType\":\"PTF_LOS_MAS_BANK_NAME;PTF_LOS_MAS_BANK_CITY\",\"metaData\":{\"id\":\"204-64204021\",\"bank_name_id\":204,\"bank_city_id\":59,\"code\":\"64204021\",\"name\":\"AGRIBANK CN H. DAK DOA\",\"name_vn\":\"AGRIBANK CN H. DAK DOA\"},\"parentValue\":\"59\"}]}}";
		} else {
			resposeBranch = callSeabank.listBranch();
		}

		JSONObject jsonObject = new JSONObject(resposeBranch);
		ArrayList<Bank> chiNhanh = new ArrayList<>();

		DanhSachNganHang danhSachChiNhanh = callSeabank.createListBranch(resposeBranch, jsonObject);
		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		for (int y = 0; y < danhSachChiNhanh.getBanks().size(); y++) {
			int bankNameId = danhSachChiNhanh.getBanks().get(y).getInforBranch().getBankNameId();
			int bankCityId = danhSachChiNhanh.getBanks().get(y).getInforBranch().getBankCityId();

			JSONObject json = new JSONObject(text);
			if (json.getString("tenNganHang").equals(String.valueOf(bankNameId)) && json.getString("khuVuc").equals(String.valueOf(bankCityId))) {
				chiNhanh.add(danhSachChiNhanh.getBanks().get(y));
			}
		}
		JSONObject jObject = new JSONObject();
		jObject.put("chiNhanh", chiNhanh);

		return jObject.toString();
	}

	public String bankCode(HttpServletRequest req, Map<String, String> allParams, Model model)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		String resposeBranch = "";
		if (MOI_TRUONG.equals("dev")) {
			resposeBranch = "{\"success\":true,\"error\":null,\"data\":{\"total\":4125,\"items\":[{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"name\":\"AGRIBANK CN HOA LU\",\"value\":\"204-64204018\",\"parentType\":\"PTF_LOS_MAS_BANK_NAME;PTF_LOS_MAS_BANK_CITY\",\"metaData\":{\"id\":\"204-64204018\",\"bank_name_id\":204,\"bank_city_id\":59,\"code\":\"64204018\",\"name\":\"AGRIBANK CN HOA LU\",\"name_vn\":\"AGRIBANK CN HOA LU\"},\"parentValue\":\"59\"},{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"name\":\"AGRIBANK CN DONG GIA LAI\",\"value\":\"204-64204020\",\"parentType\":\"PTF_LOS_MAS_BANK_NAME;PTF_LOS_MAS_BANK_CITY\",\"metaData\":{\"id\":\"204-64204020\",\"bank_name_id\":204,\"bank_city_id\":59,\"code\":\"64204020\",\"name\":\"AGRIBANK CN DONG GIA LAI\",\"name_vn\":\"AGRIBANK CN DONG GIA LAI\"},\"parentValue\":\"59\"},{\"type\":\"PTF_LOS_MAS_BANK_BRANCH\",\"name\":\"AGRIBANK CN H. DAK DOA\",\"value\":\"204-64204021\",\"parentType\":\"PTF_LOS_MAS_BANK_NAME;PTF_LOS_MAS_BANK_CITY\",\"metaData\":{\"id\":\"204-64204021\",\"bank_name_id\":204,\"bank_city_id\":59,\"code\":\"64204021\",\"name\":\"AGRIBANK CN H. DAK DOA\",\"name_vn\":\"AGRIBANK CN H. DAK DOA\"},\"parentValue\":\"59\"}]}}";
		} else {
			resposeBranch = callSeabank.listBranch();
		}

		JSONObject jsonObject = new JSONObject(resposeBranch);
		String bankCode = "";
		String id = "";
		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		DanhSachNganHang danhSachChiNhanh = callSeabank.createListBank(resposeBranch, jsonObject);
		for (int y = 0; y < danhSachChiNhanh.getBanks().size(); y++) {
			JSONObject json = new JSONObject(text);
			String branch = danhSachChiNhanh.getBanks().get(y).getName();
			if (branch.equals(json.getString("chiNhanh"))) {
				bankCode = danhSachChiNhanh.getBanks().get(y).getInforBranch().getCode();
				id = danhSachChiNhanh.getBanks().get(y).getInforBranch().getId();
			}
		}

		JSONObject jObject = new JSONObject();
		jObject.put("bankCode", bankCode);
		jObject.put("id", id);

		return jObject.toString();
	}

	public ThongTinTaiKhoanDangKy getAccountInfor(HttpServletRequest req, Map<String, String> allParams, Model model) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		String respose = "";
		String text = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8.name());
		JSONObject jsonObjectPr = new JSONObject(text);
		respose = callSeabank.getAccountInfor(jsonObjectPr.getString("stk"), jsonObjectPr.getString("bankCode"), req);

		if(respose == null) respose = "{\"success\":false}";
		JSONObject jsonObject = new JSONObject(respose);

		ThongTinTaiKhoanDangKy thongTinTaiKhoanDangKy = new ThongTinTaiKhoanDangKy();
		thongTinTaiKhoanDangKy = callSeabank.getAccountInfor(respose, jsonObject, model);

		return thongTinTaiKhoanDangKy;
	}

	public ThongTinNguoiDungTuLOS getInforLOS(HttpServletRequest req, Map<String, String> allParams, Model model) throws KeyManagementException, NoSuchAlgorithmException, IOException, ParseException {
		String respose = callSeabank.getInforLos(allParams.get("data"), req, null);
		JSONObject jsonObject = new JSONObject(respose);

		ThongTinNguoiDungTuLOS thongTinNguoiDungTuLOS = new ThongTinNguoiDungTuLOS();
		thongTinNguoiDungTuLOS = callSeabank.getInfor(respose, jsonObject, model);

		return thongTinNguoiDungTuLOS;
	}

	public String thongTinGiaiNgan(HttpServletRequest req, Model model) throws KeyManagementException, NoSuchAlgorithmException, ParseException, IOException {
		ParamsKbank params = getParams(req);
		if (params == null) return redirect();
		LOGGER.info("CodeTransaction: " + params.getCodeTransaction());

		if (!validOtpOCrLiveness(params))return redirect();
		
		EkycKyso ekycKyso = ekycKysoRepository.findBySoCmtAndToken(params.getSoCmt(), params.getMatKhau());
		
		return giaiNganTaiKhoanNganHangKhac(req, model, ekycKyso, params);
	}
	private String giaiNganTaiKhoanNganHangKhac(HttpServletRequest req, Model model, EkycKyso ekycKyso, ParamsKbank params) throws KeyManagementException, NoSuchAlgorithmException, ParseException, IOException {
		model.addAttribute("spHanMuc", ekycKyso.getSpHanMuc());
		model.addAttribute("hoTen", ekycKyso.getHoVaTen());
		DanhSachNganHang danhSachCity = listCity(model);
		model.addAttribute("listCity", danhSachCity.getBanks());
		DanhSachNganHang danhSachNganHang = listBank(model);
		for (int i = 0; i < danhSachNganHang.getBanks().size(); i++) {
			if (i == 0) {
				String idBranch = danhSachNganHang.getBanks().get(i).getValue().substring(0, 3);
				model.addAttribute("branch", idBranch);
			}

		}
		taoThongTinAppLoanAmount(params, model);
		model.addAttribute("listBank", danhSachNganHang.getBanks());
		return "demo/kySoDoanhNgiepCaNhan/step/giaiNganTaiKhoanNganHangKhac";
	}
	public String chiTietDk(HttpServletRequest req, Map<String, String> allParams, Model model) {

		String fileDk = KY_SO_FOLDER + "/dieu_khoan_mo_tk.pdf";
		model.addAttribute("fileDieuKhoan", enDeCryption.encrypt(fileDk));
		model.addAttribute("LINK_ADMIN", LINK_ADMIN);
		return "demo/kySoDoanhNgiepCaNhan/step/viewDieuKhoan";
	}
}
