package com.itau.bank.backend.itau_API_REST_challenge.controller;
import com.itau.bank.backend.itau_API_REST_challenge.dto.TransactionalRequest;
import com.itau.bank.backend.itau_API_REST_challenge.model.Transaction;
import com.itau.bank.backend.itau_API_REST_challenge.service.TransactionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/transacao")
public class TransactionalController {

    private final TransactionalService transactionalService;

    public TransactionalController(TransactionalService transactionalService) {
        this.transactionalService = transactionalService;
    }


    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody @Valid TransactionalRequest requestBody){
        //Todo: adjust the response for invalid arguments (current being handled by the DTO with return 400 BAD_REQUEST
        //Should return also 422 unprocessableEntity

        if(requestBody.getDataHora().isAfter(OffsetDateTime.now())){
            return ResponseEntity.unprocessableEntity().build();
        }

        transactionalService.addTransaction(new Transaction(requestBody.getValor(), requestBody.getDataHora()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTransactions(){

        transactionalService.clearTransactions();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
