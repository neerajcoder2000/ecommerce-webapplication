package com.neeraj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neeraj.domain.USER_ROLE;
import com.neeraj.modal.User;
import com.neeraj.modal.VerificationCode;
import com.neeraj.request.LoginOtpRequest;
import com.neeraj.request.LoginRequest;
import com.neeraj.response.ApiResponse;
import com.neeraj.response.AuthResponse;
import com.neeraj.response.SignupRequest;
import com.neeraj.respository.UserRepository;
import com.neeraj.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	private final UserRepository userRepository;
	
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception{
		
		String jwt = authService.createUser(req);
		
		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setMessage("register success");
		response.setRole(USER_ROLE.ROLE_CUSTOMER);
		
		return ResponseEntity.ok(response);
	}

	
	@PostMapping("/sent/login-signup-otp")
	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest req) throws Exception{
		
		authService.sendLoginOtp(req.getEmail(), req.getRole());
		
		ApiResponse response = new ApiResponse(); 
		
		response.setMessage("otp sent successfully");
		
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/signing")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) {
		
		AuthResponse authResponse =authService.signing(req);
		
		return ResponseEntity.ok(authResponse);
	}
}
