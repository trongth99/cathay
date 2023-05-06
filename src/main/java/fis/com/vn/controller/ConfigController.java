/**
 * 
 */
package fis.com.vn.controller;

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
import fis.com.vn.repository.ConfigRepository;
import fis.com.vn.table.Config;

/**
 * @author ChinhVD4
 *
 */
@Controller
public class ConfigController extends BaseController{
	@Autowired ConfigRepository configRepository;
	
	@GetMapping(value = "/config")
    public String config(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {
        handlingGet(allParams, model, req);
        forwartParams(allParams, model);
        return "config/danhsach";
    }

    private void handlingGet(Map<String, String> allParams, Model model, HttpServletRequest req) {
        Paginate paginate = new Paginate(allParams.get("page"), "200");

        Page<Config> configs = configRepository.selectParams(
                getStringParams(allParams, "ma"),
                getPageable(allParams, paginate));

        model.addAttribute("currentPage", paginate.getPage());
        model.addAttribute("totalPage", configs.getTotalPages());
        model.addAttribute("totalElement", configs.getTotalElements());
        model.addAttribute("configs", configs.getContent());
    }

    @GetMapping(value = "/config/them-moi")
    public String getConfigThemMoi(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) throws JsonProcessingException {

        model.addAttribute("config", new Config());
        model.addAttribute("name", "Thêm mới");
        forwartParams(allParams, model);
        return "config/add";
    }

    @PostMapping(value = "/config/them-moi")
    public String postConfigThemMoi(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                       @RequestParam Map<String, String> allParams, @ModelAttribute("config") Config config) {
        try {
            checkErrorMessage(config);

            Config checkConfig = configRepository.findByMa(config.getMa());
            if (checkConfig != null) throw new Exception("Mã đã tồn tại");

            configRepository.save(config);

            redirectAttributes.addFlashAttribute("success", "Thêm thành công");
        } catch (ErrorException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
            e.printStackTrace();
        }
        return "redirect:/config";
    }

    @GetMapping(value = "/config/sua")
    public String getconfigSua(Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {

        try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception("Sửa thất bại");
            }
            Optional<Config> config = configRepository.findById(Long.valueOf(allParams.get("id")));
            if (!config.isPresent()) {
                throw new Exception("Không tồn tại bản ghi");
            }


            model.addAttribute("config", config.get());
            model.addAttribute("name", "Sửa");
            forwartParams(allParams, model);
            return "config/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/config";
        }
    }

    @PostMapping(value = "/config/sua")
    public String postconfigSua(Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                   @RequestParam Map<String, String> allParams, @ModelAttribute("config") Config config) {

        try {
            checkErrorMessage(config);

            Config configDb = configRepository.findById(Long.valueOf(allParams.get("id"))).get();

            updateObjectToObject(configDb, config);

            configRepository.save(configDb);

            redirectAttributes.addFlashAttribute("success", "Sửa thành công");
        } catch (ErrorException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
            e.printStackTrace();
        }
        return "redirect:/config";
    }

    @RequestMapping(value = "/config/xoa", method = {RequestMethod.GET})
    public String delete(Model model, @RequestParam Map<String, String> allParams,
                         RedirectAttributes redirectAttributes, HttpServletRequest req) {
        if (!StringUtils.isEmpty(allParams.get("id"))) {
        	Config checkNd = configRepository
                    .findById(Long.valueOf(allParams.get("id"))).get();
            if (checkNd != null) {
            	configRepository.delete(checkNd);
            }
            redirectAttributes.addFlashAttribute("success", language.getMessage("xoa_thanh_cong"));
        } else {
            redirectAttributes.addFlashAttribute("error", language.getMessage("xoa_that_bai"));
        }

        return "redirect:/config?" + queryStringBuilder(allParams);
    }
}
