package com.gt.stub.web.process.r006;

import com.gt.stub.web.process.RxxxRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by noah on 2017. 5. 22..
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class R006Res extends RxxxRes {
    private String token;
}
