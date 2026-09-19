
package com.sentinelflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentinelflow.dto.AccountResponse;
import com.sentinelflow.models.Account;
import com.sentinelflow.repositories.AccountRepository;
import com.sentinelflow.service.AccountService;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

	@Autowired
    private  AccountService accountService; 
	
    @GetMapping("/account/{id}")
    public List<AccountResponse> getAllAccounts(@PathVariable("id") Long id) {
    	List<AccountResponse> response=accountService.getAccountsByCustomerId(id);
		return response;
    }
    
}
