package com.gt.stub.web.process.r005;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.web.process.AbstractRProcessExecutor;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcess;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Created by noah on 2017. 5. 21..
 */
@Component
public class R005ProcessExecutor extends AbstractRProcessExecutor<R005Req, R005Res> {
    private CardService cardService;
    private TokenEncryptor encryptor;

    R005ProcessExecutor(CardService cardService, TokenEncryptor encryptor) {
        super();
        this.cardService = cardService;
        this.encryptor = encryptor;
    }

    @Override
    public RProcess processName() {
        return RProcess.MemberCheck;
    }

    @Override
    protected R005Res process(R005Req req) {

        String cardNo = req.getKaiinno();
        String token = req.getToken();


        R005Res res = new R005Res();
        res.setKaiinno(cardNo);
        res.setToken(token);
        if (StringUtils.isEmpty(cardNo) && StringUtils.isEmpty(token)) {
            res.setSts(ProcessStatus.NegativeRequest.getStatusCode());
            return res;
        }


        CardInfo card;
        res.setSts(ProcessStatus.Success.getStatusCode());
        try {

            if (!StringUtils.isEmpty(token)) {
                if (cardService.existToken(token)) {
                    cardNo = encryptor.decrypt(token);
                } else {
                    res.setKaiinchk(ProcessStatus.InvalidToken.getStatusCode());
                    return res;
                }
            }

            card = cardService.get(cardNo);

        } catch (Exception ex) {

            res.setSts(ProcessStatus.SystemError.getStatusCode());

            return res;
        }

        if (Objects.isNull(card)) {
            res.setKaiinchk(ProcessStatus.NoData.getStatusCode());
            return res;
        }

        res.setTrksts(card.getRegStatus().getStatusNo());

        res.setKaiinchk(ProcessStatus.Success.getStatusCode());
        res.setPasschk(card.getMypageAuthenticated() ? "0" : "1"); // TODO : validator
        res.setGtchk(card.getIssuedBy().getIssueNo());

        res.setPointzan(FormatUtils.decimalToNoFraction(card.getPoints()));
        res.setPointts(FormatUtils.dateTo14digits(card.getIssuedDateOfPoints()));

        res.setPointexpzan(FormatUtils.decimalToNoFraction(card.getExpiringPoints()));
        res.setPointlimit(FormatUtils.dateTo8digits(card.getExpiringDateOfPoints()));

        res.setMsprnk(String.valueOf(card.getRank()));

        return res;
    }
}
