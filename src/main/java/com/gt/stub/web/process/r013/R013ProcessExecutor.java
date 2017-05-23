package com.gt.stub.web.process.r013;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by noah on 2017. 5. 22..
 */
@Component
public class R013ProcessExecutor extends AbstractRProcessExecutor<R013Req, R013Res> {

    private CardService cardService;

    R013ProcessExecutor(CardService cardService) {
        super();
        this.cardService = cardService;
    }

    @Override
    public RProcess processName() {
        return RProcess.JoinLinkage;
    }

    @Override
    protected R013Res process(R013Req r013Req) {


        R013Res res = new R013Res();
        res.setSts(ProcessStatus.Success.getStatusCode());

        String token = r013Req.getToken();
        if (StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }

        if (!cardService.existToken(token)) {
            res.setSts(ProcessStatus.NegativeToken.getStatusCode());  // TODO check invalidToken
            return res;
        }

        String newCardNo = cardService.nextCardNo();

        CardInfo issuedBlankCard = cardService.createBlank(newCardNo);

        Owner owner = new Owner();
        {
            owner.setCardNo(issuedBlankCard.getCardNo());
            owner.setNameSei(r013Req.getMembname1());
            owner.setNameMei(r013Req.getMembname2());
            owner.setNameSeiKana(r013Req.getMembnamekn1());
            owner.setNameMeiKana(r013Req.getMembnamekn2());
            owner.setSex(r013Req.getSex());
            owner.setBirthday(r013Req.getBirthday());
            owner.setEmail(r013Req.getEmail1());
            owner.setTelNo(r013Req.getTelno());
            owner.setPostNo(r013Req.getPostno());
            owner.setAddress1(r013Req.getAddress1());
            owner.setAddress2(r013Req.getAddress2());
            owner.setAddress3(r013Req.getAddress3());
            owner.setPassword(r013Req.getAuthkey());
        }
        issuedBlankCard.setOwner(owner);

        issuedBlankCard.setRegStatus(RegStatus.Registered);
        issuedBlankCard.setMypageAuthenticated(true);
        issuedBlankCard.setIssuedBy(IssuedBy.EC);

        CardInfo updated = cardService.update(issuedBlankCard);

        res.setCardsbt(calculateTypeNo(updated.getCardNo()));
        res.setToken(updated.getToken());
        res.setHimopt(FormatUtils.decimalToNoFraction(updated.getPoints()));
        res.setWebpt(FormatUtils.decimalToNoFraction(BigDecimal.ZERO));

        return res;
    }
}
