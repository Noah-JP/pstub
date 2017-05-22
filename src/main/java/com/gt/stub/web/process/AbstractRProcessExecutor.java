package com.gt.stub.web.process;

import com.gt.stub.persistence.entity.CardInfo;
import org.springframework.util.StringUtils;

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

    // TODO : create attr check strategy.
    public String calculateTypeNo(String cardNo) {
        if (!StringUtils.isEmpty(cardNo)) {
            if (cardNo.length() == 16) {
                return String.valueOf(new char[]{cardNo.charAt(4), cardNo.charAt(5), cardNo.charAt(8)});
            }
        }
        throw new IllegalArgumentException();
    }

    public String checkPass(CardInfo card) {
        return card.getMypageAuthenticated() ? PassCheck.Matched.getPassCheckCode() : PassCheck.Unmatched.getPassCheckCode();
    }
}
