package com.app.Citronix.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.Citronix.Exception.RecolteDetailException;
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
            .orElseThrow(() -> new RecolteDetailException("arbre non trouve"));
        return arbre;
    }

    public void validateQuantite(Arbre arbre, Double quantite) {
        if (quantite > arbre.getProduction()) {
            throw new RecolteDetailException("cette arbre et " + arbre.getEtatArbre() 
                + " et ne peut pas produire plus de " + arbre.getProduction() + " kg par saison");
        }
    }

    public void validateUniqueRecolte(Long recolteId, Long arbreId) {
        DetailRecolte existingRecolteDetail = recolteDetailRepository.findByRecolteIdAndArbreId(recolteId, arbreId);
        if (existingRecolteDetail != null) {
            throw new RecolteDetailException("l'arbre et deja recolter cette recolte");
        }
    }
}
