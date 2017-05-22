package com.gt.stub.web.process.r009;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.AppSpecificUtils;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by noah on 2017. 5. 22..
 */
@Component
public class R009ProcessExecutor extends AbstractRProcessExecutor<R009Req, R009Res> {

    private CardService cardService;
    private TokenEncryptor encryptor;

    R009ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.JoinECMember;
    }

    @Override
    protected R009Res process(R009Req r009Req) {
        LocalDateTime now = LocalDateTime.now();

        CardInfo cardInfo = new CardInfo();

        String nextCardNo = cardService.nextCardNo();

        cardInfo.setCardNo(nextCardNo);
        cardInfo.setToken(encryptor.encrypt(nextCardNo));

        cardInfo.setIssuedBy(IssuedBy.EC);
        cardInfo.setRegStatus(RegStatus.Registed);

        cardInfo.setPoints(BigDecimal.ZERO);
        cardInfo.setIssuedDateOfPoints(now);

        cardInfo.setExpiringPoints(BigDecimal.ZERO);
        cardInfo.setExpiringDateOfPoints(now);

        cardInfo.setRank(Integer.valueOf(0));

        cardInfo.setTokenExpired(false);
        cardInfo.setMypageAuthenticated(true);

        Owner owner = new Owner();
        {
            owner.setCardNo(nextCardNo);
            owner.setNameSei(r009Req.getMembname1());
            owner.setNameMei(r009Req.getMembname2());
            owner.setNameSeiKana(r009Req.getMembnamekn1());
            owner.setNameMeiKana(r009Req.getMembnamekn2());
            owner.setSex(r009Req.getSex());
            owner.setBirthday(r009Req.getBirthday());
            owner.setEmail(r009Req.getEmail1());
            owner.setPassword(r009Req.getAuthkey());
            owner.setTelNo(r009Req.getTelno());
            owner.setPostNo(r009Req.getPostno());
            owner.setAddress1(r009Req.getAddress1());
            owner.setAddress2(r009Req.getAddress2());
            owner.setAddress3(r009Req.getAddress3());
        }
        cardInfo.setOwner(owner);

        CardInfo saved = cardService.createBy(nextCardNo, cardInfo);

        R009Res res = new R009Res();
        res.setSts(ProcessStatus.Success.getStatusCode());
        res.setHimopt(FormatUtils.decimalToNoFraction(BigDecimal.ZERO));
        res.setWebpt(FormatUtils.decimalToNoFraction(BigDecimal.ZERO));
        res.setToken(saved.getToken());
        res.setCardsbt(calculateTypeNo(saved.getCardNo()));

        return res;
    }
}
