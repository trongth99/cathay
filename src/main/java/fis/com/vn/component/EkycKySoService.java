package fis.com.vn.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.repository.EkycKysoRepository;
import fis.com.vn.table.EkycKyso;

@Component
public class EkycKySoService {
	@Autowired EkycKysoRepository ekycKysoRepository;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public List<EkycKyso> searchAll(Map<String, String> allParams, boolean searchDate, String tinhTrangCapMa, String khuVuc) throws ParseException {
		List<EkycKyso> ekycKysos = ekycKysoRepository.selectParams2All(
				fromDate(allParams.get("fromDate")),
				toDate(allParams.get("toDate")), 
						getStringParams(allParams, "trangThai"), 
						getStringParams(allParams, "soDienThoai"), 
						getStringParams(allParams, "hoVaTen"), 
						getStringParams(allParams, "soCmt"), 
						getStringParams(allParams, "soTaiKhoan"),
						khuVuc,
						fromDateNull(allParams.get("fromDateSign")),
						toDateNull(allParams.get("toDateSign")),
						getStringParams(allParams, "ghiChu"),
						getStringParams(allParams, "nguoiTao"),
						getStringParams(allParams, "moTKTTSB"),
						getStringParams(allParams, "spHanMuc"),
						getStringParams(allParams, "ttTKTT"),
						getStringParams(allParams, "yeuCauGiaiNgan"),
						fromDateNullYc(allParams.get("fromNgayYC")),
						toDateNullYc(allParams.get("toDateNgayYC")), 
						getStringParams(allParams, "trangThaiGuiChungTu")
					);
		
		allParams.put("fromDate", dateFormat.format(fromDate(allParams.get("fromDate"))));
		allParams.put("toDate", dateFormat.format(fromDate(allParams.get("toDate"))));
		
		return ekycKysos;
	}
	private Date toDate(String denNgay) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String toDate = dateFormat.format(new Date());
		if(!StringUtils.isEmpty(denNgay)) {
			toDate = denNgay;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateFormat.parse(toDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		toDate = dateFormat.format(calendar.getTime());
		
		return dateFormat.parse(toDate);
	}
	private Date fromDate(String tuNgay) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fromDate = dateFormat.format(new Date());
		if(!StringUtils.isEmpty(tuNgay)) {
			fromDate = tuNgay;
		}
		
		return dateFormat.parse(fromDate);
	}
	
	private Date toDateNull(String denNgay) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if(!StringUtils.isEmpty(denNgay)) {
			String toDate = denNgay;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateFormat.parse(toDate));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			toDate = dateFormat.format(calendar.getTime());
			return dateFormat.parse(toDate);
		}
		return null;
	}
	private Date fromDateNull(String tuNgay) throws ParseException {
		if(!StringUtils.isEmpty(tuNgay)) {
			String fromDate = tuNgay;
			return dateFormat.parse(fromDate);
		}
		
		return null;
	}
	
	private Date toDateNullYc(String denNgay) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if(!StringUtils.isEmpty(denNgay)) {
			String toDate = denNgay;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateFormat.parse(toDate));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			toDate = dateFormat.format(calendar.getTime());
			return dateFormat.parse(toDate);
		}
		return null;
	}
	private Date fromDateNullYc(String tuNgay) throws ParseException {
		if(!StringUtils.isEmpty(tuNgay)) {
			String fromDate = tuNgay;
			return dateFormat.parse(fromDate);
		}
		
		return null;
	}
	
	public Page<EkycKyso> search(Model model, Map<String, String> allParams, boolean searchDate, String tinhTrangCapMa, HttpServletRequest req, String khuVuc) throws ParseException {
		Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));
		Page<EkycKyso> ekycKysos ;
		
		ekycKysos = ekycKysoRepository.selectParams2(
				fromDate(allParams.get("fromDate")),
				toDate(allParams.get("toDate")), 
				getStringParams(allParams, "trangThai"), 
				getStringParams(allParams, "soDienThoai"), 
				getStringParams(allParams, "hoVaTen"), 
				getStringParams(allParams, "soCmt"), 
				getStringParams(allParams, "soTaiKhoan"),
				khuVuc,
				fromDateNull(allParams.get("fromDateSign")),
				toDateNull(allParams.get("toDateSign")), 
				getStringParams(allParams, "ghiChu"),
				getStringParams(allParams, "nguoiTao"),
				getStringParams(allParams, "moTKTTSB"),
				getStringParams(allParams, "spHanMuc"),
				getStringParams(allParams, "ttTKTT"),
				getStringParams(allParams, "yeuCauGiaiNgan"),
				fromDateNullYc(allParams.get("fromNgayYC")),
				toDateNullYc(allParams.get("toDateNgayYC")), 
				getStringParams(allParams, "trangThaiGuiChungTu"), 
				getPageable(allParams, paginate)
				);
		
		allParams.put("fromDate", dateFormat.format(fromDate(allParams.get("fromDate"))));
		allParams.put("toDate", dateFormat.format(fromDate(allParams.get("toDate"))));
		model.addAttribute("uri", req.getRequestURI());
		model.addAttribute("currentPage", paginate.getPage());
        model.addAttribute("totalPage", ekycKysos.getTotalPages());
        model.addAttribute("totalElement", ekycKysos.getTotalElements());
        model.addAttribute("ekycKysos", ekycKysos.getContent());
		return ekycKysos;
	}
	
	
	public String getStringParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return allParams.get(nameParam).trim();
	}
	
	public String getSort(Map<String, String> allParams) {
		if (StringUtils.isEmpty(allParams.get("sort"))) {
			return "desc";
		}
		return allParams.get("sort");
	}

	public Pageable getPageable(Map<String, String> allParams, Paginate paginate) {

		Pageable pageable;
		String order = "id";
		if (!StringUtils.isEmpty(allParams.get("order"))) {
			order = allParams.get("order");
		}
		Sort sort;
		if (getSort(allParams).equals("desc")) {
			sort = Sort.by(order).descending();
		} else {
			sort = Sort.by(order).ascending();
		}
		pageable = PageRequest.of(paginate.getPage() - 1, paginate.getLimit(), sort);
		return pageable;
	}
}
