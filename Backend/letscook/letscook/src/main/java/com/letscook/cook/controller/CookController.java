package com.letscook.cook.controller;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.service.CookService;
import com.letscook.menu.model.dish.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/cooks")
public class CookController {

    @Autowired
    private CookService cookService;

    @GetMapping()
    public List<Cook> getCooks() {
        return cookService.getCooks();
    }

    @GetMapping("/{id}")
    public Cook getCook(@PathVariable() Long id) {
        return cookService.getCook(id);
    }

    @PostMapping("/createProfile")
    public ResponseEntity<Cook> createCookProfile(@ModelAttribute() CreateCookProfileInput createCookProfileInput) throws IOException {
        return cookService.createCookProfile(createCookProfileInput);
    }

    @GetMapping("/pendingCooks")
    public List<Cook> getAllPendingCooks() {
        return cookService.getAllPendingCook();
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<Cook> updateCookProfile(@ModelAttribute() UpdateCookProfileInput updateCookProfileInput) throws IOException {
        return cookService.updateCookProfile(updateCookProfileInput);
    }

    @GetMapping("/profilephoto/{id}")
    public ResponseEntity<byte[]> getCookProfile(@PathVariable() Long id) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(cookService.getProfilePhoto(id));
    }

    @GetMapping("/bannerimage/{id}")
    public ResponseEntity<byte[]> getCookBanner(@PathVariable() Long id) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(cookService.getBannerPhoto(id));
    }

    @GetMapping("/getDishes/{id}")
    public List<Dish> getDishesByCookId(@PathVariable() Long id) {
        return cookService.getDishesByCookId(id);
    }
}
