package fis.com.vn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThayDoiNgonNguController {
	@GetMapping(value = "/language") 
	public String nguoiDung(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		
		return "redirect:"+req.getContextPath()+req.getHeader("referer");
	}
}
