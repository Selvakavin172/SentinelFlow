package com.sentinelflow.service;

import com.sentinelflow.dto.TransactionRequest;
import com.sentinelflow.dto.TransactionResponse;
import com.sentinelflow.models.Transaction;

public interface TransactionService {

    TransactionResponse processTransaction(TransactionRequest request);
}