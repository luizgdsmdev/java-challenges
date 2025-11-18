package com.itau.bank.backend.itau_API_REST_challenge.controller;
import com.itau.bank.backend.itau_API_REST_challenge.dto.TransactionalRequest;
import com.itau.bank.backend.itau_API_REST_challenge.exceptions.BusinessException;
import com.itau.bank.backend.itau_API_REST_challenge.exceptions.Records.ErrorResponse;
import com.itau.bank.backend.itau_API_REST_challenge.model.Transaction;
import com.itau.bank.backend.itau_API_REST_challenge.service.TransactionalService;
import com.itau.bank.backend.itau_API_REST_challenge.utils.validations.TransactionalControllerValidations;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/transacao")
public class TransactionalController {

    private final TransactionalService transactionalService;
    private final TransactionalControllerValidations validation;

    public TransactionalController(TransactionalService transactionalService, TransactionalControllerValidations validation) {
        this.transactionalService = transactionalService;
        this.validation = validation;
    }

    @PostMapping
    public ResponseEntity<Object> createTransaction(@RequestBody @Valid TransactionalRequest requestBody){
        validation.validateRequestBodyDataHora(requestBody);
        validation.validateRequestBodyValor(requestBody);

        Transaction transaction = new Transaction(requestBody.getValor(), requestBody.getDataHora());

        return transactionalService.addTransaction(transaction)
        .map(trs -> ResponseEntity.status(HttpStatus.CREATED).build())
        .orElseGet(() -> ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Bad Request",
                        "Invalid body information for transaction, varify the information.",
                        LocalDateTime.now()
                )));
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteTransactions(){

        transactionalService.clearTransactions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
