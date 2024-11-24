package com.app.Citronix.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.app.Citronix.Model.DTO.Request.DetailRecolteRequest;
import com.app.Citronix.Model.DTO.Response.DetailRecolteResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.DetailRecolte;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Enum.Saison;
import com.app.Citronix.Model.Mapper.DetailRecolteMapper;
import com.app.Citronix.Model.Mapper.RecolteMapper;
import com.app.Citronix.Repository.ArbreRepository;
import com.app.Citronix.Repository.RecolteDetailRepository;
import com.app.Citronix.Repository.RecolteRepository;
public class RecolteDetailService {

    @Autowired
    private RecolteDetailRepository recolteDetailRepository;

    @Autowired
    private RecolteRepository recolteRepository;
    @Autowired
    private DetailRecolteMapper detailRecolteMapper;

    @Autowired
    private ArbreRepository arbreRepository;

    
    @Autowired
    private RecolteMapper recolteMapper;

    public Page<DetailRecolteResponse> getAllRecolteDetails(Pageable pageable) {
        return recolteDetailRepository.findAll(pageable).map(detailRecolteMapper::toResponse);
    }   

    public DetailRecolteResponse getRecolteDetailById(Integer id) {
        return recolteDetailRepository.findById(id).map(detailRecolteMapper::toResponse).orElse(null);
    }

    public DetailRecolteResponse createRecolteDetail(DetailRecolteRequest detailRecolteRequest) {
        Recolte recolte = recolteMapper.toEntity(detailRecolteRequest.getRecolte());
        Saison saison = recolte.getSaison();
        int year = recolte.getDateRecolte().getYear();
        Recolte existingRecolte = recolteRepository.findBySaisonAndDateYear(saison, year);
        if (existingRecolte == null && saison == Saison.HIVER) {
            int month = recolte.getDateRecolte().getMonthValue();
            if (month == 12) {
                existingRecolte = recolteRepository.findBySaisonAndDateYear(Saison.PRINTEMPS, year + 1);
            } else {
                existingRecolte = recolteRepository.findBySaisonAndDateYear(Saison.HIVER, year - 1);
            }
        }
        if (existingRecolte != null) {
            recolte = existingRecolte;
        }
        DetailRecolte detailRecolte = detailRecolteMapper.toEntity(detailRecolteRequest);
        detailRecolte.setRecolte(recolte);
        Arbre arbre = arbreRepository.findById(detailRecolteRequest.getArbre().getId()).orElse(null);
        if (arbre == null) {
            throw new RuntimeException("arbre non trouve");
        }
        if (detailRecolteRequest.getQuantite() > arbre.getProduction()) {
            throw new RuntimeException("cette arbre et " + arbre.getEtatArbre() + " et ne peut pas produire plus de " + arbre.getProduction() + " kg par saison");
        }
        detailRecolte.setArbre(arbre);
        DetailRecolte existingRecolteDetail = recolteDetailRepository.findByRecolteIdAndArbreId(recolte.getId(), detailRecolteRequest.getArbre().getId());
        if (existingRecolteDetail != null) {
            throw new RuntimeException("l'arbre et deja recolter cette recolte");
        }
        return detailRecolteMapper.toResponse(recolteDetailRepository.save(detailRecolte));
    }

    
}
