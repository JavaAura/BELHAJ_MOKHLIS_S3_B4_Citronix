package com.app.Citronix.Validation;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


import com.app.Citronix.Exception.ResponseException;
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
            .orElseThrow(() -> new ResponseException("Champ non trouvé avec l'id: " + champRequest.getId(), HttpStatus.NOT_FOUND));
        Ferme ferme = getFerme(champ);
        validateUpdateSuperficie(champRequest);
        validateMaxChamps(ferme);
        validateChampSuperficie(champRequest, ferme);
        validateSuperficieLibre(champRequest, ferme, champ);
        return champRequest;
    }

    private void validateFermeExists(ChampRequest champRequest) {
        if (champRequest.getFerme() == null) {
            throw new ResponseException("Ferme est obligatoire", HttpStatus.BAD_REQUEST);
        }
    }

    private Ferme getFerme(ChampRequest champRequest) {
        return fermeRepository.findById(champRequest.getFerme().getId())
            .orElseThrow(() -> new ResponseException("Ferme non trouvé avec l'id: " + champRequest.getFerme().getId(), HttpStatus.NOT_FOUND));
    }
    private Ferme getFerme(Champ champ) {
        return fermeRepository.findById(champ.getFerme().getId())
            .orElseThrow(() -> new ResponseException("Ferme non trouvé avec l'id: " + champ.getFerme().getId(), HttpStatus.NOT_FOUND));
    }

    private void validateChampSuperficie(ChampRequest champRequest, Ferme ferme) {
        if (champRequest.getSuperficie() > ferme.getSuperficie() * 0.5) {
            throw new ResponseException("Superficie maximale des champs : Aucun champ ne peut dépasser 50% de la superficie totale de la ferme.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateMaxChamps(Ferme ferme) {
        if (ferme.getChamps().size() == 10) {
            throw new ResponseException("Nombre maximal de champs : Une ferme ne peut contenir plus de 10 champs.", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSuperficieLibre(ChampRequest champRequest, Ferme ferme) {
        double superficieLibre = fermeService.getSuperficieLibre(ferme);
        if (champRequest.getSuperficie() >= superficieLibre) {
            throw new ResponseException("Superficie maximale des champs : Aucun champ ne peut dépasser la superficie libre "+superficieLibre+" hectares de la ferme.", HttpStatus.BAD_REQUEST);
        }
    }
    private void validateSuperficieLibre(ChampRequest champRequest, Ferme ferme, Champ champ) {

        double superficieLibre = fermeService.getSuperficieLibre(ferme);
        superficieLibre = superficieLibre + champ.getSuperficie();
        if (champRequest.getSuperficie() >= superficieLibre) {
            throw new ResponseException("Superficie maximale des champs : Aucun champ ne peut dépasser la superficie libre "+superficieLibre+" hectares de la ferme.", HttpStatus.BAD_REQUEST);
        }
    }

    public void existChamp(Long champId) {
        Optional<Champ> champ = champRepository.findById(champId);
        if (!champ.isPresent()) {
            throw new ResponseException("Champ non trouvé avec l'id: " + champId, HttpStatus.NOT_FOUND);
        }
    }

    public void validateUpdateSuperficie(ChampRequest champRequest) {
        Champ champ = champRepository.findById(champRequest.getId())
            .orElseThrow(() -> new ResponseException("Champ non trouvé avec l'id: " + champRequest.getId(), HttpStatus.NOT_FOUND));
        int numberOfArbres = champ.getArbres().size();
        double superficieMinimale = numberOfArbres / 100.0;
        if (champRequest.getSuperficie() < superficieMinimale) {
            throw new ResponseException("Superficie minimale requise : Pour " + numberOfArbres + 
                " arbres, la superficie doit être d'au moins " + superficieMinimale + " hectares.", HttpStatus.BAD_REQUEST);
        }
    }

}
