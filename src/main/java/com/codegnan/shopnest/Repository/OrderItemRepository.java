package com.codegnan.shopnest.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codegnan.shopnest.Entity.Order;
import com.codegnan.shopnest.Entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}