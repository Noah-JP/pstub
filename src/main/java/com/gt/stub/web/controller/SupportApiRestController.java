package com.gt.stub.web.controller;

import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.persistence.entity.Owner;
import com.gt.stub.persistence.enums.IssuedBy;
import com.gt.stub.persistence.enums.RegStatus;
import com.gt.stub.web.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * Created by noah on 2017. 5. 20..
 */
@RestController
@RequestMapping("/support")
public class SupportApiRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupportApiRestController.class);

    private CardService cardService;

    SupportApiRestController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create/{type}/{amount}/{issuedBy}")
    public ResponseEntity create(@PathVariable String type, @PathVariable String amount, @PathVariable(required = false) String issuedBy) {


        if (StringUtils.isEmpty(type) || Objects.isNull(RegStatus.valueOf(type)) || !isNumeric(amount)) {

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Illegal argument type [%s], amount [%s]", type, amount));
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        RegStatus typeStatus = RegStatus.valueOf(type);

        IssuedBy issuedByStatus = IssuedBy.ETC;
        if (RegStatus.Registered.equals(typeStatus) && IssuedBy.EC.name().equals(issuedBy)) {
            issuedByStatus = IssuedBy.EC;
        }

        try {

            int loop = Integer.valueOf(amount);

            for (int i = 0; i < loop; i++) {

                CardInfo createdCardInfo = cardService.createBlank(cardService.nextCardNo());
                createdCardInfo.setRegStatus(typeStatus);
                createdCardInfo.setIssuedBy(issuedByStatus);

                Owner owner = null;
                if (!RegStatus.Blank.equals(typeStatus)) {
                    owner = new Owner();
                    owner.setCardNo(createdCardInfo.getCardNo());
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

                if (RegStatus.Registered.equals(typeStatus)) {
                    owner.setPassword("gt".concat(String.valueOf(createdCardInfo.getCardNo())));
                    createdCardInfo.setMypageAuthenticated(true);
                }

                if (Objects.nonNull(owner)) {
                    createdCardInfo.setOwner(owner);
                }
                cardService.update(createdCardInfo);
            }


        } catch (Exception ex) {

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Unknown error.", ex);
            }

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

        return ResponseEntity.ok("Create Success .. !");
    }


    private boolean isNumeric(String target) {
        if (StringUtils.isEmpty(target)) {
            return false;
        } else {
            int sz = target.length();
            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(target.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}
