package com.letscook.cook.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.repository.CookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    public Cook getCook(Long id){
        return cookRepository.findById(id).orElse(null);
    }

    public ResponseEntity<Cook> createCookProfile(Cook cook){
        System.out.println(cook.getEmail());
        System.out.println(cookRepository.save(cook));
        Cook savedCook = cookRepository.save(cook);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCook);
    }

    public List<Cook> getAllPendingCook() {
        String status = "Pending";
        return cookRepository.findAllByStatusIs(status);
    }
}
