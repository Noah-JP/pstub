package com.gt.stub.web.process.r011;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R011ProcessExecutor extends AbstractRProcessExecutor<R011Req, R011Res> {

    private CardService cardService;

    R011ProcessExecutor(CardService cardService) {
        super();
        this.cardService = cardService;
    }

    @Override
    public RProcess processName() {
        return RProcess.UpdateLinkage;
    }

    @Override
    protected R011Res process(R011Req r011Req) {
        R011Res res = new R011Res();
        res.setSts(ProcessStatus.Success.getStatusCode());

        String token = r011Req.getToken();

        String updateTargetCardNo = r011Req.getKaiinno();
        if (StringUtils.isEmpty(updateTargetCardNo) || StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }


        CardInfo updateTarget = cardService.get(updateTargetCardNo);
        if (Objects.isNull(updateTarget)) {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        if (RegStatus.Blank.equals(updateTarget.getRegStatus())) {
            res.setSts(ProcessStatus.Blank.getStatusCode());
            return res;
        }

        if (!cardService.existToken(token)) {
            res.setSts(ProcessStatus.NegativeToken.getStatusCode()); // TODO check invalidToken
            return res;
        }

        res.setCardsbt(calculateTypeNo(updateTarget.getCardNo()));
        res.setToken(updateTarget.getToken());
        res.setHimopt(FormatUtils.decimalToNoFraction(updateTarget.getPoints()));

        return res;
    }
}
