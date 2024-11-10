package com.neeraj.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.stereotype.Service;

import com.neeraj.config.JwtProvider;
import com.neeraj.domain.USER_ROLE;
import com.neeraj.modal.Cart;
import com.neeraj.modal.Seller;
import com.neeraj.modal.User;
import com.neeraj.modal.VerificationCode;
import com.neeraj.request.LoginRequest;
import com.neeraj.response.AuthResponse;
import com.neeraj.response.SignupRequest;
import com.neeraj.respository.CartRepository;
import com.neeraj.respository.SellerRepository;
import com.neeraj.respository.UserRepository;
import com.neeraj.respository.VerificationCodeRepository;
import com.neeraj.service.AuthService;
import com.neeraj.service.EmailService;
import com.neeraj.utils.OtpUtil;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final CartRepository cartRepository;
	
	private final VerificationCodeRepository verificationCodeRepository;
	
	private final JwtProvider jwtProvider;
	
	private final EmailService emailService;
	
	private final CustomUserServiceImpl customUserService;
	
	private final SellerRepository sellerRepository;
	
	
	

	
	@Override
	public String createUser(SignupRequest req) throws Exception {
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
		
		if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())) {
			
			throw new Exception("wrong otp...");
		}
		
		User user= userRepository.findByEmail(req.getEmail());
		
		if(user==null) {
			
			User createdUser = new User();
			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullName());
			createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
			createdUser.setMobile("7400406278");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
			
			user = userRepository.save(createdUser);
			
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
			
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return jwtProvider.generateToken(authentication);
	}





	@Override
	public void sendLoginOtp(String email, USER_ROLE role) throws Exception {
		
		String SIGNING_PREFIX = "signing_";
		
		
		if(email.startsWith(SIGNING_PREFIX)) {
			email=email.substring(SIGNING_PREFIX.length());
			
			if(role.equals(USER_ROLE.ROLE_SELLER)) {
				
				Seller seller = sellerRepository.findByEmail(email); 
				if(seller==null) {
					throw new Exception("seller not found");
				}
				
			}else {
				
                User user = userRepository.findByEmail(email); 
				
				if(user==null) {
					throw new Exception("user not exist with provided email...");
				}
				
			}
			
			
		}
		
		VerificationCode isExist = verificationCodeRepository.findByEmail(email);
		
		if(isExist!=null) {
			
			verificationCodeRepository.delete(isExist);
		}
		
		//there is generate otp
		
		 String otp = OtpUtil.generateOtp();
		 
		 VerificationCode verificationCode = new VerificationCode();
	
		 verificationCode.setOtp(otp);
		 verificationCode.setEmail(email);
		 
		 verificationCodeRepository.save(verificationCode);  //  save the database
		 
		 // send the email 
		 
		 String subject = "Neeraj login and signup otp";
		 
		 String text = "your login and signup otp is - "+otp;
		 
		 emailService.sendVerificationOtpEmail(email, otp, subject, text);
		 
		 
		
	}





	@Override
	public AuthResponse signing(LoginRequest req) {
		
		String username = req.getEmail();
		String otp = req.getOtp();
		
		Authentication authentication = authenticate(username, otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		
		authResponse.setJwt(token);
		authResponse.setMessage("Login success");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		
		authResponse.setRole(USER_ROLE.valueOf(roleName));
		
		return authResponse;
	}





	private Authentication authenticate(String username, String otp) {
		//get customservice
		
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		
		if(userDetails==null) {
			
			throw new BadCredentialsException("Invalid username");
		}
		
		VerificationCode verificationCode= verificationCodeRepository.findByEmail(username);
		
		if(verificationCode==null || !verificationCode.getOtp().equals(otp)) {
			
			throw new BadCredentialsException("wrong otp..");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails , null, userDetails.getAuthorities());
	}

}
