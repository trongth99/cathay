package fis.com.vn.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.UserModuleRepository;
import fis.com.vn.table.UserModule;

@Controller
public class ChucNangController extends BaseController {
    @Autowired UserModuleRepository userModuleRepository;

    @GetMapping(value = "/chuc-nang")
    public String nguoiDung(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
        handlingGet(allParams, model, req);
        forwartParams(allParams, model);
        return "chucnang/danhsach";
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

    @GetMapping(value = "/chuc-nang/them-moi")
    public String getguoiDungThemMoi(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) throws JsonProcessingException {
    	List<UserModule> listParents = userModuleRepository.findByParentId(0L);

        model.addAttribute("listParents", listParents);
        model.addAttribute("userModule", new UserModule());
        model.addAttribute("name", language.getMessage("them_moi"));
        forwartParams(allParams, model);
        return "chucnang/themmoi";
    }

    @PostMapping(value = "/chuc-nang/them-moi")
    public String postnguoiDungThemMoi(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                       @RequestParam Map<String, String> allParams, @ModelAttribute("userModule") UserModule userModule) {
        try {
            checkErrorMessage(userModule);

            userModuleRepository.save(userModule);

            redirectAttributes.addFlashAttribute("success", language.getMessage("them_thanh_cong"));
        } catch (ErrorException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
        }
        return "redirect:/chuc-nang?"+ getParamsQuery(allParams);
    }

    @GetMapping(value = "/chuc-nang/sua")
    public String getnguoiDungSua(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {

        try {
    		
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception(language.getMessage("sua_that_bai"));
            }
            Optional<UserModule> userModule = userModuleRepository.findById(Long.valueOf(allParams.get("id")));
            if (!userModule.isPresent()) {
                throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
            }
            List<UserModule> listParents = userModuleRepository.findByParentId(0L); 

            model.addAttribute("listParents", listParents);
            model.addAttribute("userModule", userModule.get());
            model.addAttribute("name", language.getMessage("sua"));
            forwartParams(allParams, model);
            return "chucnang/themmoi";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/chuc-nang?"+ getParamsQuery(allParams);
        }
    }

    @PostMapping(value = "/chuc-nang/sua")
    public String postnguoiDungSua(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                   @RequestParam Map<String, String> allParams, @ModelAttribute("userModule") UserModule userModule) {

        try {
            checkErrorMessage(userModule);

            UserModule userModuleDb = userModuleRepository.findById(Long.valueOf(allParams.get("id"))).get();

            updateObjectToObject(userModuleDb, userModule);

            userModuleRepository.save(userModuleDb);

            redirectAttributes.addFlashAttribute("success", language.getMessage("sua_thanh_cong"));
        } catch (ErrorException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
        }
        return "redirect:/chuc-nang?"+ getParamsQuery(allParams);
    }

    @RequestMapping(value = "/chuc-nang/xoa", method = {RequestMethod.GET})
    public String delete(Model model, @RequestParam Map<String, String> allParams,
                         RedirectAttributes redirectAttributes, HttpServletRequest req) {
        if (!StringUtils.isEmpty(allParams.get("id"))) {
        	UserModule checkNd = userModuleRepository
                    .findById(Long.valueOf(allParams.get("id"))).get();
            if (checkNd != null) {
            	userModuleRepository.delete(checkNd);
            }
            redirectAttributes.addFlashAttribute("success", language.getMessage("xoa_thanh_cong"));
        } else {
            redirectAttributes.addFlashAttribute("error", language.getMessage("xoa_that_bai"));
        }

        return "redirect:/chuc-nang?" + getParamsQuery(allParams);
    }
}
