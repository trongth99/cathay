package fis.com.vn.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonMappingException;

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.repository.LogApiDetailSBRepository;
import fis.com.vn.repository.LogApiSeaBankRepository;
import fis.com.vn.table.LogApiSeaBank;

@Controller
public class LogApiSeaBankController extends BaseController {
	@Value("${CODE}")
	protected String code;

	@Autowired
	LogApiSeaBankRepository logApiSeaBankRepository;

	@Autowired
	LogApiDetailSBRepository logApiDetailSBRepository;

	
	@GetMapping("/danh-sach-log-api-seabank/export")
	public ResponseEntity<Resource> doiSoatKiemTraEp(@RequestParam Map<String, String> allParams,
			HttpServletRequest req) throws ParseException {
		String filename = "danh_sach_log.xlsx";
		InputStreamResource file = new InputStreamResource(loadDoiSoatKiemTra(allParams, req));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
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

		List<LogApiSeaBank> logApiSeaBanks = logApiSeaBankRepository.selectParamsAll(
				getStringParams(allParams, "uri"),
				getStringParams(allParams, "soCmt"), 
				getStringParams(allParams, "soHopDong"),
				getStringParams(allParams, "id_los"), 
				getIntParams(allParams, "status"), 
				dateFormatSql.parse(fromDate),
				dateFormatSql.parse(toDate),
				getStringParams(allParams, "loaiApi") 
				);

		return tutorialsToExcelKiemTra(logApiSeaBanks, allParams);

	}

	public ByteArrayInputStream tutorialsToExcelKiemTra(List<LogApiSeaBank> ekycKysos, Map<String, String> allParams) {
		String[] HEADERs = { "Los ID", "Số cmt	", "Số hợp đồng", "Uri", "Trạng thái",
					"Thời gian xử lý(ms)", "Thời gian", "Phương thức", "Mô tả", "Params", "Response" };

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
			for (LogApiSeaBank m : ekycKysos) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(m.getIdLos());
				row.createCell(1).setCellValue(m.getIdCard());
				row.createCell(2).setCellValue(m.getIdContract());
				row.createCell(3).setCellValue(m.getUri());
				row.createCell(4).setCellValue(m.getStatus());
				row.createCell(5).setCellValue(m.getTimeHandling());
				row.createCell(6).setCellValue(dateFormat.format(m.getDate()));
				row.createCell(7).setCellValue(m.getMethod());
				row.createCell(8).setCellValue(m.getMota());
				row.createCell(9).setCellValue(m.getParams());
				row.createCell(10).setCellValue(m.getResponse());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
	
	@GetMapping(value = "/danh-sach-log-api-seabank")
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

		Page<LogApiSeaBank> logApiSeaBanks = logApiSeaBankRepository.selectParams(
				getStringParams(allParams, "uri"),
				getStringParams(allParams, "soCmt"), 
				getStringParams(allParams, "soHopDong"),
				getStringParams(allParams, "id_los"), 
				getIntParams(allParams, "status"), 
				dateFormatSql.parse(fromDate),
				dateFormatSql.parse(toDate),
				getStringParams(allParams, "loaiApi"),
				getPageable(allParams, paginate));
		model.addAttribute("currentPage", paginate.getPage());
		model.addAttribute("totalPage", logApiSeaBanks.getTotalPages());
		model.addAttribute("totalElement", logApiSeaBanks.getTotalElements());
		model.addAttribute("logApiSeaBanks", logApiSeaBanks.getContent());

		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);

