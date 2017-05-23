package com.gt.stub.web.data;

import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import lombok.Data;

/**
 * Created by noah on 2017. 5. 20..
 */
@Data
public class SavedCardData {

    private String cardNo;
    private String token;
    private IssuedBy issuedBy;
    private RegStatus regStatus;
    private String points;
    private String expiringDateOfPoints;
    private String issuedDateOfPoints;
    private String expiringPoints;
    private Integer rank;
    private Boolean tokenExpired;
    private Boolean mypageAuthenticated;
    private OwnerData owner;
}
