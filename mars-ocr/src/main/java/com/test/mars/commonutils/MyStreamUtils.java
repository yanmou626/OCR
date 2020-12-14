package com.test.mars.commonutils;

import org.apache.poi.util.IOUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.boot.SpringApplication;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Stream Tools
 *
 * @author Kent
 */
public class MyStreamUtils {

	/**
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String streamToBase64(InputStream inputStream) throws IOException {
		return Base64Utils.encodeToString(IOUtils.toByteArray(inputStream));
	}

	/**
	 *
	 * @param correctMat
	 * @return
	 */
	public static String catToBase64(Mat correctMat) {
		return bufferToBase64(toByteArray(correctMat));
	}

	/**
	 *
	 * @param buffer
	 * @return
	 */
	public static String bufferToBase64(byte[] buffer) {
		return Base64Utils.encodeToString(buffer);
	}

	/**
	 *
	 * @param base64Str
	 * @return
	 */
	public static byte[] base64ToByteArray(String base64Str) {
		return Base64Utils.decodeFromString(base64Str);
	}

	/**
	 *
	 * @param base64
	 * @return
	 */
	public static BufferedImage base64ToBufferedImage(String base64) {
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes1 = decoder.decodeBuffer(base64);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			return ImageIO.read(bais);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param matrix
	 * @return
	 */
	public static byte[] toByteArray(Mat matrix) {
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".jpg", matrix, mob);
		return mob.toArray();
	}

	/**
	 *
	 * @param matrix
	 * @return
	 */
	public static BufferedImage toBufferedImage(Mat matrix) throws IOException {
		byte[] buffer = toByteArray(matrix);
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
		return ImageIO.read(bais);
	}

	/**
	 *
	 * @param base64
	 * @return
	 * @throws IOException
	 */
	public static Mat base642Mat(String base64) throws IOException {

		return bufImg2Mat(base64ToBufferedImage(base64), BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);
	}

	/**
	 *
	 * @param original BufferedImage
	 * @param imgType  bufferedImage typeBufferedImage.TYPE_3BYTE_BGR
	 * @param matType   CvType.CV_8UC3
	 */
	public static Mat bufImg2Mat(BufferedImage original, int imgType, int matType) {
		if (original == null) {
			throw new IllegalArgumentException("original == null");
		}
		// Don't convert if it already has correct type
		if (original.getType() != imgType) {
			// Create a buffered image
			BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);
			// Draw the image onto the new buffer
			Graphics2D g = image.createGraphics();
			try {
				g.setComposite(AlphaComposite.Src);
				g.drawImage(original, 0, 0, null);
				original = image;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				g.dispose();
			}
		}
		byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
		Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
		mat.put(0, 0, pixels);
		return mat;
	}
	
}
