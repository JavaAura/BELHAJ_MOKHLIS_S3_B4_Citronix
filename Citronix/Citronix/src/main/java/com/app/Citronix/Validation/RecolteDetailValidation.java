package com.app.Citronix.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.DetailRecolte;
import com.app.Citronix.Repository.ArbreRepository;
import com.app.Citronix.Repository.RecolteDetailRepository;

@Component
public class RecolteDetailValidation {
    
    @Autowired
    private ArbreRepository arbreRepository;
    
    @Autowired
    private RecolteDetailRepository recolteDetailRepository;

    public Arbre validateArbre(Long arbreId) {
        Arbre arbre = arbreRepository.findById(arbreId)
            .orElseThrow(() -> new ResponseException("arbre non trouve",HttpStatus.NOT_FOUND));
        return arbre;
    }

    public void validateQuantite(Arbre arbre, Double quantite) {
        if (quantite > arbre.getProduction()) {
            throw new ResponseException("cette arbre et " + arbre.getEtatArbre() 
                + " et ne peut pas produire plus de " + arbre.getProduction() + " kg par saison",HttpStatus.BAD_REQUEST );
        }
    }

    public void validateUniqueRecolte(Long recolteId, Long arbreId) {
        DetailRecolte existingRecolteDetail = recolteDetailRepository.findByRecolteIdAndArbreId(recolteId, arbreId);
        if (existingRecolteDetail != null) {
            throw new ResponseException("l'arbre et deja recolter cette recolte",HttpStatus.BAD_REQUEST);
        }
    }
}
