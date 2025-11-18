package com.itau.bank.backend.itau_API_REST_challenge.service;
import com.itau.bank.backend.itau_API_REST_challenge.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class TransactionalService {


    private final Queue<Transaction> transactionsDB = new ConcurrentLinkedQueue<>();


    public Optional<Boolean> addTransaction(Transaction transaction){
            try{
                return Optional.of(transactionsDB.add(transaction));
            } catch (Exception e) {
                throw new RuntimeException("Error for adding a new transaction on addTransaction() method.", e);
            }
    }

    public void clearTransactions(){
        transactionsDB.clear();
    }

    public DoubleSummaryStatistics getStatistics(){
        OffsetDateTime currentDate = OffsetDateTime.now();
        return transactionsDB.stream()
                .filter(transaction ->
                                transaction.getDataHora()
                                .isAfter(currentDate
                                .minusSeconds(60)))
                .mapToDouble(Transaction::getValor)
                .summaryStatistics();
    }


}
