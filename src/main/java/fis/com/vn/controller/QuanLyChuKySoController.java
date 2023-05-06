package fis.com.vn.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fis.com.vn.common.StringUtils;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.QuanLyChuKySoRepository;
import fis.com.vn.table.QuanLyChuKySo;

@Controller
public class QuanLyChuKySoController extends BaseController{
	@Autowired QuanLyChuKySoRepository quanLyChuKySoRepository;
	@GetMapping(value = "/quan-ly-chu-ky-so")
    public String quanLyChuKySo(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
        Iterable<QuanLyChuKySo> quanLyChuKySos = quanLyChuKySoRepository.findAll();
		
		model.addAttribute("quanLyChuKySos", quanLyChuKySos);
        forwartParams(allParams, model);
        return "demo/quanlychukyso/danhsach";
    }
	
	@GetMapping(value = "/quan-ly-chu-ky-so/sua")
    public String quanLyChuKySoSua(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
		try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception("Sửa thất bại");
            }
            Optional<QuanLyChuKySo> quanLyChuKySo = quanLyChuKySoRepository.findById(Long.valueOf(allParams.get("id")));
            if (!quanLyChuKySo.isPresent()) {
                throw new Exception("Không tồn tại bản ghi");
            }


            model.addAttribute("quanLyChuKySo", quanLyChuKySo.get());
            model.addAttribute("name", "Sửa");
            forwartParams(allParams, model);
            return "demo/quanlychukyso/ngaysudung";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/quan-ly-chu-ky-so";
        }
    }
	
	@GetMapping(value = "/quan-ly-chu-ky-so/mac-dinh")
    public String quanLyChuKySoThayMacDinh(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
		try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception("Sửa thất bại");
            }
            Optional<QuanLyChuKySo> quanLyChuKySo = quanLyChuKySoRepository.findById(Long.valueOf(allParams.get("id")));
            if (!quanLyChuKySo.isPresent()) {
                throw new Exception("Không tồn tại bản ghi");
            }
            Iterable<QuanLyChuKySo> quanLyChuKySos = quanLyChuKySoRepository.findAll();
            for (QuanLyChuKySo quanLyChuKySo2 : quanLyChuKySos) {
            	quanLyChuKySo2.setMacDinh("0");
            	quanLyChuKySoRepository.save(quanLyChuKySo2);
			}
            
            QuanLyChuKySo kySo = quanLyChuKySo.get();
            kySo.setMacDinh("1");
            quanLyChuKySoRepository.save(kySo);
            
            redirectAttributes.addFlashAttribute("success", "Thay đổi chữ ký mặc định thành công");
            return "redirect:/quan-ly-chu-ky-so";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/quan-ly-chu-ky-so";
        }
    }
	
	@PostMapping(value = "/quan-ly-chu-ky-so/sua")
    public String quanLyChuKySoSuaPost(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {
		try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception("Sửa thất bại");
            }
            Optional<QuanLyChuKySo> quanLyChuKySo = quanLyChuKySoRepository.findById(Long.valueOf(allParams.get("id")));
            if (!quanLyChuKySo.isPresent()) {
                throw new Exception("Không tồn tại bản ghi");
            }
            
            kiemTraKhoangPhanBoChuKy(allParams);
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            QuanLyChuKySo kySo = quanLyChuKySo.get();
            kySo.setNgayBatDau(simpleDateFormat.parse(allParams.get("ngayBatDau").toString()));
            kySo.setNgayKetThuc(simpleDateFormat.parse(allParams.get("ngayKetThuc").toString()));
            
            quanLyChuKySoRepository.save(kySo);
            
            redirectAttributes.addFlashAttribute("success", "Khởi tạo ngày sử dụng chữ ký thành công.");
            forwartParams(allParams, model);
            return "redirect:/quan-ly-chu-ky-so";
        } catch (ErrorException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/quan-ly-chu-ky-so";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống");
            return "redirect:/quan-ly-chu-ky-so";
        }
    }

	private void kiemTraKhoangPhanBoChuKy(Map<String, String> allParams) throws ParseException, ErrorException {
		Iterable<QuanLyChuKySo> quanLyChuKySos = quanLyChuKySoRepository.findAll();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		for (QuanLyChuKySo quanLyChuKySo : quanLyChuKySos) {
			if(quanLyChuKySo.getId() == Long.valueOf(allParams.get("id"))) continue;
			long ngayBatDauMs = simpleDateFormat.parse(allParams.get("ngayBatDau").toString()).getTime();
			long ngayKetThucMs = simpleDateFormat.parse(allParams.get("ngayKetThuc").toString()).getTime();
			if(!StringUtils.isEmpty(quanLyChuKySo.getNgayBatDau()) && !StringUtils.isEmpty(quanLyChuKySo.getNgayKetThuc())) {
				if(ngayBatDauMs >= quanLyChuKySo.getNgayBatDau().getTime() && ngayBatDauMs <= quanLyChuKySo.getNgayKetThuc().getTime()) throw new ErrorException("Khoảng thời gian sử dụng chữ ký số bị giao nhau");
				if(ngayKetThucMs >= quanLyChuKySo.getNgayBatDau().getTime() && ngayKetThucMs <= quanLyChuKySo.getNgayKetThuc().getTime()) throw new ErrorException("Khoảng thời gian sử dụng chữ ký số bị giao nhau");
				if(ngayBatDauMs <= quanLyChuKySo.getNgayBatDau().getTime() && ngayKetThucMs >= quanLyChuKySo.getNgayKetThuc().getTime()) throw new ErrorException("Khoảng thời gian sử dụng chữ ký số bị giao nhau");
			}
		}
	}
}
