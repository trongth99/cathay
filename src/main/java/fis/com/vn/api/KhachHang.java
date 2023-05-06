package fis.com.vn.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fis.com.vn.common.StringUtils;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.repository.QuanLyKhachHangRepository;
import fis.com.vn.table.QuanLyKhachHang;

@Component
public class KhachHang {
	@Autowired QuanLyKhachHangRepository quanLyKhachHangRepository;
	@Autowired ConfigProperties configProperties;
	
	private Map<String, Integer> listTokenKhachHang = new HashMap<String, Integer>();
	private Map<String, QuanLyKhachHang> listQuanLyKhachHang = new HashMap<String, QuanLyKhachHang>();
	
	public Map<String, Integer> getListTokenKhachHang() {
		if(listTokenKhachHang.size() == 0) {
			getKhachHangFromDb();
		}
		
		return listTokenKhachHang;
	}
	public Map<String, QuanLyKhachHang> getListQuanLyKhachHang() {
		if(listQuanLyKhachHang.size() == 0) {
			getKhachHangFromDb();
		}
		
		return listQuanLyKhachHang;
	}
	
	public Boolean luuGiaoDichVoiFace(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getLuuGiaoDich()) && getQuanLyKhachHang(maToChuc).getLuuGiaoDich().equals("1")?true:false;
		return kiemTraOcr;
	}
	
	public Boolean luuGiaoDichVoiOcr(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getLuuGiaoDich()) && getQuanLyKhachHang(maToChuc).getLuuGiaoDich().equals("0")?true:false;
		return kiemTraOcr;
	}
	public Boolean luuGiaoDichVoiOcrVaFace(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getLuuGiaoDich()) && getQuanLyKhachHang(maToChuc).getLuuGiaoDich().equals("2")?true:false;
		return kiemTraOcr;
	}
	
	public Boolean loaiVideoCallLuuLog(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getVideoCall()) && getQuanLyKhachHang(maToChuc).getVideoCall().equals("1")?true:false;
		return kiemTraOcr;
	}
	
	public Boolean getKiemTraAnh1NhinThang(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktanh1nhinthang") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraMoMom(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktmomom") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraNhamMat(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktnhammat") != -1?true:false;
		return kiemTraFace;
	}
	public Boolean getKiemTraDoiMu(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktdoimu") != -1?true:false;
		return kiemTraFace;
	}
	public Boolean getKiemTraNhinThang(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktnhinthang") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraDeoKinh(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktdeokinh") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraDeoKinhTrang(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktdeokinhtrang") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraDeoKhauTrang(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktdeokhautrang") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraChuyenDong(String maToChuc) {
		Boolean kiemTraFace = !StringUtils.isEmpty(configProperties.getConfig().getKiem_tra_liveness()) && configProperties.getConfig().getKiem_tra_liveness().indexOf("ktchuyendong") != -1?true:false;
		return kiemTraFace;
	}
	
	public Boolean getKiemTraChupTuManHinh(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("mh") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getCoLuuLogAnh(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getLuuLogAnh()) && getQuanLyKhachHang(maToChuc).getLuuLogAnh().equals("0")?false:true;
		return kiemTraOcr;
	}
	public Boolean getKiemTraVanTay(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("ktvt") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraMaTinhMatTruocSauCmt9So(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("ktmt") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraDuThongTinOcr(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("ktdtt") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraAnhBiLoa(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("abls") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraSuaDoi(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("sd") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraAnh(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("kta") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraNgayhetHan(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("nhh") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraDauQuocHuy(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("qh") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraThayTheAnh(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("tta") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraDauNoi(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("dn") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraPhoto(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("pt") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getCoKiemTraHanhDong(String maToChuc) {
		Boolean kiemTraHanhDong = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getHanhDong()) && getQuanLyKhachHang(maToChuc).getHanhDong().equals("1")?true:false;
		return kiemTraHanhDong;
	}
	public Boolean getKiemTraCatGoc(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("cg") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraDauVanTay(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("vt") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraDauXacNhanMs(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("xn") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraQuyLuatSoCccd(String maToChuc) {
		Boolean kiemTraOcr = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraGiayTo()) && getQuanLyKhachHang(maToChuc).getKiemTraGiayTo().indexOf("ql") != -1?true:false;
		return kiemTraOcr;
	}
	public Boolean getKiemTraKhuonMat(String maToChuc) {
		Boolean kiemTraKhuonMat = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getKiemTraKhuonMat()) && getQuanLyKhachHang(maToChuc).getKiemTraKhuonMat().equals("1")?true:false;
		return kiemTraKhuonMat;
	}
	public Boolean suDungOcrFis(String maToChuc) {
		Boolean suDungFptAi = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getSuDungOcr()) && getQuanLyKhachHang(maToChuc).getSuDungOcr().equals("0")?true:false;
		return suDungFptAi;
	}
	public Double getNguongSoSanh(String maToChuc) {
		try {
			Double nguong = StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getNguongSoSanhImg())?Double.valueOf(configProperties.getConfig().getNguong_chinh_xac()):Double.valueOf(getQuanLyKhachHang(maToChuc).getNguongSoSanhImg());
			return nguong;
		} catch (Exception e) {
		}
		return null;
	}
	public Double getTiLeGiaMaoLiveness(String maToChuc) {
		try {
			Double nguong = StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getTiLeGiaMaoLiveness())?null:Double.valueOf(getQuanLyKhachHang(maToChuc).getTiLeGiaMaoLiveness());
			return nguong;
		} catch (Exception e) {
		}
		return null;
	}
	public Double getNguongNhanDienKhuonMat(String maToChuc) {
		try {
			Double nguong = StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getNguongNhanDienKhuonMat())?null:Double.valueOf(getQuanLyKhachHang(maToChuc).getNguongNhanDienKhuonMat());
			return nguong;
		} catch (Exception e) {
		}
		return null;
	}
	public String getLoaiGoiKiemTra(String maToChuc) {
		String loaiGoi = !StringUtils.isEmpty(getQuanLyKhachHang(maToChuc).getLoaiGoi())?getQuanLyKhachHang(maToChuc).getLoaiGoi():null;
		return loaiGoi;
	}
	public QuanLyKhachHang getQuanLyKhachHang(String maToChuc) {
		QuanLyKhachHang quanLyKhachHang = getListQuanLyKhachHang().get(maToChuc);
		if(quanLyKhachHang == null) return new QuanLyKhachHang();
		return quanLyKhachHang;
	}
	public void resetListTokenKhachHang() {
		getKhachHangFromDb();
	}
	
	@PostConstruct
	public synchronized void getKhachHangFromDb() {
		List<QuanLyKhachHang> khachHangs = quanLyKhachHangRepository.findByStatus(1);
		listTokenKhachHang =  khachHangs.stream().collect(Collectors.toMap(o->o.getToken()+"_"+o.getCode(), o->o.getNumberRequest()==null?-1:o.getNumberRequest()));
		listQuanLyKhachHang = khachHangs.stream().collect(Collectors.toMap(o->o.getCode(), o->o));
	}
}
