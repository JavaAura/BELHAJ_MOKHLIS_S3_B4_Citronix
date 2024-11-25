package com.app.Citronix.Validation;

import com.app.Citronix.Exception.ChampException;
import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Repository.ChampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ArbreValidation {

    @Autowired
    private ChampRepository champRepository;

    public void validateArbreRequest(ArbreRequest arbreRequest) {
        validatePlantingDate(arbreRequest.getDatePlantation());
        validateChampDensity(arbreRequest.getChamp().getId());
    }

    private void validatePlantingDate(LocalDate datePlantation) {
        int mois = datePlantation.getMonthValue();
        if (mois < 3 || mois > 5) {
            throw new IllegalArgumentException("La plantation n'est autorisée qu'entre mars et mai");
        }
    }

    private void validateChampDensity(Long champId) {
        Optional<Champ> champ = champRepository.findById(champId);
        
        if (champ.isPresent()) {
            double superficie = champ.get().getSuperficie();
            long nbArbres = champ.get().getArbres().size();
            if (nbArbres >= (superficie * 100)) {
                throw new IllegalStateException("Densité maximale atteinte pour ce champ");
            }
        } else {
            throw new ChampException("Champ non trouvé");
        }
    }
}
