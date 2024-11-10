package com.neeraj.service.impl;

import org.springframework.stereotype.Service;

import com.neeraj.config.JwtProvider;
import com.neeraj.modal.User;
import com.neeraj.respository.UserRepository;
import com.neeraj.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	
	private final UserRepository userRepository;
	
	private final JwtProvider jwtProvider;
	

	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		
		User user = this.findUserByEmail(email);
		return user;
	}
	
	

	@Override
	public User findUserByEmail(String email) throws Exception {
		
		User user = userRepository.findByEmail(email);
		
		if(user==null) {
			
			throw new Exception("user not found with email - "+email);
		}
		
		return user;
	}

}
