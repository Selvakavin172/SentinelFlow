package com.sentinelflow.service;

import com.sentinelflow.dto.LoginRequest;
import com.sentinelflow.dto.LoginResponse;

public interface LoginService {

	LoginResponse validateUser(LoginRequest request);
	
}
