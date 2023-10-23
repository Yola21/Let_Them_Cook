package com.letscook.menu.controller;

import com.letscook.cook.model.Cook;
import com.letscook.menu.model.CreateDishInput;
import com.letscook.menu.model.Menu;
import com.letscook.menu.model.UpdateDishInput;
import com.letscook.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
