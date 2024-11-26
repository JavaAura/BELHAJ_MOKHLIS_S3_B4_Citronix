package com.app.Citronix.Service;

import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Model.Mapper.VenteMapper;
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

    public VenteResponse saveVente(VenteRequest venteRequest) {
        return venteMapper.toResponse(venteRepository.save(venteMapper.toEntity(venteRequest))); 
    }

    public List<VenteResponse> getAllVentes() {
        return venteRepository.findAll().stream().map(venteMapper::toResponse).collect(Collectors.toList());
    }

    public Optional<VenteResponse> getVenteById(Long id) {
        return venteRepository.findById(id).map(venteMapper::toResponse);
    }

    public VenteResponse updateVente(VenteRequest venteRequest) {
        if (venteRepository.existsById(venteRequest.getId())) {
            return venteMapper.toResponse(venteRepository.save(venteMapper.toEntity(venteRequest)));
        }
        return null;
    }

    public boolean deleteVente(Long id) {
        if (venteRepository.existsById(id)) {
            venteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}