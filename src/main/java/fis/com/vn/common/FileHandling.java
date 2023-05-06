package fis.com.vn.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class FileHandling {
	public static void main(String[] args) {
		File file = new File("/data/abc/xyz/create.txt");
		file.getParentFile().mkdirs();
	}
	
	public String saveResize(String enCodeFile, String folDer) {
		try {
			BufferedImage image = ImageScaler.decodeToImage(enCodeFile);
			BufferedImage bufferedImage = ImageScaler.resizeImage(image);
			String name = UUID.randomUUID().toString()+"_"+image.getWidth()+"_"+image.getHeight();
			
			return saveName(ImageScaler.getBase64Img(bufferedImage), folDer, name);
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}
		return "";
	}
	
	@SuppressWarnings("resource")
	public String saveName(String enCodeFile, String folDer, String nameFile) {
		String nameFileOut = nameFile + "." + getTypeFile();
		
		try {
			File file = new File(getFolder(folDer)+"create.txt");
			file.getParentFile().mkdirs();
			new FileWriter(file);
			
			byte[] decodedImg = Base64.getDecoder()
                    .decode(enCodeFile.getBytes(StandardCharsets.UTF_8));
			
			Path destinationFile = Paths.get(getFolder(folDer), nameFileOut);
			Files.write(destinationFile, decodedImg);
			
			return getFolder(folDer)+nameFileOut;
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}

		return "";
	}
	@SuppressWarnings("resource")
	public void createFolder(String folDer) {
		try {
			File file = new File(getFolder(folDer)+"create.txt");
			file.getParentFile().mkdirs();
			new FileWriter(file);
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}
	}
	@SuppressWarnings("resource")
	public String save(String enCodeFile, String folDer) {
		String nameFileOut = UUID.randomUUID().toString() + "." + getTypeFile();
		
		try {
			File file = new File(getFolder(folDer)+"create.txt");
			file.getParentFile().mkdirs();
			new FileWriter(file);
			
			byte[] decodedImg = Base64.getDecoder()
                    .decode(enCodeFile.getBytes(StandardCharsets.UTF_8));
			
			Path destinationFile = Paths.get(getFolder(folDer), nameFileOut);
			Files.write(destinationFile, decodedImg);
			
			return getFolder(folDer)+nameFileOut;
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}

		return "";
	}
	
	@SuppressWarnings("resource")
	public String saveFileFolder(String enCodeFile, String folDer, String nameFile) {
		String nameFileOut = UUID.randomUUID().toString() + "." + getTypeFileFromName(nameFile); 
		
		try {
			File file = new File(folDer+"create.txt");
			file.getParentFile().mkdirs();
			new FileWriter(file);
			
			byte[] decodedImg = Base64.getDecoder()
                    .decode(enCodeFile.getBytes(StandardCharsets.UTF_8));
			
			Path destinationFile = Paths.get(folDer, nameFileOut);
			Files.write(destinationFile, decodedImg);
			
			return folDer+nameFileOut;
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}

		return "";
	}
	
	@SuppressWarnings("resource")
	public String save(String enCodeFile, String folDer, String nameFile) {
		String nameFileOut = UUID.randomUUID().toString() + "." + getTypeFileFromName(nameFile);
		
		try {
			File file = new File(getFolder(folDer)+"create.txt");
			file.getParentFile().mkdirs();
			new FileWriter(file);
			
			byte[] decodedImg = Base64.getDecoder()
                    .decode(enCodeFile.getBytes(StandardCharsets.UTF_8));
			
			Path destinationFile = Paths.get(getFolder(folDer), nameFileOut);
			Files.write(destinationFile, decodedImg);
			
			return getFolder(folDer)+nameFileOut;
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}

		return "";
	}
	@SuppressWarnings("resource")
	public String plitPDF(String input , String output) throws IOException {
		File initialFile = new File(input);
		InputStream targetStream = new FileInputStream(initialFile);

		 int numberOfPagesFileShouldHave = 20;
		    PDDocument document = PDDocument.load(targetStream);            
		    if (document.getNumberOfPages() > 20) {
		        try {
		            int divideIntoFiles = Math.abs(document.getNumberOfPages() / numberOfPagesFileShouldHave) + 1;
		            System.out.println("Divide Into FIles =" + divideIntoFiles);
		            int startIndex = 1;
		            int endIndex= numberOfPagesFileShouldHave; 
		                for (int i = 1; i < divideIntoFiles; i++) {
		                    Splitter splitter =  new Splitter();
		                splitter.setStartPage(startIndex);
		                splitter.setEndPage(endIndex);
		                splitter.setSplitAtPage(endIndex);
		                List <PDDocument> splittedList = splitter.split(document);
		                for (PDDocument doc : splittedList) {
		                    doc.save(output);
		                    doc.close();
		                    startIndex  = endIndex + 1;
		                    endIndex = endIndex + numberOfPagesFileShouldHave;
		                }
		                }
		        }catch (Exception e) {
		                    e.printStackTrace();
		                }
		                
		            }
		    return null;
	}
	@SuppressWarnings("resource")
	public String saveFile(String bae64File, String folDer, String nameFile) {
		String nameFileOut = UUID.randomUUID().toString() + "." + getTypeFileFromName(nameFile);
		
		try {
			File file = new File(getFolder(folDer)+"create.txt");
			file.getParentFile().mkdirs();
			new FileWriter(file);
			
			byte[] decodedImg = bae64File.getBytes(StandardCharsets.UTF_8);
			
			Path destinationFile = Paths.get(getFolder(folDer), nameFileOut);
			Files.write(destinationFile, decodedImg);
			
			return getFolder(folDer)+nameFileOut;
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
		}

		return "";
	}
	/**
	 * @param nameFile
	 * @return
	 */
	public String getTypeFileFromName(String nameFile) {
		String extension = "";

		try {
			int i = nameFile.lastIndexOf('.');
			if (i >= 0) {
			    extension = nameFile.substring(i+1);
			}
		} catch (Exception e) {
		}
		return extension;
	}
	public String getFolder(String pFolder) {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		return pFolder+calendar.get(Calendar.DAY_OF_MONTH)+"_"+month+"_"+calendar.get(Calendar.YEAR)+"/";
	}
	public String getTypeFile() {
		return "jpg";
	}
}
