package com.gt.stub.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gt.stub.web.process.RProcessExecutor;
import com.gt.stub.web.utils.AppSpecificUtils;
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

    private ObjectMapper upperCaseObjectMapper;
    private List<RProcessExecutor> executors;

    PointApiRestController(List<RProcessExecutor> executors, ObjectMapper upperCaseObjectMapper) {
        this.upperCaseObjectMapper = upperCaseObjectMapper;
        this.executors = executors;
    }

    @PostMapping(path = "/{processNo}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity process(@PathVariable String processNo, @RequestParam Map<String, String> reqParams) {

        Optional<RProcessExecutor> filteredExecutor = executors.stream()
                .filter(executor -> processNo.equals(executor.processName().getProcessNo()))
                .findFirst();

        if (!filteredExecutor.isPresent()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        RProcessExecutor rProcessExecutor = filteredExecutor.get();

        Object result = rProcessExecutor.execute(upperCaseObjectMapper.convertValue(reqParams, rProcessExecutor.getRequestType()));

        String body = AppSpecificUtils.mapToQuery(upperCaseObjectMapper.convertValue(result, HashMap.class));

        return ResponseEntity.ok(body);
    }

}
