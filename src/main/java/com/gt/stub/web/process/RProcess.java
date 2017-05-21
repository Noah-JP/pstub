package com.gt.stub.web.process;

/**
 * Created by noah on 2017. 5. 21..
 */
public enum RProcess {
    MemberCheck("r005"), RegistMember("r009"), TokenDecrypt("r014");

    private String processNo;

    RProcess(String processNo) {
        this.processNo = processNo;
    }

    public String getProcessNo() {
        return processNo;
    }
}
