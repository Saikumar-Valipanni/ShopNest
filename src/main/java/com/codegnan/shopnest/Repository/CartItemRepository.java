package com.codegnan.shopnest.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.CartItem;
import com.codegnan.shopnest.Entity.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Modifying
    @Transactional
    void deleteByCart(Cart cart);
}