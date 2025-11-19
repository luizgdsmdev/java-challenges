package com.itau.bank.backend.itau_API_REST_challenge.controller;


import com.itau.bank.backend.itau_API_REST_challenge.dto.StatisticsResponse;
import com.itau.bank.backend.itau_API_REST_challenge.exceptions.Records.ErrorResponse;
import com.itau.bank.backend.itau_API_REST_challenge.service.TransactionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;

@RestController
@RequestMapping("/estatistica")
public class StatisticsController {
    private final TransactionalService transactionalService;

    public StatisticsController(TransactionalService transactionalService) {
        this.transactionalService = transactionalService;
    }

    @GetMapping
    public ResponseEntity<Object> getStatistics(){

        return transactionalService.getStatistics()
        .map(Statistics -> ResponseEntity.status(HttpStatus.OK).body((Object) Statistics))
        .orElseGet(() -> ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(
                "Service unavailable",
                "Something went wrong with your request for deletion, please try again later.",
                LocalDateTime.now()
        )));
    }
}
