package com.letscook.order.service;

import com.letscook.customer.repository.CustomerRepository;
import com.letscook.enums.OrderStatus;
import com.letscook.menu.model.meal.Meal;
import com.letscook.menu.repository.MealRepository;
import com.letscook.order.model.CreateOrderInput;
import com.letscook.order.model.Mealorder;
import com.letscook.order.model.MealorderInput;
import com.letscook.order.model.Order;
import com.letscook.order.repository.MealorderRepository;
import com.letscook.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        HashMap<Long, Meal> mealMap = new HashMap<>();
        for (Meal meal : meals) {
            mealMap.put(meal.getId(), meal);
        }
        double amount = 100;
        orderToCreate.setAmount(amount);
//        Meal meal = mealRepository.findById(createOrderInput.getMealId()).orElseThrow(() -> new EntityNotFoundException("Meal not found with ID: " + createOrderInput.getMealId()));
//        if (meal.getCurrentOrderCount() >= meal.getMaxOrderLimit()) {
//            throw new RuntimeException("Max order limit has been reached");
//        } else if (new Date().after(meal.getOrderDeadline())) {
//            throw new RuntimeException("Cannot order after deadline");
//        } else {
//            meal.setCurrentOrderCount(meal.getCurrentOrderCount() + 1);
//        }
//        orderToCreate.setMeal(meal);
        Order orderCreated = orderRepository.save(orderToCreate);
        for (MealorderInput mealorderInput : createOrderInput.getMealorderInputs()) {
            Mealorder mealorder = new Mealorder();
            mealorder.setMeal(mealMap.get(mealorderInput.getMealId()));
            mealorder.setOrder(orderCreated);
            mealorder.setQuantity(mealorderInput.getQuantity());
            mealorder.setStatus(String.valueOf(OrderStatus.PENDING));
            mealorderRepository.save(mealorder);
        }
        Order updatedOrder = getOrderById(orderCreated.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
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
        return orderRepository.findAllByMealorders_Meal_Schedule_Cook_Id(mealId);
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
