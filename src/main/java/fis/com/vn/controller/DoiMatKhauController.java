package fis.com.vn.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fis.com.vn.common.CommonUtils;
import fis.com.vn.repository.UserInfoRepository;
import fis.com.vn.table.UserInfo;

@Controller
public class DoiMatKhauController extends BaseController {
	@Autowired UserInfoRepository userInfoRepository;

	@GetMapping(value = "/change-pass")
	public String get(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
		forwartParams(allParams, model);
		return "nguoidung/doimatkhau";
	}

	@PostMapping(value = "/change-pass")
	public String get(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams,
			RedirectAttributes redirectAttributes) {
		try {
			String userName = (String) req.getSession().getAttribute("username");
			UserInfo userInfo = userInfoRepository.findByUsername(userName);
			if (userInfo == null)
				throw new Exception("Người dùng không tồn tại");
			if (!userInfo.getPassword().equals(CommonUtils.getMD5(getStringParams(allParams, "old-password"))))
				throw new Exception("Mật khẩu không đúng");
			userInfo.setPassword(CommonUtils.getMD5(getStringParams(allParams, "new-password")));

			userInfoRepository.save(userInfo);

			redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công");

			return "redirect:/change-pass";
		} catch (Exception e) {
			forwartParams(allParams, model);
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/change-pass";
		}
	}
}
