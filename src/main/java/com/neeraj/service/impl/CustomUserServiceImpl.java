package com.neeraj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neeraj.domain.USER_ROLE;
import com.neeraj.modal.Seller;
import com.neeraj.modal.User;
import com.neeraj.respository.SellerRepository;
import com.neeraj.respository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	private static final String SELLER_PREFIX = "seller_";
	
	private final SellerRepository sellerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if(username.startsWith(SELLER_PREFIX)) {
			
			String actualUsername = username.substring(SELLER_PREFIX.length());
			
			Seller seller = sellerRepository.findByEmail(actualUsername);
			
			if(seller!= null) {
				
				return buildUserDetails(seller.getEmail(), seller.getPassord(), seller.getRole()); 
			}
			
		}else {
			User user = userRepository.findByEmail(username);
			
			if(user!= null) {
				return buildUserDetails(user.getEmail(), user.getPassword(),user.getRole());
			}
			
		}
		
		throw new UsernameNotFoundException("user or seller not found with email- "+username);
	}

	private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
		
		if(role==null) role = USER_ROLE.ROLE_CUSTOMER;
		
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority(role.toString()));
		
		return new org.springframework.security.core.userdetails
				.User(email, password, authorityList);
	}

	

}
