package com.gt.stub.web.process.r014;

import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R014ProcessExecutor extends AbstractRProcessExecutor<R014Req,R014Res> {

    private CardService cardService;
    private TokenEncryptor encryptor;

    R014ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.TokenDecrypt;
    }

    @Override
    protected R014Res process(R014Req r014Req) {
        R014Res res = new R014Res();
        String token = r014Req.getToken();
        if (StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }

        if (cardService.existToken(token)) {
            res.setKaiinno(encryptor.decrypt(token));
            res.setSts(ProcessStatus.Success.getStatusCode());
        } else {
            res.setSts(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        return res;
    }
}
