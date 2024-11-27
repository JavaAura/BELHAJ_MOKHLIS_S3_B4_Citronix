package com.app.Citronix.Controller;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.FermeRequest;
import com.app.Citronix.Model.DTO.Response.FermeResponse;
import com.app.Citronix.Service.FermeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * Contrôleur pour la gestion des fermes.
 */
@RestController
@RequestMapping("/api/fermes")
@Tag(name = "Ferme", description = "API de gestion des fermes")
public class FermeController {
    
    @Autowired
    private FermeService fermeService;

    /**
     * Crée une nouvelle ferme
     * @param request Les données de la ferme à créer
     * @return La ferme créée
     * @throws ResponseException Si la création échoue
     */
    @Operation(summary = "Créer une nouvelle ferme", description = "Crée une nouvelle ferme avec les données fournies")
    @ApiResponse(responseCode = "201", description = "Ferme créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    public ResponseEntity<FermeResponse> createFerme(@Valid @RequestBody FermeRequest request) throws ResponseException { 
        FermeResponse savedFerme = fermeService.saveFerme(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFerme);
    }
    
    /**
     * Récupère une ferme par son ID
     * @param id L'identifiant de la ferme
     * @return La ferme trouvée
     * @throws ResponseException Si la ferme n'existe pas
     */
    @Operation(summary = "Récupérer une ferme par ID", description = "Retourne une ferme en fonction de son ID")
    @ApiResponse(responseCode = "200", description = "Ferme trouvée")
    @ApiResponse(responseCode = "404", description = "Ferme non trouvée")
    @GetMapping("/{id}")
    public ResponseEntity<FermeResponse> getFermeById(@PathVariable Long id) {
        FermeResponse ferme = fermeService.getFermeById(id);
        if (ferme == null) {
            throw new ResponseException("Ferme non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(ferme);
    }

    /**
     * Récupère toutes les fermes avec pagination
     * @param page Numéro de la page (commence à 0)
     * @param size Nombre d'éléments par page
     * @return Liste paginée des fermes
     */
    @Operation(summary = "Lister toutes les fermes", description = "Retourne une liste paginée de toutes les fermes")
    @ApiResponse(responseCode = "200", description = "Liste des fermes récupérée avec succès")
    @GetMapping
    public ResponseEntity<Page<FermeResponse>> getAllFermes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fermeService.getAllFermes(pageable));
    }

    /**
     * Met à jour une ferme existante
     * @param id L'identifiant de la ferme à modifier
     * @param request Les nouvelles données de la ferme
     * @return La ferme mise à jour
     * @throws ResponseException Si la ferme n'existe pas ou si la mise à jour échoue
     */
    @Operation(summary = "Mettre à jour une ferme", description = "Met à jour une ferme existante avec les données fournies")
    @ApiResponse(responseCode = "200", description = "Ferme mise à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Ferme non trouvée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PutMapping("/{id}")
    public ResponseEntity<FermeResponse> updateFerme(@PathVariable Long id, @Valid @RequestBody FermeRequest request) throws ResponseException {
        
        return ResponseEntity.ok(fermeService.updateFerme(id, request));
    }

    /**
     * Supprime une ferme
     * @param id L'identifiant de la ferme à supprimer
     * @return Réponse vide avec statut 204 si succès
     * @throws ResponseException Si la ferme n'existe pas
     */
    @Operation(summary = "Supprimer une ferme", description = "Supprime une ferme existante")
    @ApiResponse(responseCode = "204", description = "Ferme supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Ferme non trouvée")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFerme(@PathVariable Long id) {
        if (!fermeService.deleteFerme(id)) {
            throw new ResponseException("Ferme non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche des fermes selon différents critères
     * @param nom Nom de la ferme (facultatif)
     * @param adress Adresse de la ferme (facultatif)
     * @param minSuperficie Superficie minimale (facultatif)
     * @param maxSuperficie Superficie maximale (facultatif)
     * @param startDate Date de début (facultatif)
     * @param endDate Date de fin (facultatif)
     * @param page Numéro de la page (commence à 0)
     * @param size Nombre d'éléments par page
     * @return Liste paginée des fermes correspondant aux critères
     */
    @Operation(summary = "Rechercher des fermes", description = "Recherche des fermes selon différents critères avec pagination")
    @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    @GetMapping("/search")
    public ResponseEntity<Page<FermeResponse>> searchFermes(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String adress,
            @RequestParam(required = false) Double minSuperficie,
            @RequestParam(required = false) Double maxSuperficie,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fermeService.searchFermes(nom, adress, minSuperficie, maxSuperficie, startDate, endDate, pageable));
    }


}
