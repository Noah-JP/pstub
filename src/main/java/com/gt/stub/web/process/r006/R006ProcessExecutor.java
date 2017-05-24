package com.gt.stub.web.process.r006;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.holder.CardProperties;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 22..
 */
@Component
public class R006ProcessExecutor extends AbstractRProcessExecutor<R006Req, R006Res> {


    @Value("${stub.notmatched.birthday:20000101}")
    private String notMatchedBirthDay;

    private CardProperties cardProperties;
    private CardService cardService;
    private TokenEncryptor encryptor;

    R006ProcessExecutor(CardService cardService, TokenEncryptor encryptor, CardProperties cardProperties) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
        this.cardProperties = cardProperties;
    }

    @Override
    public RProcess processName() {
        return RProcess.JoinMypage;
    }

    @Override
    protected R006Res process(R006Req r006Req) {

        String token = r006Req.getToken();

        R006Res res = new R006Res();
        res.setSts(ProcessStatus.Success.getStatusCode());
        res.setToken(token);

        if (StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }

        if (!cardService.existToken(token)) {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        CardInfo cardInfo = cardService.get(encryptor.decrypt(token));
        if (Objects.isNull(cardInfo)) {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        if (cardInfo.getTokenExpired()) {
            res.setSts(ProcessStatus.ExpiredToken.getStatusCode());
            return res;
        }

        if (notMatchedBirthDay.equals(r006Req.getBirthday())) {
            res.setSts(ProcessStatus.AttrNotMatched.getStatusCode());
            return res;
        }

        if (cardInfo.getMypageAuthenticated()) {
            res.setSts(ProcessStatus.Registered.getStatusCode());
            return res;
        }

        Owner owner = new Owner();
        owner.setCardNo(cardInfo.getCardNo());
        owner.setNameSei(r006Req.getMembname1());
        owner.setNameMei(r006Req.getMembname2());
        owner.setNameSeiKana(r006Req.getMembnamekn1());
        owner.setNameMeiKana(r006Req.getMembnamekn2());
        owner.setSex(r006Req.getSex());
        owner.setBirthday(r006Req.getBirthday());
        owner.setEmail(r006Req.getEmail1());
        owner.setPassword(r006Req.getAuthkey());
        owner.setTelNo(r006Req.getTelno());
        owner.setPostNo(r006Req.getPostno());
        owner.setAddress1(r006Req.getAddress1());
        owner.setAddress2(r006Req.getAddress2());
        owner.setAddress3(r006Req.getAddress3());

        cardInfo.setOwner(owner);
        cardInfo.setMypageAuthenticated(true);
        cardInfo.setRegStatus(RegStatus.Registered);

        cardService.update(cardInfo);

        return res;
    }
}
