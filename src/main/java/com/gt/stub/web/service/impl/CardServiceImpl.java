package com.gt.stub.web.service.impl;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.persistence.repository.CardInfoCRUDRepository;
import com.gt.stub.web.holder.CardProperties;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by noah on 2017. 5. 20..
 */
@Service
public class CardServiceImpl implements CardService {

    public static final DecimalFormat CARD_NO_FORMAT = new DecimalFormat("0000000000000000");
    private CardInfoCRUDRepository cardRepository;
    private TokenEncryptor encryptor;
    private CardProperties cardProperties;

    CardServiceImpl(CardInfoCRUDRepository cardRepository, TokenEncryptor encryptor, CardProperties cardProperties) {
        this.cardRepository = cardRepository;
        this.encryptor = encryptor;
        this.cardProperties = cardProperties;
    }

    @Override
    public CardInfo createBlank(String cardNo) {


        CardInfo cardInfo = new CardInfo();

        cardInfo.setCardNo(cardNo);
        cardInfo.setToken(encryptor.encrypt(cardNo));

        cardInfo.setIssuedBy(IssuedBy.ETC);
        cardInfo.setRegStatus(RegStatus.Blank);

        CardProperties.Point point = cardProperties.getPoint();
        LocalDateTime now = LocalDateTime.now();
        cardInfo.setIssuedDateOfPoints(now);
        cardInfo.setPoints(new BigDecimal(point.getAmount()));

        LocalDateTime expirationDate = LocalDateTime.parse(point.getExpirationDate(), FormatUtils.DATE_YYYYMMDDHHMMSS);
        cardInfo.setExpiringDateOfPoints(expirationDate);
        cardInfo.setExpiringPoints(new BigDecimal(point.getExpirationAmount()));

        cardInfo.setRank(Integer.valueOf(0));

        cardInfo.setTokenExpired(false);
        cardInfo.setMypageAuthenticated(false);
        cardInfo.setWithdraw(false);

        return cardRepository.save(cardInfo);
    }

    @Override
    public CardInfo createBy(String cardNo, CardInfo createData) {

        createData.setCardNo(cardNo);

        return cardRepository.save(createData);
    }

    @Override
    public CardInfo update(CardInfo updateData) {
        return cardRepository.save(updateData);
    }

    @Override
    public void delete(String cardNo) {
        cardRepository.delete(cardNo);
    }

    @Override
    public CardInfo get(String cardNo) {
        return cardRepository.findOne(cardNo);
    }

    @Override
    public boolean existToken(String token) {
        return Objects.nonNull(cardRepository.findByToken(token));
    }

    @Override
    public String nextCardNo() {
        String maxCardNo = cardRepository.findMaxCardNo();
        if (StringUtils.isEmpty(maxCardNo)) {
            maxCardNo = "0";//TODO provider
        }
        BigDecimal nextCardNo = new BigDecimal(maxCardNo).add(BigDecimal.ONE);
        return CARD_NO_FORMAT.format(nextCardNo);
    }
}
