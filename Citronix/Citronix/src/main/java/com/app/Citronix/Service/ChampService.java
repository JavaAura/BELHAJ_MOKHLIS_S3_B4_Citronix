package com.app.Citronix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Mapper.ChampMapper;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Repository.FermeRepository;
import com.app.Citronix.Validation.ChampValidation;
import java.util.Optional;

@Service
public class ChampService {
    
    @Autowired
    private ChampRepository champRepository;

    @Autowired
    private ChampValidation champValidation;
    
    @Autowired
    private ChampMapper champMapper;

    @Autowired
    private FermeRepository fermeRepository;


    public ChampResponse saveChamp(ChampRequest champRequest) { 
        champRequest = champValidation.validateChampRequest(champRequest);
        Champ champ = champMapper.toEntity(champRequest);
        Ferme ferme = fermeRepository.findById(champRequest.getFerme().getId()).orElse(null);
        champ.setFerme(ferme);
        champ = champRepository.save(champ);
        return getChampById( champ.getId());
    }

    public Page<ChampResponse> getAllChamps(Pageable pageable) {
        Page<Champ> champs = champRepository.findAll(pageable);
        return champs.map(champMapper::toResponse);
    }

    public ChampResponse getChampById(long id) {
        Optional<Champ> champ = champRepository.findById(id);
        if (champ.isPresent()) {
            return champMapper.toResponse(champ.get());
        } else {
            throw new ResponseException("Champ non trouvé avec l'id : " + id, HttpStatus.NOT_FOUND);
        }
    }

    public ChampResponse updateChamp(Long id, ChampRequest champRequest) {
        champRequest.setId(id);
        champRequest = champValidation.validateUpdateChampRequest(champRequest);
        Optional<Champ> champOpt = champRepository.findById(id);
        if (champOpt.isPresent()) {
            Champ champ = champOpt.get();
            champ.setNom(champRequest.getNom());
            champ.setSuperficie(champRequest.getSuperficie());
            champ = champRepository.save(champ);
            return champMapper.toResponse(champ);
        }
        return null;
    }

    public boolean deleteChamp(Long id) {
        Optional<Champ> champ = champRepository.findById(id);
        if (champ.get().getArbres().stream().anyMatch(arbre -> arbre.getDetailRecoltes().size() > 0)) {
            throw new ResponseException("Impossible de supprimer le champ car il a des arbres avec des recoltes",HttpStatus.BAD_REQUEST);
        }
        if (champ.isPresent()) {
            champRepository.delete(champ.get());
            return true;
        }
        return false;
    }

  
} 