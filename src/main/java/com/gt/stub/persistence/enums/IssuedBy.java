package com.gt.stub.persistence.enums;

/**
 * Created by noah on 2017. 5. 20..
 */
public enum IssuedBy {
    EC("1"), ETC("0");

    private String issueNo;

    IssuedBy(String issueNo) {
        this.issueNo = issueNo;
    }

    public String getIssueNo() {
        return issueNo;
    }
}
