package com.letscook.order.service;

import com.letscook.customer.repository.CustomerRepository;
import com.letscook.enums.OrderStatus;
import com.letscook.menu.model.Meal;
import com.letscook.menu.repository.MealRepository;
import com.letscook.order.model.CreateOrderInput;
import com.letscook.order.model.Order;
import com.letscook.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Order> createOrder(CreateOrderInput createOrderInput) {
        Order orderToCreate = new Order();
        orderToCreate.setType(createOrderInput.getType());
        orderToCreate.setStatus(createOrderInput.getStatus());
        orderToCreate.setCustomer(customerRepository.findById(createOrderInput.getCustomerId()).orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + createOrderInput.getCustomerId())));
        Meal meal = mealRepository.findById(createOrderInput.getMealId()).orElseThrow(() -> new EntityNotFoundException("Meal not found with ID: " + createOrderInput.getMealId()));
        if (meal.getCurrentOrderCount() >= meal.getMaxOrderLimit()) {
            throw new RuntimeException("Max order limit has been reached");
        } else if (new Date().after(meal.getOrderDeadline())) {
            throw new RuntimeException("Cannot order after deadline");
        } else {
            meal.setCurrentOrderCount(meal.getCurrentOrderCount() + 1);
        }
        orderToCreate.setMeal(meal);
        Order orderCreated = orderRepository.save(orderToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreated);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findAllByCustomer_IdOrderByCreatedAtDesc(customerId);
    }

//    public List<Order> getOrdersByCook(Long cookId) {
//        return orderRepository.findAllByMeal_Menu_Cook_IdOrderByCreatedAtDesc(cookId);
//    }

//    public List<Order> getOrdersByMenu(Long menuId) {
//        return orderRepository.findAllByMeal_Menu_IdOrderByCreatedAtDesc(menuId);
//    }

    public List<Order> getOrdersByStatusAndCook(Long cookId, OrderStatus status) {
        return orderRepository.findAllByStatusAndAndCustomer_IdOrderByCreatedAtDesc(status, cookId);
    }

    public ResponseEntity<Order> updateOrderStatus(Long id, OrderStatus status) {
        Order orderToUpdate = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
        orderToUpdate.setStatus(status);
        Order updatedOrder = orderRepository.save(orderToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
    }
}
