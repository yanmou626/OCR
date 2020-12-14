package com.test.mars.orc.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Kent
 */
@Data
public class BizLicenseInfo implements Serializable {
	private String name;
	private String capital;
	private String bizType;
	private String buildOn;
	private String juridical;
	private String bizLimit;
	private String bizScope;
	private String address;
	private String creditCode;
}
