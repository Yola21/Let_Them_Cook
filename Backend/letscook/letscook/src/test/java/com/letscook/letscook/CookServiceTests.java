package com.letscook.letscook;

import com.letscook.cook.model.Cook;
import com.letscook.cook.model.CreateCookProfileInput;
import com.letscook.cook.model.UpdateCookProfileInput;
import com.letscook.cook.repository.CookRepository;
import com.letscook.cook.service.CookService;
import com.letscook.enums.CookStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CookServiceTests {

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
            mockCook.setStatus(String.valueOf(CookStatus.ACCEPTED));
            mockCook.setAddress("Mock Address");
            mockCook.setProfilePhoto("Mock Profile Photo");
            mockCook.setBannerImage("Mock Banner Image");
            mockCook.setBusinessDocument("Mock Business Document");
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
    void createCookProfileWithAllInputData() throws IOException {
        CreateCookProfileInput input = new CreateCookProfileInput();
        input.setUserId(1L);
        input.setAddress("Test Address");
        input.setBusinessName("Test Business");
        input.setProfilePhoto("Test Image");
        input.setBannerImage("Test Image");
        input.setBusinessDocument("Test Image");
        // Set other input parameters

        Cook mockCook = MockDataGenerator.createMockCook();
        when(cookRepository.save(any(Cook.class))).thenReturn(mockCook);

        ResponseEntity<Cook> result = cookService.createCookProfile(input);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockCook, result.getBody());
    }

    @Test
    void createCookProfileWithoutAllInputData() throws IOException {
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
    void updateCookProfileWithAllData() throws IOException {
        UpdateCookProfileInput input = new UpdateCookProfileInput();
        input.setId(1L);
        input.setAddress("new Address");
        input.setProfilePhoto("New Image");
        input.setBannerImage("New Image");

        Cook mockCook = MockDataGenerator.createMockCook();
        when(cookRepository.findById(input.getId())).thenReturn(Optional.of(mockCook));
        when(cookRepository.save(any(Cook.class))).thenReturn(mockCook);

        ResponseEntity<Cook> result = cookService.updateCookProfile(input);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(mockCook, result.getBody());
    }

    @Test
    void updateCookProfileBusinessDocumentWithoutStatusRejected() throws IOException {
        UpdateCookProfileInput input = new UpdateCookProfileInput();
        input.setId(1L);
        input.setAddress("new Address");
        input.setProfilePhoto("New Image");
        input.setBannerImage("New Image");
        input.setBusinessDocument("New Image");

        Cook mockCook = MockDataGenerator.createMockCook();
        mockCook.setStatus(String.valueOf(CookStatus.PENDING));
        when(cookRepository.findById(input.getId())).thenReturn(Optional.of(mockCook));

        Error exception = assertThrows(Error.class, () -> {
            cookService.updateCookProfile(input);
        });
        mockCook.setStatus(String.valueOf(CookStatus.ACCEPTED));
        assertEquals("Not allowed to change business document", exception.getMessage());
        verify(cookRepository, never()).save(any());
    }

    @Test
    void updateCookProfileWithoutAllData() throws IOException {
        UpdateCookProfileInput input = new UpdateCookProfileInput();
        input.setId(1L);

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
}
