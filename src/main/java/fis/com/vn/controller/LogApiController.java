package fis.com.vn.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonMappingException;

import fis.com.vn.api.face.ThongTinAnh;
import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.repository.LogApiDetailRepository;
import fis.com.vn.repository.LogApiRepository;
import fis.com.vn.repository.QuanLyKhachHangRepository;
import fis.com.vn.table.LogApi;
import fis.com.vn.table.LogApiDetail;

@Controller
public class LogApiController extends BaseController {
	@Value("${CODE}")
	protected String code;

	@Autowired
	LogApiRepository logApiRepository;
	@Autowired
	LogApiDetailRepository logApiDetailRepository;
	@Autowired
	QuanLyKhachHangRepository quanLyKhachHangRepository;
	@Autowired
	ThongTinAnh thongTinAnh;

	@GetMapping("/danh-sach-log-api/export")
	public ResponseEntity<Resource> doiSoatKiemTraEp(@RequestParam Map<String, String> allParams,
			HttpServletRequest req) throws ParseException {
		String filename = "danh_sach_log.xlsx";
		InputStreamResource file = new InputStreamResource(loadDoiSoatKiemTra(allParams, req));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/pdf")).body(file);
	}

	public ByteArrayInputStream loadDoiSoatKiemTra(Map<String, String> allParams, HttpServletRequest req)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		String fromDate = allParams.get("fromDate");
		String toDate = allParams.get("toDate");

