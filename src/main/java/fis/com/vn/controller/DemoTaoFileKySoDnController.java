package fis.com.vn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.Email;
import fis.com.vn.common.MediaTypeUtils;
import fis.com.vn.common.PdfHandling;
import fis.com.vn.common.SendSMS;
import fis.com.vn.common.StringUtils;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.EkycKySoService;
import fis.com.vn.component.KySoComponent;
import fis.com.vn.component.Language;
import fis.com.vn.contains.ContainsKySo;
import fis.com.vn.entities.FileObject;
import fis.com.vn.entities.FormInfo;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.entities.RespApi;
import fis.com.vn.entities.SoHopDong;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.repository.QuanLyChuKySoRepository;
import fis.com.vn.repository.SoHDRepository;
import fis.com.vn.table.EkycKyso;
import fis.com.vn.table.GiayChungNhan;
import fis.com.vn.table.SoHD;

@Controller
public class DemoTaoFileKySoDnController extends BaseController {
	@Value("${KY_SO_FOLDER}")
	String KY_SO_FOLDER;

	@Value("${LINK_ADMIN}")
	String LINK_ADMIN;

	@Value("${MOI_TRUONG}")
	String MOI_TRUONG;

	@Autowired
	EkycKySoService ekycKySoService;
	@Autowired
	EkycKysoRepository ekycKysoRepository;
	@Autowired
	ConfigProperties configProperties;
	@Autowired
	Email email;
	@Autowired
	Language language;
	@Autowired
	PdfHandling pdfHandling;
	@Autowired
	QuanLyChuKySoRepository quanLyChuKySoRepository;
	@Autowired
	KySoComponent kySoComponent;
	@Autowired
	SendSMS sendSMS;

	@Autowired
	SoHDRepository hdRepository;

	@GetMapping(value = "/danh-sach-khach-hang/ky-so/gui-mail/chon")
	public String kySoGuiMailChon(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/ky-so/gui-mail/chon"))
			return "error";

		forwartParams(allParams, model);
		return "quantrikhachhang/log/chonloaigui";
	}

	@GetMapping(value = "/danh-sach-khach-hang-bh/ky-so/gui-mail/chon")
	public String kySoGuiMailChonBH(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		System.err.println("jzhdasd111");
		// if(!isAllowUrlContains(req, "/danh-sach-khach-hang-bh/ky-so/gui-mail/chon"))
		// return "error";
		System.err.println("jzhdasd222");
		forwartParams(allParams, model);
		return "quantrikhachhang/log/chonloaigui";
	}

	@PostMapping(value = "/danh-sach-khach-hang/ky-so/gui-mail")
	public String kySoGuiMail(Model model, @RequestParam(name = "loaiGui", required = false) String[] loaiGui,
			HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/ky-so/gui-mail/chon"))
			return "error";

		try {
			if (StringUtils.isEmpty(loaiGui))
				throw new ErrorException("Chọn hình thức gửi link SMS hoặc Email");

			EkycKyso ekycKyso = ekycKysoRepository.findById(getLongParams(allParams, "id")).get();
			if (ekycKyso.getTrangThai().equals("0") || ekycKyso.getTrangThai().equals("4")) {
				String pw = Utils.randomPw();
				ekycKyso.setToken(pw);
				long thoiGianHetHanToken = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L;
				ekycKyso.setThoiGianHetHanToken(thoiGianHetHanToken);
				ekycKyso.setTrangThai("4");

				ekycKysoRepository.save(ekycKyso);

				for (String string : loaiGui) {
					if (!StringUtils.isEmpty(ekycKyso.getEmail())) {
						if (string.equals("email")) {
							email.sendText(ekycKyso.getEmail(), "Ký số hợp đồng điện tử",
									"Khoản vay tín chấp của QK  tại PTF đã được phê duyệt. Vui lòng đăng nhập  bằng CMND/CCCD tại "
											+ "<a href='" + LINK_ADMIN
											+ "/reg'>link</a> để xác thực và ký hồ sơ vay vốn" + "<br/><br/>Password: "
											+ pw);
						}
					}
					if (string.equals("sms")) {
						// gửi sms
						// Mẫu sms: Quy khach vui long su dung CMND/CCCD de truy cap link
						// https://esign.ptf.com.vn/reg xac thuc và ky ho so vay von da duoc duyet tai
						// PTF. Paswword: 12345678910535355353.
						String message = "Chuc mung QK da duoc PTF duyet vay.Vui long truy cap https://esign.ptf.com.vn/reg de xac thuc va ky hop dong. Ten DN: so CMND/CCCD, Mat khau: "
								+ pw;
						sendSMS.postRequestSMS(ekycKyso.getSoDienThoai(), message, ekycKyso);
					}
				}

			}
			redirectAttributes.addFlashAttribute("success", "Gửi thành công");
		} catch (ErrorException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
		}
		forwartParams(allParams, model);
		return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
	}

