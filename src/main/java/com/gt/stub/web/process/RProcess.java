package com.gt.stub.web.process;

/**
 * Created by noah on 2017. 5. 21..
 */
public enum RProcess {
    MemberCheck("r005"), JoinECMember("r009"), TokenDecrypt("r014"), ReissueToken("r015");

    private String processNo;

    RProcess(String processNo) {
        this.processNo = processNo;
    }

    public String getProcessNo() {
        return processNo;
    }
}
