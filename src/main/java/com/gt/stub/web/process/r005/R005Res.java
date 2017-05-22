package com.gt.stub.web.process.r005;

import com.gt.stub.web.process.RxxxRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by noah on 2017. 5. 21..
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class R005Res extends RxxxRes {
    private String kaiinno;
    private String token;
    private String kaiinchk;
    private String trksts;
    private String gtchk;
    private String passchk;
    private String pointts;
    private String pointzan;
    private String pointlimit;
    private String pointexpzan;
    private String msprnk;
}
