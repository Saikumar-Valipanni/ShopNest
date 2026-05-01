package com.codegnan.shopnest.Service;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.User;

public interface CartService {

    Cart getOrCreateCart(User user);

    void addToCart(User user, Long productId, int quantity);

    void updateQuantity(Long cartItemId, int quantity);

    void removeItem(Long cartItemId);

    void clearCart(User user);

    Double getCartTotal(User user);
}