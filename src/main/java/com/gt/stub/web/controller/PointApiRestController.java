package com.gt.stub.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.stub.web.process.ProcessStatus;
import com.gt.stub.web.process.RProcessExecutor;
import com.gt.stub.web.process.RxxxRes;
import com.gt.stub.web.utils.AppSpecificUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by noah on 2017. 5. 20..
 */
@RestController
@RequestMapping("/gt")
public class PointApiRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(PointApiRestController.class);

    private ObjectMapper upperCaseObjectMapper;
    private List<RProcessExecutor> executors;

    PointApiRestController(List<RProcessExecutor> executors, ObjectMapper upperCaseObjectMapper) {
        this.upperCaseObjectMapper = upperCaseObjectMapper;
        this.executors = executors;
    }

    @PostMapping(path = {"/{processNo}", "/{processNo}/{ip}"}, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity process(@PathVariable String processNo, @PathVariable(required = false) String ip, @RequestParam Map<String, String> reqParams) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("RECEIVE REQUEST [%s][%s] : %s", processNo, ip, AppSpecificUtils.mapToQuery(reqParams)));
        }

        Optional<RProcessExecutor> filteredExecutor = executors.stream()
                .filter(executor -> processNo.equals(executor.processName().getProcessNo()))
                .findFirst();

        if (!filteredExecutor.isPresent()) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Not found process  pid= [%s], ip = [%s]", processNo, ip));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RProcessExecutor rProcessExecutor = filteredExecutor.get();

        RxxxRes result;
        try {
            result = rProcessExecutor.execute(upperCaseObjectMapper.convertValue(reqParams, rProcessExecutor.getRequestType()));
        } catch (Exception ex) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Server Error...  pid= [%s], ip = [%s]", processNo, ip), ex);
            }

            try {
                result = (RxxxRes) rProcessExecutor.getResponseType().newInstance();
                result.setSts(ProcessStatus.SystemError.getStatusCode());

            } catch (Exception e) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(String.format("Unexpected Error...  pid= [%s], ip = [%s]", processNo, ip) , e);
                }
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
        }
        String body = AppSpecificUtils.mapToQuery(upperCaseObjectMapper.convertValue(result, HashMap.class));
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("SEND RESPONSE   [%s][%s] : %s", processNo, ip, body));
        }
        return ResponseEntity.ok(body);
    }

}
