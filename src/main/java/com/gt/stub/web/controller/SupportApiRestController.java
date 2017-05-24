package com.gt.stub.web.controller;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.holder.CardProperties;
import com.gt.stub.web.service.CardService;
import com.gt.stub.web.utils.FormatUtils;
import com.gt.stub.web.utils.TokenEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by noah on 2017. 5. 20..
 */
@RestController
@RequestMapping("/support")
public class SupportApiRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportApiRestController.class);
    public static final DecimalFormat CARD_SEQ_FORMAT = new DecimalFormat("000");
    public static final DateTimeFormatter CARD_NO_MMDD = DateTimeFormatter.ofPattern("MMdd");

    @Value("${stub.create_amount}")
    private Integer createAmount;

    private CardService cardService;
    private CardProperties cardProperties;
    private TokenEncryptor encryptor;

    SupportApiRestController(CardService cardService, TokenEncryptor encryptor, CardProperties cardProperties) {
        this.cardService = cardService;
        this.cardProperties = cardProperties;
        this.encryptor = encryptor;
    }

    @PostMapping("/create/{issue}/{type}")
    public ResponseEntity create(@PathVariable String issue, @PathVariable String type) {


        if (!Issue.isContain(issue) || !CardType.isContain(type)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Illegal argument issue [%s], type [%s]", issue, type));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Issue issueType = Issue.valueOf(issue);
        CardType cardType = CardType.valueOf(type);

        try {
            LocalDateTime now = LocalDateTime.now();
            Stream.of(CardNoPrefix.values()).forEach(cardNoPrefix -> {
                int loop = createAmount;
                StringBuilder cardPreNoBuilder = new StringBuilder();
                cardPreNoBuilder.append(cardNoPrefix.getPrefix());
                cardPreNoBuilder.append(CARD_NO_MMDD.format(now));
                cardPreNoBuilder.append(issueType.getClassification());
                cardPreNoBuilder.append(issueType.getRole());
                cardPreNoBuilder.append(cardType.getTypeCode());
                String password = cardProperties.getPassword();
                String cardPreNo = cardPreNoBuilder.toString();

                for (int i = 1; i <= loop; i++) {

                    String cardNo = cardPreNo.concat(CARD_SEQ_FORMAT.format(new BigDecimal(i)));

                    if (cardNo.length() == 16) {
                        CardInfo card = cardService.createBlank(cardNo);

                        Owner owner = null;
                        if (!CardType.Blank_Unauth.equals(cardType)) {
                            owner = new Owner();
                            owner.setCardNo(card.getCardNo());
                            owner.setSex("1");
                            owner.setTelNo("08012345678");
                            owner.setBirthday("19900101");
                            owner.setPostNo("1234567");
                            owner.setAddress1("東京都");
                            owner.setAddress2("１番地");
                            owner.setNameSei("山田");
                            owner.setNameMei("太郎");
                            owner.setNameSeiKana("ヤマダ");
                            owner.setNameMeiKana("タロウ");
                            owner.setEmail("nttd-test@pt.mitsuifudosan.co.jp");
                        }

                        if (CardType.Blank_Unauth_Withdraw.equals(cardType)) {
                            card.setWithdraw(true);
                        }

                        if (CardType.Unregistered_Unauth.equals(cardType) //
                                || CardType.Unregistered_Unauth_Withdraw.equals(cardType)) {
                            card.setRegStatus(RegStatus.Unregistered);

                            if (CardType.Unregistered_Unauth_Withdraw.equals(cardType)) {
                                card.setWithdraw(true);
                            }
                        }


                        if (CardType.Registered_Auth.equals(cardType) //
                                || CardType.Registered_Unauth.equals(cardType) //
                                || CardType.Registered_Auth_Withdraw.equals(cardType) //
                                || CardType.Registered_Unauth_Withdraw.equals(cardType) //
                                ) {
                            card.setRegStatus(RegStatus.Registered);
                            owner.setPassword(password);

                            if (CardType.Registered_Auth.equals(cardType) //
                                    || CardType.Registered_Auth_Withdraw.equals(cardType)) {
                                card.setMypageAuthenticated(true);
                            }

                            if (CardType.Registered_Auth_Withdraw.equals(cardType) //
                                    || CardType.Registered_Unauth_Withdraw.equals(cardType)) {
                                card.setWithdraw(true);
                            }
                        }

                        if (Objects.nonNull(owner)) {
                            card.setOwner(owner);
                        }
                        cardService.update(card);
                    } else {
                        throw new IllegalArgumentException(String.format("cardNo length must be 16digits. cardNo = [%s]", cardNo));
                    }
                }
            });

        } catch (Exception ex) {

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Unknown error.", ex);
            }

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

        return ResponseEntity.ok("Create Success .. !");
    }


    enum CardNoPrefix {
        Digits_12("0000"),
        Digits_16("1234");

        private String prefix;

        CardNoPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    enum Issue {
        Nttd_Test("1", "1"), //
        Nttd_Etc("1", "5"), //
        Nttd_Customer("1", "9"), //
        Ec_MF("5", "7"), //
        Ec_Manager("5", "3"); //

        private String classification;
        private String role;

        Issue(String classification, String role) {
            this.classification = classification;
            this.role = role;
        }

        public String getClassification() {
            return classification;
        }

        public String getRole() {
            return role;
        }

        public static boolean isContain(String statusName) {
            return Stream.of(Issue.values())
                    .anyMatch(status -> status.name().equals(statusName));
        }
    }

    enum CardType {
        Registered_Auth("070"), //
        Registered_Unauth("071"), //
        Unregistered_Unauth("061"), //
        Blank_Unauth("051"),
        Registered_Auth_Withdraw("170"), //
        Registered_Unauth_Withdraw("171"), //
        Unregistered_Unauth_Withdraw("161"), //
        Blank_Unauth_Withdraw("151");

        private String typeCode;

        CardType(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public static boolean isContain(String statusName) {
            return Stream.of(CardType.values())
                    .anyMatch(status -> status.name().equals(statusName));
        }
    }


}
