package com.letscook.menu.service;

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
import com.letscook.order.model.Mealorder;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private DishToMealRepository dishToMealRepository;

    @Value("letscook/uploadedDocuments/schedules")
    private String uploadScheduleImageDirectory;

    @Value("letscook/uploadedDocuments/dishes")
    private String uploadDishImageDirectory;

    public ResponseEntity<Schedule> createSchedule(CreateScheduleInput createScheduleInput) throws IOException {
        Cook cook = cookRepository.findById(createScheduleInput.getCookId()).orElseThrow(() -> new Error("Cook does not exists"));

        Schedule scheduleToCreate = new Schedule();
        scheduleToCreate.setName(createScheduleInput.getName());
        scheduleToCreate.setStart_date(createScheduleInput.getStart_date());
        scheduleToCreate.setCook(cook);

        Schedule createdSchedule = scheduleRepository.save(scheduleToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }

//    private void uploadMenuImage(Schedule createScheduleInput, MultipartFile image) throws IOException {
//        try {
//            String fileName = createScheduleInput.getId().toString() + "_" + createScheduleInput.getName() + "_menuImage" + "." + getFileExtension(image.getOriginalFilename());
//            String filePath = getFilePath(fileName, uploadScheduleImageDirectory);
//            File destFile = new File(filePath);
//            destFile.getParentFile().mkdirs();
//            image.transferTo(destFile);
//            //createScheduleInput.setImage(filePath);
//        } catch (IOException error) {
//            throw new IOException(error);
//        }
//    }

//    private String getFilePath(String fileName, String uploadCookDirectory) {
//        return Paths.get(uploadCookDirectory, fileName).toAbsolutePath().normalize().toString();
//    }
//
//    private String getFileExtension(String fileName) {
//        if (fileName == null) {
//            return null;
//        }
//        String[] fileNameParts = fileName.split("\\.");
//
//        return fileNameParts[fileNameParts.length - 1];
//    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule deleteScheduleById(Long id) {
        Schedule scheduleToDelete = scheduleRepository.findById(id).orElseThrow();
        scheduleRepository.deleteById(id);
        return scheduleToDelete;
    }

    public List<Schedule> getSchedulesByCook(Long cookId) {
        return scheduleRepository.findAllByCook_Id(cookId);
    }

    public ResponseEntity<Schedule> updateSchedule(UpdateScheduleInput updateScheduleInput) throws IOException {

        Schedule scheduleToUpdate = scheduleRepository.findById(updateScheduleInput.getId()).orElseThrow();
        if (updateScheduleInput.getName() != null) {
            scheduleToUpdate.setName(updateScheduleInput.getName());
        }
        if (updateScheduleInput.getStart_date() != null) {
            scheduleToUpdate.setStart_date(updateScheduleInput.getStart_date());
        }
        Schedule updatedSchedule = scheduleRepository.save(scheduleToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedSchedule);
    }

    public ResponseEntity<Meal> addMealToSchedule(AddMealToScheduleInput addMealToScheduleInput) {
        Schedule schedule = scheduleRepository.findById(addMealToScheduleInput.getScheduleId()).orElseThrow();
        Meal meal = new Meal();
        meal.setName(addMealToScheduleInput.getName());
        meal.setMealDate(addMealToScheduleInput.getMealDate());
        meal.setSlot(addMealToScheduleInput.getSlot());
        meal.setMaxOrderLimit(addMealToScheduleInput.getMaxOrderLimit());
        meal.setOrderDeadline(addMealToScheduleInput.getOrderDeadline());
        meal.setImage(addMealToScheduleInput.getImage());
        meal.setPrice(addMealToScheduleInput.getPrice());
        meal.setSchedule(schedule);
        Meal createdMeal = mealRepository.save(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeal);
    }

    public ResponseEntity<Meal> updateMealToSchedule(UpdateMealToScheduleInput updateMealToScheduleInput) {
        Meal updateToMeal = mealRepository.findById(updateMealToScheduleInput.getId()).orElseThrow();
        if (updateMealToScheduleInput.getScheduleId() != null) {
            Schedule schedule = scheduleRepository.findById(updateMealToScheduleInput.getScheduleId()).orElseThrow();
            updateToMeal.setSchedule(schedule);
        }
        if (updateMealToScheduleInput.getName() != null) {
            updateToMeal.setName(updateMealToScheduleInput.getName());
        }
        if (updateMealToScheduleInput.getMealDate() != null) {
            updateToMeal.setMealDate(updateMealToScheduleInput.getMealDate());
        }
        if (updateMealToScheduleInput.getSlot() != null) {
            updateToMeal.setSlot(updateMealToScheduleInput.getSlot());
        }
        if (updateMealToScheduleInput.getMaxOrderLimit() != null) {
            updateToMeal.setMaxOrderLimit(updateMealToScheduleInput.getMaxOrderLimit());
        }
        if (updateMealToScheduleInput.getOrderDeadline() != null) {
            updateToMeal.setOrderDeadline(updateMealToScheduleInput.getOrderDeadline());
        }
        if (updateMealToScheduleInput.getImage() != null) {
            updateToMeal.setImage(updateMealToScheduleInput.getImage());
        }
        if (updateMealToScheduleInput.getPrice() != null) {
            updateToMeal.setPrice(updateMealToScheduleInput.getPrice());
        }
        Meal updatedMeal = mealRepository.save(updateToMeal);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedMeal);
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElse(null);
    }

    public List<Meal> getMealsByCookId(Long cookId) {
        return mealRepository.findMealsBySchedule_Cook_Id(cookId);
    }

    public List<Meal> getMealsByScheduleId(Long scheduleId) {
        return mealRepository.findMealsBySchedule_Id(scheduleId);
    }

    public Meal deleteMealById(Long id) {
        Meal mealToDelete = mealRepository.findById(id).orElseThrow();
        mealRepository.deleteById(id);
        return mealToDelete;
    }

