package com.gt.stub.web.process;

/**
 * Created by noah on 2017. 5. 21..
 */
public enum RProcess {
    MemberCheck("r005"), JoinMypage("r006"), MypageAuthentication("r007"), //
    AttributeRegistration("r008"), JoinECMember("r009"), Linkage("r010"), //
    UpdateLinkage("r011"), AttributeRegistrationLinkage("r012"), //
    JoinLinkage("r013"), TokenDecrypt("r014"), ReissueToken("r015");

    private String processNo;

    RProcess(String processNo) {
        this.processNo = processNo;
    }

    public String getProcessNo() {
        return processNo;
    }
}
