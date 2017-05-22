package com.gt.stub.web.process;

import java.lang.reflect.ParameterizedType;

/**
 * Created by noah on 2017. 5. 21..
 */
public abstract class AbstractRProcessExecutor<REQ, RES extends RxxxRes> implements RProcessExecutor<REQ, RES> {

    public static final String POINT_ID = "00001";

    Class<REQ> requestType;
    Class<RES> responseType;

    public AbstractRProcessExecutor() {
        this.requestType = (Class<REQ>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.responseType = (Class<RES>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Override
    public final RES execute(REQ req) {
        return process(req);
    }

    protected abstract RES process(REQ req);

    @Override
    public Class<REQ> getRequestType() {
        return this.requestType;
    }

    @Override
    public Class<RES> getResponseType() {
        return this.responseType;
    }
}
