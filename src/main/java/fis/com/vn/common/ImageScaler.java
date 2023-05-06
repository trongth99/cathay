/**
 * 
 */
package fis.com.vn.common;

/**
 * @author ChinhVD4
 *
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ImageScaler {
	static int maxWidth = 250;
	static int maxHeight = 250;
	
	public static void main(String[] args) throws IOException {
		BufferedImage image = resizeImage(ImageIO.read(new File("C:\\Users\\chinhvd4\\Pictures\\img-byte (54).jpg")));
		
		
		ImageIO.write(image, "jpg", new File("C:\\Users\\chinhvd4\\Pictures\\123.jpg"));
	}
	
	public static String resizeImageBase64(String base64Img, int maxWidth, int maxHeight) {
		try {
			BufferedImage image = ImageScaler.decodeToImage(base64Img);
			BufferedImage bufferedImage = ImageScaler.resizeImage(image, maxWidth, maxHeight);
			return ImageScaler.getBase64Img(bufferedImage);
		} catch (Exception e) {
		} 
		return null;
	}
	
	public static BufferedImage resizeImage(String base64Img) {
		return resizeImage(decodeToImage(base64Img));
	}
	public static BufferedImage decodeToImage(String imageString) {
	    BufferedImage image = null;
	    byte[] imageByte;
	    try {
	        Base64.Decoder decoder = Base64.getDecoder();
	        imageByte = decoder.decode(imageString);
	        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	        image = ImageIO.read(bis);
	        bis.close();
	    } catch (Exception e) {
	    }
	    return image;
	}
	public static String getBase64Img(BufferedImage image) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(image, "JPG", os); 
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (Exception e) {
		} 
		return null;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
		Dimension scaledDimensions = getScaledDimension(originalImage, maxWidth, maxHeight);

		BufferedImage resizedImage = new BufferedImage(scaledDimensions.width, scaledDimensions.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, scaledDimensions.width, scaledDimensions.height, Color.WHITE, null);
		g.dispose();

		return resizedImage;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage) {
		Dimension scaledDimensions = getScaledDimension(originalImage, maxWidth, maxHeight);

		BufferedImage resizedImage = new BufferedImage(scaledDimensions.width, scaledDimensions.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, scaledDimensions.width, scaledDimensions.height, Color.WHITE, null);
		g.dispose();

		return resizedImage;
	}

	public static Dimension getScaledDimension(BufferedImage image, int maxWidth, int maxHeight) {
		Dimension largestDimension = new Dimension(maxWidth, maxHeight);

		// Original size
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		float aspectRatio = (float) imageWidth / imageHeight;

		if (imageWidth > maxWidth || imageHeight > maxHeight) {
			if ((float) largestDimension.width / largestDimension.height > aspectRatio) {
				largestDimension.width = (int) Math.ceil(largestDimension.height * aspectRatio);
			} else {
				largestDimension.height = (int) Math.ceil(largestDimension.width / aspectRatio);
			}

			imageWidth = largestDimension.width;
			imageHeight = largestDimension.height;
		}

		return new Dimension(imageWidth, imageHeight);
	}
}
