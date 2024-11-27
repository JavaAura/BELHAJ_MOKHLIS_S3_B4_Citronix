package com.app.Citronix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Mapper.ChampMapper;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Repository.FermeRepository;
import com.app.Citronix.Validation.ChampValidation;
import java.util.Optional;

/**
 * Service gérant les opérations liées aux champs agricoles.
 */
@Service
public class ChampService {
    
    @Autowired
    private ChampRepository champRepository;

    @Autowired
    private ChampValidation champValidation;
    
    @Autowired
    private ChampMapper champMapper;

    @Autowired
    private FermeRepository fermeRepository;


    /**
     * Enregistre un nouveau champ dans la base de données.
     * 
     * @param champRequest Les données du champ à créer
     * @return ChampResponse Les informations du champ créé
     * @throws ResponseException Si la ferme associée n'existe pas
     */
    public ChampResponse saveChamp(ChampRequest champRequest) { 
        champRequest = champValidation.validateChampRequest(champRequest);
        Champ champ = champMapper.toEntity(champRequest);
        Optional<Ferme> ferme = fermeRepository.findById(champRequest.getFerme().getId()) ;
        if (ferme.isPresent()) {
            champ.setFerme(ferme.get());
        } else {
            throw new ResponseException("La ferme avec l'ID " + champRequest.getFerme().getId() + " n'existe pas.", HttpStatus.NOT_FOUND);
        }
        champ = champRepository.save(champ);
        return getChampById( champ.getId());
    }

    /**
     * Récupère tous les champs avec pagination.
     * 
     * @param pageable Les informations de pagination
     * @return Page<ChampResponse> Une page contenant les champs
     */
    public Page<ChampResponse> getAllChamps(Pageable pageable) {
        Page<Champ> champs = champRepository.findAll(pageable);
        return champs.map(champMapper::toResponse);
    }

    /**
     * Récupère un champ par son identifiant.
     * 
     * @param id L'identifiant du champ
     * @return ChampResponse Les informations du champ
     * @throws ResponseException Si le champ n'est pas trouvé
     */
    public ChampResponse getChampById(long id) {
        Optional<Champ> champ = champRepository.findById(id);
        if (champ.isPresent()) {
            return champMapper.toResponse(champ.get());
        } else {
            throw new ResponseException("Champ non trouvé avec l'id : " + id, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Met à jour les informations d'un champ existant.
     * 
     * @param id L'identifiant du champ à modifier
     * @param champRequest Les nouvelles données du champ
     * @return ChampResponse Les informations du champ mis à jour
     * @throws ResponseException Si le champ n'est pas trouvé
     */
    public ChampResponse updateChamp(Long id, ChampRequest champRequest) {
        champRequest.setId(id);
        champRequest = champValidation.validateUpdateChampRequest(champRequest);
        Optional<Champ> champOpt = champRepository.findById(id);
        if (!champOpt.isPresent()) {
            throw new ResponseException("Champ non trouvé avec l'id : " + id, HttpStatus.NOT_FOUND);
        }
            Champ champ = champOpt.get();
            champ.setNom(champRequest.getNom());
            champ.setSuperficie(champRequest.getSuperficie());
            champ = champRepository.save(champ);
            return champMapper.toResponse(champ);
    }

    /**
     * Supprime un champ de la base de données.
     * 
     * @param id L'identifiant du champ à supprimer
     * @return boolean true si le champ a été supprimé, false sinon
     * @throws ResponseException Si le champ contient des arbres avec des récoltes
     */
    public boolean deleteChamp(Long id) {
        Optional<Champ> champ = champRepository.findById(id);
        if (champ.isPresent()) {
            if (champ.get().getArbres() != null && champ.get().getArbres().stream().anyMatch(arbre -> arbre.getDetailRecoltes().size() > 0)) {
                throw new ResponseException("Impossible de supprimer le champ car il a des arbres avec des recoltes",HttpStatus.BAD_REQUEST);
            }
            champRepository.delete(champ.get());
            return true;
        }
        return false;
    }

  
} 