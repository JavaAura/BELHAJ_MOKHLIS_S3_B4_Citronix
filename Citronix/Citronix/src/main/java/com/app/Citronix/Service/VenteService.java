package com.app.Citronix.Service;

import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Model.Entity.Vente;
import com.app.Citronix.Model.Mapper.VenteMapper;
import com.app.Citronix.Repository.VenteRepository;
import com.app.Citronix.Validation.VenteValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.validation.Valid;

/**
 * Service gérant les opérations liées aux ventes.
 * Cette classe gère la logique métier pour la création, la lecture, la mise à jour
 * et la suppression des ventes.
 */
@Service
public class VenteService {

    @Autowired
    private VenteMapper venteMapper;
    @Autowired
    private VenteRepository venteRepository;
    @Autowired
    private VenteValidation venteValidation;

    /**
     * Enregistre une nouvelle vente dans le système.
     * 
     * @param venteRequest L'objet contenant les informations de la vente à créer
     * @return VenteResponse Les détails de la vente créée
     */
    public VenteResponse saveVente( @Valid VenteRequest venteRequest) {
        Vente vente = venteMapper.toEntity(venteRequest);
        vente = venteValidation.validationVenteRequest(vente);
       
        return venteMapper.toResponse(venteRepository.save(vente)); 
    }

    /**
     * Récupère toutes les ventes de manière paginée.
     * 
     * @param pageable Les informations de pagination
     * @return Page<VenteResponse> Une page contenant les ventes
     */
    public Page<VenteResponse> getAllVentes(Pageable pageable) {
        return venteRepository.findAll(pageable).map(venteMapper::toResponse);
    }

    /**
     * Recherche une vente par son identifiant.
     * 
     * @param id L'identifiant de la vente recherchée
     * @return Optional<VenteResponse> La vente trouvée ou un Optional vide si non trouvée
     */
    public Optional<VenteResponse> getVenteById(Long id) {
        return venteRepository.findById(id).map(venteMapper::toResponse);
    }

    /**
     * Met à jour une vente existante.
     * 
     * @param id L'identifiant de la vente à modifier
     * @param venteRequest Les nouvelles informations de la vente
     * @return Optional<VenteResponse> La vente mise à jour ou un Optional vide si non trouvée
     */
    public Optional<VenteResponse> updateVente(Long id, VenteRequest venteRequest) {
        if (venteRepository.existsById(id)) {
            return Optional.of(venteMapper.toResponse(venteRepository.save(venteMapper.toEntity(venteRequest))));
        }
        return Optional.empty();
    }

    /**
     * Supprime une vente du système.
     * 
     * @param id L'identifiant de la vente à supprimer
     * @return boolean true si la suppression a réussi, false sinon
     */
    public boolean deleteVente(Long id) {
        if (venteRepository.existsById(id)) {
            venteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}