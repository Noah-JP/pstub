package com.gt.stub.web.process.r015;

import com.gt.stub.web.process.RxxxRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by nttd-gy-chie on 2017/05/22.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class R015Res extends RxxxRes {
    private String pointid;
    private String kaiinno;
    private String token;
}
