package com.neeraj.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neeraj.modal.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
