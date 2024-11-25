package com.app.Citronix.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.app.Citronix.Exception.RecolteDetailException;
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
import com.app.Citronix.Validation.RecolteDetailValidation;

@Service
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

    @Autowired
    private RecolteDetailValidation recolteDetailValidation;

    public Page<DetailRecolteResponse> getAllRecolteDetails(Pageable pageable) {
        return recolteDetailRepository.findAll(pageable).map(detailRecolteMapper::toResponse);
    }   

    public DetailRecolteResponse getRecolteDetailById(Long id) {
        return recolteDetailRepository.findById(id).map(detailRecolteMapper::toResponse).orElse(null);
    }

    public DetailRecolteResponse createRecolteDetail(DetailRecolteRequest detailRecolteRequest) {
        DetailRecolte detailRecolte = detailRecolteMapper.toEntity(detailRecolteRequest);
        Recolte recolte = recolteMapper.toEntity(detailRecolteRequest.getRecolte()); 
        setSaisonRecolte(recolte);
        Saison saison = recolte.getSaison();
        int year = recolte.getDateRecolte().getYear();

       Optional<Recolte> existingRecolte = recolteRepository.findBySaisonAndDateYear(saison, year);
       if (!existingRecolte.isPresent() && saison == Saison.HIVER) {
           int month = recolte.getDateRecolte().getMonthValue();
           if (month == 12) {
               existingRecolte = recolteRepository.findBySaisonAndDateYear(Saison.HIVER, year + 1);
           } else {
               existingRecolte = recolteRepository.findBySaisonAndDateYear(Saison.HIVER, year - 1);
           }
       }

       Recolte finalRecolte;
       if (!existingRecolte.isPresent()){
        finalRecolte = recolteRepository.save(recolte);         
       }else{
         finalRecolte = existingRecolte.get();
       }
       
       Arbre arbre = recolteDetailValidation.validateArbre(detailRecolteRequest.getArbre().getId());
       recolteDetailValidation.validateQuantite(arbre, detailRecolteRequest.getQuantite());
       recolteDetailValidation.validateUniqueRecolte(finalRecolte.getId(), detailRecolteRequest.getArbre().getId());
       
       detailRecolte.setRecolte(finalRecolte);
       detailRecolte.setArbre(arbre);
       
       return detailRecolteMapper.toResponse(recolteDetailRepository.save(detailRecolte));
    
}
public void setSaisonRecolte(Recolte recolte){
    LocalDate dateRecolte = recolte.getDateRecolte();
    if (dateRecolte.getMonthValue() >= 3 && dateRecolte.getMonthValue() <= 5) {
        recolte.setSaison(Saison.PRINTEMPS);
        } else if (dateRecolte.getMonthValue() >= 6 && dateRecolte.getMonthValue() <= 8) {
        recolte.setSaison(Saison.ETE);
    } else if (dateRecolte.getMonthValue() >= 9 && dateRecolte.getMonthValue() <= 11) {
        recolte.setSaison(Saison.AUTOMNE);
    }else {
        recolte.setSaison(Saison.HIVER);
    }
}  
    
}
