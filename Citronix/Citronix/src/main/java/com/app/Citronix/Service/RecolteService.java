package com.app.Citronix.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Response.RecolteResponse;
import com.app.Citronix.Model.Entity.Recolte;
import com.app.Citronix.Model.Mapper.RecolteMapper;
import com.app.Citronix.Repository.RecolteRepository;

/**
 * Service gérant les opérations liées aux récoltes.
 * Cette classe gère la logique métier pour la manipulation des données de récolte.
 */
@Service
public class RecolteService {

    @Autowired
    private RecolteRepository recolteRepository;

    @Autowired
    private RecolteMapper recolteMapper;

    /**
     * Récupère toutes les récoltes de manière paginée.
     * 
     * @param pageable Les informations de pagination
     * @return Une page contenant les récoltes converties en RecolteResponse
     */
    public Page<RecolteResponse> getAllRecoltes(Pageable pageable) {
        return recolteRepository.findAll(pageable).map(recolteMapper::toResponse);
    }

    /**
     * Récupère une récolte spécifique par son identifiant.
     * 
     * @param id L'identifiant de la récolte recherchée
     * @return La récolte convertie en RecolteResponse
     * @throws ResponseException Si la récolte n'est pas trouvée (HTTP 404)
     */
    public RecolteResponse getRecolteById(Long id) {
        Recolte recolte = recolteRepository.findById(id).orElse(null);
        if (recolte == null) {
            throw new ResponseException("Recolte non trouvée", HttpStatus.NOT_FOUND);
        }
        return recolteMapper.toResponse(recolte);
    }

    
    
}
