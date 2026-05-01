package com.codegnan.shopnest.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.CartItem;
import com.codegnan.shopnest.Entity.User;

public interface CartRepository extends JpaRepository<Cart,Long> {

	 Optional<Cart> findByUser(User user);

	    Optional<Cart> findByUserId(Long userId);

}