	@GetMapping(value = "/danh-sach-khach-hang/ky-so/gui-mail-hop-dong")
	public String kySoGuiMailHopDong(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		EkycKyso ekycKyso = ekycKysoRepository.findById(getLongParams(allParams, "id")).get();
		if (ekycKyso.getTrangThai().equals("3")) {
			ArrayList<FileObject> fileObjects = taoFileGuiMail(ekycKyso);
			if (!StringUtils.isEmpty(ekycKyso.getEmail())) {
				email.sendMultipleFile(ekycKyso.getEmail(), "Hợp đồng điện tử pdf",
						"Xin chào " + ekycKyso.getHoVaTen() + ", \n\n" + "Bạn đã thực hiện thành công hợp đồng vay",
						fileObjects);
			} else {
				redirectAttributes.addFlashAttribute("error", "Không có mail khách hàng");
			}
			redirectAttributes.addFlashAttribute("success", "Gửi hợp đồng thành công");
		} else {
			redirectAttributes.addFlashAttribute("error", "Hợp đống chưa hoàn thành");
		}
		forwartParams(allParams, model);
		return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
	}

	@GetMapping(value = "/danh-sach-khach-hang/sua")
	public String kySoSua(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {

		try {
			if (StringUtils.isEmpty(allParams.get("id"))) {
				throw new Exception(language.getMessage("sua_that_bai"));
			}
			Optional<EkycKyso> ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id")));
			if (!ekycKyso.isPresent()) {
				throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
			}

			model.addAttribute("anhCaNhan", Utils.encodeFileToBase64Binary(new File(ekycKyso.get().getAnhCaNhan())));
			model.addAttribute("fileKy", Utils.encodeFileToBase64Binary(new File(ekycKyso.get().getDuongDanFileKy())));

			if (!StringUtils.isEmpty(ekycKyso.get().getChiDinh())) {
				model.addAttribute("fileKyBaoHiem",
						Utils.encodeFileToBase64Binary(new File(ekycKyso.get().getChiDinh())));
			}

			model.addAttribute("ekycKyso", ekycKyso.get());
			model.addAttribute("name", language.getMessage("sua"));
			forwartParams(allParams, model);
			return "demo/tailieu/kyso";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/danh-sach-khach-hang?" + getParamsQuery(allParams);
		}
	}

	@GetMapping(value = "/danh-sach-khach-hang-bh/upload-file")
	public String uploadFile(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams)
			throws JsonProcessingException {

		model.addAttribute("userInfo", new EkycKyso());
		model.addAttribute("type", "add");
		model.addAttribute("name", language.getMessage("them_moi"));
		forwartParams(allParams, model);
		return "quantrikhachhang/log/uploadFile";
	}

	@PostMapping(value = "/danh-sach-khach-hang-bh/upload-file")
	public String uploadFilePost(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			@RequestParam(name = "file", required = false) MultipartFile file) throws JsonProcessingException {

		System.err.println("sadas");

		RespApi respApi = new RespApi();
		// sys
		try {

			InputStream file1 = file.getInputStream();
			System.err.println("sadas" + file1.toString());
			XSSFWorkbook workbook = new XSSFWorkbook(file1);
			System.err.println("workbook" + workbook.toString());
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			// Iterate through each rows one by one
			java.util.Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				try {
					Row row = rowIterator.next();
					if (row.getRowNum() == 0)
						continue;
					java.util.Iterator<Cell> cellIterator = row.cellIterator();

					String value = "";
					int i = 0;
					EkycKyso ekycKyso = new EkycKyso();
					ParamsKbank paramsKbank = new ParamsKbank();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						value = formatter.formatCellValue(cell).trim();

						if (i == 0)
							paramsKbank.setHoVaTen(value);
						if (i == 1)
							paramsKbank.setSoDienThoai(value);
						if (i == 2)
							paramsKbank.setEmail(value);
						if (i == 3)
							paramsKbank.setTrangThai(value);
						if (i == 4) {
							paramsKbank.setDuongDanFileKy(KY_SO_FOLDER + "/" + "danhsachky/" + value);
							paramsKbank.setTenFile(value);
							paramsKbank.setViTriKyDn("nganhangky");

						}

						if (i == 5)
							paramsKbank.setSoCmt(value);

						i++;
					}
					FormInfo formInfo = new FormInfo();
					updateObjectToObject(ekycKyso, paramsKbank);
					updateObjectToObject(formInfo, paramsKbank);

					ekycKyso.setTinhTrangCapMa("1");
					ekycKyso.setNgayTao(new Date());
					ekycKyso.setTrangThai("1");
					ekycKyso.setDuongDanFileKySeaBank(paramsKbank.getDuongDanFileKy());
					ekycKyso.setSoTaiKhoan(paramsKbank.getSoHopDong());
					ekycKyso.setNoiDungForm(new Gson().toJson(formInfo));
					ekycKyso.setNguoiTao(getUserName(req));

					String pw = randomPw();
					ekycKyso.setToken(pw);
					long thoiGianHetHanToken = System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L;
					ekycKyso.setThoiGianHetHanToken(thoiGianHetHanToken);
					ekycKyso.setTrangThai("1");

					ekycKysoRepository.save(ekycKyso);

				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			respApi.setStatus(400);
			respApi.setMessage("Lỗi hệ thống");
		}

		return "redirect:/danh-sach-khach-hang-bh";
	}

	private String randomPw() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 16;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();

		return generatedString;
	}

