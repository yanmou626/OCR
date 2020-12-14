package com.test.mars.common.model;

/**
 * @author simm
 */

public enum CodeEnum {

    Ok(1000, "success"),
    Error(4001, "error.");

    private int key;

    private String value;

    CodeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
