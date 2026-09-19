package com.sentinelflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sentinelflow.dto.AccountResponse;


public interface AccountService {
	List<AccountResponse> getAccountsByCustomerId(Long id);
}
