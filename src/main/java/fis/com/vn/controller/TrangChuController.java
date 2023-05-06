package fis.com.vn.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fis.com.vn.repository.EkycKysoRepository;

@Controller
public class TrangChuController extends BaseController {
//	@GetMapping(value = "/") 
//	public String get(Model model) {
//		return "index";
//	}

	@Autowired
	EkycKysoRepository ekycKysoRepository;

	@GetMapping(value = "/")
	public String get(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

		ArrayList<String> kyso = new ArrayList<String>();
		ArrayList<String> kysoDaGui = new ArrayList<String>();

		ArrayList<String> kysoDay = new ArrayList<String>();
		ArrayList<String> kysoDaGuiDay = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			int ks = ekycKysoRepository.countKySo(String.valueOf(i), "2");
			int ksdg = ekycKysoRepository.countDaGui(String.valueOf(i), "2");
			kyso.add(String.valueOf(ks));
			kysoDaGui.add(String.valueOf(ksdg));
		}

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM");

		for (int i = 1; i <= 31; i++) {
			int ks = ekycKysoRepository.countKySoDay(String.valueOf(i), dateFormat1.format(new Date()));
			int ksdg = ekycKysoRepository.countDaGuiDay(String.valueOf(i), dateFormat1.format(new Date()));
			kysoDay.add(String.valueOf(ks));
			kysoDaGuiDay.add(String.valueOf(ksdg));
		}
		System.err.println("kysoDay:" + kysoDay);
		System.err.println("kysoDaGuiDay:" + kysoDaGuiDay);
		model.addAttribute("kyso", kyso);
		model.addAttribute("kysoDaGui", kysoDaGui);
		model.addAttribute("kysoDay", kysoDay);
		model.addAttribute("kysoDaGuiDay", kysoDaGuiDay);

		model.addAttribute("sumDaKy", ekycKysoRepository.sumDaKy());
		model.addAttribute("sumDaGui", ekycKysoRepository.sumDaGui());

		return "dashboard/dashboard";
	}
}
