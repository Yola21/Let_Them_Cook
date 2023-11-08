package com.letscook.menu.controller;

import com.letscook.menu.model.*;
import com.letscook.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/createDish")
    public ResponseEntity<Menu> createDish(@ModelAttribute() CreateDishInput createDishInput) throws IOException {
        return menuService.createDish(createDishInput);
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable() Long id){
        return menuService.getMenuById(id);
    }

    @GetMapping("/cook")
    public List<Menu> getMenuByCookId(@RequestParam Long cookId){
        return menuService.getMenusByCook(cookId);
    }

    @PostMapping("/updateDish")
    public ResponseEntity<Menu> updateDish(@ModelAttribute() UpdateDishInput updateDishInput) throws IOException {
        return menuService.updateDish(updateDishInput);
    }

    @DeleteMapping("/deleteDish/{id}")
    public Menu deleteDish(@PathVariable() Long id){
        return menuService.deleteMenuById(id);
    }

    @PostMapping("/addDishToMeal")
    public ResponseEntity<Meal> addMealToDish(@RequestBody() AddDishToMealInput addDishToMealInput) throws IOException {
        return menuService.addDishToMeal(addDishToMealInput);
    }

    @PostMapping("/addDishToMeal")
    public ResponseEntity<Meal> updateMealToDish(@RequestBody() UpdateDishToMealInput updateDishToMealInput) throws IOException {
        return menuService.updateDishToMeal(updateDishToMealInput);
    }

    @GetMapping("/meal/{id}")
    public Meal getMealById(@PathVariable() Long id){
        return menuService.getMealById(id);
    }

    @GetMapping("/meal/cook")
    public List<Meal> getMealsByCookId(@RequestParam() Long id){
        return menuService.getMealsByCookId(id);
    }

    @DeleteMapping("/deleteMeal/{id}")
    public Meal deleteMeal(@PathVariable() Long id){
        return menuService.deleteMealById(id);
    }

    @GetMapping("/dishImage/{id}")
    public ResponseEntity<byte[]> getDishImage(@PathVariable() Long id) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(menuService.getDishImage(id));
    }
}
