package com.app.Citronix.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.DetailRecolteRequest;
import com.app.Citronix.Model.DTO.Response.DetailRecolteResponse;
import com.app.Citronix.Model.DTO.Response.RecolteResponse;
import com.app.Citronix.Service.RecolteDetailService;
import com.app.Citronix.Service.RecolteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * Contrôleur pour la gestion des récoltes.
 */
@RestController
@RequestMapping("/api/recoltes")
@Tag(name = "Recolte", description = "API de gestion des récoltes")
public class RecolteController {

    @Autowired
    private RecolteService recolteService;

    @Autowired
    private RecolteDetailService recolteDetailService;

    /*
     * Récupérer toutes les récoltes
     * @param pageable Pagination
     * @return Liste paginée des récoltes
     */
    @Operation(summary = "Récupérer toutes les récoltes", description = "Retourne une liste paginée de toutes les récoltes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Récoltes trouvées avec succès"),
        @ApiResponse(responseCode = "404", description = "Aucune récolte trouvée")
    })
    @GetMapping
    public ResponseEntity<Page<RecolteResponse>> getAllRecoltes(Pageable pageable) {
        Page<RecolteResponse> recoltes = recolteService.getAllRecoltes(pageable);
        return ResponseEntity.ok(recoltes);
    }

    /*
     * Récupérer une récolte par son ID
     * @param id ID de la récolte
     * @return Récolte spécifique
     */
    @Operation(summary = "Récupérer une récolte par son ID", description = "Retourne une récolte spécifique basée sur son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Récolte trouvée avec succès"),
        @ApiResponse(responseCode = "404", description = "Récolte non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecolteResponse> getRecolteById(@PathVariable Long id) {
        RecolteResponse recolte = recolteService.getRecolteById(id);
        return ResponseEntity.ok(recolte);
    }

    /*
     * Créer un détail de récolte
     * @param detailRecolteRequest Détail de récolte à créer
     * @return Détail de récolte créé
     */
    @Operation(summary = "Créer un détail de récolte", description = "Crée un nouveau détail de récolte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Détail de récolte créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/createDetailRecolte")
    public ResponseEntity<DetailRecolteResponse> createRecolteDetail(@RequestBody DetailRecolteRequest detailRecolteRequest) {
        DetailRecolteResponse detailRecolte = recolteDetailService.createRecolteDetail(detailRecolteRequest);
        return ResponseEntity.ok(detailRecolte);
    }

    /*
     * Récupérer un détail de récolte par ID
     * @param id ID du détail de récolte
     * @return Détail de récolte spécifique
     */
    @Operation(summary = "Récupérer un détail de récolte par ID", description = "Retourne un détail de récolte spécifique basé sur son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Détail de récolte trouvé avec succès"),
        @ApiResponse(responseCode = "404", description = "Détail de récolte non trouvé")
    })
    @GetMapping("/details/{id}")
    public ResponseEntity<DetailRecolteResponse> getRecolteDetails(@PathVariable Long id) {
        DetailRecolteResponse details = recolteDetailService.getRecolteDetailById(id);
        return ResponseEntity.ok(details);
    }

    /*
     * Récupérer tous les détails de récolte
     * @param pageable Pagination
     * @return Liste paginée des détails de récolte
     */
    @Operation(summary = "Récupérer tous les détails de récolte", description = "Retourne une liste paginée de tous les détails de récolte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Détails de récolte trouvés avec succès"),
        @ApiResponse(responseCode = "404", description = "Aucun détail de récolte trouvé")
    })
    @GetMapping("/details")
    public ResponseEntity<Page<DetailRecolteResponse>> getRecolteDetails(Pageable pageable) {
        Page<DetailRecolteResponse> details = recolteDetailService.getAllRecolteDetails(pageable);
        return ResponseEntity.ok(details);
    }


    /*
     * Supprimer un détail de récolte par ID
     * @param id ID du détail de récolte
     * @return boolean true si la suppression a réussi, false sinon
     */
    @Operation(summary = "Supprimer un détail de récolte par ID", description = "Supprime un détail de récolte spécifique basé sur son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Détail de récolte supprimé avec succès"),
        @ApiResponse(responseCode = "400", description = "La suppression de ce détail de récolte est impossible car la quantite restante est inferieur a la quantite de recolte"),
        @ApiResponse(responseCode = "404", description = "Détail de récolte non trouvé")
    })
    @DeleteMapping("/detail/{id}")
    public ResponseEntity<?> deleteRecolteDetail(@PathVariable Long id) {
        if (recolteDetailService.deleteRecolteDetail(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ResponseException("Détail de récolte non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
    }

} 