		if (StringUtils.isEmpty(fromDate)) {
			toDate = dateFormat.format(new Date());
			fromDate = dateFormat.format(new Date());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(toDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		toDate = dateFormat.format(calendar.getTime());

		SimpleDateFormat dateFormatSql = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		List<LogApi> logApis = logApiRepository.selectParamsAll(code, getStringParams(allParams, "uri"),
				getStringParams(allParams, "maGiaoDich"), getIntParams(allParams, "status"),
				dateFormatSql.parse(fromDate), dateFormatSql.parse(toDate), getStringParams(allParams, "soDienThoai"),
				getStringParams(allParams, "soCmt"), getStringParams(allParams, "soHopDong"),
				getStringParams(allParams, "hoVaTen"));

		return tutorialsToExcelKiemTra(logApis, allParams);

	}

	public ByteArrayInputStream tutorialsToExcelKiemTra(List<LogApi> ekycKysos, Map<String, String> allParams) {
		String[] HEADERs = null;
		if (!StringUtils.isEmpty(allParams.get("detail"))) {
			String[] HEADERs1 = { "Uri", "Trạng thái", "Thời gian xử lý(ms)", "Thời gian", "Phương thức",
					"Mã giao dịch", "Số điện thoại", "Họ và tên", "Số cmt", "Số hợp đồng", "Điểm so sánh khuôn mặt" };

			HEADERs = HEADERs1;
		} else {
			String[] HEADERs1 = { "Uri", "Trạng thái", "Thời gian xử lý(ms)", "Thời gian", "Phương thức",
					"Mã giao dịch", "Số điện thoại", "Họ và tên", "Số cmt", "Số hợp đồng" };

			HEADERs = HEADERs1;
		}

		String SHEET = "danh_sach_log";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (LogApi m : ekycKysos) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(m.getUri());
				row.createCell(1).setCellValue(m.getStatus());
				row.createCell(2).setCellValue(m.getTimeHandling());
				row.createCell(3).setCellValue(dateFormat.format(m.getDate()));
				row.createCell(4).setCellValue(m.getMethod());
				row.createCell(5).setCellValue(m.getCodeTransaction());
				row.createCell(6).setCellValue(m.getPhone());
				row.createCell(7).setCellValue(m.getFullName());
				row.createCell(8).setCellValue(m.getIdCard());
				row.createCell(9).setCellValue(m.getIdContract());
				if (!StringUtils.isEmpty(allParams.get("detail"))) {
					try {
						if (m.getUri().indexOf("/api/public/all/xac-thuc-khuon-mat") != -1) {
							LogApiDetail logApiDetail = logApiDetailRepository.findByLogId(m.getLogId());
							System.out.println(logApiDetail.getResponse());
							JSONObject jsonObject = new JSONObject(logApiDetail.getResponse());
							row.createCell(10).setCellValue(jsonObject.getDouble("data"));
						}
					} catch (Exception e) {
					}
				}
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	public static String layMaGiaoDich(int day) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, day);
			String timeStr = String.valueOf(calendar.getTimeInMillis());
			String timeStrOne = timeStr.substring(0, 4);
			String timeStrTwo = timeStr.substring(4, 8);
			String timeStrThree = timeStr.substring(8, timeStr.length());
			String maGiaDich = ranDomChar(timeStrTwo) + "-" + UUID.randomUUID().toString() + "-"
					+ ranDomChar(timeStrOne) + "-" + ranDomChar(timeStrThree);

			return maGiaDich;
		} catch (Exception e) {
		}
		return null;
	}

	private static String ranDomChar(String timeStr) {
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'a');
		char c1 = (char) (r.nextInt(26) + 'a');
		return String.valueOf(c1) + timeStr + String.valueOf(c);
	}

	@GetMapping(value = "/danh-sach-log-api")
	public String danhSachLogApi(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
			throws ParseException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		String fromDate = allParams.get("fromDate");
		String toDate = allParams.get("toDate");

		if (StringUtils.isEmpty(fromDate)) {
			toDate = dateFormat.format(new Date());
			fromDate = dateFormat.format(new Date());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(toDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		toDate = dateFormat.format(calendar.getTime());

		SimpleDateFormat dateFormatSql = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));

		Page<LogApi> logApis = logApiRepository.selectParams(code, getStringParams(allParams, "uri"),
				getStringParams(allParams, "maGiaoDich"), getIntParams(allParams, "status"),
				dateFormatSql.parse(fromDate), dateFormatSql.parse(toDate), getStringParams(allParams, "soDienThoai"),
				getStringParams(allParams, "soCmt"), getStringParams(allParams, "soHopDong"),
				getStringParams(allParams, "hoVaTen"), getPageable(allParams, paginate));

		model.addAttribute("currentPage", paginate.getPage());
		model.addAttribute("totalPage", logApis.getTotalPages());
		model.addAttribute("totalElement", logApis.getTotalElements());
		model.addAttribute("logApis", logApis.getContent());

		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		forwartParams(allParams, model);
		return "log/danhsachlogapi";
	}

	@GetMapping(value = { "/danh-sach-log-api/xem" })
	public String xemLog(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
			throws ParseException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String logId = allParams.getOrDefault("logId", "123456789");
		String time = allParams.getOrDefault("time", "");
		String id = allParams.getOrDefault("id", "");
		String fileLogName = "project_eid.log";
		if (!StringUtils.isEmpty(allParams.get("code"))) {
			LogApi logApi = logApiRepository.findById(Long.valueOf(id)).get();
			if (!StringUtils.isEmpty(logApi.getFileLog()))
				fileLogName = logApi.getFileLog();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		if (!time.equals(dateFormat.format(new Date()))) {
			fileLogName = fileLogName + "." + time + ".0";
			try {
				File f = new File("/image/logs/" + fileLogName + ".gz");
				if (f.exists()) {
					processBuilder.command("/bin/bash", "-c", "gunzip /image/logs/" + fileLogName + ".gz");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		processBuilder.command("/bin/bash", "-c",
				"cat /image/logs/" + fileLogName + " | grep " + logId + " -A1 | tail -n 200");

		try {

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			String matTruoc = "";
			String matSau = "";
			String ocr = "";
			String response = "";
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(logId) != -1) {
					stringBuilder.append(line);
				} else {
					if (line.startsWith("Mat Truoc ocr:")) {
						matTruoc = line.replaceAll("Mat Truoc ocr:", "").replaceAll("\\r|\\n", "").replace("'", "\\\'")
								.trim();
						stringBuilder.append(
								"Mat Truoc ocr:<div style='position: relative;width: 100%;height: 300px;'><div id='matTruoc'></div></div>");
					} else if (line.startsWith("Mat Sau ocr:")) {
						matSau = line.replaceAll("Mat Sau ocr:", "").replaceAll("\\r|\\n", "").replace("'", "\\\'")
								.trim();
						;
						stringBuilder.append(
								"Mat Sau ocr:<div style='position: relative;width: 100%;height: 300px;'><div id='matSau'></div></div>");
					} else if (line.startsWith("Ocr:")) {
						ocr = line.replaceAll("Ocr:", "").replaceAll("\\r|\\n", "").replace("'", "\\\'").trim();
						;
						stringBuilder.append(
								"Ocr:<div style='position: relative;width: 100%;height: 300px;'><div id='ocr'></div></div>");
					} else if (line.startsWith("Response:")) {
						response = line.replaceAll("Response:[0-9 ]*[\\|]*", "").replaceAll("\\r|\\n", "")
								.replace("'", "\\\'").trim();
						;
						stringBuilder.append(
								"Response:<div style='position: relative;width: 100%;height: 300px;'><div id='response'></div></div>");
					} else
						stringBuilder.append("<span style='color:green;'>" + line + "</span>");
				}
				stringBuilder.append("<br/>");
			}
			model.addAttribute("matTruoc", formatJson(matTruoc));
			model.addAttribute("matSau", formatJson(matSau));
			model.addAttribute("ocr", formatJson(ocr));
			model.addAttribute("response", formatJson(response));

			String linkImage = "/danh-sach-log";
			if (req.getRequestURI().indexOf("/danh-sach-log-api") != -1)
				linkImage = "/danh-sach-log-api";

			int exitCode = process.waitFor();
			System.out.println("Exited with error code : " + exitCode);
			model.addAttribute("logs",
					stringBuilder.toString().replaceAll(logId, "")
							.replaceAll("\\[INFO[ ]*\\]", "<b style='color:blue;'>[INFO]</b>")
							.replaceAll("\\[WARN[ ]*\\]", "<b style='color:yellow;'>[WARN]</b>")
							.replaceAll("\\[ERROR[ ]*\\]", "<b style='color:red;'>[ERROR]</b>")
							.replaceAll("fis.com.vn.([.a-zA-Z]+):", "<span style='color:red;'>$1</span>:")
							.replaceAll("/image([^.]+).jpg", "/image$1.jpg<br/><img src='" + req.getContextPath()
									+ linkImage + "/img-byte?path=/image$1.jpg' style='max-width:350px;'/>"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		forwartParams(allParams, model);
		return "log/xem";
	}

	@GetMapping(value = { "/danh-sach-log/xem2", "/danh-sach-log-api/xem2" })
	public String xemLog2(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
			throws ParseException, JsonMappingException {
		String logId = allParams.getOrDefault("logId", "123456789");
		String id = allParams.getOrDefault("id", "");
		LogApiDetail logApiDetail = new LogApiDetail();
		LogApi log = new LogApi();
		if (!StringUtils.isEmpty(allParams.get("code"))) {
			LogApi logApi = logApiRepository.findById(Long.valueOf(id)).get();
			updateObjectToObject(log, logApi);
			LogApiDetail logApiDetail2 = logApiDetailRepository.findByLogId(logApi.getLogId());
			if (logApiDetail2 != null)
				updateObjectToObject(logApiDetail, logApiDetail2);
		}

		try {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(log.getUri() + "|" + log.getMethod() + "|" + log.getCode());
			stringBuilder.append("<br/><hr/>");
			stringBuilder.append(logApiDetail.getParams());
			stringBuilder.append("<br/><hr/>");
			if (!StringUtils.isEmpty(logApiDetail.getImages())) {
				JSONObject jsonObject = new JSONObject(logApiDetail.getImages());
				for (String key : jsonObject.keySet()) {
					if (key.equals("anhNhanDang") || key.equals("anhVideo")) {
						for (int i = 0; i < jsonObject.getJSONArray(key).length(); i++) {
							stringBuilder.append(key + ": " + jsonObject.getJSONArray(key).getString(i) + "<br/>");
						}
					} else {
						stringBuilder.append(key + ": " + jsonObject.getString(key) + "<br/>");
					}
				}
			}
			String response = "";
			if (!StringUtils.isEmpty(logApiDetail.getResponse())) {
				response = logApiDetail.getResponse().replaceAll("Response:[0-9 ]*[\\|]*", "").replaceAll("\\r|\\n", "")
						.replace("'", "\\\'").trim();
				;
				stringBuilder.append(
						"Response:<div style='position: relative;width: 100%;height: 300px;'><div id='response'></div></div>");
				stringBuilder.append("<br/>");
			}
			model.addAttribute("response", formatJson(response));

			String linkImage = "/danh-sach-log";
			if (req.getRequestURI().indexOf("/danh-sach-log-api") != -1)
				linkImage = "/danh-sach-log-api";

			model.addAttribute("logs",
					stringBuilder.toString().replaceAll(logId, "")
							.replaceAll("\\[INFO[ ]*\\]", "<b style='color:blue;'>[INFO]</b>")
							.replaceAll("\\[WARN[ ]*\\]", "<b style='color:yellow;'>[WARN]</b>")
							.replaceAll("\\[ERROR[ ]*\\]", "<b style='color:red;'>[ERROR]</b>")
							.replaceAll("fis.com.vn.([.a-zA-Z]+):", "<span style='color:red;'>$1</span>:")
							.replaceAll("/image([^.]+).jpg", "/image$1.jpg<br/><img src='" + req.getContextPath()
									+ linkImage + "/img-byte?path=/image$1.jpg' style='max-width:350px;'/>"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		forwartParams(allParams, model);
		return "log/xem";
	}

	private String formatJson(String json) {
		try {
			return json.replace("\\n", " ").replace("\\r", " ").replace("\n", " ").replace("\r", " ");
		} catch (Exception e) {
		}
		return json;
	}

	@GetMapping(value = { "/danh-sach-log/unzip", "/danh-sach-log-api/unzip" })
	public String unzip(Model model, @RequestParam Map<String, String> allParams) throws ParseException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String time = allParams.getOrDefault("time", "");

		String id = allParams.getOrDefault("id", "");
		String fileLogName = "project_eid.log";

		if (!StringUtils.isEmpty(allParams.get("code"))) {
			LogApi logApi = logApiRepository.findById(Long.valueOf(id)).get();
			if (!StringUtils.isEmpty(logApi.getFileLog()))
				fileLogName = logApi.getFileLog();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (!time.equals(dateFormat.format(new Date()))) {
			fileLogName = fileLogName + "." + time + ".0.gz";
		}
		allParams.put("fileLogName", fileLogName);
		processBuilder.command("/bin/bash", "-c", "gunzip /image/logs/" + fileLogName);

		try {

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
			String line;
			while ((line = reader.readLine()) != null) {

			}
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		forwartParams(allParams, model);
		return "log/unzip";
	}

	@GetMapping(value = { "/danh-sach-log/img-byte",
			"/danh-sach-log-api/img-byte" }, produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<byte[]> getImage(HttpServletResponse resp, @RequestParam Map<String, String> allParams) {
		try {
			String pathImg = allParams.get("path");
			File file = new File(pathImg);

			byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(file));

			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@GetMapping(value = "/danh-sach-log/img")
	public String img(Model model, @RequestParam Map<String, String> allParams) {

		try {
			String pathImg = allParams.get("path");

			if (!StringUtils.isEmpty(pathImg)) {
				File file = new File(pathImg);
				String base64Img = CommonUtils.encodeFileToBase64Binary(file);

				model.addAttribute("base64Img", base64Img);
			}
			forwartParams(allParams, model);
		} catch (Exception e) {
		}
		return "log/img";
	}
}
