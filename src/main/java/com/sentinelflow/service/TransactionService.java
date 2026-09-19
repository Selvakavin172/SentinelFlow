package com.sentinelflow.service;

import java.util.List;

import com.sentinelflow.dto.TransactionListResponse;
import com.sentinelflow.dto.TransactionRequest;
import com.sentinelflow.dto.TransactionResponse;
import com.sentinelflow.models.Transaction;

public interface TransactionService {

    TransactionResponse processTransaction(TransactionRequest request);
    List<TransactionListResponse> getAllTransactions();
}