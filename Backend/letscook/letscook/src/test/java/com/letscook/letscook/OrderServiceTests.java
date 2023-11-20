package com.letscook.letscook;

import com.letscook.customer.model.Customer;
import com.letscook.customer.repository.CustomerRepository;
import com.letscook.enums.OrderStatus;
import com.letscook.menu.model.meal.Meal;
import com.letscook.menu.repository.MealRepository;
import com.letscook.order.model.CreateOrderInput;
import com.letscook.order.model.Order;
import com.letscook.order.repository.OrderRepository;
import com.letscook.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;


    @Test
    public void testCreateOrder() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Online");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);
        createOrderInput.setMealId(2L);

        Meal mockMeal = new Meal();
        mockMeal.setId(2L);
        mockMeal.setCurrentOrderCount(0L);
        mockMeal.setMaxOrderLimit(5L);
        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() + 100000)); // deadline in the future

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Order> response = orderService.createOrder(createOrderInput);

        //ResponseEntity<Order> response1 = ord.createOrder(createOrderInput);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(OrderStatus.PENDING, response.getBody().getStatus());
        assertEquals(1, mockMeal.getCurrentOrderCount());
    }

    @Test
    public void testUpdateOrderStatus() {
        // Arrange
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Order> response = orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(OrderStatus.COMPLETED, response.getBody().getStatus());
    }

    @Test
    public void testGetOrderById() {
        // Arrange
        Long orderId = 1L;
        Order mockOrder = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        // Act
        Order result = orderService.getOrderById(orderId);

        // Assert
        assertNotNull(result);
        assertSame(mockOrder, result);
    }

    @Test
    public void testGetAllOrders() {
        // Arrange
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")))
                .thenReturn(Arrays.asList(order2, order1));

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(order2, result.get(0)); // Order by descending createdAt
        assertSame(order1, result.get(1));
    }

    @Test
    public void testGetOrdersByCustomer() {
        // Arrange
        Long customerId = 1L;
        List<Order> mockOrders = new ArrayList<>();
        when(orderRepository.findAllByCustomer_IdOrderByCreatedAtDesc(customerId))
                .thenReturn(mockOrders);

        // Act
        List<Order> result = orderService.getOrdersByCustomer(customerId);

        // Assert
        assertNotNull(result);
        assertSame(mockOrders, result);
    }

//    @Test
//    public void testGetOrdersByCook() {
//        // Arrange
//        Long cookId = 1L;
//        List<Order> mockOrders = new ArrayList<>();
//        when(orderRepository.findAllByMeal_Menu_Cook_IdOrderByCreatedAtDesc(cookId))
//                .thenReturn(mockOrders);
//
//        // Act
//        List<Order> result = orderService.getOrdersByCook(cookId);
//
//        // Assert
//        assertNotNull(result);
//        assertSame(mockOrders, result);
//    }

//    @Test
//    public void testGetOrdersByMenu() {
//        // Arrange
//        Long menuId = 1L;
//        List<Order> mockOrders = new ArrayList<>();
//        when(orderRepository.findAllByMeal_Menu_IdOrderByCreatedAtDesc(menuId))
//                .thenReturn(mockOrders);
//
//        // Act
//        List<Order> result = orderService.getOrdersByMenu(menuId);
//
//        // Assert
//        assertNotNull(result);
//        assertSame(mockOrders, result);
//    }

    @Test
    public void testGetOrdersByStatusAndCook() {
        // Arrange
        Long cookId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        List<Order> mockOrders = new ArrayList<>();
        when(orderRepository.findAllByStatusAndAndCustomer_IdOrderByCreatedAtDesc(status, cookId))
                .thenReturn(mockOrders);

        // Act
        List<Order> result = orderService.getOrdersByStatusAndCook(cookId, status);

        // Assert
        assertNotNull(result);
        assertSame(mockOrders, result);
    }

    @Test
    public void testCreateOrder_OrderLimitReached() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Online");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);
        createOrderInput.setMealId(2L);

        Meal mockMeal = new Meal();
        mockMeal.setId(2L);
        mockMeal.setCurrentOrderCount(5L); // Max order limit reached
        mockMeal.setMaxOrderLimit(5L);
        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() + 100000)); // deadline in the future

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(createOrderInput));
        verify(mealRepository, never()).save(any(Meal.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_OrderAfterDeadline() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Online");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);
        createOrderInput.setMealId(2L);

        Meal mockMeal = new Meal();
        mockMeal.setId(2L);
        mockMeal.setCurrentOrderCount(3L); // Still below max order limit
        mockMeal.setMaxOrderLimit(5L);
        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() - 100000)); // deadline in the past

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(createOrderInput));
        verify(mealRepository, never()).save(any(Meal.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_SuccessfulOrder() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Online");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);
        createOrderInput.setMealId(2L);

        Meal mockMeal = new Meal();
        mockMeal.setId(2L);
        mockMeal.setCurrentOrderCount(3L); // Still below max order limit
        mockMeal.setMaxOrderLimit(5L);
        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() + 100000)); // deadline in the future

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Order> response = orderService.createOrder(createOrderInput);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(OrderStatus.PENDING, response.getBody().getStatus());
        assertEquals(4, mockMeal.getCurrentOrderCount()); // One more order added
    }
}