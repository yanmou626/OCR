package com.test.mars.orc.service.impl;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.test.mars.common.model.BizException;
import com.test.mars.commonutils.ImageOpencvUtils;
import com.test.mars.commonutils.MyStreamUtils;
import com.test.mars.commonutils.ocr.ImageFilter;
import com.test.mars.orc.service.IDCardOcr;
import com.test.mars.orc.service.OpenCVService;
import com.test.mars.orc.service.model.IDCardInfo;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.image.BufferedImage;

public class IDCardOcrImpl implements IDCardOcr {
	private static int[] WHITE = new int[] { 255, 255, 255 };
	private static int[] BLACK = new int[] { 0, 0, 0 };
	private static int[] GRAY = new int[] { 245, 245, 245 };
	private static int[] BIRTH_GRAY = new int[] { 73, 73, 73 };
	private static int[] ID_GRAY = new int[] { 100, 100, 100 };
	private static int targetContentBrightness = 330;
	private static int targetBirthBrightness = 300;
	private static int targetIdBrightness = 300;
	private static int targetAddressBrightness = 280;
	private static int targetDifferenceValue = 20;
	
	private static int targetBackDifferenceValue = 15;
	private static int targetBackIssuingUnitDifferenceValue = 300;

	private OpenCVService openCVService = new OpenCVServiceImpl();

	/**
	 * 解析身份证信息
	 *
	 * @param base64Img
	 * @return
	 * @throws Exception
	 */
	@Override
	public IDCardInfo getFrontInfo(String base64Img) throws BizException {
		Boolean outPartImg = openCVService.getSaveTmpImg();
		System.out.println("outPartImg:" + outPartImg);
		IDCardInfo idCardInfo = new IDCardInfo();
		try {
			// 身份证图片扶正？？
			
			//身份证裁处多余部分
			
			// String base64 = openCVService.correct(base64Img, getImgPath(outPartImg,
			// "/correct.jpg"));
			Tesseract tesseract = new Tesseract();
			tesseract.setLanguage("chi_sim");
			// 读取图片
			BufferedImage bufferedImage = MyStreamUtils.base64ToBufferedImage(base64Img);
			bufferedImage = ImageFilter.imageRGBDifferenceFilter(bufferedImage, targetDifferenceValue, null);
			bufferedImage = ImageFilter.convertImageToGrayScale(bufferedImage);
			
			
			// 缩放到真实身份证大小
			bufferedImage = ImageFilter.imageScale(bufferedImage, 673, 425);
			// 准备参数
			
			ImageOpencvUtils.saveImage(bufferedImage, outPartImg ? openCVService.getTmpPath() + "/bg.jpg" : null);
			getBufferedContentImage(tesseract, bufferedImage, idCardInfo,
					getImgPath(outPartImg, "/contentImageBefore.jpg"));
			getBufferedBirthImage(tesseract, bufferedImage, idCardInfo,
					getImgPath(outPartImg, "/birthImageBefore.jpg"));
			getBufferedAddressImage(tesseract, bufferedImage, idCardInfo,
					getImgPath(outPartImg, "/addressImageBefore.jpg"));
			getBufferedIdImage(tesseract, bufferedImage, idCardInfo, getImgPath(outPartImg, "/idImageBefore.jpg"));
			return idCardInfo;
		} catch (TesseractException e) {
			e.printStackTrace();
			throw new BizException("身份证格式不规范，无法识别");
		}
	}
	
	@Override
	public IDCardInfo getBackInfo(String base64Img,IDCardInfo idCardInfo) throws BizException, IOException {
		Boolean outPartImg = openCVService.getSaveTmpImg();
		System.out.println("outPartImg:" + outPartImg);
		if (idCardInfo == null) {
			idCardInfo = new IDCardInfo();
		}
		try {
			Tesseract tesseract = new Tesseract();
			tesseract.setLanguage("chi_sim");
			
			BufferedImage bufferedImage = MyStreamUtils.base64ToBufferedImage(base64Img);
			bufferedImage = ImageFilter.imageRGBDifferenceFilter(bufferedImage, targetBackDifferenceValue, null);
			bufferedImage = ImageFilter.convertImageToGrayScale(bufferedImage);
			// 缩放到真实身份证大小
			bufferedImage = ImageFilter.imageScale(bufferedImage, 673, 425);
			ImageOpencvUtils.saveImage(bufferedImage, outPartImg ? openCVService.getTmpPath() + "/bgb.jpg" : null);
		
			getBufferedIssuingUnitImage(tesseract, bufferedImage, idCardInfo, getImgPath(outPartImg, "/issuingUnit.jpg"));
			getBufferedFromToImage(tesseract, bufferedImage, idCardInfo, getImgPath(outPartImg, "/fromToDate.jpg"));

		} catch (TesseractException e) {
			e.printStackTrace();
			throw new BizException("身份证格式不规范，无法识别");
		}
		
		return idCardInfo;
	}

