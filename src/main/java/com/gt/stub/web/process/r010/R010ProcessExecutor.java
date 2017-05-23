package com.gt.stub.web.process.r010;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.process.r014.R014Req;
import com.gt.stub.web.process.r014.R014Res;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R010ProcessExecutor extends AbstractRProcessExecutor<R010Req,R010Res> {

    private CardService cardService;

    R010ProcessExecutor(CardService cardService) {
        super();
        this.cardService = cardService;
    }

    @Override
    public RProcess processName() {
        return RProcess.Linkage;
    }

    @Override
    protected R010Res process(R010Req r010Req) {

        R010Res res = new R010Res();
        res.setSts(ProcessStatus.Success.getStatusCode());

        String cardNo = r010Req.getKaiinno();

        if(StringUtils.isEmpty(cardNo)){
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }

        CardInfo card = cardService.get(cardNo);
        if(Objects.isNull(card)){
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        if(RegStatus.Blank.equals(card.getRegStatus())){
            res.setSts(ProcessStatus.Blank.getStatusCode());
            return res;
        }

        res.setToken(card.getToken());
        res.setCardsbt(calculateTypeNo(card.getCardNo()));
        res.setHimopt(FormatUtils.decimalToNoFraction(BigDecimal.ZERO));

        return res;
    }
}
