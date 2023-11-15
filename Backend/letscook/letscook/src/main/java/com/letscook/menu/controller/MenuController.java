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

    @PostMapping("/createMenu")
    public ResponseEntity<Menu> createMenu(@ModelAttribute() CreateMenuInput createMenuInput) throws IOException {
        return menuService.createMenu(createMenuInput);
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable() Long id){
        return menuService.getMenuById(id);
    }

    @GetMapping("/cook")
    public List<Menu> getMenuByCookId(@RequestParam Long cookId){
        return menuService.getMenusByCook(cookId);
    }

    @PostMapping("/updateMenu")
    public ResponseEntity<Menu> updateMenu(@ModelAttribute() UpdateMenuInput updateMenuInput) throws IOException {
        return menuService.updateMenu(updateMenuInput);
    }

    @DeleteMapping("/deleteMenu/{id}")
    public Menu deleteMenu(@PathVariable() Long id){
        return menuService.deleteMenuById(id);
    }

    @PostMapping("/addMealToMenu")
    public ResponseEntity<Meal> addMealToMenu(@RequestBody() AddMealToMenuInput addMealToMenuInput) throws IOException {
        return menuService.addMealToMenu(addMealToMenuInput);
    }

    @PostMapping("/updateMealToMenu")
    public ResponseEntity<Meal> updateMealToMenu(@RequestBody() UpdateMealToMenuInput updateMealToMenuInput) throws IOException {
        return menuService.updateMealToMenu(updateMealToMenuInput);
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

    @GetMapping("/menuImage/{id}")
    public ResponseEntity<byte[]> getMenuImage(@PathVariable() Long id) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(menuService.getMenuImage(id));
    }

    @PostMapping("/addDishToMeal")
    public ResponseEntity<Dish> addDishToMeal(@ModelAttribute() AddDishToMealInput addDishToMealInput) throws IOException {
        return menuService.addDishToMeal(addDishToMealInput);
    }

    @GetMapping("/dish/{id}")
    public Dish getDishById(@PathVariable() Long id){
        return menuService.getDishById(id);
    }

    @GetMapping("/dishImage/{id}")
    public ResponseEntity<byte[]> getDishImage(@PathVariable() Long id) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(menuService.getDishImage(id));
    }

    @PostMapping("/updateDishToMeal")
    public ResponseEntity<Dish> updateDishToMeal(@ModelAttribute() UpdateDishToMealInput updateDishToMealInput) throws IOException {
        return menuService.updateDishToMeal(updateDishToMealInput);
    }

    @DeleteMapping("/deleteDish/{id}")
    public Dish deleteDish(@PathVariable() Long id){
        return menuService.deleteDishById(id);
    }

    @GetMapping("/meal/address")
    public List<Meal> getMealsByCookAddress(@RequestParam() String address){
        return menuService.getMealsByCookAddress(address);
    }

    @PostMapping("/meal/cookdaterange")
    public List<Meal> getMealsByCookDateRange(@RequestBody() CookDateRangeInput cookDateRangeInput){
        return menuService.getMealsByCookDateRange(cookDateRangeInput);
    }
}
