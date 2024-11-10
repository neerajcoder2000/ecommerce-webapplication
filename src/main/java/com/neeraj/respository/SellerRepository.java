package com.neeraj.respository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neeraj.domain.AccountStatus;
import com.neeraj.modal.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long>{
	
	Seller findByEmail(String email);
	
	List<Seller> findByAccountStatus(AccountStatus status);

}
