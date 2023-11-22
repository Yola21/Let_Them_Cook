package com.letscook.cook.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import com.letscook.enums.CookStatus;
import com.letscook.menu.model.Dish;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    public Cook getCook(Long id) {
        return cookRepository.findById(id).orElse(null);
    }

    public List<Cook> getCooks() {
        return cookRepository.findAll();
    }

    public List<Cook> getCooksByName(String businessName) {
        String search = "%" + businessName + "%";
        return cookRepository.findAllByBusinessNameIsLikeIgnoreCase(search);
    }

    public ResponseEntity<Cook> createCookProfile(CreateCookProfileInput createCookProfileInput) throws IOException {
        Cook cookToUpdate = new Cook();
        cookToUpdate.setId(createCookProfileInput.getUserId());
        cookToUpdate.setAddress(createCookProfileInput.getAddress());
        cookToUpdate.setBusinessName(createCookProfileInput.getBusinessName());
        cookToUpdate.setStatus(String.valueOf(CookStatus.PENDING));
        if (createCookProfileInput.getProfilePhoto() != null) {
            cookToUpdate.setProfilePhoto(createCookProfileInput.getProfilePhoto());
        }
        if (createCookProfileInput.getBannerImage() != null) {
            cookToUpdate.setBannerImage(createCookProfileInput.getBannerImage());
        }
        if (createCookProfileInput.getBusinessDocument() != null) {
            cookToUpdate.setBusinessDocument(createCookProfileInput.getBusinessDocument());
        }
        Cook updatedCook = cookRepository.save(cookToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCook);
    }

    public List<Cook> getAllPendingCook() {
        return cookRepository.findAllByStatusIs(String.valueOf(CookStatus.PENDING));
    }

    public ResponseEntity<Cook> updateCookProfile(UpdateCookProfileInput updateCookProfileInput) throws IOException {

        Cook cookToUpdate = cookRepository.findById(updateCookProfileInput.getId()).orElseThrow(() -> new EntityNotFoundException("Cook not found with ID: " + updateCookProfileInput.getId()));

        if (updateCookProfileInput.getAddress() != null) {
            cookToUpdate.setAddress(updateCookProfileInput.getAddress());
        }

        if (updateCookProfileInput.getProfilePhoto() != null) {
            cookToUpdate.setProfilePhoto(updateCookProfileInput.getProfilePhoto());
        }
        if (updateCookProfileInput.getBannerImage() != null) {
            cookToUpdate.setBannerImage(updateCookProfileInput.getBannerImage());
        }
        if (updateCookProfileInput.getBusinessDocument() != null) {
            cookToUpdate.setBusinessDocument(updateCookProfileInput.getBusinessDocument());
        }

        if (!cookToUpdate.getStatus().equals(String.valueOf(CookStatus.REJECTED)) && updateCookProfileInput.getBusinessDocument() != null) {
            throw new Error("Not allowed to change business document");
        }

        Cook updatedCook = cookRepository.save(cookToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCook);
    }

    public List<Dish> getDishesByCookId(Long id) {
        Cook cook = cookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Cook not found with ID: " + id));
        return cook.getDishes();
    }
}
