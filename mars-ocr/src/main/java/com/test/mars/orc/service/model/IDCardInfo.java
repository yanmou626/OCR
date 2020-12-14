package com.test.mars.orc.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kent
 */
@Data
public class IDCardInfo implements Serializable {
    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;
    private String idNumber;
    private String fromDate;
    private String toDate;
    private String issuingUnit;
}
