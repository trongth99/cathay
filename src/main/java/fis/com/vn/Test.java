package fis.com.vn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aspose.pdf.TextFragment;
import com.aspose.pdf.TextFragmentAbsorber;
import com.aspose.pdf.TextFragmentCollection;
import com.aspose.pdf.TextSegment;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextMarginFinder;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class Test extends PDFTextStripper{
	public Test() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\chinhvd4\\Downloads\\result.pdf"));
        document.open();
        BaseFont base = BaseFont.createFont("D:\\image\\kyso/FreeSans.ttf", BaseFont.IDENTITY_H, false);
        Font f=new Font(base);
        document.add(new Paragraph("Số giấy tờ: ", f));
        document.add(new Paragraph("Họ và tên: ", f));
        document.add(new Paragraph("Năm sinh: ", f));
        document.add(new Paragraph("Nơi cấp: ", f));
        document.add(new Paragraph("----------------------------------------------------------------------- ", f));
        document.add(new Paragraph("Chiều đi: ", f));
        document.add(new Paragraph("Đi từ: ", f));
        document.add(new Paragraph("Đến: ", f));
        document.add(new Paragraph("Ngày khởi hành: ", f));
        document.add(new Paragraph("Mã chuyến bay: ", f));
        document.add(new Paragraph("Mã đặt chỗ (số vé): ", f));
        document.add(new Paragraph("----------------------------------------------------------------------- ", f));
        document.add(new Paragraph("Chiều về: ", f));
        document.add(new Paragraph("Đi từ: ", f));
        document.add(new Paragraph("Đến: ", f));
        document.add(new Paragraph("Ngày về: ", f));
        document.add(new Paragraph("Mã chuyến bay: ", f));
        document.add(new Paragraph("Mã đặt chỗ (số vé): ", f));

        document.close();
//		PdfReader reader = new PdfReader("C:\\Users\\chinhvd4\\Downloads\\89a8bec9-a873-4c0e-8462-0ab5eb0d7f55.pdf");
//
//        int numberOfPages = reader.getNumberOfPages();
//       
//        String findText = "Số tài khoản[  \t]*:";
//        
//        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
//        
//        System.out.println(layToaDoText(numberOfPages, findText, parser));
//        
//        System.out.println(indexOfReg("Số tài khoản[ ]+:", "adsas Số tài khoản : Ngân hàng : Chi nhánh Chủ tài 1khoản : Số tài khoản : Ngân hàng : Chi nhánh"));
	}
	private static int indexOfReg(String rexp, String fulltext) {
		try {
			Pattern p = Pattern.compile(rexp); 
	        Matcher m = p.matcher(fulltext);
	        if (m.find()) {
	           int position = m.start();
	           return position;
	        }
		} catch (Exception e) {
		}
		return -1;
	}
	private static PdfBoundingObj layToaDoText(int numberOfPages, String findText, PdfReaderContentParser parser) throws IOException {
		for (int i = 1; i <= numberOfPages; i++) {
			StringBuilder stringBuilder = new StringBuilder();
			PdfBoundingObj pdfObj = new PdfBoundingObj();
			parser.processContent(i, new TextMarginFinder() {
	            @Override
	            public void renderText(TextRenderInfo renderInfo) {
	                super.renderText(renderInfo);
	                stringBuilder.append(renderInfo.getText());
	                int indexOf = indexOfReg(findText, stringBuilder.toString());
	                int indexOfCheck = indexOf+findText.length();
	                if(indexOf!= -1 && indexOfCheck == stringBuilder.toString().length()) {
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
	
	private static JSONObject getJSon(JSONObject jsonObject, JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject2 = jsonArray.getJSONObject(i);
			if(jsonObject2.getString("id").equals(jsonObject.getString("id"))) return jsonObject2;
		}
		return null;
	}
	@Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
            System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
                    text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
                    text.getWidthDirAdj() + "]");
        }
    }
}
