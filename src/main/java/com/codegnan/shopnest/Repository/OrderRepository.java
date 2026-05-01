package com.codegnan.shopnest.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.shopnest.Entity.Order;
import com.codegnan.shopnest.Entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);
}