package com.letscook.menu.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import com.letscook.menu.model.*;
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
import java.nio.file.Paths;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CookRepository cookRepository;

    @Autowired
    private MealRepository mealRepository;

    @Value("letscook/uploadedDocuments/menus")
    private String uploadDishImageDirectory;

    public ResponseEntity<Menu> createDish(CreateDishInput createDishInput) throws IOException {
        Cook cook = cookRepository.findById(createDishInput.getCookId()).orElseThrow(() -> new Error("Cook does not exists"));

        Menu menuToCreate = new Menu();
        menuToCreate.setName(createDishInput.getName());
        menuToCreate.setPrice(createDishInput.getPrice());
        menuToCreate.setLabel(createDishInput.getLabel());
        menuToCreate.setCook(cook);

        Menu createMenu = menuRepository.save(menuToCreate);

    if (createDishInput.getImage() != null) {
            uploadDishImage(createMenu, createDishInput.getImage());
        }
        Menu createdMenu = menuRepository.save(createMenu);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    private void uploadDishImage(Menu createDishInput, MultipartFile image) throws IOException {
        try {
            String fileName = createDishInput.getId().toString() + "_" + createDishInput.getName() + "_dishImage" + "." + getFileExtension(image.getOriginalFilename());
            String filePath = getFilePath(fileName, uploadDishImageDirectory);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            image.transferTo(destFile);
            createDishInput.setImage(filePath);
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

    public ResponseEntity<Menu> updateDish(UpdateDishInput updateDishInput) throws IOException {

        Menu menuToUpdate = menuRepository.findById(updateDishInput.getId()).orElseThrow();
        if(updateDishInput.getName() != null){
            menuToUpdate.setName(updateDishInput.getName());
        }
        if(updateDishInput.getPrice() != null){
            menuToUpdate.setPrice(updateDishInput.getPrice());
        }
        if(updateDishInput.getLabel() != null) {
            menuToUpdate.setLabel(updateDishInput.getLabel());
        }
        if (updateDishInput.getImage() != null) {
            uploadDishImage(menuToUpdate, updateDishInput.getImage());
        }
        Menu updatedMenu = menuRepository.save(menuToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedMenu);
    }

    public ResponseEntity<Meal> addDishToMeal(AddDishToMealInput addDishToMealInput) {
        Menu dish = menuRepository.findById(addDishToMealInput.getMenuId()).orElseThrow();
        Meal meal = new Meal();
        meal.setMealDate(addDishToMealInput.getMealDate());
        meal.setSlot(addDishToMealInput.getSlot());
        meal.setMaxOrderLimit(addDishToMealInput.getMaxOrderLimit());
        meal.setOrderDeadline(addDishToMealInput.getOrderDeadline());
        meal.setMenu(dish);
        Meal createdMeal = mealRepository.save(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeal);
    }
}

