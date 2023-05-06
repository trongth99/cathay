package fis.com.vn.controller;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import fis.com.vn.common.Paginate;
import fis.com.vn.common.StringUtils;
import fis.com.vn.contains.Contains;
import fis.com.vn.exception.ErrorException;
import fis.com.vn.repository.UserGroupPermissionRepository;
import fis.com.vn.repository.UserGroupRepository;
import fis.com.vn.repository.UserModuleRepository;
import fis.com.vn.table.UserGroup;
import fis.com.vn.table.UserGroupPermission;
import fis.com.vn.table.UserModule;

@Controller
public class NhomNguoiDungController extends BaseController{
    @Autowired UserGroupRepository userGroupRepository;
    @Autowired UserModuleRepository userModuleRepository;
    @Autowired UserGroupPermissionRepository userGroupPermissionRepository;

    @GetMapping(value = "/nhom-nguoi-dung")
    public String nhomnguoiDung (Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

        handlingGet(allParams, model);

        forwartParams(allParams, model);
        return "nhomnguoidung/nhomnguoidung";
    }

    private void handlingGet(Map<String, String> allParams, Model model) {
        Paginate paginate = new Paginate(allParams.get("page"), allParams.get("limit"));

        Page<UserGroup> userGroups = userGroupRepository.selectParams(
                getStringParams(allParams, "s_gname"),
                getStringParams(allParams, "s_desc"),
                getIntParams(allParams, "s_status"),
                getPageable(allParams, paginate));

        model.addAttribute("currentPage", paginate.getPage());
        model.addAttribute("totalPage", userGroups.getTotalPages());
        model.addAttribute("totalElement", userGroups.getTotalElements());
        model.addAttribute("userGroups", userGroups.getContent());
    }

    @GetMapping(value = "/nhom-nguoi-dung/them-moi")
    public String getNhomnguoiDungThemMoi (Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams) {

        Iterable<UserModule> userModules = userModuleRepository.selectAll(Contains.TT_MODULE_HOATDONG);

        model.addAttribute("userModules", userModules);
        model.addAttribute("userGroup", new UserGroup());
        model.addAttribute("name", language.getMessage("them_moi"));
        forwartParams(allParams, model);
        return "nhomnguoidung/addnhomnguoidung";
    }

    @PostMapping(value = "/nhom-nguoi-dung/them-moi")
    public String postNhomnguoiDungThemMoi (Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                            @RequestParam Map<String, String> allParams, @ModelAttribute("userGroup") UserGroup userGroup,
                                            @RequestParam(value = "permissions", required = false) List<Long> permissions) {

        try {
            userGroup.setCreateTime(new Date());
            userGroup.setCreateBy(getUserName(req));
            checkErrorMessage(userGroup);

            userGroupRepository.save(userGroup);

            if(permissions != null) {
                ArrayList<UserGroupPermission> listUserGroupPermission = new ArrayList<>();
                for (Long moduleId : permissions) {
                    UserGroupPermission userGroupPermission = new UserGroupPermission();
                    userGroupPermission.setGroupId(userGroup.getId());
                    userGroupPermission.setModuleId(moduleId);
                    listUserGroupPermission.add(userGroupPermission);

                }
                userGroupPermissionRepository.saveAll(listUserGroupPermission);
            }
            redirectAttributes.addFlashAttribute("success", language.getMessage("them_thanh_cong"));

            
        }  catch (ErrorException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
        }
        return "redirect:/nhom-nguoi-dung";
    }

    @GetMapping(value = "/nhom-nguoi-dung/sua")
    public String getNhomnguoiDungSua (Model model, HttpServletRequest req, @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {

        try {
            if (StringUtils.isEmpty(allParams.get("id"))) {
                throw new Exception(language.getMessage("sua_that_bai"));
            }
            Optional<UserGroup> userGroup = userGroupRepository.findById(Long.valueOf(allParams.get("id")));
            if (!userGroup.isPresent()) {
                throw new Exception(language.getMessage("khong_ton_tai_ban_ghi"));
            }
            Iterable<UserModule> userModules = userModuleRepository.selectAll(Contains.TT_MODULE_HOATDONG);

            List<UserGroupPermission> userGroupPermissions = userGroupPermissionRepository.findByGroupId(userGroup.get().getId());

            model.addAttribute("userGroupPermissions", new Gson().toJson(userGroupPermissions));
            model.addAttribute("userModules", userModules);
            model.addAttribute("userGroup", userGroup.get());
            model.addAttribute("name", language.getMessage("sua"));
            forwartParams(allParams, model);
            return "nhomnguoidung/addnhomnguoidung";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/nhom-nguoi-dung";
        }
    }

    @PostMapping(value = "/nhom-nguoi-dung/sua")
    public String postNhomnguoiDungSua (Model model, HttpServletRequest req, RedirectAttributes redirectAttributes,
                                        @RequestParam Map<String, String> allParams, @ModelAttribute("userGroup") UserGroup userGroup,
                                        @RequestParam(value = "permissions", required = false) List<Long> permissions) {

        try {
            System.out.println(permissions);
            checkErrorMessage(userGroup);

            UserGroup userGroupDb = userGroupRepository.findById(Long.valueOf(allParams.get("id"))).get();

            updateObjectToObject(userGroupDb, userGroup);

            userGroupRepository.save(userGroupDb);

            if(permissions != null) {
                userGroupPermissionRepository.deleteByGroupId(userGroup.getId());
                ArrayList<UserGroupPermission> listUserGroupPermission = new ArrayList<>();
                for (Long moduleId : permissions) {
                    UserGroupPermission userGroupPermission = new UserGroupPermission();
                    userGroupPermission.setGroupId(userGroup.getId());
                    userGroupPermission.setModuleId(moduleId);
                    listUserGroupPermission.add(userGroupPermission);

                }
                userGroupPermissionRepository.saveAll(listUserGroupPermission);
            }

            redirectAttributes.addFlashAttribute("success", language.getMessage("sua_thanh_cong"));

            return "redirect:/nhom-nguoi-dung";
        } catch (ErrorException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", language.getMessage("loi_he_thong"));
        }
        return "redirect:/nhom-nguoi-dung";
    }

    @RequestMapping(value = "/nhom-nguoi-dung/xoa", method = { RequestMethod.GET })
    public String delete(Model model, @RequestParam Map<String, String> allParams,
                         RedirectAttributes redirectAttributes, HttpServletRequest req) {
        if (!StringUtils.isEmpty(allParams.get("id"))) {
            UserGroup checkNd = userGroupRepository
                    .findById(Long.valueOf(allParams.get("id"))).get();
            if (checkNd != null) {
                userGroupRepository.delete(checkNd);
            }
            redirectAttributes.addFlashAttribute("success", language.getMessage("xoa_thanh_cong"));
        } else {
            redirectAttributes.addFlashAttribute("error", language.getMessage("xoa_that_bai"));
        }

        return "redirect:/nhom-nguoi-dung?" + queryStringBuilder(allParams);
    }
    
    @GetMapping("/api/user-group")
    @ResponseBody
    public long nhomNguoiDungValid(@RequestParam("name") String name, @RequestParam("id") Long id) {
        return userGroupRepository.countUpdate(name, id);
    }
}
