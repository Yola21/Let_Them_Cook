package com.letscook.cook.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    public Cook getCook(Long id){
        return cookRepository.findById(id).orElse(null);
    }

    public List<Cook> getCooks() {
        return cookRepository.findAll();
    }

    public ResponseEntity<Cook> createCookProfile(Cook cook){
        Cook savedCook = cookRepository.save(cook);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCook);
    }

    public List<Cook> getAllPendingCook() {
        String status = "Pending";
        return cookRepository.findAllByStatusIs(status);
    }

    public ResponseEntity<Cook> updateCookProfile(UpdateCookProfileInput updateCookProfileInput){

        Cook cookToUpdate = cookRepository.findById(updateCookProfileInput.getId()).orElseThrow(() -> new EntityNotFoundException("Cook not found with ID: " + updateCookProfileInput.getId()));
        if(updateCookProfileInput.getAddress() != null){
            cookToUpdate.setAddress(updateCookProfileInput.getAddress());
        }
        if(updateCookProfileInput.getProfilePhoto() != null){
            cookToUpdate.setProfilePhoto(updateCookProfileInput.getProfilePhoto());
        }
        Cook updatedCook = cookRepository.save(cookToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCook);
    }
}
