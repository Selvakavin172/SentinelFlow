
package com.sentinelflow.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentinelflow.dto.AccountResponse;
import com.sentinelflow.models.Account;
import com.sentinelflow.repositories.AccountRepository;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
    	return accountRepository.findAll() .stream() .map(this::toResponse) .toList();
    }
    private AccountResponse toResponse(Account account) { AccountResponse response = new AccountResponse(); response.setAccountId(account.getAccountId()); response.setAccountNumber(account.getAccountNumber()); response.setAccountType(account.getAccountType()); response.setCurrency(account.getCurrency()); response.setCurrentBalance(account.getCurrentBalance()); return response; }
}
