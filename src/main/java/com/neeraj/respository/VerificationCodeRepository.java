package com.neeraj.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neeraj.modal.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
	
	VerificationCode findByEmail(String email);
	
	VerificationCode findByOtp(String otp);

}
