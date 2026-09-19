
package com.sentinelflow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentinelflow.dto.TransactionListResponse;
import com.sentinelflow.dto.TransactionRequest;
import com.sentinelflow.dto.TransactionResponse;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.TransactionRepository;
import com.sentinelflow.service.TransactionService;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest request) {

        TransactionResponse response =
                transactionService.processTransaction(request);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/all-transactions")
    public ResponseEntity<List<TransactionListResponse>> getAllTransactions() {

       
    	List<TransactionListResponse> response= transactionService.getAllTransactions();
        return ResponseEntity.ok(response);
    }


}
