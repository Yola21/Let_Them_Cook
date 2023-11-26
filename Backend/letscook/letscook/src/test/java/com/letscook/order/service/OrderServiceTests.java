package com.letscook.order.service;

import com.letscook.cook.model.Cook;
import com.letscook.customer.model.Customer;
import com.letscook.customer.repository.CustomerRepository;
import com.letscook.enums.OrderStatus;
import com.letscook.menu.model.meal.Meal;
import com.letscook.menu.model.meal.Schedule;
import com.letscook.menu.repository.MealRepository;
import com.letscook.order.model.CreateOrderInput;
import com.letscook.order.model.Mealorder;
import com.letscook.order.model.MealorderInput;
import com.letscook.order.model.Order;
import com.letscook.order.repository.MealorderRepository;
import com.letscook.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MealorderRepository mealorderRepository;

    @InjectMocks
    private OrderService orderService;

    private Schedule schedule;

    private Cook cook;

    private Meal meal;

    private Meal pastMeal;

    private Meal limitedMeal;

    private Customer customer;

    private Order order;

    @BeforeAll
    public void init() {
        cook = new Cook();
        cook.setId(1L);
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setName("Week 1");
        schedule.setStart_date(new Date(2023, 11, 10, 10, 10, 10));
        schedule.setCook(cook);
        meal = new Meal();
        meal.setId(2L);
        meal.setName("Pav Bhaji");
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, 10);
        Date mealDate = cal.getTime();
        meal.setMealDate(mealDate);
        meal.setSlot("lunch");
        meal.setMaxOrderLimit(100L);
        meal.setOrderDeadline(mealDate);
        meal.setImage("pavbhajiurl");
        meal.setPrice(100.00);
        meal.setSchedule(schedule);
        pastMeal = new Meal();
        pastMeal.setId(3L);
        pastMeal.setName("Chole");
        Calendar pastCalendar = Calendar.getInstance();
        pastCalendar.setTime(currentDate);
        pastCalendar.add(Calendar.DATE, -10);
        Date pastDate = pastCalendar.getTime();
        pastMeal.setMealDate(pastDate);
        pastMeal.setSlot("lunch");
        pastMeal.setMaxOrderLimit(100L);
        pastMeal.setOrderDeadline(pastDate);
        pastMeal.setImage("pavbhajiurl");
        pastMeal.setPrice(100.00);
        pastMeal.setSchedule(schedule);
        limitedMeal = new Meal();
        limitedMeal.setId(4L);
        limitedMeal.setName("Rajma");
        limitedMeal.setMealDate(mealDate);
        limitedMeal.setSlot("lunch");
        limitedMeal.setMaxOrderLimit(1L);
        limitedMeal.setOrderDeadline(mealDate);
        limitedMeal.setImage("pavbhajiurl");
        limitedMeal.setPrice(100.00);
        limitedMeal.setSchedule(schedule);
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Adam");
        customer.setPhoneNumber("474755959");
        order = new Order();
        order.setId(1L);
    }


    @Test
    public void testCreateOrder() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Subscription");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);

        MealorderInput input = new MealorderInput();
        input.setMealId(2L);
        input.setQuantity(2L);
        List<MealorderInput> mealorderInputs = Arrays.asList(input);
        createOrderInput.setMealorderInputs(mealorderInputs);

