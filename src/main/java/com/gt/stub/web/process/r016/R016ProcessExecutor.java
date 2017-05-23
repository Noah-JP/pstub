package com.gt.stub.web.process.r016;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R016ProcessExecutor extends AbstractRProcessExecutor<R016Req, R016Res> {

    private CardService cardService;
    private TokenEncryptor encryptor;

    R016ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.ReissueToken;
    }

    @Override
    protected R016Res process(R016Req r016Req) {

        R016Res res = new R016Res();
        res.setSts(ProcessStatus.Success.getStatusCode());
        res.setToken(r016Req.getToken());

        String token = r016Req.getToken();
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

        return res;
    }


}
