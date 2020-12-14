package com.test.mars.orc.service;

import com.test.mars.common.model.BizException;
import com.test.mars.orc.service.model.BizLicenseInfo;

/**
 * 营业执照号识别
 * @author Kent
 */
public interface BizCardOcr {
    /**
     * 获取营业执照信息
     * @param base64
     * @return
     * @throws BizException
     */
    BizLicenseInfo getInfo(String base64) throws BizException;
    /**
     * 按类型取营业执照信息
     * @param base64Img
     * @param type (1:横屏；2：竖屏)
     * @return
     * @throws BizException
     */
    BizLicenseInfo getInfoByType(String base64Img, Integer type) throws BizException;
}
