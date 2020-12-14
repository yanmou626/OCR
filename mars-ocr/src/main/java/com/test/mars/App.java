package com.test.mars;

import java.io.FileInputStream;

import com.test.mars.commonutils.MyStreamUtils;
import com.test.mars.orc.service.impl.BizCardOcrImpl;
import com.test.mars.orc.service.impl.IDCardOcrImpl;
import com.test.mars.orc.service.model.BizLicenseInfo;
import com.test.mars.orc.service.model.IDCardInfo;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
//		getIdCardInfo();
		
		getBizInfo();
	}
	
	public static void getBizInfo() throws Exception {
		
		//横版
//		FileInputStream fis1 = new FileInputStream("D:\\ocr\\test\\yezz_hb1.jpg");
//		BizCardOcrImpl bizCardOcrImpl = new BizCardOcrImpl();
//		BizLicenseInfo bizLicenseInfo= bizCardOcrImpl.getInfoByType(MyStreamUtils.streamToBase64(fis1), 1);
//		System.out.println(bizLicenseInfo);
		
		//竖版
		FileInputStream fis = new FileInputStream("D:\\ocr\\test\\aqjm1111111.png");
//		FileInputStream fis = new FileInputStream("D:\\ocr\\test\\yezz_sb11111.png");
		BizCardOcrImpl bizCardOcrImpl = new BizCardOcrImpl();
		BizLicenseInfo bizLicenseInfo= bizCardOcrImpl.getInfoByType(MyStreamUtils.streamToBase64(fis), 2);
		System.out.println(bizLicenseInfo);
		
	}

	public static void getIdCardInfo() throws Exception {
		FileInputStream fis = new FileInputStream("D:\\ocr\\test\\sfz11.jpg");
		FileInputStream fisb = new FileInputStream("D:\\ocr\\test\\sfz1_b1.jpg");
		
//		FileInputStream fis = new FileInputStream("D:\\ocr\\test\\sfz6.jpg");
//		FileInputStream fisb = new FileInputStream("D:\\ocr\\test\\sfz1_b1.jpg");
		
//		FileInputStream fis = new FileInputStream("D:\\ocr\\test\\sfz1.jpg");
//		FileInputStream fisb = new FileInputStream("D:\\ocr\\test\\sfz1_1.jpg");

		IDCardOcrImpl idCardOcrImpl = new IDCardOcrImpl();
		IDCardInfo idinfo = idCardOcrImpl.getFrontInfo(MyStreamUtils.streamToBase64(fis));
		idCardOcrImpl.getBackInfo(MyStreamUtils.streamToBase64(fisb), idinfo);
		System.out.println(idinfo);

	}
}