	private String getImgPath(boolean outPartImg, String path) {
		return outPartImg ? openCVService.getTmpPath() + path : null;
	}

	/**
	 * 身份证号解析
	 *
	 * @param tesseract
	 * @param bufferedImage
	 * @param idCardInfo
	 * @return
	 * @throws TesseractException
	 */
	private int[] getBufferedIdImage(Tesseract tesseract, BufferedImage bufferedImage, IDCardInfo idCardInfo,
			String temp) throws TesseractException {
		int[] positions = new int[] { bufferedImage.getMinX() + 210, 334, bufferedImage.getWidth() - 210,
				bufferedImage.getHeight() - 334 };
		BufferedImage idImage = ImageFilter.subImage(bufferedImage, positions[0], positions[1], positions[2],
				positions[3]);
		System.out.println("idImage 辉度处理");
		handBrightness(idImage, targetIdBrightness);
		ImageOpencvUtils.saveImage(idImage, temp);
		tesseract.setLanguage("eng");
		String idCardNumber = tesseract.doOCR(idImage).replaceAll("[^0-9xX]", "");
		idCardInfo.setIdNumber(idCardNumber);
		return positions;
	}

	/**
	 * 地址解析
	 *
	 * @param tesseract
	 * @param bufferedImage
	 * @param idCardInfo
	 * @return
	 * @throws TesseractException
	 */
	private int[] getBufferedAddressImage(Tesseract tesseract, BufferedImage bufferedImage, IDCardInfo idCardInfo,
			String temp) throws TesseractException {
		int[] positions = new int[] { bufferedImage.getMinX() + 110, 208, 340, 144 };
		BufferedImage addressImage = ImageFilter.subImage(bufferedImage, positions[0], positions[1], positions[2],
				positions[3]);
//        addressImage = ImageFilter.imageScale(addressImage, ((int) (addressImage.getWidth() * 2.4) + 1), ((int) (addressImage.getHeight() * 2.4) + 1));
		System.out.println("addressImage 辉度处理");
		handBrightness(addressImage, targetAddressBrightness);
		ImageOpencvUtils.saveImage(addressImage, temp);
		tesseract.setLanguage("chi_sim");
		String result = tesseract.doOCR(addressImage);
		idCardInfo.setAddress(
				result.replaceAll("[^\\s\\u4e00-\\u9fa5\\-0-9]+", "").replaceAll("\\n", "").replaceAll(" ", ""));
		return positions;
	}

	/**
	 * 内容解析
	 *
	 * @param tesseract
	 * @param bufferedImage
	 * @param idCardInfo
	 * @return
	 * @throws TesseractException
	 */
	private int[] getBufferedContentImage(Tesseract tesseract, BufferedImage bufferedImage, IDCardInfo idCardInfo,
			String temp) throws TesseractException {
		int[] positions = new int[] { bufferedImage.getMinX() + 110, bufferedImage.getMinY(), 323, 145 };
		BufferedImage contentImage = ImageFilter.subImage(bufferedImage, positions[0], positions[1], positions[2],
				positions[3]);
		System.out.println("contentImage 辉度处理");
		handBrightness(contentImage, targetContentBrightness);
		ImageOpencvUtils.saveImage(contentImage, temp);
		tesseract.setLanguage("chi_sim");
		String result = tesseract.doOCR(contentImage);
		String[] resultArray = result.split("\n");
		String name = resultArray[0].replaceAll("[^\\u4e00-\\u9fa5]", "").trim();
		idCardInfo.setName(name);
		if (resultArray.length > 1) {
			String[] sexAbout = resultArray[1].replaceAll("[^\\u4e00-\\u9fa5 ]", "").trim().split("\\s+");
			if (sexAbout.length > 0) {
				idCardInfo.setSex(sexAbout[0]);
			}
			if (sexAbout.length > 1) {
				String nation = "";
				for (int i = 1; i < sexAbout.length; i++) {
					nation+=sexAbout[i];
				}
				idCardInfo.setNation(nation);
			}
		}
		return positions;
	}
	
