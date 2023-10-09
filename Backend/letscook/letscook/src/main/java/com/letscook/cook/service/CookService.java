package com.letscook.cook.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.repository.CookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    public Optional<Cook> getCook(Long id){
        return cookRepository.findById(id);
    }

    public Cook createCookProfile(Cook cook){
        return cookRepository.save(cook);
    }

    public List<Cook> getAllPendingCook() {
        String status = "Pending";
        return cookRepository.findAllByStatusIs(status);
    }
}
