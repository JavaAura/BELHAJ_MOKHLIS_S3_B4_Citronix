package com.app.Citronix.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Mapper.ChampMapper;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Repository.FermeRepository;
import com.app.Citronix.Validation.ChampValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChampServiceTest {

    @Mock
    private ChampRepository champRepository;

    @Mock
    private ChampMapper champMapper;

    @Mock
    private ChampValidation champValidation;

    @Mock
    private FermeRepository fermeRepository;

    @InjectMocks
    private ChampService champService;

    @Captor
    private ArgumentCaptor<Champ> champCaptor;

    private ChampRequest champRequest;
    private Champ champ;
    private ChampResponse champResponse;
    private Ferme ferme;

    @BeforeEach
    void setUp() {
        // Create FermeRequest
        FermeRequest fermeRequest = new FermeRequest();
        fermeRequest.setId(1L);

        // Setup ChampRequest
        champRequest = new ChampRequest();
        champRequest.setSuperficie(100.0);
        champRequest.setNom("Test Field");
        champRequest.setFerme(fermeRequest);  

        // Setup Champ
        champ = new Champ();
        champ.setId(1L);
        champ.setSuperficie(100.0);
        champ.setNom("Test Field");
        champ.setFerme(ferme);

        // Setup ChampResponse
        champResponse = new ChampResponse();
        champResponse.setSuperficie(100.0);
        champResponse.setNom("Test Field");

        // Setup Ferme
        ferme = new Ferme();
        ferme.setId(1L);
        ferme.setAdress("test");
        ferme.setNom("testferme");
        ferme.setSuperficie(100.1);
        ferme.setDateCreation(LocalDate.of(2024, 6, 6));
    }

    @Nested
    @DisplayName("Save Champ Tests")
    class SaveTests {
        @Test
        @DisplayName("Should save champ successfully")
        void save_Success() {
            // Setup
            when(champValidation.validateChampRequest(champRequest)).thenReturn(champRequest);
            when(champMapper.toEntity(champRequest)).thenReturn(champ);
            when(fermeRepository.findById(1L)).thenReturn(Optional.of(ferme));
            when(champRepository.save(any(Champ.class))).thenReturn(champ);
            when(champRepository.findById(1L)).thenReturn(Optional.of(champ));
            when(champMapper.toResponse(any(Champ.class))).thenReturn(champResponse);

            // Execute
            ChampResponse result = champService.saveChamp(champRequest);

            // Verify
            assertNotNull(result);
            assertEquals(champResponse.getSuperficie(), result.getSuperficie());
            assertEquals(champResponse.getNom(), result.getNom());
            
            // Verify interactions
            verify(champMapper).toEntity(champRequest);
            verify(champRepository).save(any(Champ.class));
            verify(champMapper).toResponse(any(Champ.class));
        }

        @Test
        @DisplayName("Should throw exception when validation fails")
        void save_ValidationFails() {
            doThrow(new ResponseException("Invalid champ data", HttpStatus.BAD_REQUEST))
                .when(champValidation).validateChampRequest(champRequest);

            
            assertThrows(ResponseException.class, () -> champService.saveChamp(champRequest));
            verify(champRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find Champ Tests")
    class FindTests {
        @Test
        @DisplayName("Should find all champs successfully")
        void findAll_Success() {

            Pageable pageable = PageRequest.of(0, 10);
            List<Champ> champs = Arrays.asList(champ);
            Page<Champ> champPage = new PageImpl<>(champs);
            
            when(champRepository.findAll(pageable)).thenReturn(champPage);
            when(champMapper.toResponse(any(Champ.class))).thenReturn(champResponse);

            Page<ChampResponse> result = champService.getAllChamps(pageable);

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Should find champ by ID successfully")
        void findById_Success() {
            
            when(champRepository.findById(1L)).thenReturn(Optional.of(champ));
            when(champMapper.toResponse(champ)).thenReturn(champResponse);

            
            ChampResponse result = champService.getChampById(1L);

            assertNotNull(result);
            assertEquals(champResponse.getNom(), result.getNom());
            assertEquals(champResponse.getSuperficie(), result.getSuperficie());
        }

        @Test
        @DisplayName("Should throw exception when champ not found")
        void findById_NotFound() {
            when(champRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResponseException.class, () -> champService.getChampById(1L));
        }
    }

    @Nested
    @DisplayName("Update Champ Tests")
    class UpdateTests {
        @Test
        @DisplayName("Should update champ successfully")
        void update_Success() {
            // Setup
            when(champValidation.validateUpdateChampRequest(champRequest)).thenReturn(champRequest);
            when(champRepository.findById(1L)).thenReturn(Optional.of(champ));
            when(champRepository.save(any(Champ.class))).thenReturn(champ);
            when(champMapper.toResponse(any(Champ.class))).thenReturn(champResponse);

            // Execute
            ChampResponse result = champService.updateChamp(1L, champRequest);

            // Verify
            assertNotNull(result);
            verify(champValidation).validateUpdateChampRequest(champRequest);
            verify(champRepository).save(any(Champ.class));
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent champ")
        void update_NotFound() {
            when(champRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ResponseException.class, () -> champService.updateChamp(1L, champRequest));
            verify(champRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Champ Tests")
    class DeleteTests {
        @Test
        @DisplayName("Should delete champ successfully")
        void deleteById_Success() {
            
            when(champRepository.findById(1L)).thenReturn(Optional.of(champ));

            boolean result = champService.deleteChamp(1L);

            assertTrue(result);
            verify(champRepository).delete(champ);
        }

    

        @Test
        @DisplayName("Should return false when deleting non-existent champ")
        void deleteById_NotFound() {
            
            when(champRepository.findById(1L)).thenReturn(Optional.empty());

            
            boolean result = champService.deleteChamp(1L);

            assertFalse(result);
            verify(champRepository, never()).delete(any());
        }
    }
}