//        Meal mockMeal = new Meal();
//        mockMeal.setId(2L);
//        mockMeal.setCurrentOrderCount(0L);
//        mockMeal.setMaxOrderLimit(5L);
//        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() + 100000)); // deadline in the future

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findAllByIdIn(Arrays.asList(2L))).thenReturn(Arrays.asList(meal));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mealRepository.save(any(Meal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mealorderRepository.save(any(Mealorder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Order> response = orderService.createOrder(createOrderInput);

        assertEquals(response.getBody().getAmount(), meal.getPrice() * 2);
        //ResponseEntity<Order> response1 = ord.createOrder(createOrderInput);

//        // Assert
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(OrderStatus.PENDING, response.getBody().getStatus());
//        assertEquals(1, mockMeal.getCurrentOrderCount());
    }

    @Test
    public void testCreateOrder_OrderDeadlineReached() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Subscription");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);

        MealorderInput input = new MealorderInput();
        input.setMealId(3L);
        input.setQuantity(2L);
        List<MealorderInput> mealorderInputs = Arrays.asList(input);
        createOrderInput.setMealorderInputs(mealorderInputs);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findAllByIdIn(Arrays.asList(3L))).thenReturn(Arrays.asList(pastMeal));

        // Act
        assertThrows(RuntimeException.class, () -> orderService.createOrder(createOrderInput));
        verify(mealRepository, never()).save(any(Meal.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_OrderLimitReached() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Subscription");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);

        MealorderInput input = new MealorderInput();
        input.setMealId(4L);
        input.setQuantity(2L);
        List<MealorderInput> mealorderInputs = Arrays.asList(input);
        createOrderInput.setMealorderInputs(mealorderInputs);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        when(mealRepository.findAllByIdIn(Arrays.asList(4L))).thenReturn(Arrays.asList(limitedMeal));

        // Act
        assertThrows(RuntimeException.class, () -> orderService.createOrder(createOrderInput));
        verify(mealRepository, never()).save(any(Meal.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrderMealNotFound() {
        // Arrange
        CreateOrderInput createOrderInput = new CreateOrderInput();
        createOrderInput.setType("Subscription");
        createOrderInput.setStatus(OrderStatus.PENDING);
        createOrderInput.setCustomerId(1L);

        MealorderInput input = new MealorderInput();
        input.setMealId(5L);
        input.setQuantity(2L);
        List<MealorderInput> mealorderInputs = Arrays.asList(input);
        createOrderInput.setMealorderInputs(mealorderInputs);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        //doReturn(new ArrayList<Meal>()).when(mealRepository).deleteById(1L);
        when(mealRepository.findAllByIdIn(Arrays.asList(5L))).thenReturn(new ArrayList<Meal>());

        // Act
        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(createOrderInput));
        verify(mealRepository, never()).save(any(Meal.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testGetOrderById() {
        // Arrange

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order getOrder = orderService.getOrderById(1L);

        // Act
        assertEquals(1L, getOrder.getId());
    }

    @Test
    public void testgetOrdersByCustomer() {
        // Arrange
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        when(orderRepository.findAllByCustomer_IdOrderByCreatedAtDesc(1L)).thenReturn(orderList);

        List<Order> custOrderList = orderService.getOrdersByCustomer(1L);

        // Act
        assertEquals(custOrderList.size(), orderList.size());
    }

    @Test
    public void testGetOrdersByMeal() {
        // Arrange
        List<Order> orderList = new ArrayList<>();
        Mealorder mealorder = new Mealorder();
        mealorder.setMeal(meal);
        order.setMealorders(Arrays.asList(mealorder));
        orderList.add(order);


        when(orderRepository.findAllByMealorders_Meal_IdOrderByCreatedAtAsc(2L)).thenReturn(orderList);

        List<Order> custOrderList = orderService.getOrdersByMeal(2L);

        // Act
        assertEquals(custOrderList.size(), orderList.size());
    }

//    @Test
//    public void testUpdateOrderStatus() {
//        // Arrange
//        Long orderId = 1L;
//        Order existingOrder = new Order();
//        existingOrder.setId(orderId);
//        existingOrder.setStatus(OrderStatus.PENDING);
//
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
//        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        ResponseEntity<Order> response = orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED);
//
//        // Assert
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(OrderStatus.COMPLETED, response.getBody().getStatus());
//    }

//    @Test
//    public void testGetOrderById() {
//        // Arrange
//        Long orderId = 1L;
//        Order mockOrder = new Order();
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
//
//        // Act
//        Order result = orderService.getOrderById(orderId);
//
//        // Assert
//        assertNotNull(result);
//        assertSame(mockOrder, result);
//    }

//    @Test
//    public void testGetAllOrders() {
//        // Arrange
//        Order order1 = new Order();
//        Order order2 = new Order();
//        when(orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")))
//                .thenReturn(Arrays.asList(order2, order1));
//
//        // Act
//        List<Order> result = orderService.getAllOrders();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertSame(order2, result.get(0)); // Order by descending createdAt
//        assertSame(order1, result.get(1));
//    }

//    @Test
//    public void testGetOrdersByCustomer() {
//        // Arrange
//        Long customerId = 1L;
//        List<Order> mockOrders = new ArrayList<>();
//        when(orderRepository.findAllByCustomer_IdOrderByCreatedAtDesc(customerId))
//                .thenReturn(mockOrders);
//
//        // Act
//        List<Order> result = orderService.getOrdersByCustomer(customerId);
//
//        // Assert
//        assertNotNull(result);
//        assertSame(mockOrders, result);
//    }

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
//
//    @Test
//    public void testGetOrdersByStatusAndCook() {
//        // Arrange
//        Long cookId = 1L;
//        OrderStatus status = OrderStatus.COMPLETED;
//        List<Order> mockOrders = new ArrayList<>();
//        when(orderRepository.findAllByStatusAndAndCustomer_IdOrderByCreatedAtDesc(status, cookId))
//                .thenReturn(mockOrders);
//
//        // Act
//        List<Order> result = orderService.getOrdersByStatusAndCook(cookId, status);
//
//        // Assert
//        assertNotNull(result);
//        assertSame(mockOrders, result);
//    }

//    @Test
//    public void testCreateOrder_OrderLimitReached() {
//        // Arrange
//        CreateOrderInput createOrderInput = new CreateOrderInput();
//        createOrderInput.setType("Online");
//        createOrderInput.setStatus(OrderStatus.PENDING);
//        createOrderInput.setCustomerId(1L);
//        createOrderInput.setMealId(2L);
//
//        Meal mockMeal = new Meal();
//        mockMeal.setId(2L);
//        mockMeal.setCurrentOrderCount(5L); // Max order limit reached
//        mockMeal.setMaxOrderLimit(5L);
//        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() + 100000)); // deadline in the future
//
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
//        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));
//
//        // Act and Assert
//        assertThrows(RuntimeException.class, () -> orderService.createOrder(createOrderInput));
//        verify(mealRepository, never()).save(any(Meal.class));
//        verify(orderRepository, never()).save(any(Order.class));
//    }

//    @Test
//    public void testCreateOrder_OrderAfterDeadline() {
//        // Arrange
//        CreateOrderInput createOrderInput = new CreateOrderInput();
//        createOrderInput.setType("Online");
//        createOrderInput.setStatus(OrderStatus.PENDING);
//        createOrderInput.setCustomerId(1L);
//        createOrderInput.setMealId(2L);
//
//        Meal mockMeal = new Meal();
//        mockMeal.setId(2L);
//        mockMeal.setCurrentOrderCount(3L); // Still below max order limit
//        mockMeal.setMaxOrderLimit(5L);
//        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() - 100000)); // deadline in the past
//
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
//        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));
//
//        // Act and Assert
//        assertThrows(RuntimeException.class, () -> orderService.createOrder(createOrderInput));
//        verify(mealRepository, never()).save(any(Meal.class));
//        verify(orderRepository, never()).save(any(Order.class));
//    }

//    @Test
//    public void testCreateOrder_SuccessfulOrder() {
//        // Arrange
//        CreateOrderInput createOrderInput = new CreateOrderInput();
//        createOrderInput.setType("Online");
//        createOrderInput.setStatus(OrderStatus.PENDING);
//        createOrderInput.setCustomerId(1L);
//        createOrderInput.setMealId(2L);
//
//        Meal mockMeal = new Meal();
//        mockMeal.setId(2L);
//        mockMeal.setCurrentOrderCount(3L); // Still below max order limit
//        mockMeal.setMaxOrderLimit(5L);
//        mockMeal.setOrderDeadline(new Date(System.currentTimeMillis() + 100000)); // deadline in the future
//
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
//        when(mealRepository.findById(2L)).thenReturn(Optional.of(mockMeal));
//        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        ResponseEntity<Order> response = orderService.createOrder(createOrderInput);
//
//        // Assert
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(OrderStatus.PENDING, response.getBody().getStatus());
//        assertEquals(4, mockMeal.getCurrentOrderCount()); // One more order added
//    }
}