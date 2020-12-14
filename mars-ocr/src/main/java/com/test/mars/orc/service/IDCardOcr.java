package com.test.mars.orc.service;

import java.io.IOException;

import com.test.mars.common.model.BizException;
import com.test.mars.orc.service.model.IDCardInfo;

public interface IDCardOcr {
    IDCardInfo getFrontInfo(String base64) throws BizException, IOException;
    IDCardInfo getBackInfo(String base64,IDCardInfo idCardInfo) throws BizException, IOException;
}
