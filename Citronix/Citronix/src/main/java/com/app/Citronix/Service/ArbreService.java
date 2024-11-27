package com.app.Citronix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Mapper.ArbreMapper;
import com.app.Citronix.Repository.ArbreRepository;
import com.app.Citronix.Repository.ChampRepository;
import com.app.Citronix.Validation.ArbreValidation;

import java.util.Optional;

/**
 * Classe de service pour la gestion des entités Arbre.
 */
@Service
public class ArbreService {
    
    @Autowired
    private ArbreRepository arbreRepository;
    
    @Autowired
    private ChampRepository champRepository;
    
    @Autowired
    private ArbreMapper arbreMapper;
    
    @Autowired
    private ArbreValidation arbreValidation;

    /**
     * Enregistre une nouvelle entité Arbre basée sur l'ArbreRequest fourni.
     *
     * @param arbreRequest L'ArbreRequest contenant les données pour le nouvel Arbre.
     * @return ArbreResponse contenant les données de l'Arbre enregistré.
     */
    public ArbreResponse save(ArbreRequest arbreRequest) {
        arbreValidation.validateArbreRequest(arbreRequest);
        Optional<Champ> champ = champRepository.findById( arbreRequest.getChamp().getId());
        Arbre arbre = arbreMapper.toEntity(arbreRequest);
        arbre.setChamp(champ.get());
        arbre = arbreRepository.save(arbre);
        return arbreMapper.toResponse(arbre);
    }

    /**
     * Récupère une liste paginée de toutes les entités Arbre.
     *
     * @param pageable Objet Pageable pour les informations de pagination.
     * @return Page d'objets ArbreResponse.
     */
    public Page<ArbreResponse> findAll(Pageable pageable) {
        Page<Arbre> arbres = arbreRepository.findAll(pageable);
        return arbres.map(arbre -> {
            return arbreMapper.toResponse(arbre);
        });
    }

    /**
     * Trouve une entité Arbre par son ID.
     *
     * @param id L'ID de l'Arbre à trouver.
     * @return ArbreResponse contenant les données de l'Arbre trouvé.
     * @throws ResponseException si l'Arbre n'est pas trouvé.
     */
    public ArbreResponse findById(Long id) {
        Arbre arbre = arbreRepository.findById(id)
        .orElseThrow(() -> new ResponseException("Arbre non trouvé avec l'id: " + id,HttpStatus.NOT_FOUND));
     
        return arbreMapper.toResponse(arbre);
    }

    /**
     * Supprime une entité Arbre par son ID.
     *
     * @param id L'ID de l'Arbre à supprimer.
     * @return true si l'Arbre a été supprimé avec succès, false s'il n'a pas été trouvé.
     * @throws ResponseException si l'Arbre a des récoltes associées et ne peut pas être supprimé.
     */
    public boolean deleteById(Long id) {
        Optional<Arbre> arbre = arbreRepository.findById(id);
        if (arbre.isPresent()) {
            if (arbre.get().getDetailRecoltes().size() > 0) {
                throw new ResponseException("Impossible de supprimer l'arbre car il a des recoltes",HttpStatus.BAD_REQUEST);
            }
            arbreRepository.delete(arbre.get());
            return true;
        }
        return false;
    }



 
} 