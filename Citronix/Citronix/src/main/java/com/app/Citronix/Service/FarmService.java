package com.app.Citronix.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Citronix.Model.DTO.FermeDTO;
import com.app.Citronix.Model.Entity.Ferme;
import com.app.Citronix.Model.Mapper.FermeMapper;
import com.app.Citronix.Repository.FarmRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class FarmService {
	@Autowired
	private FarmRepository farmRepository;
	
    @Autowired
	private FermeMapper fermeMapper;


    public FermeDTO saveFarm(FermeDTO fermeDTO) {
        Ferme ferme = fermeMapper.toEntity(fermeDTO);
        ferme = farmRepository.save(ferme);
        return fermeMapper.toDto(ferme);
    }

	public List<FermeDTO> getAllFarms() {
        List<Ferme> fermes = farmRepository.findAll();
        return fermeMapper.toDtoList(fermes);
    }

    public FermeDTO getFarmById(Long id) {
        Optional<Ferme> ferme = farmRepository.findById(id);
        if (ferme.isPresent()) {
            return fermeMapper.toDto(ferme.get());
        }
        log.error("Ferme with id {} not found", id);
        return null;
    }

    public FermeDTO updateFarm(FermeDTO fermeDTO) {
        Ferme ferme = fermeMapper.toEntity(fermeDTO);
        ferme = farmRepository.save(ferme);
        return fermeMapper.toDto(ferme);
    }

    public void deleteFarm(Long id) {
        Optional<Ferme> ferme = farmRepository.findById(id);
        if (ferme.isPresent()) {
            farmRepository.delete(ferme.get());
        } else {
            log.error("Ferme with id {} not found", id);
        }
    }





	

	

}
