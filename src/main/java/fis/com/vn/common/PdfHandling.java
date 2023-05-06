package fis.com.vn.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextMarginFinder;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import fis.com.vn.PdfBoundingObj;
import fis.com.vn.component.ConfigProperties;
import fis.com.vn.entities.ParamsKbank;
import fis.com.vn.entities.ThongTin;
import fis.com.vn.entities.ToaDoThongTinFileHopDong;

@Component
public class PdfHandling {
	@Value("${KY_SO_FOLDER}")
	String KY_SO_FOLDER;
	private static final Logger LOGGER = LoggerFactory.getLogger(PdfHandling.class);
	@Autowired ConfigProperties configProperties;
	
	public static final String FONT = "/FreeSans.ttf";
	
	public static void main(String[] args) throws IOException {
		PdfHandling handling = new PdfHandling();
		handling.themThongTin("C:\\Users\\chinhvd4\\Downloads"+"/340d75e2-9ac3-4b30-88a6-4426530ade31.pdf", 
				"C:\\Users\\chinhvd4\\Downloads"+"/PTF1.pdf", "VDC", "0005555555", "NH ngoại thương", "hà nội", "1000000", "D:\\image\\kyso/FreeSans.ttf");
		
//		try {
//			BaseFont base = BaseFont.createFont("D:\\image\\kyso/FreeSans.ttf", BaseFont.IDENTITY_H, false);
//			try {
//				OutputStream fos = new FileOutputStream(new File("C:\\Users\\chinhvd4\\Downloads\\89a8bec9-a873-4c0e-8462-0ab5eb0d7f55_edit.pdf"));
//
//				PdfReader pdfReader = new PdfReader("C:\\Users\\chinhvd4\\Downloads\\89a8bec9-a873-4c0e-8462-0ab5eb0d7f55.pdf");
//				PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
//				for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
//					if(i==1) {
//						PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
//						pdfContentByte.beginText();
//						pdfContentByte.setFontAndSize(base, 10);
//						pdfContentByte.setTextMatrix(139+15, 237); 
//						pdfContentByte.showText("42342342");
//						pdfContentByte.endText();
//					}
//				}
//				pdfStamper.close(); 
//			} catch (Exception e) {
//				e.printStackTrace();
//				LOGGER.error("themThongTin error: {}", e);
//			}
//		} catch (Exception e) {
//			LOGGER.error("themThongTin error 2: {}", e);
//		}
	}
	
	public PdfBoundingObj layToaDoText(int numberOfPages, String findText, PdfReaderContentParser parser) throws IOException {
		for (int i = 1; i <= numberOfPages; i++) {
			StringBuilder stringBuilder = new StringBuilder();
			Map<String, String> map = new HashMap<>();
			PdfBoundingObj pdfObj = new PdfBoundingObj();
			parser.processContent(i, new TextMarginFinder() {
	            @Override
	            public void renderText(TextRenderInfo renderInfo) {
	                super.renderText(renderInfo);
	                stringBuilder.append(renderInfo.getText());
	                int indexOf = stringBuilder.indexOf(findText);
	                int indexOfCheck = indexOf+findText.length();
	                if(indexOf!= -1 && indexOfCheck == stringBuilder.toString().length()) {
	                	map.put(findText, renderInfo.getBaseline().getBoundingRectange().x+"|"+renderInfo.getBaseline().getBoundingRectange().y);
	                	pdfObj.setX(renderInfo.getBaseline().getBoundingRectange().x);
	                	pdfObj.setY(renderInfo.getBaseline().getBoundingRectange().y);
	                }
	            }
	        });
			if(pdfObj.getX() != null) {
				pdfObj.setPage(i);
				return pdfObj;
			}
		}
		return null;
	}
	
	public void themThongTin(String inputFilePath, String outputFilePath, String chuTaiKhoan, String soTaiKhoan, String nganHang, String chiNhanh, String soTien) {
		themThongTin(inputFilePath, outputFilePath, chuTaiKhoan, soTaiKhoan, nganHang, chiNhanh, soTien, KY_SO_FOLDER+FONT);
	}
	
