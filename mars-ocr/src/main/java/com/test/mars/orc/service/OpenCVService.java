package com.test.mars.orc.service;

public interface OpenCVService {
    String correct(String base64,String correctPath);
    String getTmpPath();
    Boolean getSaveTmpImg();
}
