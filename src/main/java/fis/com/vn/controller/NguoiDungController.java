package fis.com.vn.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import fis.com.vn.captcha.CaptchaImage;
import fis.com.vn.common.CommonUtils;
import fis.com.vn.common.Email;
import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.common.Utils;
import fis.com.vn.contains.Contains;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.UserGroupRepository;
import fis.com.vn.repository.UserInfoDtoRepository;
import fis.com.vn.repository.UserInfoRepository;
import fis.com.vn.table.UserGroup;
import fis.com.vn.table.UserInfo;
import fis.com.vn.table.UserInfoDto;

@Controller
public class NguoiDungController extends BaseController {
    @Autowired UserInfoRepository userInfoRepository;
    @Autowired UserInfoDtoRepository userInfoDtoRepository;
    @Autowired UserGroupRepository userGroupRepository;
    @Autowired Email email;

    
    @GetMapping(value = "/nguoi-dung/dat-mat-khau")
    public String datMatKhau(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {

        try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception(language.getMessage("sua_that_bai"));
            }
            Optional<UserInfo> userInfo = userInfoRepository.findById(Long.valueOf(allParams.get("id")));
            if (!userInfo.isPresent()) {
                throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
            }

            model.addAttribute("userInfo", userInfo.get());
            forwartParams(allParams, model);
            return "nguoidung/datlaimatkhau";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/nguoi-dung";
        }
    }
    @PostMapping(value = "/nguoi-dung/dat-mat-khau")
    public String postdatMatKhau(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes, @ModelAttribute("UserInfo") UserInfo userInfo) {

        try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception(language.getMessage("sua_that_bai"));
            }
            Optional<UserInfo> userInfoDbc = userInfoRepository.findById(Long.valueOf(allParams.get("id")));
            if (!userInfoDbc.isPresent()) {
                throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
            }
            UserInfo userInfoDb = userInfoDbc.get();
            if (!StringUtils.isEmpty(userInfo.getPassword())) {
            	userInfoDb.setPassword(CommonUtils.getMD5(userInfo.getPassword()));
            }
            
            userInfoRepository.save(userInfoDb);

            email.sendText(userInfoDb.getEmail(), "Thông tin đăng nhập tài khoản", "Thông tin đăng nhập tài khoản: <br/> Tên đăng nhập: "+userInfoDb.getUsername()+"<br/> Mật khẩu: "+userInfo.getPassword());
            
            redirectAttributes.addFlashAttribute("success", language.getMessage("sua_thanh_cong"));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/nguoi-dung";
    }
    
    @GetMapping(value = "/nguoi-dung")
    public String nguoiDung(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
        handlingGet(allParams, model, req);
        forwartParams(allParams, model);
        return "nguoidung/nguoidung";
    }

    private void handlingGet(Map<String, String> allParams, Model model, HttpServletRequest req) {
        Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));
        // clear all param if reset
        if (allParams.get("reset") != null) {
            allParams.clear();
        }
        Map<Long, String> map = listGroup();
        Page<UserInfoDto> userInfos = userInfoDtoRepository.selectParams(
                getStringParams(allParams, "s_uname"),
                getStringParams(allParams, "s_fname"),
                getStringParams(allParams, "s_email"),
                getIntParams(allParams, "s_status"),
                getPageable(allParams, paginate));

        List<UserInfoDto> userInfoDtos = new ArrayList<>();
        for (UserInfoDto userInfoDto : userInfos.getContent()) {
        	String[] arr = userInfoDto.getGroupId().split(",");
        	String group = "";
        	for (String idGroup : arr) {
        		if(map.containsKey(Long.valueOf(idGroup)))
        			group += map.get(Long.valueOf(idGroup))+", ";
        		else
        			group += idGroup+", ";
			}
        	
        	userInfoDto.setNhomNguoiDung(group.replaceAll("[, ]+$", ""));
        	userInfoDtos.add(userInfoDto);
		}
        
        model.addAttribute("currentPage", paginate.getPage());
        model.addAttribute("totalPage", userInfos.getTotalPages());
        model.addAttribute("totalElement", userInfos.getTotalElements());
        model.addAttribute("userInfos", userInfoDtos);
    }

    private Map<Long, String> listGroup() {
    	Iterable<UserGroup> userGroups = userGroupRepository.findByStatus(Contains.TT_NHOM_HOATDONG);
    	Map<Long, String> map = new HashMap<>();
    	for (UserGroup userGroup : userGroups) {
    		map.put(userGroup.getId(), userGroup.getGroupName());
		}
    	
    	return map;
    }
    
    @GetMapping(value = "/nguoi-dung/them-moi")
    public String getguoiDungThemMoi(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) throws JsonProcessingException {
        Iterable<UserGroup> userGroups = userGroupRepository.findByStatus(Contains.TT_NHOM_HOATDONG);
        model.addAttribute("thanhPhos", layTinhThanhPho());
        model.addAttribute("userGroups", userGroups);
        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("name", language.getMessage("them_moi"));
        forwartParams(allParams, model);
        return "nguoidung/addnguoidung";
    }
    
    @PostMapping(value = "/nguoi-dung/them-moi")
    public String postnguoiDungThemMoi(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                       @RequestParam Map<String, String> allParams, @ModelAttribute("userInfo") UserInfo userInfo) {
        try {
            checkErrorMessage(userInfo);

            if (StringUtils.isEmpty(userInfo.getGroupId())) throw new Exception(language.getMessage("chon_nhom_nguoi_dung"));
            UserInfo checkUserName = userInfoRepository.findByUsername(userInfo.getUsername());
            if (checkUserName != null) throw new Exception(language.getMessage("ten_dang_nhap_da_ton_tai"));

            if(StringUtils.isEmpty(userInfo.getKhuVuc()) || userInfo.getKhuVuc().indexOf("tatca") != -1) userInfo.setKhuVuc(String.join(",", layTinhThanhPho()));
            
            String password = Utils.randomPw();
            
            userInfo.setCreateBy(getUserName(req));
            userInfo.setCreateTime(new Date());
            userInfo.setPassword(CommonUtils.getMD5(password));

            email.sendText(userInfo.getEmail(), "Thông tin đăng nhập tài khoản", "Thông tin đăng nhập tài khoản: <br/> Tên đăng nhập: "+userInfo.getUsername()+"<br/> Mật khẩu: "+password);
            
            userInfoRepository.save(userInfo);

            redirectAttributes.addFlashAttribute("success", language.getMessage("them_thanh_cong"));
        } catch (ErrorException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
        }
        model.addAttribute("thanhPhos", layTinhThanhPho());
        return "redirect:/nguoi-dung?"+ getParamsQuery(allParams);
    }

    @GetMapping(value = "/nguoi-dung/sua")
    public String getnguoiDungSua(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
    	model.addAttribute("thanhPhos", layTinhThanhPho());
        try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception(language.getMessage("sua_that_bai"));
            }
            Optional<UserInfo> userInfo = userInfoRepository.findById(Long.valueOf(allParams.get("id")));
            if (!userInfo.isPresent()) {
                throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
            }

            Iterable<UserGroup> userGroups = userGroupRepository.findByStatus(Contains.TT_NHOM_HOATDONG);

            model.addAttribute("userGroups", userGroups);
            model.addAttribute("userInfo", userInfo.get());
            model.addAttribute("name", language.getMessage("sua"));
            forwartParams(allParams, model);
            return "nguoidung/addnguoidung";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/nguoi-dung?"+ getParamsQuery(allParams);
        }
    }
    
    @PostMapping(value = "/nguoi-dung/sua")
    public String postnguoiDungSua(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                   @RequestParam Map<String, String> allParams, @ModelAttribute("UserInfo") UserInfo userInfo) {
    	model.addAttribute("thanhPhos", layTinhThanhPho());
        try {
            checkErrorMessage(userInfo);

            UserInfo userInfoDb = userInfoRepository.findById(Long.valueOf(allParams.get("id"))).get();

            if (!StringUtils.isEmpty(userInfo.getPassword())) {
                userInfo.setPassword(CommonUtils.getMD5(userInfo.getPassword()));
            } else {
            	userInfo.setPassword(userInfoDb.getPassword());
            }

            updateObjectToObject(userInfoDb, userInfo);

            if(!StringUtils.isEmpty(userInfoDb.getKhuVuc()) && userInfoDb.getKhuVuc().indexOf("tatca") != -1) userInfoDb.setKhuVuc(String.join(",", layTinhThanhPho()));
            
            userInfoRepository.save(userInfoDb);

            redirectAttributes.addFlashAttribute("success", language.getMessage("sua_thanh_cong"));
        } catch (ErrorException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
            e.printStackTrace();
        }
        return "redirect:/nguoi-dung?"+ getParamsQuery(allParams);
    }

    @RequestMapping(value = "/nguoi-dung/xoa", method = {RequestMethod.GET})
    public String delete(Model model, @RequestParam Map<String, String> allParams,
                         RedirectAttributes redirectAttributes, HttpServletRequest req) {
        if (!StringUtils.isEmpty(allParams.get("id"))) {
            UserInfo checkNd = userInfoRepository
                    .findById(Long.valueOf(allParams.get("id"))).get();
            if (checkNd != null && !checkNd.getUsername().equals("supper_admin")) {
                userInfoRepository.delete(checkNd);
            }
            redirectAttributes.addFlashAttribute("success", language.getMessage("xoa_thanh_cong"));
        } else {
            redirectAttributes.addFlashAttribute("error", language.getMessage("xoa_that_bai"));
        }

        return "redirect:/nguoi-dung?" + getParamsQuery(allParams);
    }
    @GetMapping("/api/user-info")
    @ResponseBody
    public long nguoiDungValid(@RequestParam("name") String name) {
        return userInfoRepository.countByUsername(name);
    }
    
    public ArrayList<String> layTinhThanhPho() {
    	ArrayList<String> arr = new ArrayList<String>();
    	arr.add("Hà Nội");
    	arr.add("Hồ Chí Minh");
    	arr.add("An Giang");
    	arr.add("Bà Rịa - Vũng Tàu");
    	arr.add("Bình Dương");
    	arr.add("Bình Phước");
    	arr.add("Bình Thuận");
    	arr.add("Bình Định");
    	arr.add("Bạc Liêu");
    	arr.add("Bắc Giang");
    	arr.add("Bắc Kạn");
    	arr.add("Bắc Ninh");
    	arr.add("Bến Tre");
    	arr.add("Cao Bằng");
    	arr.add("Cà Mau");
    	arr.add("Cần Thơ");
    	arr.add("Gia Lai");
    	arr.add("Hải Phòng");
    	arr.add("Hoà Bình");
    	arr.add("Hà Giang");
    	arr.add("Hà Nam");
    	arr.add("Hà Tĩnh");
    	arr.add("Hưng Yên");
    	arr.add("Hải Dương");
    	arr.add("Hậu Giang");
    	arr.add("Khánh Hòa");
    	arr.add("Kiên Giang");
    	arr.add("Kon Tum");
    	arr.add("Lai Châu");
    	arr.add("Long An");
    	arr.add("Lào Cai");
    	arr.add("Lâm Đồng");
    	arr.add("Lạng Sơn");
    	arr.add("Nam Định");
    	arr.add("Nghệ An");
    	arr.add("Ninh Bình");
    	arr.add("Ninh Thuận");
    	arr.add("Phú Thọ");
    	arr.add("Phú Yên");
    	arr.add("Quảng Bình");
    	arr.add("Quảng Nam");
    	arr.add("Quảng Ngãi");
    	arr.add("Quảng Ninh");
    	arr.add("Quảng Trị");
    	arr.add("Sóc Trăng");
    	arr.add("Sơn La");
    	arr.add("Thanh Hóa");
    	arr.add("Thái Bình");
    	arr.add("Thái Nguyên");
    	arr.add("Thừa Thiên Huế");
    	arr.add("Tiền Giang");
    	arr.add("Trà Vinh");
    	arr.add("Tuyên Quang");
    	arr.add("Tây Ninh");
    	arr.add("Vĩnh Long");
    	arr.add("Vĩnh Phúc");
    	arr.add("Yên Bái");
    	arr.add("Đà Nẵng");
    	arr.add("Điện Biên");
    	arr.add("Đắk Lắk");
    	arr.add("Đắk Nông");
    	arr.add("Đồng Nai");
    	arr.add("Đồng Tháp");
    	
    	return arr;
    }
    
    @GetMapping("/nguoi-dung/report")
	public ResponseEntity<Resource> reportBaoCaoTongHop(@RequestParam Map<String, String> allParams, HttpServletRequest req) throws IllegalStateException, IOException, Exception {
    	List<UserInfoDto> userInfos = userInfoDtoRepository.selectParamsAll(
                getStringParams(allParams, "s_uname"),
                getStringParams(allParams, "s_fname"),
                getStringParams(allParams, "s_email"),
                getIntParams(allParams, "s_status"),
                getStringParams(allParams, "s_phone"));
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyyy_HH_mm");
		String filename = "report"+simpleDateFormat.format(new Date())+".xlsx";
		InputStreamResource file = new InputStreamResource(baoCaoNguoiDung(allParams, getUserName(req), userInfos));
 
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
					.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}
    
    public InputStream baoCaoNguoiDung(Map<String, String> allParams, String userName, List<UserInfoDto> userInfos) {
		
		ByteArrayInputStream in = baoCaoNguoiDungExel(userInfos, userName, allParams);
		return in;
	}

	private ByteArrayInputStream baoCaoNguoiDungExel(List<UserInfoDto> userInfos, String userName,
			Map<String, String> allParams) {
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Sheet sheet = workbook.createSheet("Report");

			// Header
			createHeaderBaoCaoNguoiDungExel(sheet, workbook, userName, allParams);
			Map<Long, String> map = listGroup();
			int rowIdx = 5;
			for (UserInfoDto ek : userInfos) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(rowIdx - 5);
				row.createCell(1).setCellValue(ek.getUsername());
				row.createCell(2).setCellValue(ek.getFullName());
				row.createCell(3).setCellValue(ek.getEmail());
				row.createCell(4).setCellValue(ek.getKhuVuc());
				row.createCell(5).setCellValue(ek.getStatus() != null && ek.getStatus() == 1?language.getMessage("hoat_dong"):language.getMessage("khong_hoat_dong"));
				
				String[] arr = ek.getGroupId().split(",");
	        	String group = "";
	        	for (String idGroup : arr) {
	        		if(map.containsKey(Long.valueOf(idGroup)))
	        			group += map.get(Long.valueOf(idGroup))+", ";
	        		else
	        			group += idGroup+", ";
				}
				row.createCell(6).setCellValue(group.replaceAll("[, ]+$", ""));
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}
	private void createHeaderBaoCaoNguoiDungExel(Sheet sheet, Workbook workbook, String userName,
			Map<String, String> allParams) {
		CellStyle cellStyle2 = getCeltype(workbook);
		Row headerRow = sheet.createRow(0);
		Cell cell = headerRow.createCell(0);
		cell.setCellValue("Báo cáo");
		headerRow = sheet.createRow(2);
		cell = headerRow.createCell(0);
		cell.setCellValue("Generated by: User "+userName);
		
		headerRow = sheet.createRow(4);
		String[] HEADERS = {language.getMessage("STT"), language.getMessage("ten_dang_nhap"), language.getMessage("ho_va_ten"), 
				language.getMessage("email"), "Khu vực", language.getMessage("trang_thai"), "Nhóm người dùng"};
		for (int col = 0; col < HEADERS.length; col++) {
			cell = headerRow.createCell(col);
			cell.setCellValue(HEADERS[col]);
			cell.setCellStyle(cellStyle2);
			sheet.setColumnWidth(col, 25 * 250);
		}
		
	}
	private CellStyle getCeltype(Workbook workbook) {
		CellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setWrapText(true);
		cellStyle2.setAlignment(HorizontalAlignment.CENTER);
		cellStyle2.setVerticalAlignment(VerticalAlignment.TOP);
		cellStyle2.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle2.setBorderBottom(BorderStyle.THIN);
		cellStyle2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle2.setBorderLeft(BorderStyle.THIN);
		cellStyle2.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle2.setBorderRight(BorderStyle.THIN);
		cellStyle2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle2.setBorderTop(BorderStyle.THIN);
		cellStyle2.setTopBorderColor(IndexedColors.BLACK.getIndex());
		
		return cellStyle2;
	}
	
	@GetMapping(value = "/lay-lai-mat-khau")
    public String layLaiMatKhau(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
        handlingGet(allParams, model, req);
        forwartParams(allParams, model);
        return "nguoidung/forgotpassword";
    }
    
    @PostMapping(value = "/lay-lai-mat-khau")
    public String layLaiMatKhauPost(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
        
    	try {
    		String tokenCapthcha = (String) req.getSession().getAttribute("tokenCaptcha");
    		if(tokenCapthcha == null) throw new ErrorException("Captcha không đúng");
        	if(!tokenCapthcha.equals(CommonUtils.getMD5(allParams.get("captcha")))) throw new ErrorException("Captcha không đúng");
        	
        	UserInfo userInfo = userInfoRepository.findByUsername(allParams.get("username"));
        	if(userInfo == null) throw new ErrorException("Tên đăng nhập không đúng");
        	if(userInfo.getEmail() == null) throw new ErrorException("Email không đúng");
        	if(!userInfo.getEmail().equals(allParams.get("email"))) throw new ErrorException("Email không đúng");
        	
        	String pw = Utils.randomPw();
        	userInfo.setPassword(CommonUtils.getMD5(pw));
        	userInfoRepository.save(userInfo);
        	
        	email.sendText(userInfo.getEmail(), "Thông tin đăng nhập tài khoản", "Thông tin đăng nhập tài khoản: <br/> Tên đăng nhập: "+userInfo.getUsername()+"<br/> Mật khẩu: "+pw);
        	
        	model.addAttribute("success", "Thông tin đăng nhập được gửi đến email của bạn");
            forwartParams(allParams, model);
		} catch (ErrorException e) {
			model.addAttribute("message", e.getMessage());
		}
    	
        return "nguoidung/forgotpassword";
    }
    
    @GetMapping(value = "/captcha",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> captcha(Model model, HttpServletRequest req) throws IOException {
    	CaptchaImage captchaImage = new CaptchaImage();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( captchaImage.getCaptchaImage(), "jpg", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		
		model.addAttribute("image", Base64.getEncoder().encodeToString(imageInByte));
		baos.close();
    	
		req.getSession().setAttribute("tokenCaptcha", CommonUtils.getMD5(captchaImage.getCaptchaString()));
		
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageInByte);
    }
	
}
