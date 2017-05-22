package com.gt.stub.web.process;

/**
 * Created by noah on 2017. 5. 21..
 */
public enum ProcessStatus {

    Success("00"), NoData("01"), Inactive("02"), Withdraw("03"), WithdrawAll("04"), Blank("05"), //
    Unregisted("06"), Registed("07"), ExpiredToken("10"), InvalidToken("11"), NegativeToken("12"), //
    AttrNotMatched("30"), Maintenance("70"), SystemError("80"), NegativeRequest("90");

    private String statusCode;

    ProcessStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }


}
