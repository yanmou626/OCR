package com.test.mars.orc.service.impl;

import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 * 营业执照信息识别(横版)
 *
 * @author Kent
 */
public class HorizontalCardOcrImpl extends BaseBizCardOcr {
    HorizontalCardOcrImpl() {
        //主图大小
        super(new int[]{3150, 1920});
    }

    @Override
    protected Function<String, int[]> getPartRect(BufferedImage bufferedImage) {
        return type -> {
            switch (type) {
                case "name":
                    return new int[]{450, 700, 1200, 120};
                case "capital":
                    return new int[]{2050, 700, bufferedImage.getWidth() - 2400, 120};
                case "bizType":
                    return new int[]{450, 820, 1200, 130};
                case "buildOn":
                    return new int[]{2050, 820, bufferedImage.getWidth() - 2400, 100};
                case "juridical":
                    return new int[]{450, 950, 1200, 120};
                case "bizLimit":
                    return new int[]{2050, 950, bufferedImage.getWidth() - 2400, 100};
                case "bizScope":
                    return new int[]{450, 1070, 1290, bufferedImage.getHeight() - 1200};
                case "address":
                    return new int[]{2050, 1070, bufferedImage.getWidth() - 2240, 270};
                case "creditCode":
                    return new int[]{bufferedImage.getMinX() + 140, 250, 550, 300};
                default:
                    return new int[3];
            }
        };
    }
}
