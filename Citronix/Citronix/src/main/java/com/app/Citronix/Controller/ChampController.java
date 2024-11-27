package com.app.Citronix.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ChampRequest;
import com.app.Citronix.Model.DTO.Response.ChampResponse;
import com.app.Citronix.Service.ChampService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * Contrôleur pour la gestion des champs agricoles.
 */
@RestController
@RequestMapping("/api/champs")
@Tag(name = "Champ", description = "API de gestion des champs agricoles")
public class ChampController {
    
    @Autowired
    private ChampService champService;


    /**
     * Crée un nouveau champ agricole.
     * 
     * @param request Les données du champ à créer
     * @return ChampResponse Les informations du champ créé
     * @throws ResponseException Si les données d'entrée sont invalides (HTTP 400)
     */
    @Operation(summary = "Créer un nouveau champ", description = "Crée un nouveau champ agricole")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Champ créé avec succès",
            content = @Content(schema = @Schema(implementation = ChampResponse.class))),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    public ResponseEntity<ChampResponse> createChamp(@Valid @RequestBody ChampRequest request) {
        return ResponseEntity.ok(champService.saveChamp(request));
    }
    

    /**
     * Récupère un champ par son identifiant.
     * 
     * @param id L'identifiant du champ recherché
     * @return ChampResponse Les informations du champ
     * @throws ResponseException Si le champ n'est pas trouvé (HTTP 404)
     */
    @Operation(summary = "Récupérer un champ par son ID", description = "Retourne un champ agricole selon l'ID fourni")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Champ trouvé",
            content = @Content(schema = @Schema(implementation = ChampResponse.class))),
        @ApiResponse(responseCode = "404", description = "Champ non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChampResponse> getChampById(@PathVariable Integer id) {
        ChampResponse champ = champService.getChampById(id);
        return ResponseEntity.ok(champ);
    }

    /**
     * Récupère tous les champs avec pagination.
     * 
     * @param page La page à récupérer
     * @param size La taille de la page
     * @return Une page contenant les champs convertis en ChampResponse
     */
    @Operation(summary = "Récupérer tous les champs", description = "Retourne une liste paginée de tous les champs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des champs récupérée avec succès")
    })
    @GetMapping
    public ResponseEntity<Page<ChampResponse>> getAllChamps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(champService.getAllChamps(pageable));
    }

    /**
     * Met à jour un champ existant.
     * 
     * @param id L'identifiant du champ à mettre à jour
     * @param request Les nouvelles données du champ
     * @return ChampResponse Les informations du champ mis à jour
     * @throws ResponseException Si le champ n'est pas trouvé (HTTP 404) ou si les données d'entrée sont invalides (HTTP 400)
     */
    @Operation(summary = "Mettre à jour un champ", description = "Met à jour un champ existant selon l'ID fourni")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Champ mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Champ non trouvé"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ChampResponse> updateChamp(
            @PathVariable Long id,
            @Valid @RequestBody ChampRequest request) {
        ChampResponse updatedChamp = champService.updateChamp(id, request);
        if (updatedChamp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedChamp);
    }

    /**
     * Supprime un champ par son identifiant.
     * 
     * @param id L'identifiant du champ à supprimer
     * @return ResponseEntity<Void> Rien si la suppression est réussie (HTTP 204), ou une erreur si le champ n'est pas trouvé (HTTP 404)
     * @throws ResponseException Si le champ n'est pas trouvé (HTTP 404)
     */
    @Operation(summary = "Supprimer un champ", description = "Supprime un champ selon l'ID fourni")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Champ supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Champ non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamp(@PathVariable Long id) {
        if (champService.deleteChamp(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ResponseException("Champ non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
        }
} 