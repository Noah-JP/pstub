package com.gt.stub.web.service.impl;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.persistence.repository.CardInfoCURDRepository;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.TokenEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by noah on 2017. 5. 20..
 */
@Service
public class CardServiceImpl implements CardService {

    private CardInfoCURDRepository cardRepository;
    private TokenEncryptor encryptor;

    CardServiceImpl(CardInfoCURDRepository cardRepository, TokenEncryptor encryptor) {
        this.cardRepository = cardRepository;
        this.encryptor = encryptor;
    }

    @Override
    public CardInfo createBlank(String cardNo) {
        LocalDateTime now = LocalDateTime.now();

        CardInfo cardInfo = new CardInfo();

        cardInfo.setCardNo(cardNo);
        cardInfo.setToken(encryptor.encrypt(cardNo));

        cardInfo.setIssuedBy(IssuedBy.ETC);
        cardInfo.setRegStatus(RegStatus.Blank);

        cardInfo.setPoints(BigDecimal.ZERO);
        cardInfo.setIssuedDateOfPoints(now);

        cardInfo.setExpiringPoints(BigDecimal.ZERO);
        cardInfo.setExpiringDateOfPoints(now);

        cardInfo.setRank(Integer.valueOf(0));

        cardInfo.setTokenExpired(false);
        cardInfo.setMypageAuthenticated(false);

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
    public CardInfo addOwner(String cardNo, Owner owner) {

        CardInfo card = cardRepository.findOne(cardNo);

        if (Objects.nonNull(card)) {
            owner.setCardInfo(card);
            card.setOwner(owner);
            return cardRepository.save(card);
        }
        return null;
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
        return String.format("%016d", Integer.valueOf(maxCardNo));
    }
}
