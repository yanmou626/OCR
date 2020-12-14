package com.test.mars.orc.service.impl;

import org.springframework.stereotype.Service;

import com.test.mars.common.model.BizException;
import com.test.mars.orc.service.BizCardOcr;
import com.test.mars.orc.service.model.BizLicenseInfo;

/**
 * 营业执照信息识别
 *
 * @author Kent
 */
@Service
public class BizCardOcrImpl implements BizCardOcr {
	/**
	 * 横版解析
	 */
	private HorizontalCardOcrImpl horizontalCardOcr = new HorizontalCardOcrImpl();
	
	/**
	 * 竖版解析
	 */
	private VerticalCardOcrImpl verticalCardOcr = new VerticalCardOcrImpl();

	/**
	 *
	 * @param base64Img
	 * @return
	 * @throws BizException
	 */
	@Override
	public BizLicenseInfo getInfoByType(String base64Img, Integer type) throws BizException {
		if (type.equals(1)) {
			// 横屏解析
			return horizontalCardOcr.getInfo(base64Img);
		} else if (type.equals(2)) {
			// 竖屏解析
			return verticalCardOcr.getInfo(base64Img);
		} else {
			return new BizLicenseInfo();
		}
	}

	/**
	 *
	 * @param base64Img
	 * @return
	 * @throws BizException
	 */
	@Override
	public BizLicenseInfo getInfo(String base64Img) throws BizException {
		return horizontalCardOcr.getInfo(base64Img);
	}
}
