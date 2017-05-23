package com.gt.stub.web.process.r015;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R015ProcessExecutor extends AbstractRProcessExecutor<R015Req, R015Res> {

    private CardService cardService;
    private TokenEncryptor encryptor;


    R015ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.ReissueToken;
    }

    @Override
    protected R015Res process(R015Req r015Req) {

        R015Res res = new R015Res();
        res.setSts(ProcessStatus.Success.getStatusCode());

        String token = r015Req.getToken();
        if (StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }

        if (!cardService.existToken(token)) {
            res.setSts(ProcessStatus.NegativeToken.getStatusCode());
            return res;
        }

        String cardNo = encryptor.decrypt(token);
        CardInfo card = cardService.get(cardNo);
        if (Objects.isNull(card)) {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }
        card.setToken(encryptor.encrypt(card.getCardNo()));

        CardInfo updated = cardService.update(card);
        res.setToken(updated.getToken());

        return res;

    }

}
