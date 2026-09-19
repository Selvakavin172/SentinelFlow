package com.sentinelflow.service.impl;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sentinelflow.aml.rule.AmlDetectionEngine;
import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.dto.TransactionRequest;
import com.sentinelflow.dto.TransactionResponse;
import com.sentinelflow.models.Account;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.AccountRepository;
import com.sentinelflow.repositories.TransactionRepository;
import com.sentinelflow.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AmlDetectionEngine amlDetectionEngine;
    private final AlertServiceImpl alertService;
    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            AmlDetectionEngine amlDetectionEngine, AlertServiceImpl alertService) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.amlDetectionEngine = amlDetectionEngine;
        this.alertService = alertService;
    }

    @Override
    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {

        Account account = accountRepository
                .findById(request.getAccountId())
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setCounterpartyAccount(
                request.getCounterpartyAccount());
        transaction.setCounterpartyName(
                request.getCounterpartyName());
        transaction.setTransactionType(
                request.getTransactionType());
        transaction.setAmount(
                request.getAmount());
        transaction.setCurrency(
                request.getCurrency());
        transaction.setTransactionDatetime(
                request.getTransactionDatetime());
        transaction.setCountryCode(
                request.getCountryCode());
        transaction.setChannel(
                request.getChannel());
        transaction.setMerchantCategory(
                request.getMerchantCategory());
        transaction.setNarration(
                request.getNarration());

       
        transaction.setAmountInr(request.getAmount());

       
        List<RuleResult> results =
                amlDetectionEngine.evaluate(transaction);

       
        int riskScore = results.stream()
                .mapToInt(RuleResult::getRiskScore)
                .sum();


        if (riskScore >= 70) {

            transaction.setStatus("HOLD");
            transaction.setIsHighRisk(true);

        } else if (riskScore > 0) {

            transaction.setStatus("SUSPICIOUS");
            transaction.setIsHighRisk(true);

        } else {

            transaction.setStatus("COMPLETED");
            transaction.setIsHighRisk(false);
            updateAccountBalance(account, transaction);
        }
        Transaction savedTransaction = transactionRepository.save(transaction);
        for (RuleResult result : results) {

            alertService.createAlert(
                    savedTransaction,
                    result);
        }

        return buildTransactionResponse(savedTransaction);
    }
    private TransactionResponse buildTransactionResponse(
            Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setTransactionId(
                transaction.getTransactionId());

        response.setAccountId(
                transaction.getAccount().getAccountId());

        response.setTransactionType(
                transaction.getTransactionType());

        response.setAmount(
                transaction.getAmount());

        response.setCurrency(
                transaction.getCurrency());

        response.setAmountInr(
                transaction.getAmountInr());

        response.setTransactionDatetime(
                transaction.getTransactionDatetime());

        response.setCountryCode(
                transaction.getCountryCode());

        response.setChannel(
                transaction.getChannel());

        response.setStatus(
                transaction.getStatus());

        response.setIsHighRisk(
                transaction.getIsHighRisk());

        return response;
    }
    private void updateAccountBalance(
            Account account,
            Transaction transaction) {

        BigDecimal currentBalance = account.getCurrentBalance();
        BigDecimal amount = transaction.getAmountInr();

        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid transaction amount");
        }

        String transactionType = transaction.getTransactionType();

        if ("CREDIT".equalsIgnoreCase(transactionType)) {

            // Money comes into the account
            currentBalance = currentBalance.add(amount);

        } else if ("DEBIT".equalsIgnoreCase(transactionType)) {

            // Money goes out of the account
            if (currentBalance.compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient account balance");
            }

            currentBalance = currentBalance.subtract(amount);

        } else {

            throw new RuntimeException(
                    "Invalid transaction type: " + transactionType);
        }

        account.setCurrentBalance(currentBalance);

        accountRepository.save(account);
    }

}