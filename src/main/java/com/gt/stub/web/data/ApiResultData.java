package com.gt.stub.web.data;

import com.gt.stub.persistence.entity.CardInfo;
import lombok.Data;

/**
 * Created by noah on 2017. 5. 20..
 */
@Data
public class ApiResultData {

    private Boolean success;
    private String message;
    private CardInfo data;
}
