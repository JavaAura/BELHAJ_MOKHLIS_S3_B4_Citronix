package com.app.Citronix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Citronix.Exception.ArbreException;
import com.app.Citronix.Exception.ChampException;
import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Mapper.ArbreMapper;
import com.app.Citronix.Repository.ArbreRepository;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Validation.ArbreValidation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ArbreService {
    
    @Autowired
    private ArbreRepository arbreRepository;
    
    @Autowired
    private ChampRepository champRepository;
    
    @Autowired
    private ArbreMapper arbreMapper;
    
    @Autowired
    private ArbreValidation arbreValidation;

    public ArbreResponse save(ArbreRequest arbreRequest) {
        arbreValidation.validateArbreRequest(arbreRequest);
        Optional<Champ> champ = champRepository.findById( arbreRequest.getChamp().getId());
        Arbre arbre = arbreMapper.toEntity(arbreRequest);
        arbre.setChamp(champ.get());
        arbre = arbreRepository.save(arbre);
        return arbreMapper.toResponse(arbre);
    }

    public Page<ArbreResponse> findAll(Pageable pageable) {
        Page<Arbre> arbres = arbreRepository.findAll(pageable);
        return arbres.map(arbre -> {
            return arbreMapper.toResponse(arbre);
        });
    }

    public ArbreResponse findById(Long id) {
        Arbre arbre = arbreRepository.findById(id)
        .orElseThrow(() -> new ArbreException("Arbre non trouvé avec l'id: " + id));
     
        return arbreMapper.toResponse(arbre);
    }
    public boolean deleteById(Long id) {
        Optional<Arbre> arbre = arbreRepository.findById(id);
        if (arbre.isPresent()) {
            arbreRepository.delete(arbre.get());
            return true;
        }
        return false;
    }



 
} 