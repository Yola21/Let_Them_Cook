package com.letscook.order.service;

import com.letscook.customer.repository.CustomerRepository;
import com.letscook.enums.OrderStatus;
import com.letscook.menu.model.meal.Meal;
import com.letscook.menu.repository.MealRepository;
import com.letscook.order.model.*;
import com.letscook.order.repository.MealorderRepository;
import com.letscook.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MealorderRepository mealorderRepository;

    public ResponseEntity<Order> createOrder(CreateOrderInput createOrderInput) {
        Order orderToCreate = new Order();
        orderToCreate.setType(createOrderInput.getType());
        orderToCreate.setStatus(createOrderInput.getStatus());
        orderToCreate.setCustomer(customerRepository.findById(createOrderInput.getCustomerId()).orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + createOrderInput.getCustomerId())));
        List<Long> mealIds = new ArrayList<>();
        for (MealorderInput mealorderInput : createOrderInput.getMealorderInputs()) {
            mealIds.add(mealorderInput.getMealId());
        }
        List<Meal> meals = mealRepository.findAllByIdIn(mealIds);
        if (mealIds.size() > meals.size()) {
            throw new EntityNotFoundException("Didn't find all meals in db");
        }
        HashMap<Long, Meal> mealMap = new HashMap<>();
        for (Meal meal : meals) {
            mealMap.put(meal.getId(), meal);
        }
        orderToCreate.setPaymentStatus(createOrderInput.getPaymentStatus());
        double amount = 0;
        for (MealorderInput mealorderInput : createOrderInput.getMealorderInputs()) {
            Meal meal = mealMap.get(mealorderInput.getMealId());
            Long currentOrderCount = Objects.requireNonNullElse(meal.getCurrentOrderCount(), 0L);
            if ((currentOrderCount + mealorderInput.getQuantity()) > meal.getMaxOrderLimit()) {
                throw new RuntimeException("Max order limit has been reached");
            } else if (new Date().after(meal.getOrderDeadline())) {
                throw new RuntimeException("Cannot order after deadline");
            }
            amount += meal.getPrice() * mealorderInput.getQuantity();
        }
        orderToCreate.setAmount(amount);
        Order orderCreated = orderRepository.save(orderToCreate);
        for (MealorderInput mealorderInput : createOrderInput.getMealorderInputs()) {
            Meal meal = mealMap.get(mealorderInput.getMealId());
            Long currentOrderCount = Objects.requireNonNullElse(meal.getCurrentOrderCount(), 0L);
            meal.setCurrentOrderCount(currentOrderCount + mealorderInput.getQuantity());
            mealRepository.save(meal);
            Mealorder mealorder = new Mealorder();
            mealorder.setMeal(meal);
            mealorder.setOrder(orderCreated);
            mealorder.setQuantity(mealorderInput.getQuantity());
            mealorder.setStatus(OrderStatus.PENDING.name());
            mealorder.setAmount(meal.getPrice() * mealorderInput.getQuantity());
            mealorderRepository.save(mealorder);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreated);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    //    public List<Order> getAllOrders() {
//        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
//    }
//
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findAllByCustomer_IdOrderByCreatedAtDesc(customerId);
    }

    public List<Order> getOrdersByMeal(Long mealId) {
        List<Order> orderList = orderRepository.findAllByMealorders_Meal_IdOrderByCreatedAtAsc(mealId);
        for (Order order : orderList) {
            List<Mealorder> mealorders = new ArrayList<>();
            for (Mealorder mealorder : order.getMealorders()) {
                if (mealorder.getMeal().getId() == mealId) {
                    mealorders.add(mealorder);
                }
            }
            order.setMealorders(mealorders);

        }
        return orderList;
    }
    public ResponseEntity<Mealorder> updateOrderStatus(UpdateOrderStatus updateOrderStatus) {
        Mealorder mealorder = mealorderRepository.findById(updateOrderStatus.getMealOrderId()).orElseThrow();
        mealorder.setStatus(updateOrderStatus.getStatus());
        Mealorder updatedMealorder = mealorderRepository.save(mealorder);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedMealorder);
    }

//    public List<Order> getOrdersByMenu(Long menuId) {
//        return orderRepository.findAllByMeal_Menu_IdOrderByCreatedAtDesc(menuId);
//    }

//    public List<Order> getOrdersByStatusAndCook(Long cookId, OrderStatus status) {
//        return orderRepository.findAllByStatusAndAndCustomer_IdOrderByCreatedAtDesc(status, cookId);
//    }
//
//    public ResponseEntity<Order> updateOrderStatus(Long id, OrderStatus status) {
//        Order orderToUpdate = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
//        orderToUpdate.setStatus(status);
//        Order updatedOrder = orderRepository.save(orderToUpdate);
//        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
//    }
}
