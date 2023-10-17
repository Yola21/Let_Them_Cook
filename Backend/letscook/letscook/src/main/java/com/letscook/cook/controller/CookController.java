package com.letscook.cook.controller;

import com.letscook.cook.model.Cook;
import com.letscook.cook.repository.CookRepository;
import com.letscook.cook.service.CookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cooks")
public class CookController {

    @Autowired
    private CookService cookService;

    @Autowired
    private CookRepository cookRepository;

    @GetMapping()
    public List<Cook> getCook() {
        System.out.println("get mapping called");
        return cookRepository.findAll();}

    @GetMapping("/{id}")
    public Cook getCook(@PathVariable() Long id){
        return cookService.getCook(id);
    }

    @PostMapping("/createProfile")
    public ResponseEntity<Cook> createCookProfile(@RequestBody() Cook cook){
        System.out.println(cook);
        return cookService.createCookProfile(cook);
    }

    @GetMapping("/pendingCooks")
    public List<Cook> getAllPendingCooks(){
        return cookService.getAllPendingCook();
    }
}
