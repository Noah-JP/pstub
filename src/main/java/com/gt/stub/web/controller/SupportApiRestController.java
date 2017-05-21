package com.gt.stub.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.stub.persistence.entity.CardInfo;
import com.gt.stub.web.data.ApiResultData;
import com.gt.stub.web.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by noah on 2017. 5. 20..
 */
@RestController
@RequestMapping("/support")
public class SupportApiRestController {

    public static final Logger LOGGER = LoggerFactory.getLogger(SupportApiRestController.class);

    private CardService cardService;
    private ObjectMapper objectMapper;

    SupportApiRestController(CardService cardService, ObjectMapper objectMapper) {
        this.cardService = cardService;
        this.objectMapper = objectMapper;
    }


    @PostMapping("/create")
    public ResponseEntity create(String cardNo) {
        ApiResultData result = new ApiResultData();

        try {

            CardInfo createdCardInfo = cardService.createBlank(cardNo);

            result.setSuccess(true);
            result.setMessage("Created.");
            result.setData(createdCardInfo);

        } catch (Exception ex) {
            LOGGER.error("Created fail.", ex);

            result.setSuccess(false);
            result.setMessage("Unknown error");

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }
        return ResponseEntity.ok(result);
    }
}
