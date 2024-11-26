package com.app.Citronix.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Entity.Vente;
import com.app.Citronix.Repository.RecolteRepository;

@Component
public class VenteValidation {

@Autowired
private RecolteRepository recolteRepository;

    public Vente validationVenteRequest(Vente vente) {
        Recolte recolte = validationExsisringRecolte(vente.getRecolte().getId());

        vente.setRecolte(recolte);
        validationQuantiteVendue(vente);
        return vente;
    }

    public Recolte validationExsisringRecolte(Long recolteId) {
        Recolte recolte = recolteRepository.findById(recolteId)
                .orElseThrow(() -> new ResponseException("Recolte non trouvée", HttpStatus.NOT_FOUND));

        return recolte;
    }

    public void validationQuantiteVendue(Vente vente) {
        if (vente.getQuantite() > vente.getRecolte().getTotalQuantiteRestante()) {
            throw new ResponseException("Quantité vendue supérieure à la quantité recoltée", HttpStatus.BAD_REQUEST);
        }
    }   


	
	

}
