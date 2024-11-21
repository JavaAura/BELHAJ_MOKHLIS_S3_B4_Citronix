package com.app.Citronix.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Mapper.FermeMapper;
import com.app.Citronix.Repository.FarmRepository;
import com.app.Citronix.Specification.FermeSpecifications;


@Service
public class FermeService {
	@Autowired
	private FarmRepository farmRepository;
	
    @Autowired
	private FermeMapper fermeMapper;


    public FermeResponse saveFerme(FermeRequest fermeRequest) {
        Ferme ferme = fermeMapper.toEntity(fermeRequest);
        ferme = farmRepository.save(ferme);
        return fermeMapper.toResponse(ferme);
    }

	public Page<FermeResponse> getAllFermes(Pageable pageable) {
        Page<Ferme> fermes = farmRepository.findAll(pageable);
        return fermes.map(fermeMapper::toResponse);
    }

    public FermeResponse getFermeById(Long id) {
        Optional<Ferme> ferme = farmRepository.findById(id);
        if (ferme.isPresent()) {
            return fermeMapper.toResponse(ferme.get());
        }
        return null;
    }

    public FermeResponse updateFerme(Long id, FermeRequest fermeRequest) {
        Optional<Ferme> fermeOpt = farmRepository.findById(id);
        if (fermeOpt.isPresent()) {
            Ferme ferme = fermeOpt.get();
            ferme.setNom(fermeRequest.getNom());
            ferme.setAdress(fermeRequest.getAdress());
            ferme.setSuperficie(fermeRequest.getSuperficie());
            ferme = farmRepository.save(ferme);
            return fermeMapper.toResponse(ferme);
        }
        return null;
    }

    public boolean deleteFerme(Long id) {
        Optional<Ferme> ferme = farmRepository.findById(id);
        if (ferme.isPresent()) {
            farmRepository.delete(ferme.get());
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

        return farmRepository.findAll(specs, pageable)
                .map(fermeMapper::toResponse);
    }





	

	

}