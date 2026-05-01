package com.codegnan.shopnest.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codegnan.shopnest.Entity.Cart;
import com.codegnan.shopnest.Entity.CartItem;
import com.codegnan.shopnest.Entity.Order;
import com.codegnan.shopnest.Entity.OrderItem;
import com.codegnan.shopnest.Entity.Product;
import com.codegnan.shopnest.Entity.User;
import com.codegnan.shopnest.Repository.OrderItemRepository;
import com.codegnan.shopnest.Repository.OrderRepository;
import com.codegnan.shopnest.Repository.ProductRepository;
import com.codegnan.shopnest.Service.CartService;
import com.codegnan.shopnest.Service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            ProductRepository productRepository,
                            CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public Order placeOrder(User user,
                            String fullName,
                            String phone,
                            String address,
                            String city,
                            String pincode) {

        Cart cart = cartService.getOrCreateCart(user);

        // validate stock
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for: " + product.getName()
                    + ". Available: " + product.getStock()
                    + ", Requested: " + cartItem.getQuantity()
                );
            }
        }

        // build order
        Order order = new Order();
        order.setUser(user);
        order.setFullName(fullName);
        order.setPhone(phone);
        order.setAddress(address);
        order.setCity(city);
        order.setPincode(pincode);
        order.setTotalAmount(cartService.getCartTotal(user));
        order.setStatus("CONFIRMED");
        order.setPaymentMethod("MOCK_PAYMENT");

        Order savedOrder = orderRepository.save(order);

        // save order items and deduct stock
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItems.add(orderItem);

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        // flush order items first then clear cart
        orderItemRepository.flush();

        // clear cart LAST after everything is saved
        cartService.clearCart(user);

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository
                .findByUserOrderByOrderDateDesc(user);
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new RuntimeException(
                    "Order not found with id: " + id);
        }
        return order.get();
    }
}