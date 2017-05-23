package com.gt.stub.web.process.r012;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.AttributeRegisterStatus;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 22..
 */
@Component
public class R012ProcessExecutor extends AbstractRProcessExecutor<R012Req, R012Res> {

    private CardService cardService;
    private TokenEncryptor encryptor;

    R012ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.AttributeRegistrationLinkage;
    }

    @Override
    protected R012Res process(R012Req r012Req) {


        R012Res res = new R012Res();
        res.setSts(ProcessStatus.Success.getStatusCode());
        res.setZkstrkkekka(AttributeRegisterStatus.Incomplete.getStatusCode());

        String token = r012Req.getToken();

        String updateTargetCardNo = r012Req.getKaiinno();
        if (StringUtils.isEmpty(updateTargetCardNo) || StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }


        CardInfo updateTarget = cardService.get(updateTargetCardNo);
        if (Objects.isNull(updateTarget)) {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        if (RegStatus.Unregisted.equals(updateTarget.getRegStatus())) {
            res.setSts(ProcessStatus.Unregisted.getStatusCode());
            return res;
        }

        if (RegStatus.Registed.equals(updateTarget.getRegStatus())) {
            res.setSts(ProcessStatus.Registed.getStatusCode());
            return res;
        }

        if(IssuedBy.EC.equals(updateTarget.getIssuedBy())){
            res.setSts(ProcessStatus.IssuedEc.getStatusCode());
            return res;
        }

        if (!cardService.existToken(token)) {
            res.setSts(ProcessStatus.NegativeToken.getStatusCode()); // TODO check invalidToken
            return res;
        }


        Owner owner = new Owner();
        {
            owner.setCardNo(updateTarget.getCardNo());
            owner.setNameSei(r012Req.getMembname1());
            owner.setNameMei(r012Req.getMembname2());
            owner.setNameSeiKana(r012Req.getMembnamekn1());
            owner.setNameMeiKana(r012Req.getMembnamekn2());
            owner.setSex(r012Req.getSex());
            owner.setBirthday(r012Req.getBirthday());
            owner.setEmail(r012Req.getEmail1());
            owner.setTelNo(r012Req.getTelno());
            owner.setPostNo(r012Req.getPostno());
            owner.setAddress1(r012Req.getAddress1());
            owner.setAddress2(r012Req.getAddress2());
            owner.setAddress3(r012Req.getAddress3());
        }

        updateTarget.setRegStatus(RegStatus.Unregisted);
        updateTarget.setMypageAuthenticated(false);
        updateTarget.setOwner(owner);

        CardInfo updated= cardService.update(updateTarget);

        res.setCardsbt(calculateTypeNo(updated.getCardNo()));
        res.setToken(updated.getToken());
        res.setHimopt(FormatUtils.decimalToNoFraction(updated.getPoints()));
        res.setZkstrkkekka(AttributeRegisterStatus.Complete.getStatusCode());

        return res;
    }
}
