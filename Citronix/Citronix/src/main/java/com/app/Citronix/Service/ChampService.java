package com.app.Citronix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Mapper.ChampMapper;
import com.app.Citronix.Repository.ChampRepository;

import java.util.Optional;

@Service
public class ChampService {
    
    @Autowired
    private ChampRepository champRepository;
    
    @Autowired
    private ChampMapper champMapper;

    public ChampResponse saveChamp(ChampRequest champRequest) {
        Champ champ = champMapper.toEntity(champRequest);
        champ = champRepository.save(champ);
        return champMapper.toResponse(champ);
    }

    public Page<ChampResponse> getAllChamps(Pageable pageable) {
        Page<Champ> champs = champRepository.findAll(pageable);
        return champs.map(champMapper::toResponse);
    }

    public ChampResponse getChampById(Integer id) {
        Optional<Champ> champ = champRepository.findById(id);
        return champ.map(champMapper::toResponse).orElse(null);
    }

    public ChampResponse updateChamp(Integer id, ChampRequest champRequest) {
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

    public boolean deleteChamp(Integer id) {
        Optional<Champ> champ = champRepository.findById(id);
        if (champ.isPresent()) {
            champRepository.delete(champ.get());
            return true;
        }
        return false;
    }
} 