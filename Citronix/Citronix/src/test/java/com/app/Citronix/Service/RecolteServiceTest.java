package com.app.Citronix.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Response.RecolteResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.DetailRecolte;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Mapper.RecolteMapper;
import com.app.Citronix.Repository.RecolteRepository;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecolteServiceTest {

    @Mock
    private RecolteRepository recolteRepository;

    @Mock
    private RecolteMapper recolteMapper;

    @InjectMocks
    private RecolteService recolteService;

    private Recolte recolte;
    private RecolteResponse recolteResponse;

    @BeforeEach
    void setUp() {
        recolte = new Recolte();
        recolte.setId(1L);
        recolte.setDateRecolte(LocalDate.now());
        recolte.setTotalQuantite(100.0);

        Arbre arbre = new Arbre();
        arbre.setId(1L);

        DetailRecolte detailRecolte = new DetailRecolte();
        detailRecolte.setId(1L);
        detailRecolte.setQuantite(100.0);
        detailRecolte.setArbre(arbre);
        detailRecolte.setRecolte(recolte);

        recolteResponse = new RecolteResponse();
        recolteResponse.setDateRecolte(LocalDate.now());
        recolteResponse.setTotalQuantite(100.0);
    }
    

    @Test
    void getAllRecoltes_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recolte> recoltePage = new PageImpl<>(Arrays.asList(recolte));
        
        when(recolteRepository.findAll(pageable)).thenReturn(recoltePage);
        when(recolteMapper.toResponse(any(Recolte.class))).thenReturn(recolteResponse);

        Page<RecolteResponse> result = recolteService.getAllRecoltes(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(recolteRepository).findAll(pageable);
    }

    @Test
    void getRecolteById_Found() {
        when(recolteRepository.findById(1L)).thenReturn(Optional.of(recolte));
        when(recolteMapper.toResponse(recolte)).thenReturn(recolteResponse);

        RecolteResponse result = recolteService.getRecolteById(1L);

        assertNotNull(result);
        assertEquals(recolteResponse.getDateRecolte(), result.getDateRecolte());
    }

    @Test
    void getRecolteById_NotFound() {
        when(recolteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseException.class, () -> recolteService.getRecolteById(1L));
    }
} 