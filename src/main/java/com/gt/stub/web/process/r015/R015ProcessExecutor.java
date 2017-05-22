package com.gt.stub.web.process.r015;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R015ProcessExecutor extends AbstractRProcessExecutor<R015Req, R015Res> {

    private static Logger LOGGER = LoggerFactory.getLogger(R015ProcessExecutor.class);

    private CardService cardService;
    private TokenEncryptor encryptor;

    @Override
    public RProcess processName() {
        return RProcess.ReissueToken;
    }

    @Override
    protected R015Res process(R015Req r015Req) {

        R015Res res = new R015Res();
        res.setPointid(POINT_ID);
        res.setSts(ProcessStatus.Success.getStatusCode());

        String token = r015Req.getToken();
        if (cardService.existToken(token)) {

            String cardNo = encryptor.decrypt(token);

            CardInfo card = cardService.get(cardNo);
            card.setToken(encryptor.encrypt(card.getCardNo()));

            CardInfo updated = cardService.update(card);
            res.setKaiinno(updated.getCardNo());
            res.setToken(updated.getToken());

        } else {
            LOGGER.error(String.format("No exist Token [%s]..", token));
            res.setSts(ProcessStatus.NoData.getStatusCode());
        }

        return res;
    }

    R015ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }


}
