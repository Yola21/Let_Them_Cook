package com.letscook.cook.controller;

import com.letscook.cook.model.Cook;
import com.letscook.cook.service.CookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cooks")
public class CookController {

    @Autowired
    private CookService cookService;

    @GetMapping("/{id}")
    public Optional<Cook> getCook(@PathVariable() Long id){
        return cookService.getCook(id);
    }

    @PostMapping("/createProfile")
    public Cook createCookProfile(@RequestBody() Cook cook){
        return cookService.createCookProfile(cook);
    }

    @GetMapping("/pendingCooks")
    public List<Cook> getAllPendingCooks(){
        return cookService.getAllPendingCook();
    }
}
