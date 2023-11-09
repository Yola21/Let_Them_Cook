package com.letscook.order.repository;

import com.letscook.enums.OrderStatus;
import com.letscook.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer_Id(Long id);

    List<Order> findAllByMeal_Menu_Cook_Id(Long id);

    List<Order> findAllByMeal_Menu_Id(Long id);

    List<Order> findAllByStatusAndAndCustomer_Id(OrderStatus status, Long id);
}
