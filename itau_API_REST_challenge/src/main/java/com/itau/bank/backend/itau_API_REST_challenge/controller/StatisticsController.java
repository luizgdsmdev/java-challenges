package com.itau.bank.backend.itau_API_REST_challenge.controller;


import com.itau.bank.backend.itau_API_REST_challenge.dto.StatisticsResponse;
import com.itau.bank.backend.itau_API_REST_challenge.service.TransactionalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.DoubleSummaryStatistics;

@RestController
@RequestMapping("/estatistica")
public class StatisticsController {
    private final TransactionalService transactionalService;

    public StatisticsController(TransactionalService transactionalService) {
        this.transactionalService = transactionalService;
    }

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics(){

        DoubleSummaryStatistics statistics = transactionalService.getStatistics();
        return ResponseEntity.status(HttpStatus.OK).body(new StatisticsResponse(statistics));
    }
}
