package com.neeraj.service;

import com.neeraj.domain.USER_ROLE;
import com.neeraj.request.LoginRequest;
import com.neeraj.response.AuthResponse;
import com.neeraj.response.SignupRequest;

public interface AuthService {
	
	void sendLoginOtp(String email , USER_ROLE role) throws Exception;
	
	String createUser(SignupRequest req) throws Exception;
	
	AuthResponse signing(LoginRequest req);

}