	public void themThongTin(String inputFilePath, String outputFilePath, String chuTaiKhoan, String soTaiKhoan, String nganHang, String chiNhanh, String soTien, String pathFont) {
		try {
			BaseFont base = BaseFont.createFont(pathFont, BaseFont.IDENTITY_H, false);
			if(StringUtils.isEmpty(soTien)) soTien = "";
			try {
				OutputStream fos = new FileOutputStream(new File(outputFilePath));
				ToaDoThongTinFileHopDong toaDoThongTinFileHopDong = new Gson().fromJson(configProperties.getConfig().getToa_do_them_thong_tin_file_hop_dong(), ToaDoThongTinFileHopDong.class);
				PdfReader pdfReader = new PdfReader(inputFilePath);
				PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
				for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
					for (ThongTin tt : toaDoThongTinFileHopDong.getThongTins()) {
						if(i==tt.getTrang()) {
							PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
							pdfContentByte.beginText();
							pdfContentByte.setFontAndSize(base, 10);
							if(!StringUtils.isEmpty(tt.getChuTaiKhoan())) {
								pdfContentByte.setTextMatrix(tt.getChuTaiKhoan().getX(), tt.getChuTaiKhoan().getY()); 
								pdfContentByte.showText(chuTaiKhoan);
							}
							if(!StringUtils.isEmpty(tt.getSoTaiKhoan())) {
								pdfContentByte.setTextMatrix(tt.getSoTaiKhoan().getX(), tt.getSoTaiKhoan().getY()); 
								pdfContentByte.showText(soTaiKhoan);
							}
							if(!StringUtils.isEmpty(tt.getNganHang())) {
								pdfContentByte.setTextMatrix(tt.getNganHang().getX(), tt.getNganHang().getY()); 
								pdfContentByte.showText(nganHang);
							}
							if(!StringUtils.isEmpty(tt.getChiNhanh())) {
								pdfContentByte.setTextMatrix(tt.getChiNhanh().getX(), tt.getChiNhanh().getY()); 
								pdfContentByte.showText(chiNhanh);
							}
							if(!StringUtils.isEmpty(tt.getSoTien())) {
								pdfContentByte.setTextMatrix(tt.getSoTien().getX(), tt.getSoTien().getY()); 
								pdfContentByte.showText(Utils.formatNumberDot(soTien));
							}
							pdfContentByte.endText();
						}
					}
					
//					if(i==1) {
//						PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
//						pdfContentByte.beginText();
//						pdfContentByte.setFontAndSize(base, 10);
//						pdfContentByte.setTextMatrix(220, 278); 
//						pdfContentByte.showText(chuTaiKhoan);
//						pdfContentByte.setTextMatrix(220, 265); 
//						pdfContentByte.showText(soTaiKhoan);
//						pdfContentByte.setTextMatrix(220, 250); 
//						pdfContentByte.showText(nganHang);
//						pdfContentByte.setTextMatrix(220, 236); 
//						pdfContentByte.showText(chiNhanh);
//						pdfContentByte.setTextMatrix(393, 223); 
//						pdfContentByte.showText(Utils.formatNumberDot(soTien));
//						pdfContentByte.endText();
//					}
//					if(i==4) {
//						PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
//						pdfContentByte.beginText();
//						pdfContentByte.setFontAndSize(base, 10);
//						pdfContentByte.setTextMatrix(146, 102); 
//						pdfContentByte.showText(chuTaiKhoan);
//						pdfContentByte.setTextMatrix(146, 87); 
//						pdfContentByte.showText(soTaiKhoan);
//						pdfContentByte.setTextMatrix(146, 73); 
//						pdfContentByte.showText(nganHang);
//						pdfContentByte.setTextMatrix(146, 59); 
//						pdfContentByte.showText(chiNhanh);
//						pdfContentByte.endText();
//					}
//					if(i==7) {
//						PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
//						pdfContentByte.beginText();
//						pdfContentByte.setFontAndSize(base, 10);
//						pdfContentByte.setTextMatrix(345, 187); 
//						pdfContentByte.showText(Utils.formatNumberDot(soTien));
//						pdfContentByte.endText();
//					}
				}
				pdfStamper.close(); 
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("themThongTin error: {}", e);
			}
		} catch (Exception e) {
			LOGGER.error("themThongTin error 2: {}", e);
		}
	}
	
	public String layThongTinNgayCap(String pdfBase64) {
		try {
			String str = getFullTextFromPdf(pdfBase64);
			String ngayCap = getStrPatterFirst(str, "Ngày cấp[ :]*([^:]+)Tại[ :]+");
			if(StringUtils.isEmpty(ngayCap))
				ngayCap = getStrPatterFirst(str, "Ngày cấp[ :]*([^:]+)Nơi cấp[ :]+");
			return ngayCap.trim();
		} catch (Exception e) {
		}
		return "";
	}
	public static String getStrPatterFirst(String str, String pattern) {
		try {
			Pattern r = Pattern.compile(pattern);

		    Matcher m = r.matcher(str);
		    while  (m.find()) {
		    	return  m.group(1).replaceAll("[\\s]+", " ").trim();
		    }
		} catch (Exception e) {
			LOGGER.error("ERROR: {}", e.getMessage());
		} 
	    return null;
	}
	private static void addText1(String text, int x, int y, PdfContentByte pdfContentByte) throws DocumentException, IOException {
		pdfContentByte.beginText();
		pdfContentByte.setFontAndSize(BaseFont.createFont("D:\\image\\kyso\\"+FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12);
		pdfContentByte.setTextMatrix(x, y); 
		pdfContentByte.showText(text);

		pdfContentByte.endText();
	}
	public void themDanhDauCoUyQuyen(String inputFilePath, String outputFilePath) {
		String[] arr = configProperties.getConfig().getToa_do_check_co_uy_quyen().split(",");
		themDanhDauUyQuyen(inputFilePath, outputFilePath, Integer.valueOf(arr[0].trim()), Integer.valueOf(arr[1].trim()));
	}
	public void themDanhDauKhongCoUyQuyen(String inputFilePath, String outputFilePath) {
		String[] arr = configProperties.getConfig().getToa_do_check_khong_co_uy_quyen().split(",");
		themDanhDauUyQuyen(inputFilePath, outputFilePath, Integer.valueOf(arr[0].trim()), Integer.valueOf(arr[1].trim()));
	}
	public void themDanhDauUyQuyen(String inputFilePath, String outputFilePath, int x, int y) {
		try {
			BaseFont base = BaseFont.createFont(KY_SO_FOLDER+"/wingding.ttf", BaseFont.IDENTITY_H, false);
			String checked="ü";
			
			try {
				OutputStream fos = new FileOutputStream(new File(outputFilePath));

				PdfReader pdfReader = new PdfReader(inputFilePath);
				PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
				for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
					if(i==Integer.valueOf(configProperties.getConfig().getTrang_danh_dau_uy_quyen())) {
						PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
						pdfContentByte.beginText();
						pdfContentByte.setFontAndSize(base, 20);
						pdfContentByte.setTextMatrix(x, y); 
						pdfContentByte.showText(checked);
						pdfContentByte.endText();
					}
				}
				pdfStamper.close(); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
	}
	public void themNgayThangNam(String inputFilePath, String outputFilePath) {
		try {
			OutputStream fos = new FileOutputStream(new File(outputFilePath));
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			PdfReader pdfReader = new PdfReader(inputFilePath);
			PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
			String dateString = dateFormat.format(new Date());
			String[] arrDate = dateString.split("/");
			String ngay = arrDate[0];
			String thang = arrDate[1];
			String nam = arrDate[2];
			String toaDo = configProperties.getConfig().getToa_do_ngay_thang_nam(); 
			String[] arrToaDo = toaDo.split("-");
			Map<Integer, String[]> mapToaDo = new HashMap<Integer, String[]>();
			for (String string : arrToaDo) {
				String[] arr = string.split("\\|"); 
				if(!mapToaDo.containsKey(Integer.valueOf(arr[0])))
					mapToaDo.put(Integer.valueOf(arr[0]), arr);
				else
					mapToaDo.put(Integer.valueOf(arr[0])+99, arr);
			}
			
			for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
				if(mapToaDo.containsKey(i)) {
					String[] arr = mapToaDo.get(i);
					PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
					addText(ngay, Integer.valueOf(arr[1].split(",")[0]), Integer.valueOf(arr[1].split(",")[1]), pdfContentByte);
					addText(thang, Integer.valueOf(arr[2].split(",")[0]), Integer.valueOf(arr[2].split(",")[1]), pdfContentByte);
					addText(nam, Integer.valueOf(arr[3].split(",")[0]), Integer.valueOf(arr[3].split(",")[1]), pdfContentByte);
				}
				if(mapToaDo.containsKey((i+99))) {
					String[] arr = mapToaDo.get((i+99));
					PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
					addText(ngay, Integer.valueOf(arr[1].split(",")[0]), Integer.valueOf(arr[1].split(",")[1]), pdfContentByte);
					addText(thang, Integer.valueOf(arr[2].split(",")[0]), Integer.valueOf(arr[2].split(",")[1]), pdfContentByte);
					addText(nam, Integer.valueOf(arr[3].split(",")[0]), Integer.valueOf(arr[3].split(",")[1]), pdfContentByte);
				}
			}

			pdfStamper.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void editContentPdf(String outputFilePath, ParamsKbank params) {
		try {
			String inputFilePath = KY_SO_FOLDER + getNameFileEsign();
			OutputStream fos = new FileOutputStream(new File(outputFilePath));

			PdfReader pdfReader = new PdfReader(inputFilePath);
			PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
			for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {

				if(i==1) {
					PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
					addText(params.getHoVaTen()!=null?params.getHoVaTen():"", 210, 713, pdfContentByte);
//					addText(params.getFormInfo()!=null && params.getFormInfo().getDiaChi() !=null?params.getFormInfo().getDiaChi():"", 210, 690, pdfContentByte);
					addText(params.getSoDienThoai()!=null?params.getSoDienThoai():"", 210, 690, pdfContentByte);
					addText(params.getSoCmt()!=null?params.getSoCmt():"", 210, 667, pdfContentByte);
					addText(params.getFormInfo()!=null&&params.getFormInfo().getNgayCap()!=null?params.getFormInfo().getNgayCap():"", 410, 667, pdfContentByte);
					addText(params.getFormInfo()!=null&&params.getFormInfo().getNoiCap()!=null?params.getFormInfo().getNoiCap():"", 210, 644, pdfContentByte);
				} else if(i==3) {
					if(StringUtils.isEmpty(params.getAnhChuKy())) continue;
					
					PdfContentByte pdfContentByte = pdfStamper.getOverContent(i);
					Image image = Image.getInstance(decodeToImage(params.getAnhChuKy()));
			        PdfImage stream = new PdfImage(image, "", null);
			        stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
			        PdfIndirectObject ref = pdfStamper.getWriter().addToBody(stream);
			        image.setDirectReference(ref.getIndirectReference());
			        image.setAbsolutePosition(0, 250);
			        pdfContentByte.addImage(image);
				}
			}

			pdfStamper.close(); 
		} catch (Exception e) {
			LOGGER.error("editContentPdf error: {}", e);
			e.printStackTrace();
		}
	}
	
	public String getBase64Esign() {
		String inputFilePathEsign = KY_SO_FOLDER + getNameFileEsign();
		return Utils.encodeFileToBase64Binary(new File(inputFilePathEsign));
	}
	
	public String getNameFileEsign() {
		return "/Template_for_Using_eSignCloud_v0.1_edit.pdf";
	}
	
	public String getFullTextFromPdf(String pdfBase64) throws IOException {
		byte[] pdfByte;
		Base64.Decoder decoder = Base64.getDecoder();
		pdfByte = decoder.decode(pdfBase64);
        ByteArrayInputStream bis = new ByteArrayInputStream(pdfByte);
        PdfReader reader = new PdfReader(bis);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        StringBuilder stringBuilder = new StringBuilder();
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            stringBuilder.append(strategy.getResultantText());
        }
        reader.close();
        return stringBuilder.toString();
    }
	public  byte[] decodeToImage(String imageString) {
		BufferedImage image = null;
	    byte[] imageByte;
	    try {
	        Base64.Decoder decoder = Base64.getDecoder();
	        imageByte = decoder.decode(imageString);
	        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	        image = ImageIO.read(bis);
	        
	        BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
	        
            result = ImageScaler.resizeImage(result, 700, 700);
            
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(result, "jpg", baos);
	        byte[] bytes = baos.toByteArray();
	        bis.close();
	        
	        return bytes;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return null;
	}
	private void addText(String text, int x, int y, PdfContentByte pdfContentByte) throws DocumentException, IOException {
		pdfContentByte.beginText();
		pdfContentByte.setFontAndSize(BaseFont.createFont(KY_SO_FOLDER+FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10);
		pdfContentByte.setTextMatrix(x, y); 
		pdfContentByte.showText(text);

		pdfContentByte.endText();
	}

	public String cutPdf(Integer startIndex, Integer endIndex, Integer numberOfPagesFileShouldHave,
			ParamsKbank paramsKbank) throws IOException {

		byte[] bytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(paramsKbank.getNoiDungFile().toString());

		PDDocument document = PDDocument.load(bytes);

		try {
			int divideIntoFiles = Math.abs(document.getNumberOfPages() / numberOfPagesFileShouldHave) + 1;
			String base64String = "";
			System.out.println("Divide Into FIles =" + divideIntoFiles);
			for (int i = 1; i < divideIntoFiles; i++) {
				Splitter splitter = new Splitter();
				splitter.setStartPage(startIndex);
				splitter.setEndPage(endIndex);
				splitter.setSplitAtPage(endIndex);
				List<PDDocument> splittedList = splitter.split(document);
				for (PDDocument doc : splittedList) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					doc.save(baos);
					base64String = java.util.Base64.getEncoder().encodeToString(baos.toByteArray());

					startIndex = endIndex + 1;
					endIndex = endIndex + numberOfPagesFileShouldHave;
				}
			}

			return base64String;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
