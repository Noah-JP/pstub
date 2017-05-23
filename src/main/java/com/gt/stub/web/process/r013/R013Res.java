package com.gt.stub.web.process.r013;

import com.gt.stub.web.process.RxxxRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by noah on 2017. 5. 22..
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class R013Res extends RxxxRes {
    private String token;
    private String cardsbt;
    private String himopt;
    private String webpt;
}
