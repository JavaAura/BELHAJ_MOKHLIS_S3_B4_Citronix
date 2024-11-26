package com.app.Citronix.Service;

import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Request.DetailRecolteRequest;
import com.app.Citronix.Model.DTO.Request.RecolteRequest;
import com.app.Citronix.Model.DTO.Response.DetailRecolteResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.DetailRecolte;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Enum.Saison;
import com.app.Citronix.Model.Mapper.DetailRecolteMapper;
import com.app.Citronix.Model.Mapper.RecolteMapper;
import com.app.Citronix.Repository.RecolteDetailRepository;
import com.app.Citronix.Repository.RecolteRepository;
import com.app.Citronix.Validation.RecolteDetailValidation;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecolteDetailServiceTest {

    @Mock
    private RecolteDetailRepository recolteDetailRepository;

    @Mock
    private RecolteRepository recolteRepository;

    @Mock
    private DetailRecolteMapper detailRecolteMapper;

    @Mock
    private RecolteMapper recolteMapper;

    @Mock
    private RecolteDetailValidation recolteDetailValidation;

    @InjectMocks
    private RecolteDetailService recolteDetailService;

    @Captor
    private ArgumentCaptor<DetailRecolte> detailRecolteCaptor;

    @Captor
    private ArgumentCaptor<Recolte> recolteCaptor;

    private DetailRecolteRequest detailRecolteRequest;
    private DetailRecolte detailRecolte;
    private DetailRecolteResponse detailRecolteResponse;
    private Recolte recolte;
    private Arbre arbre;
    private RecolteRequest recolteRequest;
    private ArbreRequest arbreRequest;

    @BeforeEach
    void setUp() {
        // Initialize test data
        arbre = new Arbre();
        arbre.setId(1L);

        arbreRequest = new ArbreRequest();
        arbreRequest.setId(1L);

        recolte = new Recolte();
        recolte.setId(1L);
        recolte.setDateRecolte(LocalDate.now());

        recolteRequest = new RecolteRequest();
        recolteRequest.setDateRecolte(LocalDate.now());

        detailRecolteRequest = new DetailRecolteRequest();
        detailRecolteRequest.setQuantite(100.0);
        detailRecolteRequest.setRecolte(recolteRequest);
        detailRecolteRequest.setArbre(arbreRequest);

        detailRecolte = new DetailRecolte();
        detailRecolte.setId(1L);
        detailRecolte.setQuantite(100.0);
        detailRecolte.setRecolte(recolte);
        detailRecolte.setArbre(arbre);

        detailRecolteResponse = new DetailRecolteResponse();
        detailRecolteResponse.setQuantite(100.0);
    }

    @Nested
    @DisplayName("Get RecolteDetail Tests")
    class GetTests {
        @Test
        @DisplayName("Should get all recolte details successfully")
        void getAllRecolteDetails_Success() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<DetailRecolte> detailRecoltePage = new PageImpl<>(Arrays.asList(detailRecolte));
            
            when(recolteDetailRepository.findAll(pageable)).thenReturn(detailRecoltePage);
            when(detailRecolteMapper.toResponse(any(DetailRecolte.class))).thenReturn(detailRecolteResponse);

            // Act
            Page<DetailRecolteResponse> result = recolteDetailService.getAllRecolteDetails(pageable);

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Should get recolte detail by ID successfully")
        void getRecolteDetailById_Success() {
            // Arrange
            when(recolteDetailRepository.findById(1L)).thenReturn(Optional.of(detailRecolte));
            when(detailRecolteMapper.toResponse(detailRecolte)).thenReturn(detailRecolteResponse);

            // Act
            DetailRecolteResponse result = recolteDetailService.getRecolteDetailById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(detailRecolteResponse.getQuantite(), result.getQuantite());
        }
    }

    @Nested
    @DisplayName("Create RecolteDetail Tests")
    class CreateTests {
        @Test
        @DisplayName("Should create new recolte detail successfully")
        void createRecolteDetail_NewRecolte_Success() {
            // Arrange
            when(detailRecolteMapper.toEntity(detailRecolteRequest)).thenReturn(detailRecolte);
            when(recolteMapper.toEntity(recolteRequest)).thenReturn(recolte);
            when(recolteRepository.findBySaisonAndDateYear(any(), anyInt())).thenReturn(Optional.empty());
            when(recolteRepository.save(any(Recolte.class))).thenReturn(recolte);
            when(recolteDetailValidation.validateArbre(1L)).thenReturn(arbre);
            when(recolteDetailRepository.save(any(DetailRecolte.class))).thenReturn(detailRecolte);
            when(detailRecolteMapper.toResponse(detailRecolte)).thenReturn(detailRecolteResponse);

            // Act
            DetailRecolteResponse result = recolteDetailService.createRecolteDetail(detailRecolteRequest);

            // Assert
            assertNotNull(result);
            verify(recolteDetailValidation).validateQuantite(arbre, detailRecolteRequest.getQuantite());
            verify(recolteDetailValidation).validateUniqueRecolte(recolte.getId(), arbre.getId());
        }

        @Test
        @DisplayName("Should create recolte detail with existing recolte successfully")
        void createRecolteDetail_ExistingRecolte_Success() {
            // Arrange
            when(detailRecolteMapper.toEntity(detailRecolteRequest)).thenReturn(detailRecolte);
            when(recolteMapper.toEntity(recolteRequest)).thenReturn(recolte);
            when(recolteRepository.findBySaisonAndDateYear(any(), anyInt())).thenReturn(Optional.of(recolte));
            when(recolteDetailValidation.validateArbre(1L)).thenReturn(arbre);
            when(recolteDetailRepository.save(any(DetailRecolte.class))).thenReturn(detailRecolte);
            when(detailRecolteMapper.toResponse(detailRecolte)).thenReturn(detailRecolteResponse);

            // Act
            DetailRecolteResponse result = recolteDetailService.createRecolteDetail(detailRecolteRequest);

            // Assert
            assertNotNull(result);
            verify(recolteRepository, never()).save(any(Recolte.class));
        }
    }

    @Nested
    @DisplayName("Set Saison Tests")
    class SetSaisonTests {
        @Test
        @DisplayName("Should set PRINTEMPS for March-May")
        void setSaison_Printemps() {
            // Arrange
            Recolte recolte = new Recolte();
            recolte.setDateRecolte(LocalDate.of(2024, 4, 15));

            // Act
            recolteDetailService.setSaisonRecolte(recolte);

            // Assert
            assertEquals(Saison.PRINTEMPS, recolte.getSaison());
        }

        @Test
        @DisplayName("Should set ETE for June-August")
        void setSaison_Ete() {
            // Arrange
            Recolte recolte = new Recolte();
            recolte.setDateRecolte(LocalDate.of(2024, 7, 15));

            // Act
            recolteDetailService.setSaisonRecolte(recolte);

            // Assert
            assertEquals(Saison.ETE, recolte.getSaison());
        }

        @Test
        @DisplayName("Should set AUTOMNE for September-November")
        void setSaison_Automne() {
            // Arrange
            Recolte recolte = new Recolte();
            recolte.setDateRecolte(LocalDate.of(2024, 10, 15));

            // Act
            recolteDetailService.setSaisonRecolte(recolte);

            // Assert
            assertEquals(Saison.AUTOMNE, recolte.getSaison());
        }

        @Test
        @DisplayName("Should set HIVER for December-February")
        void setSaison_Hiver() {
            // Arrange
            Recolte recolte = new Recolte();
            recolte.setDateRecolte(LocalDate.of(2024, 1, 15));

            // Act
            recolteDetailService.setSaisonRecolte(recolte);

            // Assert
            assertEquals(Saison.HIVER, recolte.getSaison());
        }
    }
}