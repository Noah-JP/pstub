package com.gt.stub.persistence.enums;

/**
 * Created by noah on 2017. 5. 20..
 */
public enum RegStatus {
    Blank("05"), Unregistered("06"), Registered("07");

    private String statusNo;

    RegStatus(String statusNo) {
        this.statusNo = statusNo;
    }

    public String getStatusNo() {
        return statusNo;
    }
}
