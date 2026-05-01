package com.codegnan.shopnest.Service.Impl;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.CartItem;
import com.codegnan.shopnest.Entity.Product;
import com.codegnan.shopnest.Entity.User;
import com.codegnan.shopnest.Repository.CartItemRepository;
import com.codegnan.shopnest.Repository.CartRepository;
import com.codegnan.shopnest.Service.CartItemService;
import com.codegnan.shopnest.Service.CartService;
import com.codegnan.shopnest.Service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           CartItemService cartItemService,
                           ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public Cart getOrCreateCart(User user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
    @Override
    @Transactional
    public void addToCart(User user, Long productId, int quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productService.getProductById(productId);

        // check if product already exists in cart
        Optional<CartItem> existingItem = cartItemService
                .findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            // product already in cart — just update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // new product — insert a new cart item row
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        Optional<Cart> existingCart = cartRepository.findByUser(user);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
    }

    @Override
    public Double getCartTotal(User user) {
        Cart cart = getOrCreateCart(user);
        return cart.getTotalPrice();
    }
}
