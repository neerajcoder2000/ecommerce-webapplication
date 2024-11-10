package com.neeraj.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.neeraj.modal.User;
import com.neeraj.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	
	@GetMapping("/user/profile")
	public ResponseEntity<User> findUserByJwtHandler(@RequestHeader("Authorization") String jwt ) throws Exception {
		
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(user);
	}
	
	

}
