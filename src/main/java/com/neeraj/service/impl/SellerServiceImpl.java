package com.neeraj.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neeraj.config.JwtProvider;
import com.neeraj.domain.AccountStatus;
import com.neeraj.domain.USER_ROLE;
import com.neeraj.modal.Address;
import com.neeraj.modal.Seller;
import com.neeraj.respository.AddressRepository;
import com.neeraj.respository.SellerRepository;
import com.neeraj.service.SellerService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{
	
	
	private final SellerRepository sellerRepository;
	
	private final JwtProvider jwtProvider;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AddressRepository addressRepository;
	

	@Override
	public Seller getSellerProfile(String jwt) throws Exception {
		
		String  email = jwtProvider.getEmailFromJwtToken(jwt);
		
		Seller seller = this.getSellerByEmail(email);
		
		return seller;
	}

	@Override
	public Seller createSeller(Seller seller) throws Exception {
		
		Seller existSeller = sellerRepository.findByEmail(seller.getEmail());
		
		if(existSeller!= null) {
				
			throw new Exception("Seller already exist, used different email");
		}
		
		Address saveAddress = addressRepository.save(seller.getPickupAddress());
		
		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassord(passwordEncoder.encode(seller.getPassord()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAddress(saveAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(USER_ROLE.ROLE_SELLER);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBankDetails(seller.getBankDetails());
		newSeller.setBusinessDetails(seller.getBusinessDetails());
		
		
		return sellerRepository.save(newSeller);
	}

	@Override
	public Seller getSellerById(Long id) throws Exception {
		
		Seller seller= sellerRepository.findById(id)
				.orElseThrow(() ->new Exception("seller not found with id - "+id));
		
		return seller;
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		
		Seller seller =sellerRepository.findByEmail(email);
		
		if(seller==null) {
			throw new Exception("seller not found...");
		}
		
		return seller;
	}

	@Override
	public List<Seller> getAllSeller(AccountStatus status) {
		return sellerRepository.findByAccountStatus(status);
		
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws Exception {
		
		Seller existingSeller = this.getSellerById(id);
		
		if(seller.getSellerName() != null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		
		if(seller.getMobile() != null) {
			existingSeller.setMobile(seller.getMobile());
		}
		
		if(seller.getEmail() != null) {
			existingSeller.setEmail(seller.getEmail());
		}
		
		if(seller.getBusinessDetails() != null 
				&& seller.getBusinessDetails().getBusinessName() != null)
		{
			existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
		}
		
		if(seller.getBankDetails() != null 
				&& seller.getBankDetails().getAccountHolderName() != null 
				&& seller.getBankDetails().getIfscCode() != null
			    && seller.getBankDetails().getAccountNumber() != null)

               {
			    	 existingSeller.getBankDetails().setAccountHolderName(
			    	 seller.getBankDetails().getAccountHolderName());
			    	 
			    	 existingSeller.getBankDetails().setIfscCode(
			    	 seller.getBankDetails().getIfscCode());
			    	 
			    	 existingSeller.getBankDetails().setAccountNumber(
			    	 seller.getBankDetails().getAccountNumber());
			    	 
			    }
		
		if(seller.getPickupAddress() != null 
				&& seller.getPickupAddress().getName() != null
				&& seller.getPickupAddress().getMobile() != null
				&& seller.getPickupAddress().getCity() != null
				&& seller.getPickupAddress().getState() != null) 
		      {
			
			        existingSeller.getPickupAddress().setName(seller.getPickupAddress().getName());
			        existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
			        existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
			        existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
			        existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
			
		      }
			    
		
		if(seller.getGSTIN() != null) {
			
			existingSeller.setGSTIN(seller.getGSTIN());
		}
		
		
		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long id) throws Exception {
		
		Seller seller = this.getSellerById(id);
		
		sellerRepository.delete(seller);
		
	}

	@Override
	public Seller verifyEmail(String email, String otp) throws Exception {
		
		Seller seller = this.getSellerByEmail(email);
		
		seller.setEmailVerified(true);
		
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
		
		Seller seller = this.getSellerById(sellerId);
		
		seller.setAccountStatus(status);
		
		return sellerRepository.save(seller);
	}

}
