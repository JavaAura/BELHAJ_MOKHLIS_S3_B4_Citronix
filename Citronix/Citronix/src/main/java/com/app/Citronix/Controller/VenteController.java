package com.app.Citronix.Controller;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.VenteRequest;
import com.app.Citronix.Model.DTO.Response.VenteResponse;
import com.app.Citronix.Service.VenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur pour la gestion des ventes.
 */
@RestController
@RequestMapping("api/ventes")
@Tag(name = "Ventes", description = "API de gestion des ventes")
public class VenteController {
    
    @Autowired
    private VenteService venteService;
    
    /**
     * Récupère toutes les ventes avec pagination
     * @param pageable Les informations de pagination
     * @return Une page contenant les ventes
     */
    @Operation(summary = "Récupérer toutes les ventes", description = "Retourne une liste paginée de toutes les ventes")
    @ApiResponse(responseCode = "200", description = "Liste des ventes récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<VenteResponse>> getAllVentes(Pageable pageable) {
        return ResponseEntity.ok(venteService.getAllVentes(pageable));
    }
    
    /**
     * Récupère une vente spécifique par son identifiant
     * @param id L'identifiant de la vente à récupérer
     * @return La vente correspondante à l'identifiant
     * @throws ResponseException Si la vente n'est pas trouvée
     */
    @Operation(summary = "Récupérer une vente par son ID", description = "Retourne une vente unique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vente trouvée"),
        @ApiResponse(responseCode = "404", description = "Vente non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenteResponse> getVenteById(
        @Parameter(description = "ID de la vente", required = true) @PathVariable Long id) {
        return venteService.getVenteById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseException("Vente non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND));
    }
    
    /**
     * Crée une nouvelle vente
     * @param venteRequest Les données de la vente à créer
     * @return La vente créée
     */
    @Operation(summary = "Créer une nouvelle vente", description = "Crée une nouvelle vente et retourne les détails")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vente créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données de la requête invalides")
    })
    @PostMapping
    public VenteResponse createVente(@RequestBody VenteRequest venteRequest) {
        return venteService.saveVente(venteRequest);
    }
    
    /**
     * Met à jour une vente existante
     * @param id L'identifiant de la vente à modifier
     * @param venteRequest Les nouvelles données de la vente
     * @return La vente mise à jour
     * @throws ResponseException Si la vente n'est pas trouvée
     */
    @Operation(summary = "Mettre à jour une vente", description = "Met à jour une vente existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vente mise à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Vente non trouvée"),
        @ApiResponse(responseCode = "400", description = "Données de la requête invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VenteResponse> updateVente(@PathVariable Long id, @RequestBody VenteRequest venteRequest) {
        return venteService.updateVente(id, venteRequest)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseException("Vente non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND));
    }
    
    /**
     * Supprime une vente existante
     * @param id L'identifiant de la vente à supprimer
     * @return ResponseEntity sans contenu en cas de succès
     * @throws ResponseException Si la vente n'est pas trouvée
     */
    @Operation(summary = "Supprimer une vente", description = "Supprime une vente existante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vente supprimée avec succès"),
        @ApiResponse(responseCode = "404", description = "Vente non trouvée")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVente(@PathVariable Long id) {
        if (venteService.deleteVente(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ResponseException("Vente non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
    }
}
