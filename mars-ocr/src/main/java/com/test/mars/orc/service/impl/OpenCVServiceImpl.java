package com.test.mars.orc.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.opencv.core.Mat;
import org.springframework.util.StringUtils;

import com.test.mars.commonutils.ImageOpencvUtils;
import com.test.mars.commonutils.MyStreamUtils;
import com.test.mars.orc.service.OpenCVService;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 *
 * @author Kent
 */
@Slf4j
public class OpenCVServiceImpl implements OpenCVService {
    /**
     * 是否需要保存临时图片
     */
    private Boolean saveTmpImg = true;

    private String TMP_PATH = "./tmp";

    /**
     * 项目启动时执行
     */
    @PostConstruct
    public void init() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String ext = ".so";
        if (os.startsWith("win")) {
            //window系统加载dll文件
            ext = ".dll";
        }
        //从jar包中把资源文件读取出来
        String dllPath = "/mylib/opencv_java430" + ext;
        //临时图片存在的位置
        File libDir = new File("/mylib");
        if (!libDir.exists()) {
            libDir.mkdir();
        }
        File f = new File(dllPath);
        if (!f.exists()) {
            //dll存放的位置
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("lib/opencv_java430" + ext);
                 OutputStream outputStream = new FileOutputStream(f)) {
                IOUtils.copy(in, outputStream);
            }
        }
        System.load(f.getAbsolutePath());
        File file = new File(TMP_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    @Override
    public String getTmpPath() {
        return TMP_PATH;
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    @Override
    public Boolean getSaveTmpImg() {
        return saveTmpImg;
    }

    /**
     * 图片裁剪扶正
     *
     * @param base64
     * @return
     */
    @Override
    public String correct(String base64,String correctPath) {
        try {
            Mat src = MyStreamUtils.base642Mat(base64);
            Mat correctMat = ImageOpencvUtils.correct(src, null);
            if(!StringUtils.isEmpty(correctPath)){
                ImageOpencvUtils.saveImg(correctMat,correctPath);
            }
            return MyStreamUtils.catToBase64(correctMat);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("图片扶正失败", e);
            return base64;
        }
    }
}
