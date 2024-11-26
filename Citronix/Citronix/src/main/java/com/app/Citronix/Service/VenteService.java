package com.app.Citronix.Service;

import com.app.Citronix.Exception.RecolteException;
import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Entity.Vente;
import com.app.Citronix.Model.Mapper.VenteMapper;
import com.app.Citronix.Repository.RecolteRepository;
import com.app.Citronix.Repository.VenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VenteService {

    @Autowired
    private VenteMapper venteMapper;
    @Autowired
    private VenteRepository venteRepository;
    @Autowired
    private RecolteRepository recolteRepository;

    public VenteResponse saveVente(VenteRequest venteRequest) {
        Vente vente = venteMapper.toEntity(venteRequest);
        Recolte recolte = recolteRepository.findById(venteRequest.getRecolte().getId())     
                .orElseThrow(  () -> new RecolteException("Recolte non trouvée"));
        vente.setRecolte(recolte);

        if (vente.getQuantite() > recolte.getTotalQuantiteRestante()) {
            throw new RecolteException("Quantité vendue supérieure à la quantité recoltée");
        }
        return venteMapper.toResponse(venteRepository.save(vente)); 
    }

    public List<VenteResponse> getAllVentes() {
        return venteRepository.findAll().stream().map(venteMapper::toResponse).collect(Collectors.toList());
    }

    public Optional<VenteResponse> getVenteById(Long id) {
        return venteRepository.findById(id).map(venteMapper::toResponse);
    }

    public Optional<VenteResponse> updateVente(Long id, VenteRequest venteRequest) {
        if (venteRepository.existsById(id)) {
            return Optional.of(venteMapper.toResponse(venteRepository.save(venteMapper.toEntity(venteRequest))));
        }
        return Optional.empty();
    }

    public boolean deleteVente(Long id) {
        if (venteRepository.existsById(id)) {
            venteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}