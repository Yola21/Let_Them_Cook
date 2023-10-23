package com.letscook.cook.controller;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import com.letscook.cook.service.CookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cooks")
public class CookController {

    @Autowired
    private CookService cookService;

    @GetMapping()
    public List<Cook> getCook() {
        return cookService.getCooks();
    }

    @GetMapping("/{id}")
    public Cook getCook(@PathVariable() Long id){
        return cookService.getCook(id);
    }

    @PostMapping("/createProfile")
    public ResponseEntity<Cook> createCookProfile(@ModelAttribute() CreateCookProfileInput createCookProfileInput) throws IOException {
        return cookService.createCookProfile(createCookProfileInput);
    }

    @GetMapping("/pendingCooks")
    public List<Cook> getAllPendingCooks(){
        return cookService.getAllPendingCook();
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<Cook> updateCookProfile(@ModelAttribute() UpdateCookProfileInput updateCookProfileInput) throws IOException {
        return cookService.updateCookProfile(updateCookProfileInput);
    }
}