//    public byte[] getMenuImage(Long id) throws IOException {
//
//        Schedule schedule = scheduleRepository.findById(id).orElse(null);
//        String path = schedule.getImage();
//        File destFile = new File(path);
//        byte[] res = Files.readAllBytes(destFile.toPath());
//        return res;
//    }

    public ResponseEntity<Dish> createDish(CreateDish createDish) throws IOException {
        Cook cook = cookRepository.findById(createDish.getCookId()).orElseThrow();
        Dish dish = new Dish();
        dish.setName(createDish.getName());
        dish.setType(createDish.getType());
        dish.setDescription(createDish.getDescription());
//        dish.setMeal_id(meal);
        dish.setImage(createDish.getImage());
        dish.setCook(cook);
        Dish createdDish = dishRepository.save(dish);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Dish> updateDish(UpdateDish updateDish) throws IOException {
        Dish dishToUpdate = dishRepository.findById(updateDish.getId()).orElseThrow();
//        if (updateDishToMealInput.getMealId() != null) {
//            Meal meal = mealRepository.findById(updateDishToMealInput.getMealId()).orElseThrow();
//            dishToUpdate.setMeal_id(meal);
//        }
        if (updateDish.getName() != null) {
            dishToUpdate.setName(updateDish.getName());
        }
        if (updateDish.getDescription() != null) {
            dishToUpdate.setDescription(updateDish.getDescription());
        }
        if (updateDish.getType() != null) {
            dishToUpdate.setType(updateDish.getType());
        }
        if (updateDish.getImage() != null) {
            dishToUpdate.setImage(updateDish.getImage());
        }
        Dish updatedDish = dishRepository.save(dishToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedDish);
    }

    public Dish deleteDishById(Long id) {
        Dish dishToDelete = dishRepository.findById(id).orElseThrow();
        dishRepository.deleteById(id);
        return dishToDelete;
    }

    public List<Meal> getMealsByCookDateRange(CookDateRangeInput cookDateRangeInput) {
        return mealRepository.findAllByMealDateBetweenAndSchedule_Cook_Id(cookDateRangeInput.getStartDate(),
                cookDateRangeInput.getEndDate(), cookDateRangeInput.getId());
    }

    public ResponseEntity<DishToMealMap> addDishToMeal(AddDishToMealInput addDishToMealInput) throws IOException {
        Meal meal = mealRepository.findById(addDishToMealInput.getMeal_id()).orElseThrow();
        Dish dish = dishRepository.findById(addDishToMealInput.getDish_id()).orElseThrow();
        DishToMealMap dishToMealMap = new DishToMealMap();
        DishToMealId dishToMealId = new DishToMealId();
        dishToMealId.setMealId(meal.getId());
        dishToMealId.setDishId(dish.getId());
        dishToMealMap.setId(dishToMealId);
        DishToMealMap createdDishToMealMap = dishToMealRepository.save(dishToMealMap);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDishToMealMap);
    }

    public List<Dish> getDishesByMealId(Long mealId) {
        List<DishToMealMap> dishToMealMaps = dishToMealRepository.findByIdMealId(mealId);
        List<Dish> dishes = new ArrayList<>();
        for (DishToMealMap dishToMealMap : dishToMealMaps) {
            dishes.add(dishRepository.findById(dishToMealMap.getId().getDishId()).orElseThrow());
        }
        return dishes;
    }

    public List<Mealorder> getMealOrdersByMealId(Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Meal not found with ID: " + id));
        return meal.getMealorders();
    }
}

