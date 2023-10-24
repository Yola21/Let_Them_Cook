package com.letscook.cook.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    @Value("${cook.profile.photo.upload.directory}")
    private String uploadCookProfileDirectory;

    @Value("${cook.business.document.upload.directory}")
    private String uploadCookBusinessDocumentDirectory;

    public Cook getCook(Long id) {
        return cookRepository.findById(id).orElse(null);
    }

    public List<Cook> getCooks() {
        return cookRepository.findAll();
    }

    public ResponseEntity<Cook> createCookProfile(CreateCookProfileInput createCookProfileInput) throws IOException {
        Cook cookToUpdate = new Cook();
        cookToUpdate.setId(createCookProfileInput.getUserId());
        cookToUpdate.setAddress(createCookProfileInput.getAddress());
        cookToUpdate.setBusinessName(createCookProfileInput.getBusinessName());
        cookToUpdate.setStatus("pending");
        if (createCookProfileInput.getProfilePhoto() != null) {
            uploadCookProfilePhoto(cookToUpdate, createCookProfileInput.getProfilePhoto());
        }
        if (createCookProfileInput.getBusinessDocument() != null) {
            uploadBusinessDocument(cookToUpdate, createCookProfileInput.getBusinessDocument());
        }
        Cook updatedCook = cookRepository.save(cookToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCook);
    }

    public List<Cook> getAllPendingCook() {
        String status = "Pending";
        return cookRepository.findAllByStatusIs(status);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    public ResponseEntity<Cook> updateCookProfile(UpdateCookProfileInput updateCookProfileInput) throws IOException {

        Cook cookToUpdate = cookRepository.findById(updateCookProfileInput.getId()).orElseThrow(() -> new EntityNotFoundException("Cook not found with ID: " + updateCookProfileInput.getId()));

        if (updateCookProfileInput.getAddress() != null) {
            cookToUpdate.setAddress(updateCookProfileInput.getAddress());
        }

        if (updateCookProfileInput.getProfilePhoto() != null) {
            uploadCookProfilePhoto(cookToUpdate, updateCookProfileInput.getProfilePhoto());
        }

        if (cookToUpdate.getStatus().equals("Rejected") && updateCookProfileInput.getBusinessDocument() != null) {
            uploadBusinessDocument(cookToUpdate, updateCookProfileInput.getBusinessDocument());
        }

        if (!Objects.equals(cookToUpdate.getStatus(), "Rejected") && updateCookProfileInput.getBusinessDocument() != null) {
            throw new Error("Not allowed to change business document");
        }

        Cook updatedCook = cookRepository.save(cookToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCook);
    }

    private void uploadBusinessDocument(Cook cookToUpdate, MultipartFile businessDocument) throws IOException {
        try {
            String fileName = cookToUpdate.getId().toString() + "_" + cookToUpdate.getBusinessName() + "_businessDocument" + "." + getFileExtension(businessDocument.getOriginalFilename());
            String filePath = getFilePath(fileName, uploadCookBusinessDocumentDirectory);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            businessDocument.transferTo(destFile);
            cookToUpdate.setBusinessDocument(filePath);
        } catch (IOException error) {
            throw new IOException(error);
        }
    }

    private void uploadCookProfilePhoto(Cook cookToUpdate, MultipartFile profilePhoto) throws IOException {
        try {
            String fileName = cookToUpdate.getId().toString() + "_" + cookToUpdate.getBusinessName() + "_profilePhoto" + "." + getFileExtension(profilePhoto.getOriginalFilename());
            String filePath = getFilePath(fileName, uploadCookProfileDirectory);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            profilePhoto.transferTo(destFile);
            cookToUpdate.setProfilePhoto(filePath);
        } catch (IOException error) {
            throw new IOException(error);
        }
    }

    private String getFilePath(String fileName, String uploadCookDirectory) {
        return Paths.get(uploadCookDirectory, fileName).toAbsolutePath().normalize().toString();
    }
}
