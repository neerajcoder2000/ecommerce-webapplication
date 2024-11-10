package com.neeraj.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neeraj.modal.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	
	User findByEmail(String email);
	
		

}
