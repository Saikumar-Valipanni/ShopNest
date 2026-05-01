package com.codegnan.shopnest.Service;

import java.util.List;

import com.codegnan.shopnest.Entity.Order;
import com.codegnan.shopnest.Entity.User;

public interface OrderService {

    Order placeOrder(User user,
                     String fullName,
                     String phone,
                     String address,
                     String city,
                     String pincode);

    List<Order> getOrdersByUser(User user);

    Order getOrderById(Long id);
}