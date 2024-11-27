package com.app.Citronix.Service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Mapper.FermeMapper;
import com.app.Citronix.Repository.FermeRepository;
import com.app.Citronix.Specification.FermeSpecifications;

/**
 * Classe de service pour la gestion des fermes.
 * Gère les opérations CRUD et la logique métier liée aux fermes.
 */
@Service
public class FermeService {
	@Autowired
	private FermeRepository fermeRepository;
	
    @Autowired
	private FermeMapper fermeMapper;

    /**
     * Crée une nouvelle ferme dans le système.
     * @param fermeRequest L'objet de transfert de données contenant les détails de la ferme
     * @return FermeResponse contenant les informations de la ferme sauvegardée
     */
    public FermeResponse saveFerme(FermeRequest fermeRequest) {
        Ferme ferme = fermeMapper.toEntity(fermeRequest);
        ferme = fermeRepository.save(ferme);
        return fermeMapper.toResponse(ferme);
    }

    /**
     * Récupère toutes les fermes avec pagination.
     * @param pageable Informations de pagination
     * @return Page de FermeResponse contenant les informations des fermes
     */
	public Page<FermeResponse> getAllFermes(Pageable pageable) {
        Page<Ferme> fermes = fermeRepository.findAll(pageable);
        return fermes.map(fermeMapper::toResponse);
    }

    /**
     * Récupère une ferme spécifique par son ID.
     * @param id L'ID de la ferme à récupérer
     * @return FermeResponse contenant les informations de la ferme ou null si non trouvée
     */
    public FermeResponse getFermeById(Long id) {
        Optional<Ferme> ferme = fermeRepository.findById(id);
        if (ferme.isPresent()) {
            return fermeMapper.toResponse(ferme.get());
        }
        return null;
    }

    /**
     * Met à jour les informations d'une ferme existante.
     * @param id L'ID de la ferme à mettre à jour
     * @param fermeRequest Les informations mises à jour de la ferme
     * @return FermeResponse contenant les informations mises à jour ou null si non trouvée
     */
    public FermeResponse updateFerme(Long id, FermeRequest fermeRequest) {
        fermeRequest.setId(id);
        validateUpdateFerme(fermeRequest);
        Optional<Ferme> fermeOpt = fermeRepository.findById(id);
        if (fermeOpt.isPresent()) {
            Ferme ferme = fermeOpt.get();
            ferme.setNom(fermeRequest.getNom());
            ferme.setAdress(fermeRequest.getAdress());
            ferme.setSuperficie(fermeRequest.getSuperficie());
            ferme = fermeRepository.save(ferme);
            return fermeMapper.toResponse(ferme);
        }
        return null;
    }

    /**
     * Valide la requête de mise à jour de la ferme, en s'assurant que la ferme existe et a une superficie valide.
     * @param fermeRequest La requête de mise à jour de la ferme à valider
     * @throws ResponseException si la validation échoue
     */
    private void validateUpdateFerme(FermeRequest fermeRequest) {
        Ferme ferme = fermeRepository.findById(fermeRequest.getId())
                .orElseThrow(() -> new ResponseException("Ferme non trouvé avec l'id: " + fermeRequest.getId(),HttpStatus.NOT_FOUND));
        double totalSuperficieChamps = totalSuperficieChamps(fermeRequest.getId());
        if (fermeRequest.getSuperficie() <= totalSuperficieChamps) {
            throw new ResponseException("ferme ne peut soit moins du totale des superficies des  champs "+totalSuperficieChamps+"hectares",HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Supprime une ferme si elle n'a pas de récoltes enregistrées.
     * @param id L'ID de la ferme à supprimer
     * @return true si la suppression a réussi, false si la ferme n'est pas trouvée
     * @throws ResponseException si la ferme a des champs avec des arbres ayant des récoltes
     */
    public boolean deleteFerme(Long id) {
        Optional<Ferme> ferme = fermeRepository.findById(id);
        if (ferme.isPresent()) {
            if (ferme.get().getChamps().stream().anyMatch(champ -> champ.getArbres().stream().anyMatch(arbre -> arbre.getDetailRecoltes().size() > 0))) {
                    throw new ResponseException("Impossible de supprimer la ferme car elle a des champs avec des arbres avec des recoltes",HttpStatus.BAD_REQUEST);
            }
            fermeRepository.delete(ferme.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Recherche des fermes selon différents critères avec pagination.
     * @param nom Filtre sur le nom de la ferme
     * @param adress Filtre sur l'adresse de la ferme
     * @param minSuperficie Filtre sur la superficie minimale
     * @param maxSuperficie Filtre sur la superficie maximale
     * @param startDate Filtre sur la date de début
     * @param endDate Filtre sur la date de fin
     * @param pageable Informations de pagination
     * @return Page de FermeResponse correspondant aux critères de recherche
     */
    public Page<FermeResponse> searchFermes(
            String nom, 
            String adress, 
            Double minSuperficie, 
            Double maxSuperficie, 
            LocalDate startDate, 
            LocalDate endDate,
            Pageable pageable) {
        
        Specification<Ferme> specs = Specification
                .where(FermeSpecifications.withNom(nom))
                .and(FermeSpecifications.withAdress(adress))
                .and(FermeSpecifications.withMinSuperficie(minSuperficie))
                .and(FermeSpecifications.withMaxSuperficie(maxSuperficie))
                .and(FermeSpecifications.withStartDate(startDate))
                .and(FermeSpecifications.withEndDate(endDate));

        return fermeRepository.findAll(specs, pageable)
                .map(fermeMapper::toResponse);
    }

    /**
     * Calcule la superficie disponible (non utilisée) d'une ferme.
     * @param ferme La ferme pour laquelle calculer la superficie disponible
     * @return La superficie disponible en hectares
     */
    public double getSuperficieLibre(Ferme ferme) {
        double superficieLibre = ferme.getSuperficie();
        for (Champ champ : ferme.getChamps()) {
            superficieLibre -= champ.getSuperficie();
        }
        return superficieLibre;
    }

    /**
     * Calcule la superficie totale de tous les champs d'une ferme.
     * @param id L'ID de la ferme
     * @return La superficie totale de tous les champs en hectares
     * @throws ResponseException si la ferme n'est pas trouvée
     */
    public double totalSuperficieChamps (Long id) {
        Ferme ferme = fermeRepository.findById(id)
        .orElseThrow(() -> new ResponseException("Ferme non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND));
        double totalSuperficie = 0;
        for (Champ champ : ferme.getChamps()) {
            totalSuperficie += champ.getSuperficie();
        }
        return totalSuperficie;
    }

  




	

	

}
