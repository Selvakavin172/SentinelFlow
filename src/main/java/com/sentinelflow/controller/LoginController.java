package com.sentinelflow.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentinelflow.dto.LoginRequest;
import com.sentinelflow.dto.LoginResponse;
import com.sentinelflow.models.Customer;
import com.sentinelflow.repositories.CustomerRepository;
import com.sentinelflow.service.LoginService;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

	@Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {    	
       
    	LoginResponse response= loginService.validateUser(request);
    	if(response.isSuccess()) {
    		return ResponseEntity.status(200).body(response);
    	}
    	
        return ResponseEntity.status(401).body(response);
    }
}