		forwartParams(allParams, model);
		return "log/danhsachlogapiSeaBank";
	}

	@GetMapping(value = { "/danh-sach-log-seabank/xem2", "/danh-sach-log-api-seabank/xem2" })
	public String xemLog2(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
			throws ParseException, JsonMappingException {
		String id = allParams.getOrDefault("id", "");
		LogApiSeaBank logApiSeaBank = logApiSeaBankRepository.findById(Long.valueOf(id)).get();

		try {
			model.addAttribute("logApiSeaBank", logApiSeaBank);
			model.addAttribute("response", formatJson(logApiSeaBank.getResponse()));
			model.addAttribute("params", formatJson(logApiSeaBank.getParams()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		forwartParams(allParams, model);
		return "log/xemseabank";
	}

//	@GetMapping(value = { "/danh-sach-log-seabank/xem2", "/danh-sach-log-seabank/xem2" })
//	public String xemLog3(Model model, @RequestParam Map<String, String> allParams, HttpServletRequest req)
//			throws ParseException, JsonMappingException {
//		String logId = allParams.getOrDefault("logId", "123456789");
//		String id = allParams.getOrDefault("id", "");
//		LogApiDetailSeaBank logApiDetailSeaBank = new LogApiDetailSeaBank();
//		LogApiSeaBank log = new LogApiSeaBank();
//		if (!StringUtils.isEmpty(allParams.get("code"))) {
//			LogApiSeaBank logApiSeaBank = logApiSeaBankRepository.findById(Long.valueOf(id)).get();
//			updateObjectToObject(log, logApiSeaBank);
//			LogApiDetailSeaBank logApiDetailSeaBank2 = logApiDetailSBRepository.findByLogId(logApiSeaBank.getLogId());
//			if (logApiDetailSeaBank2 != null)
//				updateObjectToObject(logApiDetailSeaBank, logApiDetailSeaBank2);
//		}
//
//		try {
//			StringBuilder stringBuilder = new StringBuilder();
//			stringBuilder.append(log.getUri() + "|" + log.getMethod() + "|" + log.getCode());
//			stringBuilder.append("<br/><hr/>");
//			stringBuilder.append(logApiDetailSeaBank.getParams());
//			stringBuilder.append("<br/><hr/>");
//			if (!StringUtils.isEmpty(logApiDetailSeaBank.getImages())) {
//				JSONObject jsonObject = new JSONObject(logApiDetailSeaBank.getImages());
//				for (String key : jsonObject.keySet()) {
//					if (key.equals("anhNhanDang") || key.equals("anhVideo")) {
//						for (int i = 0; i < jsonObject.getJSONArray(key).length(); i++) {
//							stringBuilder.append(key + ": " + jsonObject.getJSONArray(key).getString(i) + "<br/>");
//						}
//					} else {
//						stringBuilder.append(key + ": " + jsonObject.getString(key) + "<br/>");
//					}
//				}
//			}
//			String response = "";
//			if (!StringUtils.isEmpty(logApiDetailSeaBank.getResponse())) {
//				response = logApiDetailSeaBank.getResponse().replaceAll("Response:[0-9 ]*[\\|]*", "")
//						.replaceAll("\\r|\\n", "").replace("'", "\\\'").trim();
//				;
//				stringBuilder.append(
//						"Response:<div style='position: relative;width: 100%;height: 300px;'><div id='response'></div></div>");
//				stringBuilder.append("<br/>");
//			}
//			model.addAttribute("response", formatJson(response));
//
//			String linkImage = "/danh-sach-log";
//			if (req.getRequestURI().indexOf("/danh-sach-log-api") != -1)
//				linkImage = "/danh-sach-log-api";
//
//			model.addAttribute("logs",
//					stringBuilder.toString().replaceAll(logId, "")
//							.replaceAll("\\[INFO[ ]*\\]", "<b style='color:blue;'>[INFO]</b>")
//							.replaceAll("\\[WARN[ ]*\\]", "<b style='color:yellow;'>[WARN]</b>")
//							.replaceAll("\\[ERROR[ ]*\\]", "<b style='color:red;'>[ERROR]</b>")
//							.replaceAll("fis.com.vn.([.a-zA-Z]+):", "<span style='color:red;'>$1</span>:")
//							.replaceAll("/image([^.]+).jpg", "/image$1.jpg<br/><img src='" + req.getContextPath()
//									+ linkImage + "/img-byte?path=/image$1.jpg' style='max-width:350px;'/>"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		forwartParams(allParams, model);
//		return "log/xemseabank";
//	}

	private String formatJson(String json) {
		try {
			return json.replace("\\n", " ").replace("\\r", " ").replace("\n", " ").replace("\r", " ").replace("\t", " ")
					.replace("\\t", " ").replaceAll("\\r|\\n", "").replace("'", "\\\'").trim();
		} catch (Exception e) {
		}
		return json;
	}

}
