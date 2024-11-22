package com.app.Citronix.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.Citronix.Exception.ChampException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Repository.FermeRepository;
import com.app.Citronix.Service.FermeService;

@Component
public class ChampValidation {

    @Autowired
    private FermeRepository fermeRepository;

    @Autowired
    private FermeService fermeService;

    public ChampRequest validateChampRequest(ChampRequest champRequest) {
        if (champRequest.getFerme().getId() == null) {
            throw new ChampException("Ferme is required");
        }
        Ferme ferme = fermeRepository.findById(champRequest.getFerme().getId())
            .orElseThrow(() -> new ChampException("Ferme not found"));
        // champ ne peut dépasser 50% 
        if (champRequest.getSuperficie() > ferme.getSuperficie() * 0.5) {
            throw new ChampException("Superficie maximale des champs : Aucun champ ne peut dépasser 50% de la superficie totale de la ferme.");
        }

        if (ferme.getChamps().size() == 10) {
            throw new ChampException("Nombre maximal de champs : Une ferme ne peut contenir plus de 10 champs.");
        }

        double superficieLibre = fermeService.getSuperficieLibre(ferme);
        if (champRequest.getSuperficie() >= superficieLibre) {
            throw new ChampException("Superficie maximale des champs : Aucun champ ne peut dépasser la superficie libre "+superficieLibre+"hectares de la ferme.");
        }


        return champRequest;
    }
}
