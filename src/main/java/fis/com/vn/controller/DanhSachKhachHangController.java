package fis.com.vn.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fis.com.vn.api.entities.NoiDungHopDong;
import fis.com.vn.callapi.CallSeabank;
import fis.com.vn.callapi.entities.TaiKhoan;
import fis.com.vn.callapi.entities.ThongTinNguoiDungTuLOS;
import fis.com.vn.common.Common;
import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.FileHandling;
import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.common.Utils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.component.EkycKySoService;
import fis.com.vn.component.Language;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.FormInfo;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.entities.ReturnRest;
import fis.com.vn.entities.TaiLieu;
import fis.com.vn.entities.Thongtingiaingan;
import fis.com.vn.exception.CheckException;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.exception.ValidException;
import fis.com.vn.ocr.Ocr;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.repository.LogApiSeaBankRepository;
import fis.com.vn.table.EkycKyso;

@Controller
public class DanhSachKhachHangController extends BaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DanhSachKhachHangController.class);
	@Autowired
	EkycKysoRepository ekycKysoRepository;
	@Autowired
	Language language;
	@Autowired
	EkycKySoService ekycKySoService;
	@Autowired
	ConfigProperties configProperties;
	@Autowired
	CallSeabank callSeabank;
	@Autowired
	LogApiSeaBankRepository logApiSeaBankRepository;

