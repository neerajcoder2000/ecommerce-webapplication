package com.neeraj.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neeraj.domain.AccountStatus;
import com.neeraj.modal.Seller;
import com.neeraj.modal.SellerReport;
import com.neeraj.modal.VerificationCode;
import com.neeraj.request.LoginRequest;
import com.neeraj.response.ApiResponse;
import com.neeraj.response.AuthResponse;
import com.neeraj.respository.VerificationCodeRepository;
import com.neeraj.service.AuthService;
import com.neeraj.service.EmailService;
import com.neeraj.service.SellerService;
import com.neeraj.utils.OtpUtil;

import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
	
	private final SellerService sellerService;
	
	private final VerificationCodeRepository verificationCodeRepository;
	
	private final AuthService authService;
	
	private final EmailService emailService;
	
	
	
	@PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception{
    	
    	String otp = req.getOtp();
    	String email = req.getEmail();
    	    	
    	req.setEmail("seller_"+email);
    	AuthResponse authResponse = authService.signing(req);
    	
		return ResponseEntity.ok(authResponse);
    	
    }
	


	
	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt ) throws Exception {
		
		Seller seller = sellerService.getSellerProfile(jwt);
		
		return new ResponseEntity<Seller>(seller, HttpStatus.OK);
	}
	
	
	
	@PatchMapping("/verify/{otp}")
	public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception{
		
		VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
		
		if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			
			throw new Exception("wrong otp");
			
		}
		
		Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
			
		return new ResponseEntity<Seller>(seller, HttpStatus.OK);
		
	}
	
	@PostMapping("/createseller")
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception{
		
		Seller savedSeller = sellerService.createSeller(seller);
		
		String otp = OtpUtil.generateOtp();
		 
		 VerificationCode verificationCode = new VerificationCode();
	
		 verificationCode.setOtp(otp);
		 verificationCode.setEmail(seller.getEmail());
		 verificationCodeRepository.save(verificationCode);
		 
		 String subject = "Neeraj Shop Email Verification Code ";
		 String text = "Welcome to Neeraj Shop, verify your account using this link ";
		 String frontend_url ="http://localhost:3000/verify-seller";
		 
		 emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
		
		return new ResponseEntity<Seller>(savedSeller, HttpStatus.CREATED);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception{
		
	    Seller seller =	sellerService.getSellerById(id);
		
		return new ResponseEntity<Seller>(seller, HttpStatus.OK);
		
		
	}
	
	
//	public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt){
//		
//		
//		return null;
//		
//	}
	
	@GetMapping
	public ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required = false) AccountStatus status){
		
		List<Seller> seller = sellerService.getAllSeller(status);
		
		return ResponseEntity.ok(seller);
	}
	
	@PatchMapping
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception{
		
		Seller profile= sellerService.getSellerProfile(jwt);
		Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);
		return ResponseEntity.ok(updateSeller);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
		
		sellerService.deleteSeller(id);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	

}
