package com.test.mars.orc.service.impl;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 * 营业执照信息识别(竖版)
 *
 * @author Kent
 */
@Service("vertical")
public class VerticalCardOcrImpl extends BaseBizCardOcr {
    VerticalCardOcrImpl() {
        //主图大小
        super(new int[]{1566, 2256});
    }

    @Override
    protected Function<String, int[]> getPartRect(BufferedImage bufferedImage) {
        return type -> {
            switch (type) {
                case "name":
                    return new int[]{435, 940, 1010, 80};
                case "bizType":
                    return new int[]{435, 1020, 1010, 66};
                case "address":
                    return new int[]{435, 1100, 1010, 74};
                case "juridical":
                    return new int[]{435, 1180, 1010, 70};
                case "capital":
                    return new int[]{435, 1240, 1010, 70};
                case "buildOn":
                    return new int[]{435, 1310, 1010, 70};
                case "bizLimit":
                    return new int[]{435, 1380, 1010, 70};
                case "bizScope":
                    return new int[]{435, 1440, bufferedImage.getWidth()-495, 360};
                case "creditCode":
                    return new int[]{1035, 700, 440, 200};
                default:
                    return new int[3];
            }
        };
    }
}
