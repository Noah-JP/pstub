package com.gt.stub.web.data;

import lombok.Data;

import java.util.List;

/**
 * Created by noah on 2017. 5. 20..
 */
@Data
public class ApiResultData {

    private Boolean success;
    private String message;
    private List<SavedCardData> result;

}
