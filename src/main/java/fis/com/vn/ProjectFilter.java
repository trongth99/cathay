package fis.com.vn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import fis.com.vn.api.LogService;
import fis.com.vn.common.Common;
import fis.com.vn.common.StringUtils;
import fis.com.vn.contains.Contains;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.table.UserModule;


@Configuration
public class ProjectFilter extends GenericFilterBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectFilter.class);
	
	@Value("${TOKEN}")
	protected String token;
	
	@Value("${CODE}")
	protected String code;
	
	@Autowired LogService logService;
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        resp.setCharacterEncoding("utf-8");
		req.setCharacterEncoding("utf-8");
        
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Headers", "*");
		resp.addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST");
		
		String requestId = Common.layMaGiaoDich(1);
		MDC.put("requestId", requestId);
		
		System.setProperty("logging.file.name", "/image/logs/first.log");
		
		if(req.getRequestURI().startsWith(req.getContextPath()+"/khach-hang/")){
			LOGGER.info("Url: " + req.getRequestURI()+"?"+req.getQueryString()+"|"+req.getMethod());
			try {
				ParamsKbank params = (ParamsKbank) req.getSession().getAttribute("params");
				LOGGER.info("SoCmt: " + params.getSoCmt());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
        if (allowUrl(req)) {
            chain.doFilter(request, response);
            return;
        }
        
        if(req.getRequestURI().startsWith(req.getContextPath()+"/api")){
        	String tokenHd = req.getHeader("token");
        	String codeHd = req.getHeader(Contains.MA_TO_CHUC_HEADER);
        	
        	if(StringUtils.isEmpty(tokenHd)) {
        		resp.sendRedirect(req.getContextPath()+"/login");
    			return;
        	}
        	
        	if(StringUtils.isEmpty(codeHd)) {
        		resp.sendRedirect(req.getContextPath()+"/login");
    			return;
        	}
        	
        	if(!tokenHd.equals(token)) {
        		resp.sendRedirect(req.getContextPath()+"/login");
    			return;
        	}
        	
        	if(!codeHd.equals(code)) {
        		resp.sendRedirect(req.getContextPath()+"/login");
    			return;
        	}
        	
			logService.logRequest(req, requestId);
			
			chain.doFilter(request, response);
			return;
		} 
        
		if(req.getSession().getAttribute("username") == null || req.getSession().getAttribute("userModuleMenus") == null) {
			resp.sendRedirect(req.getContextPath()+"/login");
			return;
		}

		if(req.getRequestURI().startsWith(req.getContextPath()+"/api")){
			
		}else{
			if(checkPermission(req)) {
				String query = req.getQueryString()!=null?"?"+req.getQueryString():"";
				Cookie cookie = new Cookie("urlReferer", req.getRequestURI()+query);
			    cookie.setMaxAge(30*24*60*60);
			    resp.addCookie(cookie);
			    
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect(req.getContextPath()+"/");
				resp.getWriter().print("You do not have permission.");
			}
		}

//		chain.doFilter(request, response);
	}
	public boolean checkPermission(HttpServletRequest req) {
        @SuppressWarnings("unchecked")
        List<UserModule> danhMucQuyens = (List<UserModule>) req.getSession().getAttribute("userModuleMenus");
        
        String uri = req.getRequestURI();
        if (uri.equals(req.getContextPath()+"/")) return true;
        if (uri.equals(req.getContextPath()+"/language")) return true;
        if (uri.equals(req.getContextPath()+"/chia-se-thu-muc")) return true;
        boolean checkPermission = false;
        for (UserModule tbDanhMucQuyen : danhMucQuyens) {
            if (tbDanhMucQuyen.getUrl() != null && uri.startsWith(req.getContextPath()+tbDanhMucQuyen.getUrl()) && tbDanhMucQuyen.getParentId() != 0) {
                checkPermission = true;
            }
        }
        return checkPermission;
	}
	public Boolean allowUrl(HttpServletRequest req) {
		ArrayList<String> listAllow = new ArrayList<>();
		listAllow.add(req.getContextPath()+"/font/*");
		listAllow.add(req.getContextPath()+"/fonts/*");
		listAllow.add(req.getContextPath()+"/webfonts/*");
		listAllow.add(req.getContextPath()+"/select2-develop/*");
		listAllow.add(req.getContextPath()+"/build/*");
		listAllow.add(req.getContextPath()+"/web/*");
		listAllow.add(req.getContextPath()+"/viewpdf");
		listAllow.add(req.getContextPath()+"/viewpdf/byte/*");
		listAllow.add(req.getContextPath()+"/download/byte/*");
		listAllow.add(req.getContextPath()+"/viewpdf/byte");
		listAllow.add(req.getContextPath()+"/css/*");
		listAllow.add(req.getContextPath()+"/dist/*");
		listAllow.add(req.getContextPath()+"/js/*");
		listAllow.add(req.getContextPath()+"/image/*");
		listAllow.add(req.getContextPath()+"/images/*");
		listAllow.add(req.getContextPath()+"/img/*");
		listAllow.add(req.getContextPath()+"/favicon.ico");
		listAllow.add(req.getContextPath()+"/login");
		listAllow.add(req.getContextPath()+"/reg");
		listAllow.add(req.getContextPath()+"/logout");
		listAllow.add(req.getContextPath()+"/static/file/*");
		listAllow.add(req.getContextPath()+"/register");
		listAllow.add(req.getContextPath()+"/plugins/*");
		listAllow.add(req.getContextPath()+"/plugins1/*");
		listAllow.add(req.getContextPath()+"/change-pass");
		listAllow.add(req.getContextPath()+"/ekyc");
		listAllow.add(req.getContextPath()+"/khach-hang/*");
		listAllow.add(req.getContextPath()+"/kbiz-loan/*");
		listAllow.add(req.getContextPath()+"/ekyc/*");
		listAllow.add(req.getContextPath()+"/count-request");
		listAllow.add(req.getContextPath()+"/danh-sach-log/img-byte");
		listAllow.add(req.getContextPath()+"/danh-sach-hop-dong");
		listAllow.add(req.getContextPath()+"/lay-lai-mat-khau");
		listAllow.add(req.getContextPath()+"/captcha");
		listAllow.add(req.getContextPath()+"/image/kyso/*");
		

		return checkAllow(listAllow, req);
	}
	private Boolean checkAllow(ArrayList<String> listAllow, HttpServletRequest req) {
		for (String string : listAllow) {
			if (req.getRequestURI().matches(string.replace("*", "[\\w\\W]*"))) {
				return true;
			}
		}
		return false;
	}
}
