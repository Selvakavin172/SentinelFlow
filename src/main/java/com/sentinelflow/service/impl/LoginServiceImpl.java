package com.sentinelflow.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sentinelflow.dto.LoginRequest;
import com.sentinelflow.dto.LoginResponse;
import com.sentinelflow.models.Customer;
import com.sentinelflow.repositories.CustomerRepository;
import com.sentinelflow.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public LoginResponse validateUser(LoginRequest request) {
		Optional<Customer> customer = customerRepository.findByEmail(request.getEmail());

		LoginResponse response = new LoginResponse();
		if (customer.isPresent()) {
			response.setSuccess(true);
			response.setMessage("Login successful");
			response.setEmail(customer.get().getEmail());
			response.setCustomerId(customer.get().getCustomerId());
			return response;
		}

		response.setSuccess(false);
		response.setMessage("Email not registered");
		return response;
	}

}
