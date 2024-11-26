package com.app.Citronix.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Response.RecolteResponse;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Mapper.RecolteMapper;
import com.app.Citronix.Repository.RecolteRepository;

@Service
public class RecolteService {

    @Autowired
    private RecolteRepository recolteRepository;

    @Autowired
    private RecolteMapper recolteMapper;

    public Page<RecolteResponse> getAllRecoltes(Pageable pageable) {
        return recolteRepository.findAll(pageable).map(recolteMapper::toResponse);
    }

    public RecolteResponse getRecolteById(Long id) {
        Recolte recolte = recolteRepository.findById(id).orElse(null);
        if (recolte == null) {
            throw new ResponseException("Recolte non trouvée", HttpStatus.NOT_FOUND);
        }
        return recolteMapper.toResponse(recolte);
    }

    
    
}
