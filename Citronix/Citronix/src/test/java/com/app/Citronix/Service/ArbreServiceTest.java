package com.app.Citronix.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Entity.DetailRecolte;
import com.app.Citronix.Model.Mapper.ArbreMapper;
import com.app.Citronix.Repository.ArbreRepository;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Validation.ArbreValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArbreServiceTest {

    @Mock
    private ArbreRepository arbreRepository;

    @Mock
    private ChampRepository champRepository;

    @Mock
    private ArbreMapper arbreMapper;

    @Mock
    private ArbreValidation arbreValidation;

    @InjectMocks
    private ArbreService arbreService;

    private ArbreRequest arbreRequest;
    private Arbre arbre;
    private ArbreResponse arbreResponse;
    private Champ champ;

    @BeforeEach
    void setUp() {
        champ = new Champ();
        champ.setId(1L);
        champ.setSuperficie(100.0);

        arbreRequest = new ArbreRequest();
        arbreRequest.setDatePlantation(LocalDate.now());
        ChampRequest champRequest = new ChampRequest();
        champRequest.setId(1L);
        arbreRequest.setChamp(champRequest);

        arbre = new Arbre();
        arbre.setId(1L);
        arbre.setDatePlantation(LocalDate.now());
        arbre.setChamp(champ);
        arbre.setDetailRecoltes(new ArrayList<>());

        arbreResponse = new ArbreResponse();
        arbreResponse.setDatePlantation(LocalDate.now());
    }

    @Test
    void save_Success() {
        when(champRepository.findById(1L)).thenReturn(Optional.of(champ));
        when(arbreMapper.toEntity(any(ArbreRequest.class))).thenReturn(arbre);
        when(arbreRepository.save(any(Arbre.class))).thenReturn(arbre);
        when(arbreMapper.toResponse(any(Arbre.class))).thenReturn(arbreResponse);

        ArbreResponse result = arbreService.save(arbreRequest);

        assertNotNull(result);
        assertEquals(arbreResponse.getDatePlantation(), result.getDatePlantation());
        verify(arbreValidation).validateArbreRequest(arbreRequest);
        verify(arbreRepository).save(any(Arbre.class));
    }

    @Test
    void findAll_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Arbre> arbrePage = new PageImpl<>(Arrays.asList(arbre));
        
        when(arbreRepository.findAll(pageable)).thenReturn(arbrePage);
        when(arbreMapper.toResponse(any(Arbre.class))).thenReturn(arbreResponse);

        Page<ArbreResponse> result = arbreService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(arbreRepository).findAll(pageable);
    }

    @Test
    void findById_Found() {
        when(arbreRepository.findById(1L)).thenReturn(Optional.of(arbre));
        when(arbreMapper.toResponse(arbre)).thenReturn(arbreResponse);

        ArbreResponse result = arbreService.findById(1L);

        assertNotNull(result);
        assertEquals(arbreResponse.getDatePlantation(), result.getDatePlantation());
    }

    @Test
    void findById_NotFound() {
        when(arbreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseException.class, () -> arbreService.findById(1L));
    }

    @Test
    void deleteById_Success() {
        when(arbreRepository.findById(1L)).thenReturn(Optional.of(arbre));

        boolean result = arbreService.deleteById(1L);

        assertTrue(result);
        verify(arbreRepository).delete(arbre);
    }

    @Test
    void deleteById_WithRecoltes_ThrowsException() {
        Arbre arbreWithRecoltes = new Arbre();
        arbreWithRecoltes.setDetailRecoltes(Arrays.asList(new DetailRecolte()));
        
        when(arbreRepository.findById(1L)).thenReturn(Optional.of(arbreWithRecoltes));

        assertThrows(ResponseException.class, () -> arbreService.deleteById(1L));
    }

    @Test
    void deleteById_NotFound() {
        when(arbreRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = arbreService.deleteById(1L);

        assertFalse(result);
        verify(arbreRepository, never()).delete(any());
    }
} 