	@GetMapping(value = "/danh-sach-khach-hang/template")
	public ResponseEntity<InputStreamResource> template(Model model, HttpServletRequest req,
			@RequestParam Map<String, String> allParams, ServletContext servletContext)
			throws JsonProcessingException, FileNotFoundException {

		MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(servletContext, "80137193.zip");
		File fileZip = new File("C:\\image\\kyso\\80137193.zip");
		InputStreamResource resource = new InputStreamResource(new FileInputStream(fileZip));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileZip.getName())
				.contentType(mediaType).contentLength(fileZip.length()).body(resource);
	}

	@GetMapping(value = "/danh-sach-khach-hang-bh/ky-so", produces = MediaType.APPLICATION_JSON_VALUE)
	public String kyso(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams)
			throws IOException {
		try {
			String list = allParams.getOrDefault("list", "");
			String[] arr = list.split("-");
			for (String string : arr) {
				if (!StringUtils.isEmpty(string)) {
					try {
						EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(string)).get();

						/*
						 * if (ekycKyso.getTrangThai().equals("1")) continue;
						 */

						System.err.println("11111");
						FormInfo formInfo = new Gson().fromJson(ekycKyso.getNoiDungForm(), FormInfo.class);

						String str = ekycKyso.getDuongDanFileKySeaBank();
						System.err.println("2222233: ");
						System.err.println("22222");
//				        String str1 = str.split("\\/")[str.split("\\/").length - 1];
//				        str = str.replaceAll(str1, ".signed."+str1).replaceAll("/[0-9_]+/", "/");

						String pathFileSign = kySoComponent.signFile(Utils.encodeFileToBase64Binary(new File(str)),
								"Tổng Giám đốc", ContainsKySo.UUID_KY_BAO_HIEM_DEMO,
								ContainsKySo.PASS_CODE_KY_BAO_HIEM_DEMO, null, null);

						System.err.println("vi tri ky: " + formInfo.getViTriKyDn());

						ekycKyso.setDuongDanFileKySeaBank(pathFileSign);

						ekycKyso.setTrangThai("2");
						ekycKyso.setBaoHiemKy(new Date());

						ekycKysoRepository.save(ekycKyso);

						System.err.println("6666666666" + formInfo.getViTriKyDn());
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/danh-sach-khach-hang-bh?" + queryStringBuilder(allParams);
	}

	@GetMapping(value = "/danh-sach-khach-hang-bh/ky-so/gui-mail")
	public String kySoGuiMailBH(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {

		/// if(!isAllowUrlContains(req, "/danh-sach-khach-hang-bh/ky-so/gui-mail/chon"))
		/// return "error";
		System.err.println("jzhdasd33");
		String list = allParams.getOrDefault("list", "");
		String[] arr = list.split("-");
		for (String string : arr) {
			if (!StringUtils.isEmpty(string)) {

				try {

					EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(string)).get();
					if (ekycKyso.getTrangThai().equals("2")) {
						String pw = Utils.randomPw();
						ekycKyso.setToken(pw);
						long thoiGianHetHanToken = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L;
						ekycKyso.setThoiGianHetHanToken(thoiGianHetHanToken);
						ekycKyso.setTrangThai("2");

						if (!StringUtils.isEmpty(ekycKyso.getEmail())) {
							SoHD hd = hdRepository.findBySoHd(ekycKyso.getSohd());
							System.err.println("hd: " + hd);
							File folder = new File(KY_SO_FOLDER + hd.getSoHD() + "/");
							File[] listOfFiles = folder.listFiles();
							ArrayList<FileObject> fileObjects = new ArrayList<>();

							for (File file : listOfFiles) {
								FileObject fileObject = new FileObject();
								fileObject.setTen(file.getName());
								fileObject.setDuongDan(file.getPath());
								fileObjects.add(fileObject);
							}

							System.err.println("77777");
							email.sendMultipleFile(ekycKyso.getEmail(), "Ký số hợp đồng điện tử", "Ký số thành công",
									fileObjects);
							ekycKyso.setTrangThaiGui("2");
						}

						ekycKysoRepository.save(ekycKyso);
					}

				} catch (Exception e) {
					e.printStackTrace();
					redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
				}
			}
		}
		redirectAttributes.addFlashAttribute("success", "Gửi thành công");
		forwartParams(allParams, model);
		return "redirect:/danh-sach-khach-hang-bh?" + queryStringBuilder(allParams);
	}

	private ArrayList<FileObject> taoFileGuiMail(EkycKyso ekycKyso) {
		ArrayList<FileObject> fileObjects = new ArrayList<FileObject>();
		FileObject fileObject = taoObject("hop_dong_vay.pdf", ekycKyso.getDuongDanFileKy(), ekycKyso.getTrangThai(),
				true);
		if (fileObject != null)
			fileObjects.add(fileObject);

		fileObject = taoObject("hop_dong_bao_hiem.pdf", ekycKyso.getChiDinh(), ekycKyso.getTrangThai(), false);
		if (fileObject != null)
			fileObjects.add(fileObject);

		fileObject = taoObject("dang_ky_cap_chung_thu_so.pdf", ekycKyso.getAnhTinNhan(), ekycKyso.getTrangThai(),
				false);
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
//        if(!StringUtils.isEmpty(trangThai) && trangThai.equals("3") && checkSign) {
//        	str = str.replaceAll(str1, ".signed."+str1).replaceAll("/[0-9_]+/", "/");
//        }
		fileObject.setDuongDan(str);
		return fileObject;
	}

	@GetMapping(value = "/sao-ke-ngan-hang")
	public String giayChungNhan(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

		/*
		 * if (!isAllowUrlContains(req, "/chia-se-thu-muc")) return
		 * "redirect:/danh-sach-khach-hang?" + getParamsQuery(allParams);
		 */

		File folder = new File(KY_SO_FOLDER + "cathay/");
		File[] listOfFiles = folder.listFiles();
		ArrayList<GiayChungNhan> giayChungNhans = new ArrayList<>();
		System.err.println("cathay:");
		if (listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				if (file.isFile()) {
					GiayChungNhan giayChungNhan = new GiayChungNhan();
					giayChungNhan.setTenFile(file.getName().replaceAll(".pdf", ""));

					giayChungNhans.add(giayChungNhan);
				}
			}
		}
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPage", 1);
		model.addAttribute("totalElement", listOfFiles.length);
		model.addAttribute("giayChungNhans", giayChungNhans);
		return "sharefolder/giaychungnhan";
	}

	@GetMapping(value = "/sao-ke-ngan-hang/xem")
	public String giayChungNhanChiTiep(Model model, HttpServletRequest req,
			@RequestParam Map<String, String> allParams) {

		/*
		 * if (!isAllowUrlContains(req, "/chia-se-thu-muc")) return
		 * "redirect:/danh-sach-khach-hang?" + getParamsQuery(allParams);
		 */

		File folder = new File(KY_SO_FOLDER + "cathay/");
		File[] listOfFiles = folder.listFiles();

		System.err.println("cathay:" + allParams.get("id"));
		String path = "";
		if (listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				if (file.isFile() && file.getName().replaceAll(".pdf", "").equals(allParams.get("id"))) {
					path = file.getPath();

				}
			}
		}
		if (!StringUtils.isEmpty(path)) {
			File file1 = new File(path);
			String base64Img3 = CommonUtils.encodeFileToBase64Binary(file1);

			model.addAttribute("path", base64Img3);
		}
		return "sharefolder/giaychungnhanchitiet";
	}

	@GetMapping(value = "/sao-ke-ngan-hang-da-ky")
	public String soHopDong(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

		/*
		 * if (!isAllowUrlContains(req, "/chia-se-thu-muc")) return
		 * "redirect:/danh-sach-khach-hang?" + getParamsQuery(allParams);
		 */
		ArrayList<SoHD> soHD = (ArrayList<SoHD>) hdRepository.findAll();
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPage", 1);
		model.addAttribute("totalElement", soHD.size());
		model.addAttribute("soHD", soHD);
		return "sharefolder/sohopdong";
	}

	@GetMapping(value = "/sao-ke-ngan-hang-da-ky/xem")
	public String soHopDongChiTiet(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

		/*
		 * if (!isAllowUrlContains(req, "/chia-se-thu-muc")) return
		 * "redirect:/danh-sach-khach-hang?" + getParamsQuery(allParams);
		 */
		System.err.println("hjsdbf: " + KY_SO_FOLDER + allParams.get("hd"));

		File folder = new File(KY_SO_FOLDER + allParams.get("hd"));
		File[] listOfFiles = folder.listFiles();
		ArrayList<SoHopDong> soHopDongs = new ArrayList<>();

		if (listOfFiles.length > 0) {
			for (File file : listOfFiles) {
				SoHopDong soHopDong = new SoHopDong();
				soHopDong.setTenFile(file.getName());
				soHopDong.setPathFile(file.getPath());
				soHopDong.setTenFolder(allParams.get("hd"));
				soHopDongs.add(soHopDong);
			}
		}
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPage", 1);
		model.addAttribute("totalElement", listOfFiles.length);

		model.addAttribute("soHopDongs", soHopDongs);

		return "sharefolder/sohopdongchitiet1";
	}

	@GetMapping(value = "/sao-ke-ngan-hang-da-ky/xem-file")
	public String xemFile(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

		File folder = new File(KY_SO_FOLDER + allParams.get("hd"));
		System.err.println("hjsdbf111: " + allParams.get("pathfile"));
		System.err.println("hjsdbf2222: " + allParams.get("tenfile"));

		String pathfile = allParams.get("pathfile");
		String tenfile = allParams.get("tenfile");

		if (!StringUtils.isEmpty(KY_SO_FOLDER + pathfile + "/" + tenfile)) {
			File file1 = new File(KY_SO_FOLDER + pathfile + "/" + tenfile);
			String base64Img3 = CommonUtils.encodeFileToBase64Binary(file1);
			model.addAttribute("path", base64Img3);
		}

		return "sharefolder/xemfile";
	}

	@GetMapping("/sao-ke-ngan-hang-da-ky/dowloand")
	public ResponseEntity<Resource> doiSoatKiemTraEp(@RequestParam Map<String, String> allParams,
			HttpServletRequest req) throws Exception {
		System.err.println("hjdsf:" + allParams.get("hd"));
		System.err.println("hjxbzhjhj:" + KY_SO_FOLDER + "/" + allParams.get("hd") + "");
		System.err.println("ajhsdh111:" + KY_SO_FOLDER + "/" + allParams.get("hd") + ".zip");
		zipFolder(KY_SO_FOLDER + allParams.get("hd") + "", KY_SO_FOLDER + allParams.get("hd") + ".zip");

		System.err.println("ajhsdh:" + KY_SO_FOLDER + "/" + allParams.get("hd") + ".zip");

		Path path = Paths.get(KY_SO_FOLDER + "/" + allParams.get("hd") + ".zip");
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/zip"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}

	static public void zipFolder(String srcFolder, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);
		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
		}
	}

	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}

}
