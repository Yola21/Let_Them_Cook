package com.letscook.letscook;

import com.letscook.cook.model.Cook;
import com.letscook.cook.repository.CookRepository;
import com.letscook.menu.model.CreateDish;
import com.letscook.menu.model.DishToMealId;
import com.letscook.menu.model.DishToMealMap;
import com.letscook.menu.model.dish.AddDishToMealInput;
import com.letscook.menu.model.dish.Dish;
import com.letscook.menu.model.dish.UpdateDish;
import com.letscook.menu.model.input.*;
import com.letscook.menu.model.meal.Meal;
import com.letscook.menu.model.meal.Schedule;
import com.letscook.menu.repository.DishRepository;
import com.letscook.menu.repository.DishToMealRepository;
import com.letscook.menu.repository.MealRepository;
import com.letscook.menu.repository.ScheduleRepository;
import com.letscook.menu.service.ScheduleService;
import com.letscook.order.model.Mealorder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MenuServiceTests {

    @Mock
    private ScheduleRepository menuRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishToMealRepository dishToMealRepository;

    @Mock
    private CookRepository cookRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Schedule schedule;

    private Cook cook;

    private Meal meal;

    private Dish dish;

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
        meal.setId(1L);
        meal.setName("Pav Bhaji");
        meal.setMealDate(new Date(2023, 11, 10, 17, 10, 10));
        meal.setSlot("lunch");
        meal.setMaxOrderLimit(100L);
        meal.setOrderDeadline(new Date(2023, 11, 10, 15, 10, 10));
        meal.setImage("pavbhajiurl");
        meal.setPrice(100.00);
        meal.setSchedule(schedule);
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Bhaji");
        dish.setType("Veg");
        dish.setDescription("Bhaji of Pav Bhaji");
        dish.setImage("dish image url");
        dish.setCook(cook);
    }

    @Test
    public void createScheduleTest() throws IOException {
        // Arrange

        CreateScheduleInput createScheduleInput = new CreateScheduleInput();
        createScheduleInput.setName("Week 1");
        createScheduleInput.setStart_date(new Date(2023, 11, 10, 10, 10, 10));
        createScheduleInput.setCookId(1L);

        when(cookRepository.findById(1L)).thenReturn(Optional.of(cook));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Schedule> response = scheduleService.createSchedule(createScheduleInput);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Week 1", response.getBody().getName());
    }

    @Test
    public void getScheduleTest() throws IOException {
        // Arrange
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        // Act
        Schedule scheduleResponse = scheduleService.getScheduleById(1L);
        // Assert
        assertEquals(scheduleResponse.getId(), 1L);
    }

    @Test
    public void deleteScheduleTest() throws IOException {
        // Arrange
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        doNothing().when(scheduleRepository).deleteById(1L);
        // Act
        Schedule scheduleResponse = scheduleService.deleteScheduleById(1L);
        // Assert
        assertEquals(scheduleResponse.getId(), 1L);
    }

    @Test
    public void getSchedulesByCookTest() throws IOException {
        // Arrange
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule);
        when(scheduleRepository.findAllByCook_Id(1L)).thenReturn(scheduleList);

        // Act
        List<Schedule> scheduleResponse = scheduleService.getSchedulesByCook(1L);
        // Assert
        assertEquals(scheduleResponse.get(0).getCook().getId(), 1L);
    }

    @Test
    public void updateScheduleTest() throws IOException {
        // Arrange

        UpdateScheduleInput updateScheduleInput = new UpdateScheduleInput();
        updateScheduleInput.setId(1L);
        updateScheduleInput.setName("Week 1");
        updateScheduleInput.setStart_date(new Date(2023, 11, 10, 10, 10, 10));

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Schedule> response = scheduleService.updateSchedule(updateScheduleInput);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Week 1", response.getBody().getName());
    }

    @Test
    public void addMealToScheduleTests() throws IOException {
        // Arrange

        AddMealToScheduleInput addMealToScheduleInput = new AddMealToScheduleInput();
        addMealToScheduleInput.setName("Pav Bhaji");
        addMealToScheduleInput.setMealDate(new Date(2023, 11, 10, 17, 10, 10));
        addMealToScheduleInput.setSlot("lunch");
        addMealToScheduleInput.setMaxOrderLimit(100L);
        addMealToScheduleInput.setOrderDeadline(new Date(2023, 11, 10, 15, 10, 10));
        addMealToScheduleInput.setImage("pavbhajiurl");
        addMealToScheduleInput.setPrice(100.00);
        addMealToScheduleInput.setScheduleId(1L);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(mealRepository.save(any(Meal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Meal> response = scheduleService.addMealToSchedule(addMealToScheduleInput);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pav Bhaji", response.getBody().getName());
    }

    @Test
    public void getMealByIdTest() throws IOException {
        // Arrange
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        // Act
        Meal mealResponse = scheduleService.getMealById(1L);
        // Assert
        assertEquals(mealResponse.getId(), 1L);
    }

    @Test
    public void updateMealToScheduleTest() throws IOException {
        // Arrange

        UpdateMealToScheduleInput updateMealToScheduleInput = new UpdateMealToScheduleInput();
        updateMealToScheduleInput.setId(1L);
        updateMealToScheduleInput.setName("Pav Bhaji");
        updateMealToScheduleInput.setMealDate(new Date(2023, 11, 10, 17, 10, 10));
        updateMealToScheduleInput.setSlot("lunch");
        updateMealToScheduleInput.setMaxOrderLimit(100L);
        updateMealToScheduleInput.setOrderDeadline(new Date(2023, 11, 10, 15, 10, 10));
        updateMealToScheduleInput.setImage("pavbhajiurl");
        updateMealToScheduleInput.setPrice(100.00);
        updateMealToScheduleInput.setScheduleId(1L);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(mealRepository.save(any(Meal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Meal> response = scheduleService.updateMealToSchedule(updateMealToScheduleInput);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Pav Bhaji", response.getBody().getName());
    }

    @Test
    public void deleteMealByIdTest() throws IOException {
        // Arrange
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        doNothing().when(mealRepository).deleteById(1L);
        // Act
        Meal mealResponse = scheduleService.deleteMealById(1L);
        // Assert
        assertEquals(mealResponse.getId(), 1L);
    }

    @Test
    public void getMealsByCookIdTest() throws IOException {
        // Arrange
        List<Meal> mealList = new ArrayList<>();
        mealList.add(meal);
        when(mealRepository.findMealsBySchedule_Cook_IdOrderByMealDateAsc(1L)).thenReturn(mealList);

        // Act
        List<Meal> mealResponse = scheduleService.getMealsByCookId(1L);
        // Assert
        assertEquals(mealResponse.get(0).getSchedule().getCook().getId(), 1L);
    }

    @Test
    public void getMealsByScheduleIdTest() throws IOException {
        // Arrange
        List<Meal> mealList = new ArrayList<>();
        mealList.add(meal);
        when(mealRepository.findMealsBySchedule_Id(1L)).thenReturn(mealList);

        // Act
        List<Meal> mealResponse = scheduleService.getMealsByScheduleId(1L);
        // Assert
        assertEquals(mealResponse.get(0).getSchedule().getCook().getId(), 1L);
    }

    @Test
    public void createDishTest() throws IOException {
        // Arrange

        CreateDish createDish = new CreateDish();
        createDish.setName("Bhaji");
        createDish.setType("Veg");
        createDish.setDescription("Bhaji of Pav Bhaji");
        createDish.setImage("dish image url");
        createDish.setCookId(1L);

        when(cookRepository.findById(1L)).thenReturn(Optional.of(cook));
        when(dishRepository.save(any(Dish.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Dish> response = scheduleService.createDish(createDish);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bhaji", response.getBody().getName());
    }

    @Test
    public void getDishByIdTest() throws IOException {
        // Arrange
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        // Act
        Dish dish = scheduleService.getDishById(1L);
        // Assert
        assertEquals(dish.getId(), 1L);
    }

    @Test
    public void updateDishTest() throws IOException {
        // Arrange

        UpdateDish updateDish = new UpdateDish();
        updateDish.setId(1L);
        updateDish.setName("Bhaji");
        updateDish.setType("Veg");
        updateDish.setDescription("Bhaji of Pav Bhaji");
        updateDish.setImage("dish image url");


        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishRepository.save(any(Dish.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Dish> response = scheduleService.updateDish(updateDish);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bhaji", response.getBody().getName());
    }

    @Test
    public void deleteDishByIdTest() throws IOException {
        // Arrange
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        doNothing().when(dishRepository).deleteById(1L);
        // Act
        Dish dishResponse = scheduleService.deleteDishById(1L);
        // Assert
        assertEquals(dishResponse.getId(), 1L);
    }

    @Test
    public void getMealsByCookDateRangeTest() throws IOException {
        // Arrange
        CookDateRangeInput cookDateRangeInput = new CookDateRangeInput();
        cookDateRangeInput.setStartDate(new Date(2023, 11, 9, 15, 10, 10));
        cookDateRangeInput.setEndDate(new Date(2023, 11, 12, 15, 10, 10));
        cookDateRangeInput.setId(1L);
        List<Meal> mealList = new ArrayList<>();
        mealList.add(meal);
        when(mealRepository.findAllByMealDateBetweenAndSchedule_Cook_Id(any(Date.class), any(Date.class), any(Long.class))).thenReturn(mealList);
        // Act
        List<Meal> mealResponse = scheduleService.getMealsByCookDateRange(cookDateRangeInput);
        // Assert
        assertEquals(mealResponse.get(0).getId(), 1L);
    }

    @Test
    public void addDishToMealTest() throws IOException {
        // Arrange

        AddDishToMealInput addDishToMealInput = new AddDishToMealInput();
        addDishToMealInput.setDish_id(1L);
        addDishToMealInput.setMeal_id(1L);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishToMealRepository.save(any(DishToMealMap.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<DishToMealMap> response = scheduleService.addDishToMeal(addDishToMealInput);
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void getDishesByMealIdTest() throws IOException {
        // Arrange

        DishToMealMap dishToMealMap = new DishToMealMap();
        DishToMealId id = new DishToMealId();
        id.setDishId(1L);
        id.setMealId(2L);
        dishToMealMap.setId(id);

        when(dishToMealRepository.findByIdMealId(2L)).thenReturn(Arrays.asList(dishToMealMap));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        // Act
        List<Dish> dishList = scheduleService.getDishesByMealId(2L);
        // Assert
        assertEquals(dishList.get(0).getId(), dish.getId());
    }

    @Test
    public void getMealOrdersByMealIdTest() throws IOException {
        // Arrange

        List<Mealorder> mealorderList = new ArrayList<>();
        Mealorder mealorder = new Mealorder();
        mealorder.setId(1L);
        mealorderList.add(mealorder);
        meal.setMealorders(mealorderList);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));

        // Act
        List<Mealorder> mealorders = scheduleService.getMealOrdersByMealId(1L);
        // Assert
        assertEquals(mealorders.size(), mealorderList.size());
    }

    @Test
    public void testMealOrder() throws IOException {
        // Arrange
        Mealorder mealorder = new Mealorder();
        mealorder.setId(1L);
        mealorder.setQuantity(4L);
        mealorder.setStatus("PENDING");
        mealorder.setAmount(100.00);
        mealorder.setMeal(meal);
        // Assert
        assertTrue(!Objects.isNull(mealorder));
        assertTrue(mealorder.getId() == 1L);
        assertTrue(!Objects.isNull(mealorder.getQuantity()));
        assertTrue(!Objects.isNull(mealorder.getStatus()));
        assertTrue(!Objects.isNull(mealorder.getAmount()));
        assertTrue(!Objects.isNull(mealorder.getMeal()));
    }

    @Test
    public void testDish() throws IOException {
        // Arrange
        Dish newDish = new Dish();
        Long id = 6L;
        String img = "dishUrl";
        String name = "chole";
        String description = "Chole Tiffin";
        String type = "Veg";
        newDish.setId(id);
        newDish.setName(name);
        newDish.setImage(img);
        newDish.setDescription(description);
        newDish.setType(type);
        newDish.setCook(cook);
        // Assert
        assertEquals(newDish.getId(), id);
        assertEquals(newDish.getImage(), img);
        assertEquals(newDish.getName(), name);
        assertEquals(newDish.getDescription(), description);
        assertEquals(newDish.getType(), type);
        assertEquals(newDish.getCook().getId(), cook.getId());
    }


}
