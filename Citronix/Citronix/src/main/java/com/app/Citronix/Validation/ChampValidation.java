package com.app.Citronix.Validation;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.Citronix.Exception.ChampException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Repository.FermeRepository;
import com.app.Citronix.Service.FermeService;

@Component
public class ChampValidation {

    @Autowired
    private FermeRepository fermeRepository;

    @Autowired
    private FermeService fermeService;

    @Autowired
    private ChampRepository champRepository;

    public ChampRequest validateChampRequest(ChampRequest champRequest) {
        validateFermeExists(champRequest);
        Ferme ferme = getFerme(champRequest);
        validateChampSuperficie(champRequest, ferme);
        validateMaxChamps(ferme);
        validateSuperficieLibre(champRequest, ferme);
        return champRequest;
    }

    public ChampRequest validateUpdateChampRequest(ChampRequest champRequest) {
        existChamp(champRequest.getId());
        Champ champ = champRepository.findById(champRequest.getId())
            .orElseThrow(() -> new ChampException("Champ non trouvé avec l'id: " + champRequest.getId()));
        Ferme ferme = getFerme(champ);
        validateUpdateSuperficie(champRequest);
        validateMaxChamps(ferme);
        validateChampSuperficie(champRequest, ferme);
        validateSuperficieLibre(champRequest, ferme, champ);
        return champRequest;
    }

    private void validateFermeExists(ChampRequest champRequest) {
        if (champRequest.getFerme().getId() == null) {
            throw new ChampException("Ferme est obligatoire");
        }
    }

    private Ferme getFerme(ChampRequest champRequest) {
        return fermeRepository.findById(champRequest.getFerme().getId())
            .orElseThrow(() -> new ChampException("Ferme non trouvé avec l'id: " + champRequest.getFerme().getId()));
    }
    private Ferme getFerme(Champ champ) {
        return fermeRepository.findById(champ.getFerme().getId())
            .orElseThrow(() -> new ChampException("Ferme non trouvé avec l'id: " + champ.getFerme().getId()));
    }

    private void validateChampSuperficie(ChampRequest champRequest, Ferme ferme) {
        if (champRequest.getSuperficie() > ferme.getSuperficie() * 0.5) {
            throw new ChampException("Superficie maximale des champs : Aucun champ ne peut dépasser 50% de la superficie totale de la ferme.");
        }
    }

    private void validateMaxChamps(Ferme ferme) {
        if (ferme.getChamps().size() == 10) {
            throw new ChampException("Nombre maximal de champs : Une ferme ne peut contenir plus de 10 champs.");
        }
    }

    private void validateSuperficieLibre(ChampRequest champRequest, Ferme ferme) {
        double superficieLibre = fermeService.getSuperficieLibre(ferme);
        if (champRequest.getSuperficie() >= superficieLibre) {
            throw new ChampException("Superficie maximale des champs : Aucun champ ne peut dépasser la superficie libre "+superficieLibre+" hectares de la ferme.");
        }
    }
    private void validateSuperficieLibre(ChampRequest champRequest, Ferme ferme, Champ champ) {

        double superficieLibre = fermeService.getSuperficieLibre(ferme);
        superficieLibre = superficieLibre + champ.getSuperficie();
        if (champRequest.getSuperficie() >= superficieLibre) {
            throw new ChampException("Superficie maximale des champs : Aucun champ ne peut dépasser la superficie libre "+superficieLibre+" hectares de la ferme.");
        }
    }

    public void existChamp(Integer champId) {
        Optional<Champ> champ = champRepository.findById(champId);
        if (!champ.isPresent()) {
            throw new ChampException("Champ non trouvé avec l'id: " + champId);
        }
    }

    public void validateUpdateSuperficie(ChampRequest champRequest) {
        Champ champ = champRepository.findById(champRequest.getId())
            .orElseThrow(() -> new ChampException("Champ non trouvé avec l'id: " + champRequest.getId()));
        int numberOfArbres = champ.getArbres().size();
        double superficieMinimale = numberOfArbres / 100.0;
        if (champRequest.getSuperficie() < superficieMinimale) {
            throw new ChampException("Superficie minimale requise : Pour " + numberOfArbres + 
                " arbres, la superficie doit être d'au moins " + superficieMinimale + " hectares.");
        }
    }

}
