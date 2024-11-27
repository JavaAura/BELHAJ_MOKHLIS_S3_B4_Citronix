package com.app.Citronix.Service;

import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Model.Entity.Vente;
import com.app.Citronix.Model.Mapper.VenteMapper;
import com.app.Citronix.Repository.VenteRepository;
import com.app.Citronix.Repository.RecolteRepository;
import com.app.Citronix.Validation.VenteValidation;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenteServiceTest {

    @Mock
    private VenteRepository venteRepository;

    @Mock
    private RecolteRepository recolteRepository;

    @Mock
    private VenteMapper venteMapper;

    @Mock
    private VenteValidation venteValidation;

    @InjectMocks
    private VenteService venteService;

    private VenteRequest venteRequest;
    private Vente vente;
    private VenteResponse venteResponse;

    @BeforeEach
    void setUp() {
        venteRequest = new VenteRequest();

        vente = new Vente();
        vente.setId(1L);

        venteResponse = new VenteResponse();
    }

    @Test
    void saveVente_Success() {
        when(venteMapper.toEntity(venteRequest)).thenReturn(vente);
        when(venteValidation.validationVenteRequest(vente)).thenReturn(vente);
        when(venteRepository.save(any(Vente.class))).thenReturn(vente);
        when(venteMapper.toResponse(vente)).thenReturn(venteResponse);

        VenteResponse result = venteService.saveVente(venteRequest);

        assertNotNull(result);
        verify(venteRepository).save(any(Vente.class));
        verify(venteMapper).toResponse(any(Vente.class));
    }

    @Test
    void getAllVentes_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vente> ventePage = new PageImpl<>(Arrays.asList(vente));
        
        when(venteRepository.findAll(pageable)).thenReturn(ventePage);
        when(venteMapper.toResponse(any(Vente.class))).thenReturn(venteResponse);

        Page<VenteResponse> result = venteService.getAllVentes(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(venteRepository).findAll(pageable);
    }

    @Test
    void getVenteById_Found() {
        when(venteRepository.findById(1L)).thenReturn(Optional.of(vente));
        when(venteMapper.toResponse(vente)).thenReturn(venteResponse);

        Optional<VenteResponse> result = venteService.getVenteById(1L);

        assertTrue(result.isPresent());
        verify(venteRepository).findById(1L);
    }

    @Test
    void deleteVente_Success() {
        when(venteRepository.existsById(1L)).thenReturn(true);

        boolean result = venteService.deleteVente(1L);

        assertTrue(result);
        verify(venteRepository).deleteById(1L);
    }
}