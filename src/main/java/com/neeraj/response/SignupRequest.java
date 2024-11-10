package com.neeraj.response;

import lombok.Data;

@Data
public class SignupRequest {
	
	private String email;
	private String fullName;
	private String otp;

}
