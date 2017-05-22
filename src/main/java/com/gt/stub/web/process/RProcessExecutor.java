package com.gt.stub.web.process;

/**
 * Created by noah on 2017. 5. 21..
 */
public interface RProcessExecutor<REQ, RES extends RxxxRes> {

    RProcess processName();

    RES execute(REQ req);

    Class<REQ> getRequestType();

    Class<RES> getResponseType();
}
