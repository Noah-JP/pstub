package com.gt.stub.web.process.r008;

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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by noah on 2017. 5. 22..
 */
@Component
public class R008ProcessExecutor extends AbstractRProcessExecutor<R008Req, R008Res> {

    private CardService cardService;

    R008ProcessExecutor(CardService cardService) {
        super();
        this.cardService = cardService;
    }

    @Override
    public RProcess processName() {
        return RProcess.AttributeRegistration;
    }

    @Override
    protected R008Res process(R008Req r008Req) {


        R008Res res = new R008Res();
        res.setSts(ProcessStatus.Success.getStatusCode());
        res.setZkstrkkekka(AttributeRegisterStatus.Incomplete.getStatusCode());

        String cardNo = r008Req.getKaiinno();
        if (StringUtils.isEmpty(cardNo)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }

        CardInfo card = cardService.get(cardNo);
        if(Objects.isNull(card)){
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        if(RegStatus.Unregistered.equals(card.getRegStatus())){
            res.setSts(ProcessStatus.Unregistered.getStatusCode());
            return res;
        }

        if(RegStatus.Registered.equals(card.getRegStatus())){
            res.setSts(ProcessStatus.Registered.getStatusCode());
            return res;
        }
        
        if(IssuedBy.EC.equals(card.getIssuedBy())){
            res.setSts(ProcessStatus.IssuedEc.getStatusCode());
            return res;
        }


        Owner owner = new Owner();
        {
            owner.setCardNo(cardNo);
            owner.setNameSei(r008Req.getMembname1());
            owner.setNameMei(r008Req.getMembname2());
            owner.setNameSeiKana(r008Req.getMembnamekn1());
            owner.setNameMeiKana(r008Req.getMembnamekn2());
            owner.setSex(r008Req.getSex());
            owner.setBirthday(r008Req.getBirthday());
            owner.setEmail(r008Req.getEmail1());
            owner.setTelNo(r008Req.getTelno());
            owner.setPostNo(r008Req.getPostno());
            owner.setAddress1(r008Req.getAddress1());
            owner.setAddress2(r008Req.getAddress2());
            owner.setAddress3(r008Req.getAddress3());
        }

        card.setRegStatus(RegStatus.Unregistered);
        card.setMypageAuthenticated(false);
        card.setOwner(owner);

        CardInfo updated = cardService.update(card);
        
        res.setToken(updated.getToken());
        res.setCardsbt(calculateTypeNo(updated.getCardNo()));
        res.setHimopt(FormatUtils.decimalToNoFraction(BigDecimal.ZERO));
        res.setZkstrkkekka(AttributeRegisterStatus.Complete.getStatusCode());
        

        return res;
    }
}
