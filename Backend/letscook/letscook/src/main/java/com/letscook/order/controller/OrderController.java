package com.letscook.order.controller;

import com.letscook.enums.OrderStatus;
import com.letscook.order.model.CreateOrderInput;
import com.letscook.order.model.Order;
import com.letscook.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderInput createOrderInput) {
        return orderService.createOrder(createOrderInput);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping()
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/customer")
    public List<Order> getOrdersByCustomer(@RequestParam Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/cook")
    public List<Order> getOrdersByCook(@RequestParam Long cookId) {
        return orderService.getOrdersByCook(cookId);
    }

    @GetMapping("/menu")
    public List<Order> getOrdersByMenu(@RequestParam Long menuId) {
        return orderService.getOrdersByMenu(menuId);
    }

    @GetMapping("/cook/status")
    public List<Order> getOrdersByCookAndStatus(@RequestParam Long cookId, @RequestParam OrderStatus status) {
        return orderService.getOrdersByStatusAndCook(cookId,status);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<Order> updateOrderStatus(@RequestParam Long id, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(id, status);
    }
}
