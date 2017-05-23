package com.gt.stub.web.process;

/**
 * Created by nttd-gy-chie on 2017/05/23.
 */
public enum AttributeRegisterStatus {

    Incomplete("0"), Complete("1");

    private String statusCode;

    AttributeRegisterStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

}
