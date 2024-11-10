package com.neeraj.service;

import com.neeraj.modal.User;

public interface UserService {
	
	User findUserByJwtToken(String jwt) throws Exception;
	
	User findUserByEmail(String email) throws Exception;

}
