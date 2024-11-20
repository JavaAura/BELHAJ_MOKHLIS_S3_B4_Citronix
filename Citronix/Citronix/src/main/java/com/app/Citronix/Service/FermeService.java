package com.app.Citronix.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.app.Citronix.Model.DTO.FermeDTO;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Mapper.FermeMapper;
import com.app.Citronix.Model.View.FermeView;
import com.app.Citronix.Repository.FarmRepository;
import lombok.extern.slf4j.Slf4j;


@Service
public class FermeService {
	@Autowired
	private FarmRepository farmRepository;
	
    @Autowired
	private FermeMapper fermeMapper;


    public FermeDTO saveFerme(FermeDTO fermeDTO) {
        Ferme ferme = fermeMapper.toEntity(fermeDTO);
        ferme = farmRepository.save(ferme);
        return fermeMapper.toDto(ferme);
    }

	public Page<FermeView> getAllFermes(Pageable pageable) {
        Page<Ferme> fermes = farmRepository.findAll(pageable);
        return fermes.map(fermeMapper::toView);
    }

    public FermeView getFermeById(Long id) {
        Optional<Ferme> ferme = farmRepository.findById(id);
        if (ferme.isPresent()) {
            return fermeMapper.toView(ferme.get());
        }
        return null;
    }

    public FermeDTO updateFerme(FermeDTO fermeDTO) {
        Ferme ferme = fermeMapper.toEntity(fermeDTO);
        ferme = farmRepository.save(ferme);
        return fermeMapper.toDto(ferme);
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





	

	

}