//	@GetMapping(value = {"/danh-sach-khach-hang/updatecmt"})	
//	@ResponseBody
//	public String updatecmt(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req) throws ParseException {
//		Iterable<EkycKyso> ekycKysos = ekycKysoRepository.findAll();
//		for (EkycKyso ekycKyso : ekycKysos) {
//			ekycKyso.setSoCmt(ekycKyso.getSoCmt().trim());
//			ekycKyso.setHoVaTen(ekycKyso.getHoVaTen().trim());
//			ekycKyso.setSoDienThoai(ekycKyso.getSoDienThoai().trim());
//			ekycKysoRepository.save(ekycKyso);
//		}
//		return "okie";
//	}

	@GetMapping(value = { "/danh-sach-hop-dong" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String danhSachHopDong(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
			throws ParseException, CheckException, ValidException {
		String token = allParams.get("token");
		String soHopDong = allParams.get("soHopDong");
		if (StringUtils.isEmpty(token))
			throw new ValidException("Not permission");
		if (StringUtils.isEmpty(soHopDong))
			throw new ValidException("soHopDong không được để trống");
		if (!token.equals(configProperties.getConfig().getToken_get_list_contract()))
			throw new ValidException("Not permission");

		List<EkycKyso> ekycKysos = ekycKysoRepository.findBySoTaiKhoan(soHopDong);
		ArrayList<NoiDungHopDong> noiDungHopDongs = new ArrayList<>();
		for (EkycKyso ekycKyso : ekycKysos) {
			NoiDungHopDong noiDungHopDong = convertHopDong(ekycKyso);
			noiDungHopDongs.add(noiDungHopDong);
		}

		return ReturnRest.successFullDate(noiDungHopDongs);
	}

	private NoiDungHopDong convertHopDong(EkycKyso ekycKyso) {
		try {
			Ocr ocr = new Ocr();
			if (!StringUtils.isEmpty(ekycKyso.getOcr())) {
				JSONObject objectRespGiayTo = new JSONObject(ekycKyso.getOcr());
				ocr = new Gson().fromJson(objectRespGiayTo.get("data").toString(), Ocr.class);
			}
			String anhMatTruoc = null;
			if (!StringUtils.isEmpty(ekycKyso.getAnhMatTruoc())) {
				File file = new File(ekycKyso.getAnhMatTruoc());
				anhMatTruoc = CommonUtils.encodeFileToBase64Binary(file);
			}
			String anhMatSau = null;
			if (!StringUtils.isEmpty(ekycKyso.getAnhMatSau())) {
				File file1 = new File(ekycKyso.getAnhMatSau());
				anhMatSau = CommonUtils.encodeFileToBase64Binary(file1);
			}
			ArrayList<String> anhVideo = null;
			if (!StringUtils.isEmpty(ekycKyso.getAnhVideo())) {
				String[] arr = ekycKyso.getAnhVideo().split(",");
				anhVideo = new ArrayList<String>();
				for (String string : arr) {
					File file1 = new File(string);
					String base64Img1 = CommonUtils.encodeFileToBase64Binary(file1);
					anhVideo.add(base64Img1);
				}
			}
			String anhCaNhan = null;
			if (!StringUtils.isEmpty(ekycKyso.getAnhCaNhan())) {
				File file3 = new File(ekycKyso.getAnhCaNhan());
				anhCaNhan = CommonUtils.encodeFileToBase64Binary(file3);
			}
			String dangKyChungThuSo = null;
			if (!StringUtils.isEmpty(ekycKyso.getAnhTinNhan())) {
				File file3 = new File(ekycKyso.getAnhTinNhan());
				dangKyChungThuSo = CommonUtils.encodeFileToBase64Binary(file3);
			}

			String str = ekycKyso.getDuongDanFileKy();
			File file2 = new File(str);
			String hopDongVay = CommonUtils.encodeFileToBase64Binary(file2);

			String hopDongBaoHiem = null;
			if (!StringUtils.isEmpty(ekycKyso.getChiDinh())) {
				File file3 = new File(ekycKyso.getChiDinh());
				hopDongBaoHiem = CommonUtils.encodeFileToBase64Binary(file3);
			}

			NoiDungHopDong noiDungHopDong = NoiDungHopDong.builder().soHopDong(ekycKyso.getSoTaiKhoan())
					.email(ekycKyso.getEmail()).trangThai(ekycKyso.getTrangThai()).hoVaTen(ekycKyso.getHoVaTen())
					.soDienThoai(ekycKyso.getSoDienThoai()).soCmt(ekycKyso.getSoCmt()).gioiTinh(ekycKyso.getGioiTinh())
					.namSinh(ocr.getNamSinh()).queQuan(ocr.getQueQuan()).noiCap(ocr.getNoiCap()).noiTru(ocr.getNoiTru())
					.diemSoSanhKhuonMat(ekycKyso.getDiemEkyc()).uuidkySo(ekycKyso.getUuidKySo())
					.billcodeKySo(ekycKyso.getBillCodeKySo()).anhMatTruoc(anhMatTruoc).anhMatSau(anhMatSau)
					.anhCaNhan(anhCaNhan).anhVideo(anhVideo).hopDongVay(hopDongVay).hopDongBaoHiem(hopDongBaoHiem)
					.dangKyChungThuSo(dangKyChungThuSo).trangThaiEkyc(ekycKyso.getTrangThaiEkyc())
					.ngayTao(ekycKyso.getNgayTao()).khuVuc(ekycKyso.getKhuVuc())
					.ngayKhachHangKy(ekycKyso.getKhachHangKy()).ngayBaoHiemKy(ekycKyso.getBaoHiemKy()).build();

			return noiDungHopDong;
		} catch (Exception e) {
			LOGGER.error("get contract error " + ekycKyso.getSoDienThoai() + " - " + ekycKyso.getId() + ": {}", e);
		}
		return null;
	}

	@GetMapping(value = { "/danh-sach-khach-hang" })
	public String kbank(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
			throws ParseException {
		forwartParams(allParams, model);

		model.addAttribute("dongTaiKhoan", isAllowUrlContains(req, "/danh-sach-khach-hang/dong-tai-khoan"));
		model.addAttribute("guiThongTin", isAllowUrlContains(req, "/danh-sach-khach-hang/ky-so/gui-mail/chon"));
		model.addAttribute("ekycTruyenThong", isAllowUrlContains(req, "/danh-sach-khach-hang/trang-thai"));
		model.addAttribute("xemChiTiet", isAllowUrlContains(req, "/danh-sach-khach-hang/xem"));
		model.addAttribute("hoanThanhHopDong", isAllowUrlContains(req, "/danh-sach-khach-hang/thay-doi-trang-thai"));
		model.addAttribute("xemTaiKhoanSeabank",
				isAllowUrlContains(req, "/danh-sach-khach-hang/xem-thong-tin-tktt-sb"));
		model.addAttribute("thongTinGiaiNgan",
				isAllowUrlContains(req, "/danh-sach-khach-hang/xem-thong-tin-giai-ngan"));

		String tinhTrangCapMa = "1";
		boolean searchDate = true;
		allParams.put("soTaiKhoan", allParams.get("soHopDong"));
		System.out.println("thogn tin giai ngan11: ");

		model.addAttribute("themMoiHopDong", isAllowUrlContains(req, "/danh-sach-khach-hang/ky-so"));

		model.addAttribute("khuVucs", danhSachKhuVuc(getKhuVuc(req)));
		model.addAttribute("danhSachGhiChu", danhSachGhiChu());

		String khuVuc = layKhuVuc(allParams, req);

		ekycKySoService.search(model, allParams, searchDate, tinhTrangCapMa, req, khuVuc);
		model.addAttribute("khuVuc", khuVuc);
		forwartParams(allParams, model);
		return "quantrikhachhang/log/danhsach";
	}

	public ParamsKbank getParams(HttpServletRequest req) {
		return (ParamsKbank) req.getSession().getAttribute("params");
	}

	private ArrayList<String> danhSachGhiChu() {
		ArrayList<String> arr = new ArrayList<>();
		arr.add("Thành công");
		arr.add("Ký truyền thống");
		arr.add("Sai thông tin trên chứng thư số");
		arr.add("Sai thông tin trên hợp đồng");
		arr.add("Sai chữ ký KH");
		arr.add("Gian lận eKYC");
		arr.add("Lỗi ảnh xác thực KH");
		arr.add("Lỗi ảnh Los đầu vào");
		arr.add("Lỗi ảnh CCCD ekyc");
		arr.add("HS quá hạn ký");
		return arr;
	}

	@GetMapping(value = "/danh-sach-khach-hang/ky-so/ghi-chu")
	public String ghiChu(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("danhSachGhiChu", danhSachGhiChu());
		forwartParams(allParams, model);
		return "quantrikhachhang/log/chonloaighichu";
	}

	@PostMapping(value = "/danh-sach-khach-hang/ky-so/ghi-chu")
	public String postghiChu(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		try {
			EkycKyso ekycKyso = ekycKysoRepository.findById(getLongParams(allParams, "id")).get();
			ekycKyso.setGhiChu(allParams.get("ghiChu"));

			if (laLoaiGhiChuKiemTra(allParams.get("ghiChu")))
				ekycKyso.setLoaiKiemTra(Contains.LOAI_HOP_DONG_CAN_KIEM_TRA);
			else
				ekycKyso.setLoaiKiemTra(Contains.LOAI_HOP_DONG_KHONG_CAN_KIEM_TRA);

			ekycKysoRepository.save(ekycKyso);

			redirectAttributes.addFlashAttribute("success", "Ghi chú thành công");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
		}
		forwartParams(allParams, model);
		return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
	}

	private boolean laLoaiGhiChuKiemTra(String ghiChu) {
		if (ghiChu.equals(Contains.TEN_LOAI_HOP_DONG_CAN_KIEM_TRA_1))
			return true;
		if (ghiChu.equals(Contains.TEN_LOAI_HOP_DONG_CAN_KIEM_TRA_2))
			return true;
		if (ghiChu.equals(Contains.TEN_LOAI_HOP_DONG_CAN_KIEM_TRA_3))
			return true;
		if (ghiChu.equals(Contains.TEN_LOAI_HOP_DONG_CAN_KIEM_TRA_4))
			return true;
		return false;
	}

	private String layKhuVuc(Map<String, String> allParams, HttpServletRequest req) {
		String khuVuc = getStringParams(allParams, "khuVuc");

		if (StringUtils.isEmpty(khuVuc)) {
			if (StringUtils.isEmpty(khuVuc) && danhSachKhuVuc(getKhuVuc(req)).size() == 63) {
				khuVuc = null;
			} else {
				khuVuc = danhSachKhuVuc(getKhuVuc(req)).size() > 0 ? danhSachKhuVuc(getKhuVuc(req)).get(0) : "";
			}
		}

		if (khuVuc != null && !danhSachKhuVuc(getKhuVuc(req)).contains(khuVuc)) {
			khuVuc = "";
		}
		return khuVuc;
	}

	public ArrayList<String> danhSachKhuVuc(String khuVucs) {
		String khuVuc = khuVucs != null ? khuVucs : "";
		String[] arr = khuVuc.split(",");
		ArrayList<String> arrs = new ArrayList<String>();
		for (String string : arr) {
			arrs.add(string);
		}
		return arrs;
	}

	@GetMapping(value = { "/danh-sach-khach-hang/trang-thai" })
	public String trangThai(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req,
			RedirectAttributes redirectAttributes) throws ParseException {

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/trang-thai"))
			return "error";

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			Optional<EkycKyso> ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id")));
			if (!ekycKyso.isPresent()) {
				redirectAttributes.addFlashAttribute("error", "Không tồn tại bản ghi");
				return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
			}

			if (!coKhuVuc(getKhuVuc(req), ekycKyso.get().getKhuVuc())) {
				redirectAttributes.addFlashAttribute("error", "Bạn không được xem khu vực này");
				return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
			}

		} else {
			redirectAttributes.addFlashAttribute("error", "Lỗi xử lý");
			return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
		}

		forwartParams(allParams, model);
		return "quantrikhachhang/log/trangThai";
	}

	private boolean coKhuVuc(String khuVucs, String khuVuc) {
		if (khuVuc != null && danhSachKhuVuc(khuVucs).contains(khuVuc)) {
			return true;
		}
		return false;
	}

	@PostMapping(value = { "/danh-sach-khach-hang/trang-thai" })
	public String trangThaiPost(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req,
			RedirectAttributes redirectAttributes) throws ParseException {

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/trang-thai"))
			return "error";

		try {
			if (StringUtils.isEmpty(allParams.get("id")))
				throw new ErrorException("Không có id");

			Optional<EkycKyso> ekycKysoDb = ekycKysoRepository.findById(Long.valueOf(allParams.get("id")));
			if (!ekycKysoDb.isPresent())
				throw new ErrorException("Không tồn tại bản ghi");

			EkycKyso ekycKyso = ekycKysoDb.get();

			if (!coKhuVuc(getKhuVuc(req), ekycKyso.getKhuVuc())) {
				redirectAttributes.addFlashAttribute("error", "Bạn không được xem khu vực này");
				return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
			}

			JSONObject jsonObjectSS = new JSONObject();
			jsonObjectSS.put("anhMatTruoc", CommonUtils.encodeFileToBase64Binary(new File(ekycKyso.getAnhCaNhan())));
			jsonObjectSS.put("anhKhachHang", allParams.get("base64AnhCaNhan"));

			ParamsKbank paramsKbank = new ParamsKbank();
			paramsKbank.setCodeTransaction(Common.layMaGiaoDich(1));
			paramsKbank.setSoDienThoai(ekycKyso.getSoDienThoai());
			paramsKbank.setSoCmt(ekycKyso.getSoCmt());
			paramsKbank.setSoHopDong(ekycKyso.getSoDienThoai());
			paramsKbank.setHoVaTen(ekycKyso.getHoVaTen());

			String respone1 = postRequest(jsonObjectSS.toString(), "/public/all/so-sanh-anh", paramsKbank);
			JSONObject object = new JSONObject(respone1);
			if (object.getInt("status") != 200)
				throw new ErrorException("Ảnh không khớp với ảnh trong hồ sơ");

			FileHandling fileHandling = new FileHandling();
			String imgFolderLog = configProperties.getConfig().getImage_folder_log() + code + "/";
			String pathImg = fileHandling.save(allParams.get("base64AnhCaNhan"), imgFolderLog);
			ekycKyso.setAnhVideo(pathImg);
			ekycKyso.setDiemEkyc(object.get("data").toString());

			ekycKyso.setTrangThaiEkyc(Contains.TT_EKYC_TRUYEN_THONG);
			ekycKysoRepository.save(ekycKyso);
			redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công");
		} catch (ErrorException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
		}
		return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
	}

	@GetMapping(value = { "/danh-sach-khach-hang-bh" })
	public String danhSachKhachHangDemoBh(Model model, @RequestParam Map<String, String> allParams,
			HttpServletRequest req) throws ParseException {
		String tinhTrangCapMa = "1";

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fromDate = dateFormat.format(new Date());
		if (!StringUtils.isEmpty(allParams.get("fromDate"))) {
			fromDate = allParams.get("fromDate");
		}
		String toDate = dateFormat.format(new Date());
		if (!StringUtils.isEmpty(allParams.get("toDate"))) {
			toDate = allParams.get("toDate");
		}
		allParams.put("fromDate", fromDate);
		allParams.put("toDate", toDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(toDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		toDate = dateFormat.format(calendar.getTime());

		SimpleDateFormat dateFormatSql = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		String[] trangThais;
		if (!StringUtils.isEmpty(allParams.get("trangThai"))) {
			String[] trangThais1 = { allParams.get("trangThai") };
			trangThais = trangThais1;
		} else {
			String[] trangThais2 = { "1", "2" };
			trangThais = trangThais2;
		}

		Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));
		Page<EkycKyso> ekycKysos = ekycKysoRepository.selectParamsDemo(dateFormatSql.parse(fromDate),
				dateFormatSql.parse(toDate), "2", getStringParams(allParams, "soDienThoai"),
				getStringParams(allParams, "hoVaTen"), getStringParams(allParams, "soCmt"),
				getPageable(allParams, paginate));

		model.addAttribute("uri", req.getRequestURI());
		model.addAttribute("currentPage", paginate.getPage());
		model.addAttribute("totalPage", ekycKysos.getTotalPages());
		model.addAttribute("totalElement", ekycKysos.getTotalElements());
		model.addAttribute("ekycKysos", ekycKysos.getContent());
		forwartParams(allParams, model);
		return "quantrikhachhang/log/danhsach2";
	}

	@GetMapping(value = { "/danh-sach-khach-hang/xem" })
	public String danhSachXemGiaoDichDemo(Model model, @RequestParam Map<String, String> allParams,
			HttpServletRequest req, RedirectAttributes redirectAttributes) throws ParseException {
		allParams.put("uri", req.getRequestURI());

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/xem"))
			return "error";

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();

			if (req.getRequestURI().indexOf("/danh-sach-khach-hang/xem") != -1
					&& !coKhuVuc(getKhuVuc(req), ekycKyso.getKhuVuc())) {
				redirectAttributes.addFlashAttribute("error", "Bạn không được xem khu vực này");
				return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
			}

			xemChiTiet(model, allParams, req, ekycKyso);

			ThongTinNguoiDungTuLOS thongTinNguoiDungTuLOS = new ThongTinNguoiDungTuLOS();

			if (!StringUtils.isEmpty(ekycKyso.getThongTinNguoiDungTuLos())) {
				thongTinNguoiDungTuLOS = new Gson().fromJson(ekycKyso.getThongTinNguoiDungTuLos(),
						ThongTinNguoiDungTuLOS.class);
			} else {
				String respose = callSeabank.getInforLos(ekycKyso.getCaseId(), req, ekycKyso);

				JSONObject jsonObject = new JSONObject(respose);
				thongTinNguoiDungTuLOS = callSeabank.getInfor(respose, jsonObject, model);
				ekycKyso.setThongTinNguoiDungTuLos(new Gson().toJson(thongTinNguoiDungTuLOS));
				ekycKysoRepository.save(ekycKyso);
			}
			model.addAttribute("thongTinNguoiDungTuLOS", thongTinNguoiDungTuLOS);
		}

		forwartParams(allParams, model);
		return "quantrikhachhang/log/xemdemo";
	}

	@GetMapping(value = { "/danh-sach-khach-hang/dong-tai-khoan" })
	public String dongTaiKhoan(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req,
			RedirectAttributes redirectAttributes) throws ParseException {
		allParams.put("uri", req.getRequestURI());
		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/dong-tai-khoan"))
			return "error";
		try {
			if (!StringUtils.isEmpty(allParams.get("id"))) {
				EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();

				LOGGER.info("Close Acc: " + ekycKyso.getSoDienThoai() + " - " + ekycKyso.getAccountId());

				String resp = callSeabank.closeAcccCron(ekycKyso.getAccountId(), ekycKyso.getCoCode(), ekycKyso,
						getUserName(req));
				if (resp == null)
					throw new ErrorException("Đóng tài khoản không thành công");

				ekycKyso.setTtTKTT(Contains.TRANG_THAI_MO_TAI_KHOAN_TT_THAT_BAI);
				ekycKyso.setCronDongTk(2);
				ekycKysoRepository.save(ekycKyso);

				redirectAttributes.addFlashAttribute("success", "Đóng tài khoản thành công");
			} else {
				throw new ErrorException("Đóng tài khoản không thành công");
			}
		} catch (ErrorException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
		}

		return "redirect:/danh-sach-khach-hang";
	}

	@GetMapping(value = { "/danh-sach-khach-hang-bh/xem" })
	public String danhSachXemBhGiaoDichDemo(Model model, @RequestParam Map<String, String> allParams,
			HttpServletRequest req, RedirectAttributes redirectAttributes) throws ParseException {
		allParams.put("uri", req.getRequestURI());

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();

			if (req.getRequestURI().indexOf("/danh-sach-khach-hang/xem") != -1
					&& !coKhuVuc(getKhuVuc(req), ekycKyso.getKhuVuc())) {
				redirectAttributes.addFlashAttribute("error", "Bạn không được xem khu vực này");
				return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
			}

			xemChiTiet(model, allParams, req, ekycKyso);
		}

		forwartParams(allParams, model);
		return "quantrikhachhang/log/xemdemo";
	}

	@GetMapping(value = { "/chia-se-thu-muc/sao-ke-ngan-hang" })
	public String xemGiayChungNhan(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req,
			RedirectAttributes redirectAttributes) throws ParseException {
		allParams.put("uri", req.getRequestURI());

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();

			if (req.getRequestURI().indexOf("/danh-sach-khach-hang/xem") != -1
					&& !coKhuVuc(getKhuVuc(req), ekycKyso.getKhuVuc())) {
				redirectAttributes.addFlashAttribute("error", "Bạn không được xem khu vực này");
				return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
			}

			xemChiTiet(model, allParams, req, ekycKyso);
		}

		forwartParams(allParams, model);
		return "quantrikhachhang/log/giaychungnhan";
	}

	@GetMapping(value = { "/danh-sach-khach-hang/xem-thong-tin-giai-ngan" })
	public String thongTinGiaiNgan(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req,
			RedirectAttributes redirectAttributes) throws ParseException {
		allParams.put("uri", req.getRequestURI());

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/xem-thong-tin-giai-ngan"))
			return "error";

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			Thongtingiaingan thongtingiaingan = gson.fromJson(ekycKyso.getNoiDungGiaiNgan(), Thongtingiaingan.class);
			model.addAttribute("thongtingiaingan", thongtingiaingan);
		}

		forwartParams(allParams, model);
		return "quantrikhachhang/log/thongTinGiaiNgan";
	}

	@GetMapping(value = { "/danh-sach-khach-hang/xem-thong-tin-tktt-sb" })
	public String thongTinTKTTSb(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req,
			RedirectAttributes redirectAttributes) throws ParseException {
		allParams.put("uri", req.getRequestURI());

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/xem-thong-tin-tktt-sb"))
			return "error";

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			EkycKyso ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();

			Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
			if (!StringUtils.isEmpty(ekycKyso.getThongTinTaiKhoanSeabank())) {
				TaiKhoan thongtingiaingan = gson.fromJson(ekycKyso.getThongTinTaiKhoanSeabank(), TaiKhoan.class);
				model.addAttribute("thongtingiaingan", thongtingiaingan);
			}
		}

		forwartParams(allParams, model);
		return "quantrikhachhang/log/thongTinTKTTSB";
	}

	private void xemChiTiet(Model model, Map<String, String> allParams, HttpServletRequest req, EkycKyso ekycKyso) {
		if (!StringUtils.isEmpty(allParams.get("id"))) {
			FormInfo formInfo = new Gson().fromJson(ekycKyso.getNoiDungForm(), FormInfo.class);

			if (!StringUtils.isEmpty(ekycKyso.getDuongDanFileKySeaBank())) {
				String str = ekycKyso.getDuongDanFileKySeaBank();
				System.err.println("str: " + str);
				File file3 = new File(str);
				System.err.println("file3: " + file3);
				String base64Img3 = CommonUtils.encodeFileToBase64Binary(file3);
				System.err.println("base64Img3: " + base64Img3);
				model.addAttribute("fileSeaBank", base64Img3);
			}

			model.addAttribute("ekycKyso", ekycKyso);
			model.addAttribute("formInfo", formInfo);

			if (!StringUtils.isEmpty(ekycKyso.getDanhSachFile())) {
				try {
					TaiLieu saokeTl = new Gson().fromJson(ekycKyso.getDanhSachFile(), TaiLieu.class);
					model.addAttribute("danhSachFile", saokeTl);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			if (!StringUtils.isEmpty(ekycKyso.getDanhSachFilePaySlip())) {
				try {
					TaiLieu saokePs = new Gson().fromJson(ekycKyso.getDanhSachFilePaySlip(), TaiLieu.class);
					model.addAttribute("danhSachFilePaySlip", saokePs);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

	}

	@RequestMapping(value = "/danh-sach-khach-hang/xoa", method = { RequestMethod.GET })
	public String delete(Model model, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes, HttpServletRequest req) {
		if (!StringUtils.isEmpty(allParams.get("id"))) {
			EkycKyso checkNd = ekycKysoRepository.findById(Long.valueOf(allParams.get("id"))).get();
			if (checkNd != null && StringUtils.isEmpty(checkNd.getToken())) {

				if (!coKhuVuc(getKhuVuc(req), checkNd.getKhuVuc())) {
					redirectAttributes.addFlashAttribute("error", "Bạn không được xóa khu vực này");
					return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
				}

				ekycKysoRepository.delete(checkNd);
				redirectAttributes.addFlashAttribute("success", "Xóa thành công");
			} else {
				redirectAttributes.addFlashAttribute("error", "Xóa thất bại");
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "Xóa thất bại");
		}

		return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
	}

	@RequestMapping(value = "/danh-sach-khach-hang/thay-doi-trang-thai", method = { RequestMethod.GET })
	public String thayDoiTrangThai(Model model, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes, HttpServletRequest req) {

		if (!isAllowUrlContains(req, "/danh-sach-khach-hang/thay-doi-trang-thai"))
			return "error";

		if (!StringUtils.isEmpty(allParams.get("id"))) {
			Optional<EkycKyso> ekycKyso = ekycKysoRepository.findById(Long.valueOf(allParams.get("id")));
			if (!ekycKyso.isPresent()) {
				redirectAttributes.addFlashAttribute("error", "Không tồn tại bản ghi");
			} else {
				if (!coKhuVuc(getKhuVuc(req), ekycKyso.get().getKhuVuc())) {
					redirectAttributes.addFlashAttribute("error", "Bạn không được xóa khu vực này");
					return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
				}

				if ((StringUtils.isEmpty(ekycKyso.get().getChiDinh()) && ekycKyso.get().getTrangThai().equals("1"))
						|| (!StringUtils.isEmpty(ekycKyso.get().getChiDinh())
								&& ekycKyso.get().getTrangThai().equals("2"))) {
					ekycKyso.get().setTrangThai("3");
					ekycKysoRepository.save(ekycKyso.get());
					redirectAttributes.addFlashAttribute("success", "Hoàn thành hồ sơ vay");
				} else {
					redirectAttributes.addFlashAttribute("error", "Trạng thái hợp đồng không thể hoàn thành hồ sơ");
				}
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "Không có Id");
		}

		return "redirect:/danh-sach-khach-hang?" + queryStringBuilder(allParams);
	}

	@GetMapping("/danh-sach-khach-hang/export")
	public ResponseEntity<Resource> doiSoatKiemTraEp(@RequestParam Map<String, String> allParams,
			HttpServletRequest req) throws ParseException {

		Path path = Paths.get("C:\\image\\kyso\\80137193.zip");
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/zip"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

//		String filename = "danh_sach_hd.xlsx";
//		InputStreamResource file = new InputStreamResource(loadDoiSoatKiemTra(allParams, req));
//
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	public ByteArrayInputStream loadDoiSoatKiemTra(Map<String, String> allParams, HttpServletRequest req)
			throws ParseException {
		String tinhTrangCapMa = "1";
		boolean searchDate = true;

		String khuVuc = getStringParams(allParams, "khuVuc");

		if (StringUtils.isEmpty(khuVuc)) {
			if (StringUtils.isEmpty(khuVuc) && danhSachKhuVuc(getKhuVuc(req)).size() == 63) {
				khuVuc = null;
			} else {
				khuVuc = danhSachKhuVuc(getKhuVuc(req)).size() > 0 ? danhSachKhuVuc(getKhuVuc(req)).get(0) : "";
			}
		}

		if (khuVuc != null && !danhSachKhuVuc(getKhuVuc(req)).contains(khuVuc)) {
			khuVuc = "";
		}

		List<EkycKyso> ekycKysos = ekycKySoService.searchAll(allParams, searchDate, tinhTrangCapMa, khuVuc);
		return tutorialsToExcelKiemTra(ekycKysos);

	}

	public ByteArrayInputStream tutorialsToExcelKiemTra(List<EkycKyso> ekycKysos) {
		String[] HEADERs = { "Số hợp đồng", "Số chứng minh thư", "Họ và tên", "Điện thoại", "Email", "Trạng Thái",
				"Trạng Thái eKYC", "Lỗi eKyc", "Bảo hiểm", "Ngày tạo", "Ngày khách hàng ký", "Ngày bảo hiểm ký",
				"Khu vực", "Điểm so sánh khuôn mặt", "Ghi chú", "Billcode", "UUID", "Người tạo",
				"Ngày yêu cầu mở TKTT Seabank", "Mở TKTT SeaBank", "Trạng thái TKTT", "Status gửi chứng tử mở TKTT SB",
				"Sản phẩm hạn mức", "Yêu cầu giải ngân", "Số tiền giải ngân" };
		SimpleDateFormat dateFormatSql = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String SHEET = "danh_sach_hd";
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (EkycKyso m : ekycKysos) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(m.getSoTaiKhoan());
				row.createCell(1).setCellValue(m.getSoCmt());
				row.createCell(2).setCellValue(m.getHoVaTen());
				row.createCell(3).setCellValue(m.getSoDienThoai());
				row.createCell(4).setCellValue(m.getEmail());
				row.createCell(5).setCellValue(convertTrangThai(m.getTrangThai(), m.getToken()));
				row.createCell(6).setCellValue(convertTrangThaiEkyc(m.getTrangThaiEkyc()));
				row.createCell(7).setCellValue(m.getThongBao());
				row.createCell(8).setCellValue(convertBaoHiem(m.getChiDinh()));
				row.createCell(9).setCellValue(dateFormatSql.format(m.getNgayTao()));
				row.createCell(10)
						.setCellValue(m.getKhachHangKy() != null ? dateFormatSql.format(m.getKhachHangKy()) : "");
				row.createCell(11).setCellValue(m.getBaoHiemKy() != null ? dateFormatSql.format(m.getBaoHiemKy()) : "");
				row.createCell(12).setCellValue(m.getKhuVuc());
				row.createCell(13).setCellValue(m.getDiemEkyc());
				row.createCell(14).setCellValue(m.getGhiChu());
				row.createCell(15).setCellValue(m.getBillCodeKySo());
				row.createCell(16).setCellValue(m.getUuidKySo());
				row.createCell(17).setCellValue(m.getNguoiTao());
				row.createCell(18)
						.setCellValue(m.getNgayYCMoTKTT() != null ? dateFormatSql.format(m.getNgayYCMoTKTT()) : "");
				row.createCell(19).setCellValue(m.getMoTKTTSB());
				row.createCell(20).setCellValue(m.getTtTKTT());
				row.createCell(21).setCellValue(m.getTtGuiChungTu());
				row.createCell(22).setCellValue(m.getSpHanMuc());
				row.createCell(23).setCellValue(m.getYeuCauGiaiNgan());
				row.createCell(24).setCellValue(Utils.formatNumberDotVND(m.getSoTienGiaiNgan()));
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	private String convertBaoHiem(String chiDinh) {
		try {
			if (StringUtils.isEmpty(chiDinh))
				return "Không có";
			if (!StringUtils.isEmpty(chiDinh))
				return "Có";
		} catch (Exception e) {
		}
		return "";
	}

	private String convertTrangThaiEkyc(String trangThaiEkyc) {
		try {
			if (StringUtils.isEmpty(trangThaiEkyc))
				return "Chưa xác thực";
			if (trangThaiEkyc.equals("thanhcong"))
				return "Thành công";
			if (trangThaiEkyc.equals("thatbai"))
				return "Thất bại";
		} catch (Exception e) {
		}
		return trangThaiEkyc;
	}

	private String convertTrangThai(String trangThai, String token) {
		try {
			if (trangThai.equals("0")) {
				return "Chưa ký";
			}
			if (trangThai.equals("2")) {
				return "Bảo hiểm ký";
			}
			if (trangThai.equals("3")) {
				return "Hoàn thành";
			}
			if (trangThai.equals("4")) {
				return "Chờ ký";
			}
			if (trangThai.equals("1")) {
				return "Khách hàng ký";
			}
		} catch (Exception e) {
		}
		return trangThai;
	}
}
