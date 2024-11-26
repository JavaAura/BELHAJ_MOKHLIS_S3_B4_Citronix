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

@Service
public class FermeService {
	@Autowired
	private FermeRepository fermeRepository;
	
    @Autowired
	private FermeMapper fermeMapper;

    public FermeResponse saveFerme(FermeRequest fermeRequest) {
        Ferme ferme = fermeMapper.toEntity(fermeRequest);
        ferme = fermeRepository.save(ferme);
        return fermeMapper.toResponse(ferme);
    }

	public Page<FermeResponse> getAllFermes(Pageable pageable) {
        Page<Ferme> fermes = fermeRepository.findAll(pageable);
        return fermes.map(fermeMapper::toResponse);
    }

    public FermeResponse getFermeById(Long id) {
        Optional<Ferme> ferme = fermeRepository.findById(id);
        if (ferme.isPresent()) {
            return fermeMapper.toResponse(ferme.get());
        }
        return null;
    }

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

    private void validateUpdateFerme(FermeRequest fermeRequest) {
        Ferme ferme = fermeRepository.findById(fermeRequest.getId())
                .orElseThrow(() -> new ResponseException("Ferme non trouvé avec l'id: " + fermeRequest.getId(),HttpStatus.NOT_FOUND));
        double totalSuperficieChamps = totalSuperficieChamps(fermeRequest.getId());
        if (fermeRequest.getSuperficie() <= totalSuperficieChamps) {
            throw new ResponseException("ferme ne peut soit moins du totale des superficies des  champs "+totalSuperficieChamps+"hectares",HttpStatus.BAD_REQUEST);
        }
    }

    public boolean deleteFerme(Long id) {
        Optional<Ferme> ferme = fermeRepository.findById(id);
        if (ferme.get().getChamps().stream().anyMatch(champ -> champ.getArbres().stream().anyMatch(arbre -> arbre.getDetailRecoltes().size() > 0))) {
                throw new ResponseException("Impossible de supprimer la ferme car elle a des champs avec des arbres avec des recoltes",HttpStatus.BAD_REQUEST);
        }
        if (ferme.isPresent()) {
            fermeRepository.delete(ferme.get());
            return true;
        } else {
            return false;
        }
    }

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


    public double getSuperficieLibre(Ferme ferme) {
        double superficieLibre = ferme.getSuperficie();
        for (Champ champ : ferme.getChamps()) {
            superficieLibre -= champ.getSuperficie();
        }
        return superficieLibre;
    }

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
