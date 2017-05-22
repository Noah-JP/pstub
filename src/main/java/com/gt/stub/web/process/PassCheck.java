package com.gt.stub.web.process;

/**
 * Created by nttd-gy-chie on 2017/05/22.
 */
public enum PassCheck {
    Matched("0"), Unmatched("1"), UnregistedPass("2"), EcLock("3"), MypageLock("4");

    private String passCheckCode;

    PassCheck(String passCheckCode) {
        this.passCheckCode = passCheckCode;
    }

    public String getPassCheckCode() {
        return passCheckCode;
    }
}
