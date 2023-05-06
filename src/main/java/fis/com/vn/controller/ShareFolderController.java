package fis.com.vn.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.repository.UserModuleRepository;
import fis.com.vn.table.UserModule;

@Controller
public class ShareFolderController extends BaseController {
	 @Autowired UserModuleRepository userModuleRepository;
//	@GetMapping(value = "/chia-se-thu-muc")
//    public String  shareFolder(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams , ServletContext servletContext) throws JsonProcessingException, FileNotFoundException {
//   System.err.println("hjabsdhabshj");
//   handlingGet(allParams, model, req);
//   forwartParams(allParams, model);
//        return "sharefolder/sharefolder";
//    }
	
	public void forwartParams(Map<String, String> allParams, Model model) {
		for (Entry<String, String> entry : allParams.entrySet()) {
			model.addAttribute(entry.getKey(), entry.getValue());
		}
	}
	 private void handlingGet(Map<String, String> allParams, Model model, HttpServletRequest req) {
	        Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));

	        Page<UserModule> userModules = userModuleRepository.selectParams(
	                getStringParams(allParams, "url"),
	                getPageable(allParams, paginate));
	        
	        model.addAttribute("currentPage", paginate.getPage());
	        model.addAttribute("totalPage", userModules.getTotalPages());
	        model.addAttribute("totalElement", userModules.getTotalElements());
	        model.addAttribute("userModules", userModules.getContent());
	    }
}
