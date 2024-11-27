package com.app.Citronix.Controller;

import com.app.Citronix.Exception.ResponseException;
import com.app.Citronix.Model.DTO.Request.ArbreRequest;
import com.app.Citronix.Model.DTO.Response.ArbreResponse;
import com.app.Citronix.Service.ArbreService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * Contrôleur pour la gestion des arbres.
 */
@RestController
@RequestMapping("/api/arbres")
@Tag(name = "Arbre", description = "API de gestion des arbres")
public class ArbreController {
    
    @Autowired
    private ArbreService arbreService;



    /**
     * Récupère tous les arbres avec pagination.
     * 
     * @param pageable Les informations de pagination
     * @return Une page contenant les arbres convertis en ArbreResponse
     */
    @Operation(summary = "Récupérer tous les arbres", description = "Retourne une liste paginée des arbres")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste des arbres récupérée avec succès"),
        @ApiResponse(responseCode = "400", description = "Paramètres de pagination invalides")
    })
    @GetMapping
    public Page<ArbreResponse> getAllArbres(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "0") int page) {
        
        Pageable customPageable = PageRequest.of(
            page, 
            size        );
        
        return arbreService.findAll(customPageable);
    }

    /**
     * Récupère un arbre par son identifiant.
     * 
     * @param id L'identifiant de l'arbre recherché
     * @return ArbreResponse Les informations de l'arbre
     * @throws ResponseException Si l'arbre n'est pas trouvé (HTTP 404)
     */
    @Operation(summary = "Récupérer un arbre par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arbre trouvé"),
        @ApiResponse(responseCode = "404", description = "Arbre non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArbreResponse> getArbreById(@PathVariable Long id) {
        ArbreResponse arbre = arbreService.findById(id);
        return arbre != null ? ResponseEntity.ok(arbre) : ResponseEntity.notFound().build();
    }


    /**
     * Crée un nouvel arbre.
     * 
     * @param arbreRequest Les données de l'arbre à créer
     * @return ArbreResponse Les informations de l'arbre créé
     * @throws ResponseException Si les données d'entrée sont invalides (HTTP 400)
     */
    @Operation(summary = "Créer un nouvel arbre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arbre créé avec succès"),
        @ApiResponse(responseCode = "400", description = "validation error")
    })
    @PostMapping
    public ArbreResponse createArbre( @Valid @RequestBody ArbreRequest arbreRequest) {
        return arbreService.save(arbreRequest);
    }


    /**
     * Supprime un arbre par son identifiant.
     * 
     * @param id L'identifiant de l'arbre à supprimer
     * @return ResponseEntity<Void> Rien si la suppression est réussie (HTTP 204), ou une erreur si l'arbre n'est pas trouvé (HTTP 404)
     * @throws ResponseException Si l'arbre n'est pas trouvé (HTTP 404)
     */
    @Operation(summary = "Supprimer un arbre par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Arbre supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Arbre non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArbre(@PathVariable Long id) {

        if (arbreService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        
        throw new ResponseException("Arbre non trouvé avec l'id: " + id, HttpStatus.NOT_FOUND);
    }

}
