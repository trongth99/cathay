package fis.com.vn.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import fis.com.vn.callapi.CallSeabank;
import fis.com.vn.callapi.entities.AccountInfo;
import fis.com.vn.callapi.entities.Bank;
import fis.com.vn.callapi.entities.DanhSachNganHang;
import fis.com.vn.callapi.entities.ThongTinNguoiDungTuLOS;
import fis.com.vn.callapi.entities.ThongTinTaiKhoanDangKy;

@Controller
public class KySoDoanhNghiepCaNhanController extends BaseController {
	@Autowired
	KySoDoanhNghiepCaNhanComponent kySoDoanhNghiepCaNhanComponent;
	@Autowired
	CallSeabank callSeabank;
	@Value("${MOI_TRUONG}")
	String MOI_TRUONG;

	@GetMapping(value = "/reg")
	public String reg(HttpServletRequest req, Model model) {
		return "redirect:/khach-hang/ky-so/step1";
	}

	@GetMapping(value = "/khach-hang/ky-so/thong-tin-luu-y")
	public String luuY(HttpServletRequest req, Model model) {
		return "demo/kySoDoanhNgiepCaNhan/step/thongtinluuy";
	}

	@GetMapping(value = "/khach-hang/ky-so/chi-tiet-dieu-khoan")
	public String chiTietDk(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model) {
		return kySoDoanhNghiepCaNhanComponent.chiTietDk(req, allParams, model);
	}

	@GetMapping(value = "/khach-hang/ky-so/step1")
	public String step1(HttpServletRequest req, Model model) {
		return kySoDoanhNghiepCaNhanComponent.step1(req);
	}

	@PostMapping(value = "/khach-hang/ky-so/step1")
	public String poststep1(HttpServletRequest req, @RequestParam Map<String, String> allParams, Model model) {
		return kySoDoanhNghiepCaNhanComponent.poststep1(req, allParams, model);
	}

	@GetMapping(value = "/khach-hang/ky-so/step2")
	public String getstep2(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		return kySoDoanhNghiepCaNhanComponent.getstep2(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/step2")
	public String step2(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		return kySoDoanhNghiepCaNhanComponent.step2(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/step20")
	public String step20(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		return kySoDoanhNghiepCaNhanComponent.step20(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/step31")
	public String ste31(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		return kySoDoanhNghiepCaNhanComponent.step31(req, allParams, model);
	}

	@GetMapping(value = "/khach-hang/ky-so/mo-tai-khoan-1")
	public String getmoTaiKhoanStep1(HttpServletRequest req, Model model, @RequestParam Map<String, String> allParams) throws IOException {
		return kySoDoanhNghiepCaNhanComponent.moTaiKhoanStep1(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/mo-tai-khoan-1")
	public String moTaiKhoanStep1(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		return kySoDoanhNghiepCaNhanComponent.moTaiKhoanStep1(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/mo-tai-khoan-2")
	public String moTaiKhoanStep2(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		return kySoDoanhNghiepCaNhanComponent.moTaiKhoanStep2(req, allParams, model);
	}

	@GetMapping(value = "/khach-hang/ky-so/thong-tin-giai-ngan")
	public String thongTinGiaiNgan(HttpServletRequest req, Model model)
			throws KeyManagementException, NoSuchAlgorithmException, ParseException, IOException {

		return kySoDoanhNghiepCaNhanComponent.thongTinGiaiNgan(req, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/mo-tai-khoan-3")
	public String moTaiKhoanStep3(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model, RedirectAttributes redirectAttributes)
			throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		return kySoDoanhNghiepCaNhanComponent.moTaiKhoanStep3(req, allParams, model, redirectAttributes);
	}

	@PostMapping(value = "/khach-hang/ky-so/list-branch", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String listBranch(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		return kySoDoanhNghiepCaNhanComponent.listBranch(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/bankCode", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String bankCode(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws KeyManagementException, NoSuchAlgorithmException, IOException {
		return kySoDoanhNghiepCaNhanComponent.bankCode(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/bankacc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ThongTinTaiKhoanDangKy getAccountInfor(@RequestParam Map<String, String> allParams, HttpServletRequest req,
			Model model) throws KeyManagementException, NoSuchAlgorithmException, IOException {
		return kySoDoanhNghiepCaNhanComponent.getAccountInfor(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/inforLos", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ThongTinNguoiDungTuLOS getInforLOS(@RequestParam Map<String, String> allParams, HttpServletRequest req,
			Model model) throws KeyManagementException, NoSuchAlgorithmException, IOException, ParseException {
		return kySoDoanhNghiepCaNhanComponent.getInforLOS(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/mo-tai-khoan-4")
	public String tmoTaiKhoanStep3(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException, ParseException, KeyManagementException, NoSuchAlgorithmException {
		return kySoDoanhNghiepCaNhanComponent.moTaiKhoanStep4(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/step32")
	public String step32(HttpServletRequest req, Model model, @RequestParam Map<String, String> allParams) {
		return kySoDoanhNghiepCaNhanComponent.step32(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/step3")
	public String nhapThongTin(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model)
			throws IOException {
		return kySoDoanhNghiepCaNhanComponent.step3(req, allParams, model);
	}

	@PostMapping(value = "/khach-hang/ky-so/dang-ky", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String kySo(@RequestParam Map<String, String> allParams, HttpServletRequest req, Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		return kySoDoanhNghiepCaNhanComponent.dangKyKySo(req, allParams, model, redirectAttributes);
	}

	@GetMapping(value = "/viewpdf/byte/{path}", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getImage(HttpServletResponse resp, @RequestParam Map<String, String> allParams,
			@PathVariable("path") String path) {
		return kySoDoanhNghiepCaNhanComponent.getImage(resp, allParams, path);
	}

	@GetMapping(value = "/download/byte/{path}", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<InputStreamResource> download(HttpServletResponse resp,
			@RequestParam Map<String, String> allParams, @PathVariable("path") String path) {
		return kySoDoanhNghiepCaNhanComponent.download(resp, allParams, path);
	}

	@GetMapping(value = "/viewpdf")
	public String viewpdf(Model model) {
		return "viewpdf";
	}
}
