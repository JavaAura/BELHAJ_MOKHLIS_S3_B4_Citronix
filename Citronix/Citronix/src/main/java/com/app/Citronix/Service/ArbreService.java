package com.app.Citronix.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Citronix.Exception.ChampException;
import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Model.Entity.Arbre;
import com.app.Citronix.Model.Entity.Champ;
import com.app.Citronix.Model.Mapper.ArbreMapper;
import com.app.Citronix.Repository.ArbreRepository;
import com.app.Citronix.Repository.ChampRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ArbreService {
    
    @Autowired
    private ArbreRepository arbreRepository;
    
    @Autowired
    private ChampRepository champRepository;
    
    @Autowired
    private ArbreMapper arbreMapper;

    public ArbreResponse save(ArbreRequest arbreRequest) {
        LocalDate datePlantation = arbreRequest.getDatePlantation();
        int mois = datePlantation.getMonthValue();
        if (mois < 3 || mois > 5) {
            throw new IllegalArgumentException("La plantation n'est autorisée qu'entre mars et mai");
        }

        Optional<Champ> champ = champRepository.findById(arbreRequest.getChamp().getId());
        if (champ.isPresent()) {
            double superficie = champ.get().getSuperficie(); // en hectare
            long nbArbres = champ.get().getArbres().size();
            if (nbArbres >= (superficie * 100)) { // 100 arbre pour 1 hectare
                throw new IllegalStateException("Densité maximale atteinte pour ce champ");
            }
        } else {
            throw new ChampException("Champ non trouvé");
        }

        Arbre arbre = arbreMapper.toEntity(arbreRequest);
        arbre = arbreRepository.save(arbre);
        return arbreMapper.toResponse(arbre);
    }

    public Page<ArbreResponse> findAll(Pageable pageable) {
        Page<Arbre> arbres = arbreRepository.findAll(pageable);
        return arbres.map(arbre -> {
            arbre.setAge(calculerAge(arbre));
            return arbreMapper.toResponse(arbre);
        });
    }

    public Optional<ArbreResponse> findById(Long id) {
        Optional<Arbre> arbre = arbreRepository.findById(id);
        if (arbre.isPresent()) {
            arbre.get().setAge(calculerAge(arbre.get()));
        }
        return arbre.map(arbreMapper::toResponse);
    }
    public boolean deleteById(Long id) {
        Optional<Arbre> arbre = arbreRepository.findById(id);
        if (arbre.isPresent()) {
            arbreRepository.delete(arbre.get());
            return true;
        }
        return false;
    }



    public long calculerAge(Arbre arbre) {
        return ChronoUnit.YEARS.between(arbre.getDatePlantation(), LocalDate.now());
    }
} 