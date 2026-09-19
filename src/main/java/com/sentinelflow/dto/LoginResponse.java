package com.sentinelflow.dto;

public class LoginResponse {

    private boolean success;
    private String message;
    private String email;
    private Long customerId;
    public void setCustomerId(Long id) {
    	this.customerId=id;
    }
    public Long getCustomerId() {
    	return this.customerId;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}