	/**
	 * 发证机关
	 * @param tesseract
	 * @param bufferedImage
	 * @param idCardInfo
	 * @param temp
	 * @return
	 * @throws TesseractException
	 */
	private int[] getBufferedIssuingUnitImage(Tesseract tesseract, BufferedImage bufferedImage, IDCardInfo idCardInfo,
			String temp) throws TesseractException {
		int[] positions = new int[] { bufferedImage.getMinX() + 250, bufferedImage.getMinY()+ 200, 400, 145 };
		BufferedImage contentImage = ImageFilter.subImage(bufferedImage, positions[0], positions[1], positions[2],
				positions[3]);
		System.out.println("contentImage 辉度处理");
		handBrightness(contentImage, targetBackIssuingUnitDifferenceValue);
		ImageOpencvUtils.saveImage(contentImage, temp);
		tesseract.setLanguage("chi_sim");
		String result = tesseract.doOCR(contentImage);
		idCardInfo.setIssuingUnit(
				result.replaceAll("[^\\s\\u4e00-\\u9fa5\\-0-9]+", "").replaceAll("\\n", "").replaceAll(" ", ""));
		return positions;
	}
	
	/**
	 * 
	 * @param tesseract
	 * @param bufferedImage
	 * @param idCardInfo
	 * @param temp
	 * @return
	 * @throws TesseractException
	 */
	private int[] getBufferedFromToImage(Tesseract tesseract, BufferedImage bufferedImage, IDCardInfo idCardInfo,
			String temp) throws TesseractException {
		int[] positions = new int[] { bufferedImage.getMinX() + 250, 340, bufferedImage.getWidth() - 250,
				bufferedImage.getHeight() - 340  };
		BufferedImage contentImage = ImageFilter.subImage(bufferedImage, positions[0], positions[1], positions[2],
				positions[3]);
		System.out.println("contentImage 辉度处理");
		handBrightness(contentImage, targetBackIssuingUnitDifferenceValue);
		ImageOpencvUtils.saveImage(contentImage, temp);
		tesseract.setLanguage("chi_sim");
		String result = tesseract.doOCR(contentImage);
		
		String date = result.replaceAll("[^\\s\\u4e00-\\u9fa5\\-0-9]+", "").replaceAll("\\n", "").replaceAll("-", "").replaceAll(" ", "");
		if (!StringUtils.isEmpty(date)) {
			idCardInfo.setFromDate(date.substring(0, 8));
			idCardInfo.setToDate(date.substring(8));
		}
				
		return positions;
	}

	/**
	 * 获取缓存图片
	 *
	 * @param bufferedImage
	 * @return
	 * @throws IOException
	 */
	private int[] getBufferedBirthImage(Tesseract tesseract, BufferedImage bufferedImage, IDCardInfo idCardInfo,
			String temp) throws TesseractException {
		int[] positions = new int[] { bufferedImage.getMinX() + 110, 154, 300, 54 };
		// 裁剪图片
		BufferedImage birthImage = ImageFilter.subImage(bufferedImage, positions[0], positions[1], positions[2],
				positions[3]);
		System.out.println("birthImage 辉度处理");
		handBrightness(birthImage, targetBirthBrightness);
		// 解析图片
		ImageOpencvUtils.saveImage(birthImage, temp);
		tesseract.setLanguage("eng");
		idCardInfo.setBirth(tesseract.doOCR(birthImage).replaceAll("[^0-9 ]", ""));
		return positions;
	}

	/**
	 * 处理图片辉度
	 *
	 * @param subImage
	 */
	private void handBrightness(BufferedImage subImage, int targetBrightness) {
		int birthBrightness = ImageFilter.imageBrightness(subImage);
		System.out.println("brightness = " + birthBrightness);
		int fixedBrightness = targetBrightness - birthBrightness;
		// 辉度处理
		if (fixedBrightness != 0) {
			subImage = ImageFilter.imageBrightness(subImage, fixedBrightness);
		}
		System.out.println("after brightness = " + ImageFilter.imageBrightness(subImage));
	}

}
