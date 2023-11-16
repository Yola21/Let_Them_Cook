package com.letscook.letscook;

import com.letscook.cook.model.*;
import com.letscook.cook.repository.CookRepository;
import com.letscook.cook.service.CookService;
import com.letscook.enums.CookStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CookTests {

    @Mock
    private CookRepository cookRepository;

    @InjectMocks
    private CookService cookService;

    // Utility class for generating mock data
    private static class MockDataGenerator {
        static Cook createMockCook() {
            Cook mockCook = new Cook();
            // Set mock data for the Cook object
            mockCook.setId(1L);
            mockCook.setBusinessName("Mock Business");
            mockCook.setPhoneNumber("Mock Phone");
            mockCook.setStatus("Mock Status");
            mockCook.setAddress("Mock Address");
//            mockCook.setProfilePhoto("Mock Profile Photo");
//            mockCook.setBannerImage("Mock Banner Image");
//            mockCook.setBusinessDocument("Mock Business Document");
            return mockCook;
        }
    }

    @Test
    void getCook() {
        Long cookId = 1L;
        Cook mockCook = MockDataGenerator.createMockCook();
        when(cookRepository.findById(cookId)).thenReturn(Optional.of(mockCook));

        Cook result = cookService.getCook(cookId);

        assertEquals(mockCook, result);
    }

    @Test
    void getCooks() {
        List<Cook> mockCooks = List.of(MockDataGenerator.createMockCook(), MockDataGenerator.createMockCook());
        when(cookRepository.findAll()).thenReturn(mockCooks);

        List<Cook> result = cookService.getCooks();

        assertEquals(mockCooks, result);
    }

    @Test
    void createCookProfile() throws IOException {
        CreateCookProfileInput input = new CreateCookProfileInput();
        input.setUserId(1L);
        input.setAddress("Test Address");
        input.setBusinessName("Test Business");
        // Set other input parameters

        Cook mockCook = MockDataGenerator.createMockCook();
        when(cookRepository.save(any(Cook.class))).thenReturn(mockCook);

        ResponseEntity<Cook> result = cookService.createCookProfile(input);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockCook, result.getBody());
    }

    @Test
    void updateCookProfile() throws IOException {
        UpdateCookProfileInput input = new UpdateCookProfileInput();
        input.setId(1L);
        input.setAddress("new Address");
        MultipartFile file = new MockMultipartFile("file","test.jpeg", String.valueOf(MediaType.IMAGE_JPEG), "test".getBytes());
        input.setProfilePhoto(file);
        input.setBannerImage(file);
        input.setBusinessDocument(file);

//        when(Files.readAllBytes(any())).thenReturn("New Data".getBytes());
        // Set other input parameters

        Cook mockCook = MockDataGenerator.createMockCook();
        when(cookRepository.findById(input.getId())).thenReturn(Optional.of(mockCook));
        when(cookRepository.save(any(Cook.class))).thenReturn(mockCook);

        ResponseEntity<Cook> result = cookService.updateCookProfile(input);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockCook, result.getBody());
    }

    @Test
    void updateCookProfile_EntityNotFoundException() {
        UpdateCookProfileInput input = new UpdateCookProfileInput();
        input.setId(1L);

        when(cookRepository.findById(input.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cookService.updateCookProfile(input));
    }

    @Test
    void getAllPendingCook() {
        List<Cook> mockPendingCooks = List.of(MockDataGenerator.createMockCook(), MockDataGenerator.createMockCook());
        when(cookRepository.findAllByStatusIs(String.valueOf(CookStatus.PENDING))).thenReturn(mockPendingCooks);

        List<Cook> result = cookService.getAllPendingCook();

        assertEquals(mockPendingCooks, result);
    }

    // Add more tests for other methods...

//    @Test
//    void getProfilePhoto() throws IOException {
//        Long cookId = 1L;
//        Cook mockCook = MockDataGenerator.createMockCook();
//        when(cookRepository.findById(cookId)).thenReturn(Optional.of(mockCook));
//
//        byte[] mockPhotoData = "Mock Photo Data".getBytes();
//
//        // Use ArgumentMatchers.eq for the specific argument
//        when(Files.readAllBytes(argThat(argument -> argument.endsWith(mockCook.getProfilePhoto())))).thenReturn(mockPhotoData);
//
//        byte[] result = cookService.getProfilePhoto(cookId);
//
//        assertArrayEquals(mockPhotoData, result);
//    }
//
//    @Test
//    void getBannerPhoto() throws IOException {
//        Long cookId = 1L;
//        Cook mockCook = MockDataGenerator.createMockCook();
//        when(cookRepository.findById(cookId)).thenReturn(Optional.of(mockCook));
//
//        byte[] mockPhotoData = "Mock Photo Data".getBytes();
//        when(Files.readAllBytes(any())).thenReturn(mockPhotoData);
//
//        byte[] result = cookService.getBannerPhoto(cookId);
//
//        assertArrayEquals(mockPhotoData, result);
//    }
}
