package com.sentinelflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sentinelflow.dto.AccountResponse;
import com.sentinelflow.models.Account;
import com.sentinelflow.repositories.AccountRepository;
import com.sentinelflow.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public List<AccountResponse> getAccountsByCustomerId(Long id) {
		return accountRepository.findByCustomerId(id) .stream() .map(this::toResponse) .toList();
	}
	private AccountResponse toResponse(Account account) { AccountResponse response = new AccountResponse(); response.setAccountId(account.getAccountId()); response.setAccountNumber(account.getAccountNumber()); response.setAccountType(account.getAccountType()); response.setCurrency(account.getCurrency()); response.setCurrentBalance(account.getCurrentBalance()); return response; }
}
