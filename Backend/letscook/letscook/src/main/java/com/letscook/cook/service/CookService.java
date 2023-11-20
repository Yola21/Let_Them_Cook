package com.letscook.cook.service;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import com.letscook.enums.CookStatus;
import com.letscook.menu.model.dish.Dish;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class CookService {
    @Autowired
    private CookRepository cookRepository;

    @Value("${cook.profile.photo.upload.directory}")
    private String uploadCookProfileDirectory;

    @Value("${cook.banner.image.upload.directory}")
    private String uploadCookBannerImageDirectory;

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
        cookToUpdate.setStatus(String.valueOf(CookStatus.PENDING));
        if (createCookProfileInput.getProfilePhoto() != null) {
            cookToUpdate.setProfilePhoto(Arrays.toString(createCookProfileInput.getProfilePhoto().getBytes()));
            uploadCookProfilePhoto(cookToUpdate, createCookProfileInput.getProfilePhoto());
        }
        if (createCookProfileInput.getBannerImage() != null) {
            uploadCookBannerImage(cookToUpdate, createCookProfileInput.getBannerImage());
        }
        if (createCookProfileInput.getBusinessDocument() != null) {
            uploadBusinessDocument(cookToUpdate, createCookProfileInput.getBusinessDocument());
        }
        Cook updatedCook = cookRepository.save(cookToUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCook);
    }

    public List<Cook> getAllPendingCook() {
        return cookRepository.findAllByStatusIs(String.valueOf(CookStatus.PENDING));
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

        if (updateCookProfileInput.getBannerImage() != null) {
            uploadCookBannerImage(cookToUpdate, updateCookProfileInput.getBannerImage());
        }

        if (cookToUpdate.getStatus().equals(String.valueOf(CookStatus.REJECTED)) && updateCookProfileInput.getBusinessDocument() != null) {
            uploadBusinessDocument(cookToUpdate, updateCookProfileInput.getBusinessDocument());
        }

        if (cookToUpdate.getStatus().equals(String.valueOf(CookStatus.REJECTED)) && updateCookProfileInput.getBusinessDocument() != null) {
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
            System.out.println(uploadCookProfileDirectory);
            String filePath = getFilePath(fileName, uploadCookProfileDirectory);
            System.out.println(fileName + " " + filePath);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            profilePhoto.transferTo(destFile);
            cookToUpdate.setProfilePhoto(filePath);
        } catch (IOException error) {
            throw new IOException(error);
        }
    }

    private void uploadCookBannerImage(Cook cookToUpdate, MultipartFile bannerImage) throws IOException {
        try {
            String fileName = cookToUpdate.getId().toString() + "_" + cookToUpdate.getBusinessName() + "_bannerImage" + "." + getFileExtension(bannerImage.getOriginalFilename());
            String filePath = getFilePath(fileName, uploadCookBannerImageDirectory);
            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs();
            bannerImage.transferTo(destFile);
            cookToUpdate.setBannerImage(filePath);
        } catch (IOException error) {
            throw new IOException(error);
        }
    }


    private String getFilePath(String fileName, String uploadCookDirectory) {
        System.out.println(uploadCookDirectory);
        return Paths.get(uploadCookDirectory, fileName).toAbsolutePath().normalize().toString();
    }

    public byte[] getProfilePhoto(Long id) throws IOException {

        Cook cookProfile = cookRepository.findById(id).orElse(null);
        String path = cookProfile.getProfilePhoto();
        File destFile = new File(path);
        byte[] res = Files.readAllBytes(destFile.toPath());
        return res;
    }

    public byte[] getBannerPhoto(Long id) throws IOException {

        Cook cookProfile = cookRepository.findById(id).orElse(null);
        String path = cookProfile.getBannerImage();
        File destFile = new File(path);
        byte[] res = Files.readAllBytes(destFile.toPath());
        return res;
    }

    public List<Dish> getDishesByCookId(Long id) {
        Cook cook = cookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Cook not found with ID: " + id));
        return cook.getDishes();
    }
}
