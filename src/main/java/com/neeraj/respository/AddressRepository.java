package com.neeraj.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neeraj.modal.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
