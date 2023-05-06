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

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.repository.LogApiRepository;
import fis.com.vn.repository.LogCodeTransactionRepository;
import fis.com.vn.repository.QuanLyKhachHangRepository;
import fis.com.vn.table.LogApi;
import fis.com.vn.table.LogCodeTransaction;

@Controller
public class DoiSoatController  extends BaseController{
	@Value("${CODE}")
	protected String code;
	
	@Autowired QuanLyKhachHangRepository quanLyKhachHangRepository;
	@Autowired LogCodeTransactionRepository logCodeTransactionRepository;
	@Autowired LogApiRepository logApiRepository;
	
	@GetMapping(value = "/doi-soat-giao-dich")
	public String doiSoatGiaoDich(Model model, @RequestParam Map<String, String> allParams) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		String fromDate = allParams.get("fromDate");
		String toDate = allParams.get("toDate");
		
		if(StringUtils.isEmpty(fromDate)) {
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
		Page<LogCodeTransaction> logApis = logCodeTransactionRepository.selectParams(
				code,
				getStringParams(allParams, "maGiaoDich"),
				dateFormatSql.parse(fromDate),
				dateFormatSql.parse(toDate),
				getStringParams(allParams, "soDienThoai"),
				getStringParams(allParams, "soCmt"),
				getStringParams(allParams, "soHopDong"),
				getStringParams(allParams, "hoVaTen"), 
				getPageable(allParams, paginate)
				);
		
		model.addAttribute("currentPage", paginate.getPage());
        model.addAttribute("totalPage", logApis.getTotalPages());
        model.addAttribute("totalElement", logApis.getTotalElements());
        model.addAttribute("logApis", logApis.getContent());
		
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		
		forwartParams(allParams, model);
		return "doisoat/danhsachgiaodich";
	}
	@GetMapping(value = "/doi-soat-giao-dich/xem")
	public String danhSachXemGiaoDich(Model model, @RequestParam Map<String, String> allParams) throws ParseException {
		if(!StringUtils.isEmpty(allParams.get("maGiaoDich"))) {
			List<LogApi> logApis = logApiRepository.findByCodeTransaction(allParams.get("maGiaoDich").split("_")[0]);
			model.addAttribute("logApis", logApis);
		}
		
		forwartParams(allParams, model);
		return "doisoat/xemgiaodich";
	}
	
	@GetMapping("/doi-soat-giao-dich/export")
	public ResponseEntity<Resource> getFile(@RequestParam Map<String, String> allParams) throws ParseException {
		String filename = "doi-soat.xlsx";
		InputStreamResource file = new InputStreamResource(load(allParams));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}
	
	public ByteArrayInputStream load(Map<String, String> allParams) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		String fromDate = allParams.get("fromDate");
		String toDate = allParams.get("toDate");
		
		if(StringUtils.isEmpty(fromDate)) {
			toDate = dateFormat.format(new Date());
			fromDate = dateFormat.format(new Date());
		}
			
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(toDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		toDate = dateFormat.format(calendar.getTime());
		
		SimpleDateFormat dateFormatSql = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		List<LogCodeTransaction> logApis = logCodeTransactionRepository.selectParamsAll(
				code,
				getStringParams(allParams, "maGiaoDich"),
				dateFormatSql.parse(fromDate),
				dateFormatSql.parse(toDate),
				getStringParams(allParams, "soDienThoai"),
				getStringParams(allParams, "soCmt"),
				getStringParams(allParams, "soHopDong"),
				getStringParams(allParams, "hoVaTen")
				);
		
		ByteArrayInputStream in = tutorialsToExcel(logApis);
		return in;
	}

	public static ByteArrayInputStream tutorialsToExcel(List<LogCodeTransaction> list) {
		String[] HEADERs = { "Mã giao dịch", "Ngày thực hiện", "Số điện thoại", "Họ và tên", "Số cmt", "Số hợp đồng"};
		String SHEET = "Tutorials";
		SimpleDateFormat dateFormatSql = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet(SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			for (int col = 0; col < HEADERs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADERs[col]);
			}

			int rowIdx = 1;
			for (LogCodeTransaction m : list) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(m.getCodeTransaction());
				row.createCell(1).setCellValue(dateFormatSql.format(m.getDate()));
				row.createCell(2).setCellValue(m.getPhone());
				row.createCell(3).setCellValue(m.getFullName());
				row.createCell(4).setCellValue(m.getIdCard());
				row.createCell(5).setCellValue(m.getIdContract());
			}

			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
}
