package com.codegnan.shopnest.Service;

import java.util.List;
import java.util.Optional;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.CartItem;
import com.codegnan.shopnest.Entity.Product;

public interface CartItemService {

    List<CartItem> getCartItems(Cart cart);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}