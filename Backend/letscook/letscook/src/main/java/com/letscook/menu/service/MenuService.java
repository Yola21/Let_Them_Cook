package com.letscook.menu.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.repository.CookRepository;
import com.letscook.menu.model.*;
import com.letscook.menu.repository.DishRepository;
import com.letscook.menu.repository.MealRepository;
import com.letscook.menu.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private DishRepository dishRepository;

    @Value("letscook/uploadedDocuments/menus")
    private String uploadMenuImageDirectory;

    @Value("letscook/uploadedDocuments/dishes")
    private String uploadDishImageDirectory;

    public ResponseEntity<Menu> createMenu(CreateMenuInput createMenuInput) throws IOException {
        Cook cook = cookRepository.findById(createMenuInput.getCookId()).orElseThrow(() -> new Error("Cook does not exists"));

        Menu menuToCreate = new Menu();
        menuToCreate.setName(createMenuInput.getName());
        menuToCreate.setPrice(createMenuInput.getPrice());
        menuToCreate.setLabel(createMenuInput.getLabel());
        menuToCreate.setCook(cook);

        Menu createMenu = menuRepository.save(menuToCreate);

    if (createMenuInput.getImage() != null) {
            uploadMenuImage(createMenu, createMenuInput.getImage());
        }
        Menu createdMenu = menuRepository.save(createMenu);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    private void uploadMenuImage(Menu createMenuInput, MultipartFile image) throws IOException {
        try {
            String fileName = createMenuInput.getId().toString() + "_" + createMenuInput.getName() + "_menuImage" + "." + getFileExtension(image.getOriginalFilename());
            String filePath = getFilePath(fileName, uploadMenuImageDirectory);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            image.transferTo(destFile);
            createMenuInput.setImage(filePath);
        } catch (IOException error) {
            throw new IOException(error);
        }
    }

    private String getFilePath(String fileName, String uploadCookDirectory) {
        return Paths.get(uploadCookDirectory, fileName).toAbsolutePath().normalize().toString();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public Menu deleteMenuById(Long id) {
        Menu menuToDelete = menuRepository.findById(id).orElseThrow();
        menuRepository.deleteById(id);
        return menuToDelete;
    }

    public List<Menu> getMenusByCook(Long cookId) {
        return menuRepository.findAllByCook_Id(cookId);
    }

    public ResponseEntity<Menu> updateMenu(UpdateMenuInput updateMenuInput) throws IOException {

        Menu menuToUpdate = menuRepository.findById(updateMenuInput.getId()).orElseThrow();
        if(updateMenuInput.getName() != null){
            menuToUpdate.setName(updateMenuInput.getName());
        }
        if(updateMenuInput.getPrice() != null){
            menuToUpdate.setPrice(updateMenuInput.getPrice());
        }
        if(updateMenuInput.getLabel() != null) {
            menuToUpdate.setLabel(updateMenuInput.getLabel());
        }
        if (updateMenuInput.getImage() != null) {
            uploadMenuImage(menuToUpdate, updateMenuInput.getImage());
        }
        Menu updatedMenu = menuRepository.save(menuToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedMenu);
    }

    public ResponseEntity<Meal> addMealToMenu(AddMealToMenuInput addMealToMenuInput) {
        Menu menu = menuRepository.findById(addMealToMenuInput.getMenuId()).orElseThrow();
        Meal meal = new Meal();
        meal.setName(addMealToMenuInput.getName());
        meal.setMealDate(addMealToMenuInput.getMealDate());
        meal.setSlot(addMealToMenuInput.getSlot());
        meal.setMaxOrderLimit(addMealToMenuInput.getMaxOrderLimit());
        meal.setOrderDeadline(addMealToMenuInput.getOrderDeadline());
        meal.setMenu(menu);
        Meal createdMeal = mealRepository.save(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeal);
    }

    public ResponseEntity<Meal> updateMealToMenu(UpdateMealToMenuInput updateMealToMenuInput) {
        Meal updateToMeal = mealRepository.findById(updateMealToMenuInput.getId()).orElseThrow();
        if (updateMealToMenuInput.getMenuId() != null) {
            Menu menu = menuRepository.findById(updateMealToMenuInput.getMenuId()).orElseThrow();
            updateToMeal.setMenu(menu);
        }
        if(updateMealToMenuInput.getName() != null) {
            updateToMeal.setName(updateMealToMenuInput.getName());
        }
        if (updateMealToMenuInput.getMealDate() != null) {
            updateToMeal.setMealDate(updateMealToMenuInput.getMealDate());
        }
        if (updateMealToMenuInput.getSlot() != null) {
            updateToMeal.setSlot(updateMealToMenuInput.getSlot());
        }
        if (updateMealToMenuInput.getMaxOrderLimit() != null) {
            updateToMeal.setMaxOrderLimit(updateMealToMenuInput.getMaxOrderLimit());
        }
        if (updateMealToMenuInput.getOrderDeadline() != null) {
            updateToMeal.setOrderDeadline(updateMealToMenuInput.getOrderDeadline());
        }
        Meal updatedMeal = mealRepository.save(updateToMeal);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedMeal);
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElse(null);
    }

    public List<Meal> getMealsByCookId(Long cookId) {
        return mealRepository.findMealsByMenu_Cook_Id(cookId);
    }

    public Meal deleteMealById(Long id) {
        Meal mealToDelete = mealRepository.findById(id).orElseThrow();
        mealRepository.deleteById(id);
        return mealToDelete;
    }

    public byte[] getMenuImage(Long id) throws IOException {

        Menu menu = menuRepository.findById(id).orElse(null);
        String path = menu.getImage();
        File destFile = new File(path);
        byte[] res = Files.readAllBytes(destFile.toPath());
        return res;
    }

    public ResponseEntity<Dish> addDishToMeal(AddDishToMealInput addDishToMealInput) throws IOException {
        Meal meal = mealRepository.findById(addDishToMealInput.getMealId()).orElseThrow();
        Dish dish = new Dish();
        dish.setName(addDishToMealInput.getName());
        dish.setType(addDishToMealInput.getType());
        dish.setDescription(addDishToMealInput.getDescription());
        dish.setMeal_id(meal);

        Dish createDish = dishRepository.save(dish);

        if (addDishToMealInput.getImage() != null) {
            uploadDishImage(createDish, addDishToMealInput.getImage());
        }

        Dish createdDish = dishRepository.save(createDish);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Dish> updateDishToMeal(UpdateDishToMealInput updateDishToMealInput) throws IOException {
        Dish dishToUpdate = dishRepository.findById(updateDishToMealInput.getId()).orElseThrow();
        if (updateDishToMealInput.getMealId() != null) {
            Meal meal = mealRepository.findById(updateDishToMealInput.getMealId()).orElseThrow();
            dishToUpdate.setMeal_id(meal);
        }
        if (updateDishToMealInput.getName() != null) {
            dishToUpdate.setName(updateDishToMealInput.getName());
        }
        if (updateDishToMealInput.getDescription() != null) {
            dishToUpdate.setDescription(updateDishToMealInput.getDescription());
        }
        if (updateDishToMealInput.getType() != null) {
            dishToUpdate.setType(updateDishToMealInput.getType());
        }
        if (updateDishToMealInput.getImage() != null) {
            uploadDishImage(dishToUpdate, updateDishToMealInput.getImage());
        }
        Dish updatedDish = dishRepository.save(dishToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedDish);
    }


    private void uploadDishImage(Dish createDishInput, MultipartFile image) throws IOException {
        try {
            String fileName = createDishInput.getId().toString() + "_" + createDishInput.getName()
                    + "_dishImage" + "." + getFileExtension(image.getOriginalFilename());
            String filePath = getFilePath(fileName, uploadDishImageDirectory);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            image.transferTo(destFile);
            createDishInput.setImage(filePath);
        } catch (IOException error) {
            throw new IOException(error);
        }
    }

    public byte[] getDishImage(Long id) throws IOException {

        Dish dish = dishRepository.findById(id).orElse(null);
        String path = dish.getImage();
        File destFile = new File(path);
        byte[] res = Files.readAllBytes(destFile.toPath());
        return res;
    }

    public Dish deleteDishById(Long id) {
        Dish dishToDelete = dishRepository.findById(id).orElseThrow();
        dishRepository.deleteById(id);
        return dishToDelete;
    }

    public List<Meal> getMealsByCookAddress(String address) {
        return mealRepository.findMealsByMenu_Cook_Address(address);
    }

    public List<Meal> getMealsByCookDateRange(CookDateRangeInput cookDateRangeInput) {
        return mealRepository.findAllByMealDateBetweenAndMenu_Cook_Id(cookDateRangeInput.getStartDate(),
                cookDateRangeInput.getEndDate(),cookDateRangeInput.getId());
    }
}

