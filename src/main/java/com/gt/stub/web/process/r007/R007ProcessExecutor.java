package com.gt.stub.web.process.r007;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.PassCheck;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 22..
 */
@Component
public class R007ProcessExecutor extends AbstractRProcessExecutor<R007Req, R007Res> {

    private CardService cardService;
    private TokenEncryptor encryptor;

    R007ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.MypageAuthentication;
    }

    @Override
    protected R007Res process(R007Req r007Req) {

        R007Res res = new R007Res();
        res.setSts(ProcessStatus.Success.getStatusCode());

        String token = r007Req.getToken();
        if (StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }


        String cardNo;
        if (cardService.existToken(token)) {
            cardNo = encryptor.decrypt(token);
        } else {
            res.setSts(ProcessStatus.InvalidToken.getStatusCode());
            return res;
        }

        CardInfo card = cardService.get(cardNo);
        if (Objects.isNull(card)) {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        if (card.getTokenExpired()) {
            res.setSts(ProcessStatus.ExpiredToken.getStatusCode());
            return res;
        }

        if (RegStatus.Blank.equals(card.getRegStatus())) {
            res.setSts(ProcessStatus.Blank.getStatusCode());
            return res;
        }

        if (RegStatus.Unregistered.equals(card.getRegStatus())) {
            res.setSts(ProcessStatus.Unregistered.getStatusCode());
            return res;
        }

        Owner owner = card.getOwner();
        Boolean matched = owner.getPassword().equals(r007Req.getAuthkey());
        res.setPasschk(matched ? PassCheck.Matched.getPassCheckCode() : PassCheck.Unmatched.getPassCheckCode());

        card.setMypageAuthenticated(true);
        cardService.update(card);

        return res;
    }
}
