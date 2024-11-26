package com.app.Citronix.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.DetailRecolte;
import com.app.Citronix.Model.Mapper.FermeMapper;
import com.app.Citronix.Repository.FermeRepository;
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
class FermeServiceTest {

    @Mock
    private FermeRepository fermeRepository;

    @Mock
    private FermeMapper fermeMapper;

    @InjectMocks
    private FermeService fermeService;

    private FermeRequest fermeRequest;
    private Ferme ferme;
    private FermeResponse fermeResponse;

    @BeforeEach
    void setUp() {
        fermeRequest = new FermeRequest();
        fermeRequest.setNom("Test Ferme");
        fermeRequest.setAdress("Test Address");
        fermeRequest.setSuperficie(100.0);

        ferme = new Ferme();
        ferme.setId(1L);
        ferme.setNom("Test Ferme");
        ferme.setAdress("Test Address");
        ferme.setSuperficie(100.0);

        fermeResponse = new FermeResponse();
        fermeResponse.setNom("Test Ferme");
        fermeResponse.setAdress("Test Address");
        fermeResponse.setSuperficie(100.0);
    }

    @Test
    void saveFerme_Success() {
        when(fermeMapper.toEntity(any(FermeRequest.class))).thenReturn(ferme);
        when(fermeRepository.save(any(Ferme.class))).thenReturn(ferme);
        when(fermeMapper.toResponse(any(Ferme.class))).thenReturn(fermeResponse);

        FermeResponse result = fermeService.saveFerme(fermeRequest);

        assertNotNull(result);
        assertEquals(fermeResponse.getNom(), result.getNom());
        verify(fermeRepository).save(any(Ferme.class));
    }

    @Test
    void getAllFermes_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ferme> fermePage = new PageImpl<>(Arrays.asList(ferme));
        
        when(fermeRepository.findAll(pageable)).thenReturn(fermePage);
        when(fermeMapper.toResponse(any(Ferme.class))).thenReturn(fermeResponse);

        Page<FermeResponse> result = fermeService.getAllFermes(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(fermeRepository).findAll(pageable);
    }

    @Test
    void getFermeById_Found() {
        when(fermeRepository.findById(1L)).thenReturn(Optional.of(ferme));
        when(fermeMapper.toResponse(ferme)).thenReturn(fermeResponse);

        FermeResponse result = fermeService.getFermeById(1L);

        assertNotNull(result);
        assertEquals(fermeResponse.getNom(), result.getNom());
    }

    @Test
    void getFermeById_NotFound() {
        when(fermeRepository.findById(1L)).thenReturn(Optional.empty());

        FermeResponse result = fermeService.getFermeById(1L);

        assertNull(result);
    }

    @Test
    void deleteFerme_Success() {
        Ferme fermeWithoutRecoltes = new Ferme();
        fermeWithoutRecoltes.setId(1L);
        fermeWithoutRecoltes.setChamps(Arrays.asList());

        when(fermeRepository.findById(1L)).thenReturn(Optional.of(fermeWithoutRecoltes));

        boolean result = fermeService.deleteFerme(1L);

        assertTrue(result);
        verify(fermeRepository).delete(fermeWithoutRecoltes);
    }

   @Test
void deleteFerme_WithRecoltes_ThrowsException() {
    // Créer un mock de Ferme
    Ferme fermeWithRecoltes = mock(Ferme.class);

    // Créer des champs et des arbres fictifs avec des récoltes
    Champ champMock = mock(Champ.class);
    Arbre arbreMock = mock(Arbre.class);

    when(arbreMock.getDetailRecoltes()).thenReturn(Arrays.asList(new DetailRecolte()));
    when(champMock.getArbres()).thenReturn(Arrays.asList(arbreMock));
    when(fermeWithRecoltes.getChamps()).thenReturn(Arrays.asList(champMock));
    when(fermeRepository.findById(1L)).thenReturn(Optional.of(fermeWithRecoltes));

    // Vérifier que l'exception est levée
    ResponseException exception = assertThrows(ResponseException.class, () -> fermeService.deleteFerme(1L));
    assertEquals("Impossible de supprimer la ferme car elle a des champs avec des arbres avec des recoltes", exception.getMessage());
}
} 